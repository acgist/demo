package com.acgist.ding.view.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.function.Consumer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.io.pem.PemObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import com.acgist.ding.Config;
import com.acgist.ding.Dialogs;
import com.acgist.ding.DingView;
import com.acgist.ding.Exceptions;
import com.acgist.ding.Internationalization;
import com.acgist.ding.Strings;

/**
 * 加密解密
 * 
 * @author acgist
 */
public class CryptView extends DingView {
	
	/**
	 * PKCS#1公钥
	 */
	public static final String PKCS1_RSA_PUBLIC_KEY = "RSA PUBLIC KEY";
	/**
	 * PKCS#1私钥
	 */
	public static final String PKCS1_RSA_PRIVATE_KEY = "RSA PRIVATE KEY";
	/**
	 * PKCS#8公钥
	 */
	public static final String PKCS8_RSA_PUBLIC_KEY = "PUBLIC KEY";
	/**
	 * PKCS#8私钥
	 */
	public static final String PKCS8_RSA_PRIVATE_KEY = "PRIVATE KEY";
	/**
	 * PKCS#8加密私钥
	 */
	public static final String PKCS8_RSA_ENCRYPTED_PRIVATE_KEY = "ENCRYPTED PRIVATE KEY";
	/**
	 * PKCS#1私钥加密算法
	 */
	public static final String ENCRYPT_ALGO = "DES-EDE3-CBC";
	/**
	 * 公钥格式
	 */
	public static final String CER_TYPE = "X.509";
	/**
	 * 私钥格式
	 */
	public static final String PFX_TYPE = "PKCS12";
	/**
	 * AES
	 */
	private static final String AES = "AES";
	/**
	 * DES
	 */
	private static final String DES = "DES";
	/**
	 * RSA
	 */
	private static final String RSA = "RSA";
	/**
	 * 加密长度
	 */
	private static final int ENCRYPT_BLOCK = 117;
	/**
	 * 解密长度
	 */
	private static final int DECRYPT_BLOCK = 128;
	
	/**
	 * 原始数据
	 */
	private Text source;
	/**
	 * 加密数据
	 */
	private Text target;
	/**
	 * 公钥
	 */
	private Text publicKey;
	/**
	 * 私钥
	 */
	private Text privateKey;
	/**
	 * AES/DES加密模式
	 */
	private Combo aesDesModel;
	/**
	 * AES/DES填充方式
	 */
	private Combo aesDesPadding;
	/**
	 * AES/DES数据偏移
	 */
	private Text aesDesIv;
	/**
	 * AES区块长度
	 * None=使用密码直接加密
	 */
	private Combo aesBlock;
	/**
	 * RSA密钥位数
	 */
	private Combo rsaSize;
	/**
	 * RSA密钥格式
	 */
	private Combo rsaFormat;
	/**
	 * RSA加密算法
	 */
	private Combo rsaTransformation;
	/**
	 * RSA签名算法
	 */
	private Combo rsaSignatureAlgo;
	/**
	 * 输出格式
	 */
	private Combo outputFormat;
	/**
	 * 使用代理
	 */
	private Button provider;
	/**
	 * 加密密码
	 */
	private Text password;
	
