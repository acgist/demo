package com.acgist.ding;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具
 * 
 * @author acgist
 */
public final class Internationalization {

	/**
	 * 资源文件
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n", Locale.getDefault());
	
	/**
	 * 获取文本
	 * 
	 * @param key 名称
	 * 
	 * @return 文本
	 */
	public static final String get(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}

	/**
	 * 标签/选项
	 * 
	 * @author acgist
	 */
	public interface Label {
		
		/**
		 * 放松
		 */
		String REST = "label.rest";
		
		/**
		 * 颜色
		 */
		String COLOR = "label.color";
		
		/**
		 * 操作
		 */
		String OPERATION = "label.operation";
		
		/**
		 * 休息时长
		 */
		String REST_TIME = "label.rest.time";
		
		/**
		 * 配置
		 */
		String CONFIG = "label.config";
		
		/**
		 * 正则表达式
		 */
		String REGEX = "label.regex";
	
		/**
		 * AES/DES配置
		 */
		String AES_DES_CONFIG = "label.aes.des.config";
		
		/**
		 * 加密模式
		 */
		String AES_DES_MODEL = "label.aes.des.model";
		
		/**
		 * 填充方式
		 */
		String AES_DES_PADDING = "label.aes.des.padding";
		
		/**
		 * 数据偏移
		 */
		String AES_DES_IV = "label.aes.des.iv";
		
		/**
		 * AES配置
		 */
		String AES_CONFIG = "label.aes.config";
		
		/**
		 * 区块长度
		 */
		String AES_BLOCK = "label.aes.block";
		
		/**
		 * RSA配置
		 */
		String RSA_CONFIG = "label.rsa.config";
		
		/**
		 * 密钥位数
		 */
		String RSA_SIZE = "label.rsa.size";
		
		/**
		 * 密钥格式
		 */
		String RSA_FORMAT = "label.rsa.format";
		
		/**
		 * 加密算法
		 */
		String RSA_TRANSFORMATION = "label.rsa.transformation";
		
		/**
		 * 签名算法
		 */
		String RSA_SIGNATURE_ALGO = "label.rsa.signature.algo";
		
		/**
		 * 使用代理
		 */
		String PROVIDER = "label.provider";
		
		/**
		 * 输出格式
		 */
		String OUTPUT_FORMAT = "label.output.format";
		
		/**
		 * 加密密码
		 */
		String PASSWORD = "label.password";
		
		/**
		 * 数据类型
		 */
		String HTTP_TYPE = "label.http.type";
		
		/**
		 * 请求方法
		 */
		String HTTP_METHOD = "label.http.method";
		
		/**
		 * 数据编码
		 */
		String HTTP_CHARSET = "label.http.charset";
		
		/**
		 * 响应状态
		 */
		String HTTP_STATUS = "label.http.status";
		
		/**
		 * 质量
		 */
		String QUALITY = "label.quality";
		
		/**
		 * 二维码
		 */
		String QRCODE = "label.qrcode";
		
		/**
		 * 大小
		 */
		String SIZE = "label.size";
		
		/**
		 * 图片
		 */
		String IMAGE = "label.image";
		
		/**
		 * 排序
		 */
		String SORT = "label.sort";
		
		/**
		 * 编码
		 */
		String ENCODE = "label.encode";
		
		/**
		 * 解码
		 */
		String DECODE = "label.decode";
		
	}

	/**
	 * 按钮
	 * 
	 * @author acgist
	 */
	public interface Button {
		
		/**
		 * 重置
		 */
		String RESET = "button.reset";
		
		/**
		 * 定时休息
		 */
		String REST = "button.rest";
		
		/**
		 * 取消定时
		 */
		String REST_CANCEL = "button.rest.cancel";
		
		/**
		 * 屏幕取色
		 */
		String COLOR = "button.color";
		
		/**
		 * 关闭取色
		 */
		String COLOR_OFF = "button.color.off";
		
		/**
		 * 颜色Hex转RGB
		 */
		String COLOR_HEX_RGB = "button.color.hex.rgb";
		
		/**
		 * 颜色RGB转Hex
		 */
		String COLOR_RGB_HEX = "button.color.rgb.hex";
		
		/**
		 * Hex编码
		 */
		String HEX_ENCODE = "button.hex.encode";
		
		/**
		 * Hex解码
		 */
		String HEX_DECODE = "button.hex.decode";
		
		/**
		 * URL编码
		 */
		String URL_ENCODE = "button.url.encode";
		
		/**
		 * URL解码
		 */
		String URL_DECODE = "button.url.decode";
		
		/**
		 * Base64编码
		 */
		String BASE64_ENCODE = "button.base64.encode";
		
		/**
		 * Base64解码
		 */
		String BASE64_DECODE = "button.base64.decode";
		
		/**
		 * Unicode编码
		 */
		String UNICODE_ENCODE = "button.unicode.encode";
		
		/**
		 * Unicode解码
		 */
		String UNICODE_DECODE = "button.unicode.decode";
		
		/**
		 * 导入文件
		 */
		String FILE_IMPORT = "button.file.import";
		
		/**
		 * 导出文件
		 */
		String FILE_EXPORT = "button.file.export";
		
		/**
		 * 字体调整
		 */
		String FONT = "button.font";
		
		/**
		 * 编码调整
		 */
		String ENCODING = "button.encoding";
		
		/**
		 * 护眼模式
		 */
		String EYESHIELD = "button.eyeshield";
		
		/**
		 * 正则表达式提取
		 */
		String REGEX_FIND = "button.regex.find";
		
		/**
		 * 正则表达式匹配
		 */
		String REGEX_MATCH = "button.regex.match";
		
		/**
		 * MD5
		 */
		String MD5 = "button.md5";
		
