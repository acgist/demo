package com.acgist.bce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

import org.springframework.boot.SpringApplication;
import org.springframework.core.io.DefaultResourceLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密算法
 * 
 * @author acgist
 */
@Slf4j
public class Encrypt {
    
    // 是否解密
    public static boolean encrypt = false;
    // 是否解密
    public static boolean decrypt = false;
    // 加密魔数
    public static final int MAGIC = 0x16795E15;
    // 文件路径
    public static final String FILE_PATH = "com/acgist/bce";
    // 文件后缀
    public static final String FILE_SUFFIX = ".class";
    // URL资源
    public static final String SPRING_BOOT_URL_RESOURCE = "org/springframework/core/io/UrlResource.class";
    
    /**
     * 加密入口
     * 
     * @param args 启动参数
     * 
     * @return 是否启动
     */
    public static final boolean encryptMain(String[] args) {
        final Properties properties = System.getProperties();
        // 是否加密
        boolean encrypt = "true".equals(properties.get("encrypt"));
        // 是否解密
        boolean decrypt = "true".equals(properties.get("decrypt"));
        // 配置全局
        Encrypt.encrypt = encrypt;
        Encrypt.decrypt = decrypt;
        if(encrypt) {
            try {
                Encrypt.encryptProxy(args);
            } catch (IOException e) {
                log.error("加密文件异常", e);
            }
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 加密代理
     * 
     * @param args 启动参数
     * 
     * @throws IOException IO异常
     */
    private static final void encryptProxy(String[] args) throws IOException {
        final Properties properties = System.getProperties();
        final String srcName = properties.getProperty("src");
        final String dstName = properties.getProperty("dst", "dst.jar");
        final File srcFile = new File(srcName);
        final File dstFile = new File(dstName);
        log.info("开始加密文件：{} -> {}", srcFile, dstFile);
        final JarFile srcJar = new JarFile(srcFile);
        final JarOutputStream dstJar = new JarOutputStream(new FileOutputStream(dstFile));
        Encrypt.encryptJar(srcJar, dstJar);
        srcJar.close();
        dstJar.close();
    }

    /**
     * Jar文件加密
     * 
     * @param srcJar 原始文件
     * @param dstJar 目标文件
     * 
     * @throws IOException IO异常
     */
    private static final void encryptJar(JarFile srcJar, JarOutputStream dstJar) throws IOException {
        final Enumeration<JarEntry> enumeration = srcJar.entries();
        while (enumeration.hasMoreElements()) {
            final JarEntry srcEntry = enumeration.nextElement();
            final String name       = srcEntry.getName();
            log.debug("开始处理文件：{}", name);
            if(srcEntry.isDirectory()) {
                // 目录
                Encrypt.encryptDirectory(dstJar, srcEntry);
            } else if(name.endsWith(".jar")) {
                // Jar
                try(final InputStream input = srcJar.getInputStream(srcEntry)) {
                    final ByteArrayOutputStream out = Encrypt.encryptStream(new JarInputStream(input));
                    Encrypt.encryptJar(dstJar, srcEntry, out.toByteArray());
                }
            } else {
                // 文件
                try(final InputStream input = srcJar.getInputStream(srcEntry)) {
                    Encrypt.encryptFile(dstJar, srcEntry, input);
                }
            }
            dstJar.closeEntry();
            dstJar.flush();
        }
    }
    
    /**
     * Jar文件流加密
     * 
     * @param srcInput 文件输入流
     * 
     * @return 加密输出流
     * 
     * @throws IOException IO异常
     */
    private static final ByteArrayOutputStream encryptStream(JarInputStream srcInput) throws IOException {
        JarOutputStream dstJar  = null;
        final List<String> list = new ArrayList<>();
        final ByteArrayOutputStream ret = new ByteArrayOutputStream();
        while(true) {
            // MANIFEST.MF
            if(dstJar == null) {
                if(srcInput.getManifest() == null) {
                    dstJar = new JarOutputStream(ret);
                } else {
                    dstJar = new JarOutputStream(ret, srcInput.getManifest());
                }
            }
            final JarEntry srcEntry = srcInput.getNextJarEntry();
            if(srcEntry == null) {
                break;
            }
            final String name = srcEntry.getName();
            if(list.contains(name)) {
                log.warn("处理文件重复：{}", name);
                continue;
            }
            list.add(name);
            log.debug("开始处理文件：{}", name);
            if(srcEntry.isDirectory()) {
                // 目录
                Encrypt.encryptDirectory(dstJar, srcEntry);
            } else if(name.endsWith(".jar")) {
                // Jar
                final ByteArrayOutputStream out = Encrypt.encryptStream(new JarInputStream(new ByteArrayInputStream(srcInput.readAllBytes())));
                Encrypt.encryptJar(dstJar, srcEntry, out.toByteArray());
            } else {
                // 文件
                Encrypt.encryptFile(dstJar, srcEntry, srcInput);
            }
            dstJar.closeEntry();
            dstJar.flush();
        }
        dstJar.close();
        return ret;
    }
    
    /**
     * 加密目录
     * 
     * @param  dstJar      目标文件
     * @param  srcEntry    原始文件
     * 
     * @throws IOException IO异常
     */
    private static final void encryptDirectory(JarOutputStream dstJar, JarEntry srcEntry) throws IOException {
        final JarEntry dstEntry = (JarEntry) srcEntry.clone();
        dstJar.setMethod(ZipEntry.DEFLATED);
        dstJar.putNextEntry(dstEntry);
    }
    
    /**
     * 加密Jar文件
     * 注意：Jar不能压缩
     * 
     * @param dstJar   目标文件
     * @param srcEntry 原始文件
     * @param bytes    文件内容
     * 
     * @throws IOException IO异常
     */
    private static final void encryptJar(JarOutputStream dstJar, JarEntry srcEntry, byte[] bytes) throws IOException {
        final JarEntry dstEntry = (JarEntry) srcEntry.clone();
        final CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        dstEntry.setCrc(crc32.getValue());
        dstEntry.setSize(bytes.length);
        dstEntry.setMethod(ZipEntry.STORED);
        dstEntry.setCompressedSize(bytes.length);
        dstJar.setMethod(ZipEntry.STORED);
        dstJar.putNextEntry(dstEntry);
        dstJar.write(bytes);
    }
    
    /**
     * 加密文件
     * 
     * @param dstJar   目标文件
     * @param srcEntry 原始文件
     * @param srcInput 文件输入流
     * 
     * @throws IOException IO异常
     */
    private static final void encryptFile(JarOutputStream dstJar, JarEntry srcEntry, InputStream srcInput) throws IOException {
        final String name = srcEntry.getName();
        if(SPRING_BOOT_URL_RESOURCE.equals(name)) {
            log.debug("忽略文件：{}", name);
            return;
        }
        final JarEntry dstEntry = (JarEntry) srcEntry.clone();
//      dstEntry.setCrc(-1L);
//      dstEntry.setSize(-1L);
        dstEntry.setMethod(ZipEntry.DEFLATED);
        dstEntry.setCompressedSize(-1L);
        dstJar.setMethod(ZipEntry.DEFLATED);
        dstJar.putNextEntry(dstEntry);
        final byte[] buffer = srcInput.readAllBytes();
        final boolean encrypt = name.contains(FILE_PATH) && name.endsWith(FILE_SUFFIX);
        if(encrypt) {
            log.info("开始加密文件：{} - {} - {}", name, srcEntry.getSize(), srcEntry.getCompressedSize());
            dstJar.write(Encrypt.encrypt(buffer), 0, buffer.length);
        } else {
            dstJar.write(buffer, 0, buffer.length);
        }
    }
    
    /**
     * 解密
     * 
     * @param bytes 原始数据
     * 
     * @return 加密数据
     */
    public static final byte[] encrypt(byte[] bytes) {
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) (bytes[index] ^ MAGIC);
        }
        return bytes;
    }
    
    /**
     * 解密
     * 
     * @param bytes 加密数据
     * 
     * @return 原始数据
     */
    public static final byte[] decrypt(byte[] bytes) {
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) (bytes[index] ^ MAGIC);
        }
        return bytes;
    }
    
    /**
     * 自定义资源加载器
     * 
     * @param application application
     */
    public static final void customResourceLoader(SpringApplication application) {
        log.info("加载自定义资源加载器");
        application.setResourceLoader(
            new DefaultResourceLoader(new URLClassLoader(new URL[0], Application.class.getClassLoader()) {
                @Override
                public InputStream getResourceAsStream(String name) {
                    if (name.contains(Encrypt.FILE_PATH) && name.endsWith(Encrypt.FILE_SUFFIX)) {
                        log.debug("加载资源文件：{}", name);
                        try (final InputStream input = super.getResourceAsStream(name);) {
                            return new ByteArrayInputStream(Encrypt.decrypt(input.readAllBytes()));
                        } catch (Exception e) {
                            log.error("加载资源文件异常：{}", name);
                        }
                    }
                    return super.getResourceAsStream(name);
                }
            })
        );
    }
    
}