	static {
		if(Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	@Override
	public void createPartControl(Composite composite) {
		this.layout(composite, SWT.VERTICAL);
		// 文本模块
		final Composite resultGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.source = this.buildMultiText(resultGroup, Internationalization.Message.CRYPT_SOURCE);
		this.target = this.buildMultiText(resultGroup, Internationalization.Message.CRYPT_TARGET);
		// AES/DES模块
		final Group operationGroup = this.buildGroup(composite, Internationalization.Label.CONFIG, 0, 0, SWT.VERTICAL);
		final Group aesDesConfigGroup = this.buildGroup(operationGroup, Internationalization.Label.AES_DES_CONFIG, 0, 0, SWT.VERTICAL);
		this.aesDesModel = this.buildSelect(aesDesConfigGroup, Internationalization.Label.AES_DES_MODEL, 0, "ECB", "CBC", "CTR", "OFB", "CFB");
		this.aesDesPadding = this.buildSelect(aesDesConfigGroup, Internationalization.Label.AES_DES_PADDING, 1, "ZeroPadding", "PKCS5Padding", "PKCS7Padding", "NoPadding");
		this.aesDesIv = this.buildText(aesDesConfigGroup, Internationalization.Label.AES_DES_IV);
		// AES模块
		final Group aesConfigGroup = this.buildGroup(operationGroup, Internationalization.Label.AES_CONFIG, 0, 0, SWT.VERTICAL);
		this.aesBlock = this.buildSelect(aesConfigGroup, Internationalization.Label.AES_BLOCK, 0, "None", "128", "192", "256");
		// RSA模块
		final Group rsaConfigGroup = this.buildGroup(operationGroup, Internationalization.Label.RSA_CONFIG, 0, 0, SWT.VERTICAL);
		this.rsaSize = this.buildSelect(rsaConfigGroup, Internationalization.Label.RSA_SIZE, 1, "512", "1024", "2048", "4096");
		this.rsaFormat = this.buildSelect(rsaConfigGroup, Internationalization.Label.RSA_FORMAT, 1, "PKCS#1", "PKCS#8");
		this.rsaTransformation = this.buildSelect(rsaConfigGroup, Internationalization.Label.RSA_TRANSFORMATION, 0, "RSA", "RSA/NONE/PKCS1Padding", "RSA/NONE/NoPadding");
		this.rsaSignatureAlgo = this.buildSelect(rsaConfigGroup, Internationalization.Label.RSA_SIGNATURE_ALGO, 0, "SHA1WithRSA", "SHA256WithRSA", "SHA1withRSAEncryption", "SHA256withRSAEncryption");
		// 通用配置模块
		final Group configGroup = this.buildGroup(operationGroup, Internationalization.Label.CONFIG, 0, 0, SWT.VERTICAL);
		this.outputFormat = this.buildSelect(configGroup, Internationalization.Label.OUTPUT_FORMAT, 0, "Base64", "Hex");
		this.provider = this.buildCheck(configGroup, Internationalization.Label.PROVIDER);
		this.password = this.buildText(configGroup, Internationalization.Label.PASSWORD);
		// 操作模块
		final Group buttonGroup = this.buildGroup(composite, Internationalization.Label.OPERATION, 0, 0, SWT.VERTICAL);
		// HASH模块
		final Composite hashGroup = this.buildRowComposite(buttonGroup, SWT.HORIZONTAL);
		this.buildButton(hashGroup, Internationalization.Button.MD5, this.md5Consumer);
		this.buildButton(hashGroup, Internationalization.Button.SHA, this.shaConsumer);
		this.buildButton(hashGroup, Internationalization.Button.SHA256, this.sha256Consumer);
		this.buildButton(hashGroup, Internationalization.Button.SHA512, this.sha512Consumer);
		// RSA模块
		final Composite rsaGroup = this.buildRowComposite(buttonGroup, SWT.HORIZONTAL);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_BUILD, this.rsaBuildConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_SIGNATURE, this.rsaSignatureConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_VERIFY, this.rsaVerifyConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_IMPORT, this.rsaImportConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_EXPORT, this.rsaExportConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_ENCRYPT, this.rsaEncryptConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_DECRYPT, this.rsaDecryptConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_PRIVATE_PUBLIC, this.rsaPrivatePublicConsumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_PKCS1_PKCS8, this.rsaPkcs1Pkcs8Consumer);
		this.buildButton(rsaGroup, Internationalization.Button.RSA_PKCS8_PKCS1, this.rsaPkcs8Pkcs1Consumer);
		// AES/DES模块
		final Composite aesDesGroup = this.buildRowComposite(buttonGroup, SWT.HORIZONTAL);
		this.buildButton(aesDesGroup, Internationalization.Button.AES_ENCRYPT, this.aesEncryptConsumer);
		this.buildButton(aesDesGroup, Internationalization.Button.AES_DECRYPT, this.aesDecryptConsumer);
		this.buildButton(aesDesGroup, Internationalization.Button.DES_ENCRYPT, this.desEncryptConsumer);
		this.buildButton(aesDesGroup, Internationalization.Button.DES_DECRYPT, this.desDecryptConsumer);
		// 二级操作模块
		final Composite subOperationGroup = this.buildRowComposite(buttonGroup, SWT.HORIZONTAL);
		this.buildButton(subOperationGroup, Internationalization.Button.RESET, this.resetConsumer);
		// 密钥模块
		final Composite keyGroup = this.buildFullComposite(composite, SWT.HORIZONTAL);
		this.publicKey = this.buildMultiText(keyGroup, Internationalization.Message.CRYPT_PUBLIC_KEY);
		this.privateKey = this.buildMultiText(keyGroup, Internationalization.Message.CRYPT_PRIVATE_KEY);
	}
	
	/**
	 * 判定是否Hex输出
	 * 
	 * @return 是否Hex输出
	 */
	private boolean hex() {
		return "Hex".equals(this.outputFormat.getText());
	}
	
	/**
	 * 读取数据
	 * 
	 * @param value 数据
	 * 
	 * @return 原始数据
	 */
	private byte[] input(String value) {
		if(this.hex()) {
			return Strings.unhex(value);
		} else {
			return Base64.getMimeDecoder().decode(value);
		}
	}
	
	/**
	 * 输出结果
	 * 
	 * @param value 结果
	 */
	private void output(Text text, byte[] value) {
		this.output(text, value, false);
	}
	
	/**
	 * 输出结果
	 * 
	 * @param value 结果
	 * @param append 是否追加
	 */
	private void output(Text text, byte[] value, boolean append) {
		if(this.hex()) {
			if(append) {
				text.append(Strings.hex(value));
			} else {
				text.setText(Strings.hex(value));
			}
		} else {
			if(append) {
				text.append(Base64.getMimeEncoder().encodeToString(value));
			} else {
				text.setText(Base64.getMimeEncoder().encodeToString(value));
			}
		}
	}
	
	/**
	 * 判定是否PKCS#1密钥
	 * 
	 * @return 是否PKCS#1密钥
	 */
	private boolean pkcs1() {
		return "PKCS#1".equals(this.rsaFormat.getText());
	}
	
	/**
	 * MD5
	 */
	private Consumer<MouseEvent> md5Consumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final byte[] bytes = this.hash("MD5", sourceValue);
			this.output(this.target, bytes);
		});
	};
	
	/**
	 * SHA-1
	 */
	private Consumer<MouseEvent> shaConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final byte[] bytes = this.hash("SHA-1", sourceValue);
			this.output(this.target, bytes);
		});
	};
	
	/**
	 * SHA-256
	 */
	private Consumer<MouseEvent> sha256Consumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final byte[] bytes = this.hash("SHA-256", sourceValue);
			this.output(this.target, bytes);
		});
	};
	
	/**
	 * SHA-512
	 */
	private Consumer<MouseEvent> sha512Consumer = event -> {
		this.consumer(this.source, sourceValue -> {
			final byte[] bytes = this.hash("SHA-512", sourceValue);
			this.output(this.target, bytes);
		});
	};
	
	/**
	 * RSA生成密钥
	 */
	private Consumer<MouseEvent> rsaBuildConsumer = event -> {
		try {
			final KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
			generator.initialize(Integer.parseInt(this.rsaSize.getText()));
			final KeyPair keyPair = generator.generateKeyPair();
			final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			byte[] publicKeyBytes = publicKey.getEncoded();
			byte[] privateKeyBytes = privateKey.getEncoded();
			if (this.pkcs1()) {
				publicKeyBytes = this.pkcs8ToPkcs1(false, publicKeyBytes);
				privateKeyBytes = this.pkcs8ToPkcs1(true, privateKeyBytes);
			}
			this.output(this.publicKey, publicKeyBytes);
			this.output(this.privateKey, privateKeyBytes);
		} catch (NoSuchAlgorithmException e) {
			Exceptions.error(this.getClass(), e);
		}
	};
	
	/**
	 * RSA签名
	 */
	private Consumer<MouseEvent> rsaSignatureConsumer = event -> {
		this.consumer(this.privateKey, this.source, (privateKeyValue, sourceValue) -> {
			try {
				final Signature signature = Signature.getInstance(this.rsaSignatureAlgo.getText());
				signature.initSign(this.loadPrivateKey(privateKeyValue));
				signature.update(sourceValue.getBytes());
				this.output(this.target, signature.sign());
			} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * RSA验签
	 */
	private Consumer<MouseEvent> rsaVerifyConsumer = event -> {
		this.consumer(this.source, this.target, (sourceValue, targetValue) -> {
			final String publicKeyValue = this.publicKey.getText();
			if(Strings.isEmpty(publicKeyValue)) {
				Dialogs.info(Internationalization.Message.DATA_EMPTY);
				return;
			}
			boolean success = false;
			try {
				final Signature signature = Signature.getInstance(this.rsaSignatureAlgo.getText());
				signature.initVerify(this.loadPublicKey(publicKeyValue));
				signature.update(sourceValue.getBytes());
				success = signature.verify(this.input(targetValue));
			} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
				Exceptions.error(this.getClass(), e);
			}
			if(success) {
				Dialogs.info(Internationalization.Message.SUCCESS);
			} else {
				Dialogs.info(Internationalization.Message.FAIL);
			}
		});
	};
	
	/**
	 * 证书导入
	 */
	private Consumer<MouseEvent> rsaImportConsumer = event -> {
		final String file = Dialogs.file("*.pfx;*.p12;*.jks;*.cer;*.crt;*.pem");
		if(file == null) {
			return;
		}
		try {
			this.publicKey.setText(Config.EMPTY);
			this.privateKey.setText(Config.EMPTY);
			if(file.endsWith("pem")) {
				this.importPem(file);
			} else if(file.endsWith("pfx") || file.endsWith("p12") || file.endsWith("jks")) {
				this.importPfx(file);
			} else if(file.endsWith("cer") || file.endsWith("crt")) {
				this.importCer(file);
			} else {
				Dialogs.info(Internationalization.Message.DATA_NONSUPPORT);
			}
		} catch (IOException | PKCSException | OperatorCreationException | NoSuchAlgorithmException | InvalidKeySpecException | UnrecoverableKeyException | KeyStoreException | CertificateException e) {
			Exceptions.error(this.getClass(), e);
		}
	};
	
	/**
	 * 证书导出
	 */
	private Consumer<MouseEvent> rsaExportConsumer = event -> {
		this.consumer(this.publicKey, this.privateKey, (publicKeyValue, privateKeyValue) -> {
			final String folder = Dialogs.folder();
			if(folder == null) {
				return;
			}
			final PublicKey publicKey = this.loadPublicKey(publicKeyValue);
			final PrivateKey privateKey = this.loadPrivateKey(privateKeyValue);
			try {
				this.exportPem(folder, publicKey, privateKey);
				this.exportPfxCer(folder, publicKey, privateKey);
			} catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | OperatorCreationException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * RSA加密
	 */
	private Consumer<MouseEvent> rsaEncryptConsumer = event -> {
		this.consumer(this.publicKey, this.source, (publicKeyValue, sourceValue) -> {
			int index = 0;
			final byte[] data = sourceValue.getBytes();
			final int length = data.length;
			try {
				final Cipher cipher = this.rsaCipher();
				cipher.init(Cipher.ENCRYPT_MODE, this.loadPublicKey(publicKeyValue));
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				while(index < length) {
					if(length - index > ENCRYPT_BLOCK) {
						out.write(cipher.update(data, index, ENCRYPT_BLOCK));
					} else {
						out.write(cipher.doFinal(data, index, length - index));
					}
					index += ENCRYPT_BLOCK;
				}
				this.output(this.target, out.toByteArray());
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * RSA解密
	 */
	private Consumer<MouseEvent> rsaDecryptConsumer = event -> {
		this.consumer(this.privateKey, this.target, (privateKeyValue, targetValue) -> {
			int index = 0;
			final byte[] data = this.input(targetValue);
			final int length = data.length;
			try {
				final Cipher cipher = this.rsaCipher();
				cipher.init(Cipher.DECRYPT_MODE, this.loadPrivateKey(privateKeyValue));
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				while(index < length) {
					if(length - index > DECRYPT_BLOCK) {
						out.write(cipher.update(data, index, DECRYPT_BLOCK));
					} else {
						out.write(cipher.doFinal(data, index, length - index));
					}
					index += DECRYPT_BLOCK;
				}
				this.source.setText(new String(out.toByteArray()));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * 私钥提取公钥
	 */
	private Consumer<MouseEvent> rsaPrivatePublicConsumer = event -> {
		this.consumer(this.privateKey, privateKeyValue -> {
			final RSAPrivateKey privateKey = (RSAPrivateKey) this.loadPrivateKey(privateKeyValue);
			final RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(privateKey.getModulus(), BigInteger.valueOf(65537));
			try {
				final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
				final PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
				byte[] publicKeyBytes = publicKey.getEncoded();
				if(this.pkcs1()) {
					publicKeyBytes = this.pkcs8ToPkcs1(false, publicKeyBytes);
				}
				this.output(this.publicKey, publicKeyBytes);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * PKCS#1 To PKCS#8
	 */
	private Consumer<MouseEvent> rsaPkcs1Pkcs8Consumer = event -> {
		this.consumer(this.publicKey, this.privateKey, (publicKeyValue, privateKeyValue) -> {
			final byte[] publicKeyBytes = this.input(publicKeyValue);
			this.output(this.source, this.pkcs1ToPkcs8(false, publicKeyBytes));
			final byte[] privateKeyBytes = this.input(privateKeyValue);
			this.output(this.target, this.pkcs1ToPkcs8(true, privateKeyBytes));
		});
	};
	
	/**
	 * PKCS#8 To PKCS#1
	 */
	private Consumer<MouseEvent> rsaPkcs8Pkcs1Consumer = event -> {
		this.consumer(this.publicKey, this.privateKey, (publicKeyValue, privateKeyValue) -> {
			final byte[] publicKeyBytes = this.input(publicKeyValue);
			this.output(this.source, this.pkcs8ToPkcs1(false, publicKeyBytes));
			final byte[] privateKeyBytes = this.input(privateKeyValue);
			this.output(this.target, this.pkcs8ToPkcs1(true, privateKeyBytes));
		});
	};
	
	/**
	 * AES加密
	 */
	private Consumer<MouseEvent> aesEncryptConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			try {
				final Cipher cipher = this.aesCipher(Cipher.ENCRYPT_MODE);
				final byte[] targetValue = cipher.doFinal(sourceValue.getBytes());
				this.output(this.target, targetValue);
			} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * AES解密
	 */
	private Consumer<MouseEvent> aesDecryptConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			try {
				final Cipher cipher = this.aesCipher(Cipher.DECRYPT_MODE);
				final String sourceValue = new String(cipher.doFinal(this.input(targetValue)));
				this.source.setText(sourceValue);
			} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * DES加密
	 */
	private Consumer<MouseEvent> desEncryptConsumer = event -> {
		this.consumer(this.source, sourceValue -> {
			try {
				final Cipher cipher = this.desCipher(Cipher.ENCRYPT_MODE);
				final byte[] targetValue = cipher.doFinal(sourceValue.getBytes());
				this.output(this.target, targetValue);
			} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * DES解密
	 */
	private Consumer<MouseEvent> desDecryptConsumer = event -> {
		this.consumer(this.target, targetValue -> {
			try {
				final Cipher cipher = this.desCipher(Cipher.DECRYPT_MODE);
				final String sourceValue = new String(cipher.doFinal(this.input(targetValue)));
				this.source.setText(sourceValue);
			} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException | NoSuchProviderException e) {
				Exceptions.error(this.getClass(), e);
			}
		});
	};
	
	/**
	 * 重置
	 */
	private Consumer<MouseEvent> resetConsumer = event -> {
		this.source.setText(Config.EMPTY);
		this.target.setText(Config.EMPTY);
		this.publicKey.setText(Config.EMPTY);
		this.privateKey.setText(Config.EMPTY);
		this.aesDesIv.setText(Config.EMPTY);
		this.password.setText(Config.EMPTY);
	};
	
	/**
	 * HASH
	 * 
	 * @param algo 算法
	 * @param value 原始数据
	 * 
	 * @return 结果数据
	 */
	private byte[] hash(String algo, String value) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(algo);
			return digest.digest(value.getBytes());
		} catch (NoSuchAlgorithmException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}

	/**
	 * 加载公钥
	 * 
	 * @param value 公钥数据
	 * 
	 * @return 公钥
	 */
	private PublicKey loadPublicKey(String value) {
		final byte[] bytes = this.input(value);
		try {
			if (this.pkcs1()) {
				return this.loadPublicKey(this.pkcs1ToPkcs8(false, bytes));
			} else {
				return this.loadPublicKey(bytes);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}
	
	/**
	 * 加载公钥
	 * 
	 * @param bytes 公钥数据
	 * 
	 * @return 公钥
	 * 
	 * @throws NoSuchAlgorithmException 未知算法
	 * @throws InvalidKeySpecException 错误密钥
	 */
	private PublicKey loadPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
		final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return keyFactory.generatePublic(x509EncodedKeySpec);
	}
	
	/**
	 * 加载私钥
	 * 
	 * @param value 私钥数据
	 * 
	 * @return 私钥
	 */
	private PrivateKey loadPrivateKey(String value) {
		final byte[] bytes = this.input(value);
		PrivateKey privateKey = null;
		try {
			if(this.pkcs1()) {
				privateKey = this.loadPrivateKey(this.pkcs1ToPkcs8(true, bytes));
			} else {
				privateKey = this.loadPrivateKey(bytes);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Exceptions.error(this.getClass(), e);
		}
		return privateKey;
	}
	
	/**
	 * 加载私钥
	 * 
	 * @param bytes 私钥数据
	 * 
	 * @return 私钥
	 * 
	 * @throws NoSuchAlgorithmException 未知算法
	 * @throws InvalidKeySpecException 错误密钥
	 */
	private PrivateKey loadPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
		final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	}
	
	/**
	 * PKCS#1转为PKCS#8
	 * 
	 * @param privateKey 是否私钥
	 * @param bytes PKCS#1密钥
	 * EncryptedPrivateKeyInfo
	 * @return PKCS#8密钥
	 */
	private byte[] pkcs1ToPkcs8(boolean privateKey, byte[] bytes) {
		try {
			if (privateKey) {
				try(
					final Reader reader = new StringReader(this.toPemString(PKCS1_RSA_PRIVATE_KEY, bytes));
					final PEMParser pemParser = new PEMParser(reader);
				) {
					final Object object = pemParser.readObject();
					if (object instanceof PEMKeyPair pemKeyPair) {
						final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
						return pemKeyConverter.getPrivateKey(pemKeyPair.getPrivateKeyInfo()).getEncoded();
					}
				}
			} else {
				try(
					final Reader reader = new StringReader(this.toPemString(PKCS1_RSA_PUBLIC_KEY, bytes));
					final PEMParser pemParser = new PEMParser(reader);
				) {
					final Object object = pemParser.readObject();
					if (object instanceof SubjectPublicKeyInfo subjectPublicKeyInfo) {
						return subjectPublicKeyInfo.getEncoded();
					}
				}
			}
//			下面代码不能使用：转换结果错误
//			final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);
//			final ASN1Primitive asn1Primitive = ASN1Primitive.fromByteArray(bytes);
//			if (privateKey) {
//				final PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Primitive);
//				return privateKeyInfo.getEncoded();
//			} else {
//				final SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, asn1Primitive);
//				return subjectPublicKeyInfo.getEncoded();
//			}
		} catch (IOException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}
	
	/**
	 * PKCS#8转PKCS#1
	 * 
	 * @param privateKey 是否私钥
	 * @param bytes PKCS#8密钥
	 * 
	 * @return PKCS#1密钥
	 */
	private byte[] pkcs8ToPkcs1(boolean privateKey, byte[] bytes) {
		try {
			if (privateKey) {
				final PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(bytes);
				return privateKeyInfo.parsePrivateKey().toASN1Primitive().getEncoded();
			} else {
				final SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(bytes);
				return subjectPublicKeyInfo.parsePublicKey().toASN1Primitive().getEncoded();
			}
		} catch (IOException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}
	
	/**
	 * 加载Pem证书
	 * 
	 * @param path 文件路径
	 * 
	 * @throws IOException IO异常 
	 * @throws PKCSException PKCS异常
	 * @throws FileNotFoundException 文件异常 
	 * @throws OperatorCreationException 操作异常
	 * @throws InvalidKeySpecException 密钥异常
	 * @throws NoSuchAlgorithmException 算法异常
	 */
	private void importPem(String path) throws IOException, PKCSException, FileNotFoundException, OperatorCreationException, NoSuchAlgorithmException, InvalidKeySpecException {
		try(
			final PEMParser pemParser = new PEMParser(new FileReader(path));
		) {
			Object object;
			while((object = pemParser.readObject()) != null) {
				final JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
				if (object instanceof PEMEncryptedKeyPair pemEncryptedKeyPair) {
					final String password = this.password.getText();
					final PEMDecryptorProvider pemDecryptorProvider = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
					final KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemEncryptedKeyPair.decryptKeyPair(pemDecryptorProvider));
					this.output(this.publicKey, keyPair.getPublic().getEncoded());
					this.output(this.privateKey, keyPair.getPrivate().getEncoded());
				} else if (object instanceof PEMKeyPair pemKeyPair) {
					final KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
					this.output(this.publicKey, keyPair.getPublic().getEncoded());
					this.output(this.privateKey, keyPair.getPrivate().getEncoded());
				} else if(object instanceof PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo) {
					final String password = this.password.getText();
					final JceOpenSSLPKCS8DecryptorProviderBuilder jceOpenSSLPKCS8DecryptorProviderBuilder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
					final InputDecryptorProvider inputDecryptorProvider = jceOpenSSLPKCS8DecryptorProviderBuilder.build(password.toCharArray());
					final PrivateKeyInfo privateKeyInfo = pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(inputDecryptorProvider);
					if(privateKeyInfo.hasPublicKey()) {
						this.output(this.publicKey, privateKeyInfo.getPublicKeyData().getEncoded());
					} else {
						this.output(this.publicKey, this.privateKeyToPublicKey(privateKeyInfo));
					}
					this.output(this.privateKey, jcaPEMKeyConverter.getPrivateKey(privateKeyInfo).getEncoded());
				} else if (object instanceof PrivateKeyInfo privateKeyInfo) {
					if(privateKeyInfo.hasPublicKey()) {
						this.output(this.publicKey, privateKeyInfo.getPublicKeyData().getEncoded());
					} else {
						this.output(this.publicKey, this.privateKeyToPublicKey(privateKeyInfo));
					}
					this.output(this.privateKey, jcaPEMKeyConverter.getPrivateKey(privateKeyInfo).getEncoded());
				} else if (object instanceof SubjectPublicKeyInfo subjectPublicKeyInfo) {
					// 方法一
					this.output(this.publicKey, subjectPublicKeyInfo.getEncoded());
					// 方法二
//					this.output(this.publicKey, jcaPEMKeyConverter.getPublicKey(subjectPublicKeyInfo).getEncoded());
				}
			}
		}
	}
	
	/**
	 * 加载PFX证书
	 * 
	 * @param path 文件路径
	 * 
	 * @throws IOException IO异常
	 * @throws KeyStoreException 密钥异常
	 * @throws CertificateException 证书异常
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws UnrecoverableKeyException 密钥异常
	 */
	private void importPfx(String path) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
		try (
			final InputStream input = new FileInputStream(path);
		) {
			final KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
			final String password = this.password.getText();
			keyStore.load(input, password.toCharArray());
			final Enumeration<String> aliases = keyStore.aliases();
			String aliase = null;
			while(aliases.hasMoreElements()) {
				aliase = aliases.nextElement();
				final PublicKey publicKey = keyStore.getCertificate(aliase).getPublicKey();
				final PrivateKey privateKey = (PrivateKey) keyStore.getKey(aliase, password.toCharArray());
				this.output(this.publicKey, publicKey.getEncoded(), true);
				this.output(this.privateKey, privateKey.getEncoded(), true);
			}
		}
	}
	
	/**
	 * 加载CER证书
	 * 
	 * @param path 文件路径
	 * 
	 * @throws CertificateException 证书异常
	 * @throws FileNotFoundException 文件异常
	 * @throws IOException IO异常
	 */
	private void importCer(String path) throws CertificateException, FileNotFoundException, IOException {
		try (
			final InputStream input = new FileInputStream(path);
		) {
			final CertificateFactory certificatefactory = CertificateFactory.getInstance(CER_TYPE);
			final Certificate certificate = certificatefactory.generateCertificate(input);
			this.output(this.publicKey, certificate.getPublicKey().getEncoded());
		}
	}
	
	/**
	 * 私钥读取公钥
	 * 
	 * @param privateKeyInfo 私钥
	 * 
	 * @return 公钥
	 * 
	 * @throws IOException IO异常
	 * @throws InvalidKeySpecException 密钥异常
	 * @throws NoSuchAlgorithmException 算法异常 
	 */
	private byte[] privateKeyToPublicKey(PrivateKeyInfo privateKeyInfo) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		final ASN1Encodable asn1Encodable = privateKeyInfo.parsePrivateKey();
		final org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(asn1Encodable);
		// 方法一
//		final org.bouncycastle.asn1.pkcs.RSAPublicKey rsaPublicKey = new org.bouncycastle.asn1.pkcs.RSAPublicKey(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
//		return this.pkcs1ToPkcs8(false, rsaPublicKey.getEncoded());
		// 方法二
		final RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
		final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		return keyFactory.generatePublic(rsaPublicKeySpec).getEncoded();
	}
	
	/**
	 * 导出PEM
	 * 
	 * @param path 路径
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * 
	 * @throws IOException IO异常 
	 * @throws OperatorCreationException 创建异常 
	 */
	private void exportPem(String path, PublicKey publicKey, PrivateKey privateKey) throws IOException, OperatorCreationException {
		final String password = this.password.getText();
		final boolean encrypt = Strings.isNotEmpty(password);
		try(
			final JcaPEMWriter publicKeyWriter = new JcaPEMWriter(new FileWriter(Paths.get(path, "publicKey.pem").toFile()));
			final JcaPEMWriter privateKeyWriter = new JcaPEMWriter(new FileWriter(Paths.get(path, "privateKey.pem").toFile()));
		) {
			if(this.pkcs1()) {
				publicKeyWriter.writeObject(new PemObject(PKCS1_RSA_PUBLIC_KEY, this.pkcs8ToPkcs1(false, publicKey.getEncoded())));
				if(encrypt) {
					final JcePEMEncryptorBuilder jcePEMEncryptorBuilder = new JcePEMEncryptorBuilder(ENCRYPT_ALGO);
					jcePEMEncryptorBuilder.setSecureRandom(new SecureRandom());
					privateKeyWriter.writeObject(privateKey, jcePEMEncryptorBuilder.build(password.toCharArray()));
				} else {
					privateKeyWriter.writeObject(privateKey);
				}
			} else {
				publicKeyWriter.writeObject(publicKey);
				if(encrypt) {
					final JceOpenSSLPKCS8EncryptorBuilder openSSLPKCS8EncryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.PBE_SHA1_3DES);
					openSSLPKCS8EncryptorBuilder.setRandom(new SecureRandom());
					openSSLPKCS8EncryptorBuilder.setPassword(password.toCharArray());
					final JcaPKCS8Generator jcaPKCS8Generator = new JcaPKCS8Generator(privateKey, openSSLPKCS8EncryptorBuilder.build());
					privateKeyWriter.writeObject(jcaPKCS8Generator.generate());
				} else {
					final JcaPKCS8Generator jcaPKCS8Generator = new JcaPKCS8Generator(privateKey, null);
					privateKeyWriter.writeObject(jcaPKCS8Generator.generate());
				}
			}
		}
	}
	
	/**
	 * 导出CER/PFX证书
	 * 
	 * @param path 路径
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * 
	 * @throws IOException IO异常
	 * @throws KeyStoreException 证书异常
	 * @throws CertificateException 证书异常
	 * @throws FileNotFoundException 文件异常
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws OperatorCreationException 操纵异常
	 */
	private void exportPfxCer(String path, PublicKey publicKey, PrivateKey privateKey) throws IOException, KeyStoreException, CertificateException, FileNotFoundException, NoSuchAlgorithmException, OperatorCreationException {
		try(
			OutputStream cerOutput = new FileOutputStream(Paths.get(path, "publicKey.cer").toFile());
			OutputStream pfxOutput = new FileOutputStream(Paths.get(path, "privateKey.pfx").toFile());
		) {
			// 有效时间
			final long validity = 365L * 24 * 60 * 60 * 1000;
			final long currentTimeMillis = System.currentTimeMillis();
			// 别名
			final String alias = "acgist.ding";
//			CN=姓名和姓氏, OU=组织单位名称, O=组织名称, L=城市或区域名称, ST=省/市/自治区名称, C=单位的双字母国家/地区代码
			// 颁发者
			final String issuer = "CN=acgist, OU=acgist.com, O=acgist, L=GZ, ST=GD, C=CN";
			// 使用者
			final String subject = "CN=ding, OU=acgist.com, O=acgist, L=GZ, ST=GD, C=CN";
			final X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(
				// 颁发者
				new X500Name(issuer),
				// 序列号
				new BigInteger(String.valueOf(currentTimeMillis)),
				// 签发时间
				new Date(currentTimeMillis),
				// 有效时间
				new Date(currentTimeMillis + validity),
				// 使用者
				new X500Name(subject),
				// 证书信息
				SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKey.getEncoded()))
			);
			// 添加签名
			final ContentSigner contentSigner = new JcaContentSignerBuilder(this.rsaSignatureAlgo.getText()).build(privateKey);
			final X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(contentSigner);
			// 公钥生成
			cerOutput.write(x509CertificateHolder.getEncoded());
			// 私钥生成
			final String password = this.password.getText();
			final KeyStore keyStore = KeyStore.getInstance(PFX_TYPE);
			keyStore.load(null, password.toCharArray());
			final CertificateFactory certificateFactory = CertificateFactory.getInstance(CER_TYPE);
			final Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(x509CertificateHolder.getEncoded()));
			keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[] { certificate });
			keyStore.store(pfxOutput, password.toCharArray());
		}
	}
	
	/**
	 * 获取PEM内容
	 * 
	 * @param type 类型
	 * @param value 属性
	 * 
	 * @return PEM内容
	 */
	private String toPemString(String type, byte[] value) {
		try(
			final StringWriter stringWriter = new StringWriter();
			final JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(stringWriter);
		) {
			jcaPEMWriter.writeObject(new PemObject(type, value));
			jcaPEMWriter.flush();
			return stringWriter.toString();
		} catch (IOException e) {
			Exceptions.error(this.getClass(), e);
		}
		return null;
	}
	
	/**
	 * 获取RSA加密套件
	 * 
	 * @return RSA加密套件
	 * 
	 * @throws NoSuchPaddingException 填充异常 
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws NoSuchProviderException 代理异常
	 */
	private Cipher rsaCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
		final String transformation = this.rsaTransformation.getText();
		if(this.provider.getSelection()) {
			return Cipher.getInstance(transformation, BouncyCastleProvider.PROVIDER_NAME);
		} else {
			return Cipher.getInstance(transformation);
		}
	}
	
	/**
	 * 获取AES加密套件
	 * 
	 * @param mode 模式
	 * 
	 * @return AES加密套件
	 * 
	 * @throws InvalidKeyException 密钥异常
	 * @throws NoSuchPaddingException 填充异常
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws InvalidAlgorithmParameterException 算法异常
	 * @throws NoSuchProviderException 代理异常
	 */
	private Cipher aesCipher(int mode) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
		final String block = this.aesBlock.getText();
		final String iv = this.aesDesIv.getText();
		final String model = this.aesDesModel.getText();
		final String padding = this.aesDesPadding.getText();
		final String password = this.password.getText();
		final Cipher cipher;
		final SecretKey secretKey;
		if("None".equals(block)) {
			secretKey = new SecretKeySpec(password.getBytes(), AES);
		} else {
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			final SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			keyGenerator.init(Integer.parseInt(block), secureRandom);
//			不能使用下面的随机数：每次结果都不一样
//			keyGenerator.init(Integer.parseInt(block), new SecureRandom(password.getBytes()));
			secretKey = keyGenerator.generateKey();
		}
		if(this.provider.getSelection()) {
			cipher = Cipher.getInstance(AES + "/" + model + "/" + padding, BouncyCastleProvider.PROVIDER_NAME);
		} else {
			cipher = Cipher.getInstance(AES + "/" + model + "/" + padding);
		}
		if(Strings.isEmpty(iv)) {
			cipher.init(mode, secretKey);
		} else {
			final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
			cipher.init(mode, secretKey, ivParameterSpec);
		}
		return cipher;
	}
	
	/**
	 * 获取DES加密套件
	 * 
	 * @param mode 模式
	 * 
	 * @return DES加密套件
	 * 
	 * @throws InvalidKeyException 密钥异常
	 * @throws NoSuchPaddingException 填充异常
	 * @throws NoSuchProviderException 代理异常
	 * @throws InvalidKeySpecException 密钥异常
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws InvalidAlgorithmParameterException 算法异常
	 */
	private Cipher desCipher(int mode) throws InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
		final String iv = this.aesDesIv.getText();
		final String model = this.aesDesModel.getText();
		final String padding = this.aesDesPadding.getText();
		final String password = this.password.getText();
		final Cipher cipher;
		if(this.provider.getSelection()) {
			cipher = Cipher.getInstance(DES + "/" + model + "/" + padding, BouncyCastleProvider.PROVIDER_NAME);
		} else {
			cipher = Cipher.getInstance(DES + "/" + model + "/" + padding);
		}
		final DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
		final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
        final SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        if(Strings.isEmpty(iv)) {
        	cipher.init(mode, secretKey);
        } else {
        	final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        	cipher.init(mode, secretKey, ivParameterSpec);
        }
        return cipher;
	}
	
}