		/**
		 * SHA
		 */
		String SHA = "button.sha";
		
		/**
		 * SHA-256
		 */
		String SHA256 = "button.sha-256";
		
		/**
		 * SHA-512
		 */
		String SHA512 = "button.sha-512";
		
		/**
		 * RSA生成密钥
		 */
		String RSA_BUILD = "button.rsa.build";
		
		/**
		 * RSA签名
		 */
		String RSA_SIGNATURE = "button.rsa.signature";
		
		/**
		 * RSA验签
		 */
		String RSA_VERIFY = "button.rsa.verify";
		
		/**
		 * RSA导入密钥
		 */
		String RSA_IMPORT = "button.rsa.import";
		
		/**
		 * RSA导出密钥
		 */
		String RSA_EXPORT = "button.rsa.export";
		
		/**
		 * RSA加密
		 */
		String RSA_ENCRYPT = "button.rsa.encrypt";
		
		/**
		 * RSA解密
		 */
		String RSA_DECRYPT = "button.rsa.decrypt";
		
		/**
		 * RSA私钥提取公钥
		 */
		String RSA_PRIVATE_PUBLIC = "button.rsa.private.public";
		
		/**
		 * RSA PKCS#1转换PKCS#8
		 */
		String RSA_PKCS1_PKCS8 = "button.rsa.pkcs1.pkcs8";
		
		/**
		 * RSA PKCS#8转换PKCS#1
		 */
		String RSA_PKCS8_PKCS1 = "button.rsa.pkcs8.pkcs1";
		
		/**
		 * AES加密
		 */
		String AES_ENCRYPT = "button.aes.encrypt";
		
		/**
		 * AES解密
		 */
		String AES_DECRYPT = "button.aes.decrypt";
		
		/**
		 * DES加密
		 */
		String DES_ENCRYPT = "button.des.encrypt";
		
		/**
		 * DES解密
		 */
		String DES_DECRYPT = "button.des.decrypt";
		
		/**
		 * 发送请求
		 */
		String HTTP_SUBMIT = "button.http.submit";
		
		/**
		 * 生成
		 */
		String BUILD = "button.build";
		
		/**
		 * 复制
		 */
		String COPY = "button.copy";
		
		/**
		 * 压缩
		 */
		String COMPRESS = "button.compress";
		
		/**
		 * 格式化
		 */
		String JSON_FORMAT = "button.json.format";
		
		/**
		 * 压缩
		 */
		String JSON_COMPRESS = "button.json.compress";
		
		/**
		 * 比较
		 */
		String JSON_COMPARE = "button.json.compare";
		
		/**
		 * URL转JSON
		 */
		String URL_TO_JSON = "button.url.to.json";
		
		/**
		 * JSON转URL
		 */
		String JSON_TO_URL = "button.json.to.url";
		
	}
	
	/**
	 * 消息提示
	 * 
	 * @author acgist
	 */
	public interface Message {
		
		/**
		 * 成功
		 */
		String SUCCESS = "message.success";
		
		/**
		 * 失败
		 */
		String FAIL = "message.fail";
		
		/**
		 * 没有实现功能
		 */
		String FUNCTION_DISABLE = "message.function.disable";
		
		/**
		 * 走一走
		 * 喝点水
		 * 去去厕所
		 * 看下远方
		 */
		String REST = "message.rest";
		
		/**
		 * 数据为空
		 */
		String DATA_EMPTY = "message.data.empty";
		
		/**
		 * 证书错误
		 */
		String CERTIFICATE_ERROR = "message.certificate.error";
	
		/**
		 * 选择文件
		 */
		String FILE_SELECT = "message.file.select";
		
		/**
		 * 选择目录
		 */
		String FOLDER_SELECT = "message.folder.select";
		
		/**
		 * 不支持的数据格式
		 */
		String DATA_NONSUPPORT = "message.data.nonsupport";
		
		/**
		 * 提示
		 */
		String TITLE_INFO = "message.title.info";
		
		/**
		 * 原始文本
		 */
		String CODEC_SOURCE = "message.codec.source";
		
		/**
		 * 编码文本
		 */
		String CODEC_TARGET = "message.codec.target";
		
		/**
		 * 原始文本或者Hex颜色
		 */
		String COMMON_SOURCE = "message.common.source";
		
		/**
		 * 正则表达式或者RGB颜色
		 */
		String COMMON_TARGET = "message.common.target";
		
		/**
		 * 匹配结果
		 */
		String COMMON_RESULT = "message.common.result";
		
		/**
		 * 原始文本
		 */
		String CRYPT_SOURCE = "message.crypt.source";
		
		/**
		 * 加密文本
		 */
		String CRYPT_TARGET = "message.crypt.target";
		
		/**
		 * 公钥
		 */
		String CRYPT_PUBLIC_KEY = "message.crypt.public.key";
		
		/**
		 * 私钥
		 */
		String CRYPT_PRIVATE_KEY = "message.crypt.private.key";
		
		/**
		 * 请求地址
		 */
		String HTTP_URL = "message.http.url";
		
		/**
		 * 请求头部
		 */
		String HTTP_HEADER = "message.http.header";
		
		/**
		 * 请求数据
		 */
		String HTTP_BODY = "message.http.body";
		
		/**
		 * 响应数据
		 */
		String HTTP_RESPONSE = "message.http.response";
		
		/**
		 * 原始文本
		 */
		String IMAGE_SOURCE = "message.image.source";
		
		/**
		 * 原始JSON
		 */
		String JSON_SOURCE = "message.json.source";
		
		/**
		 * 对比JSON或者URL
		 */
		String JSON_TARGET = "message.json.target";
		
	}
	
}
