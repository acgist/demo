package com.jsh.erp.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author acgist
 */
@RestController
@RequestMapping(value = "/cert")
@Api(tags = { "生成证书" })
public class CertController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CertController.class);
    
    @GetMapping(value = "/build")
    @ApiOperation(value = "生成证书")
    public void batchSetStatus(String[] hosts, String password, HttpServletResponse response) throws Exception {
        final String host = hosts[0];
        final AtomicInteger index = new AtomicInteger(2);
        final String[] commands = new String[] {
            "mkdir -p /tmp/" + host,
            "cd /tmp/" + host,
            "echo \"keyUsage = nonRepudiation, digitalSignature, keyEncipherment\r\n" +
            "extendedKeyUsage = serverAuth, clientAuth\r\n"                           +
            "subjectAltName=@SubjectAlternativeName\r\n"                              +
            "\r\n"                                                                    +
            "[ SubjectAlternativeName ]\r\n"                                          +
            "IP.1=127.0.0.1\r\n"                                                      +
            "" + Stream.of(hosts).map(v -> "IP." + index.getAndIncrement() + "=" + v).collect(Collectors.joining("\r\n")) + "\" > " + host + ".ext",
            "openssl genrsa -out " + host + ".key 2048",
            "openssl req -new -key " + host + ".key -out " + host + ".csr -subj \"/C=cn/ST=gd/L=gz/O=acgist/OU=taoyao/CN=" + host + "\"",
            "openssl x509 -req -in " + host + ".csr -out " + host + ".crt -CA /data/cert/ca.crt -CAkey /data/cert/ca.key -CAcreateserial -days 3650 -extfile " + host + ".ext",
            "openssl x509 -in " + host + ".crt -subject -issuer -noout",
            "openssl pkcs12 -passout pass:" + password + " -export -in " + host + ".crt -inkey " + host + ".key -out " + host + ".p12",
            "keytool -srcstorepass " + password + " -deststorepass " + password + " -importkeystore -v -srckeystore " + host + ".p12 -srcstoretype pkcs12 -destkeystore " + host + ".jks -deststoretype jks"
        };
        final Process process = Runtime.getRuntime().exec(new String[] {
            "/bin/bash",
            "-c",
            String.join(";", commands)
        });
        LOGGER.info("生成证书：{}", String.join(";", commands));
        process.waitFor();
        process.destroy();
        final String[] exts = new String[] {
            ".crt", ".csr", ".ext", ".jks", ".key", ".p12"
        };
        try (
            final OutputStream output = response.getOutputStream();
            final ZipOutputStream zipOutput = new ZipOutputStream(output);
        ) {
            /* 设置ContentType字段告知浏览器返回内容类型 */
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + host + ".zip");
            for (String ext : exts) {
                try (
                    final InputStream input = Files.newInputStream(Paths.get("/tmp", host, host + ext));
                ) {
                    zipOutput.putNextEntry(new ZipEntry(host + ext));
                    IOUtils.copy(input, zipOutput);
                    zipOutput.flush();
                }
            }
        }
    }
    
}
