/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.common.util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Base64Utils;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月23日 下午7:45:39
 * @version 1.0
 */
public class EncryptUtils {

	/**
	 * 对密码进行加密
	 * @author eko.zhan at 2017年12月23日 下午7:48:47
	 * @param password 明文
	 * @param salt 明文
	 * @return
	 */
	public static String encode(String password, String salt){
		String origin = password + "&" + salt;
        // 获得密文
		return DigestUtils.md5Hex(origin);
	}
	/**
	 * 验证用户密码准确性
	 * @author eko.zhan at 2017年12月23日 下午7:55:47
	 * @param input 用户输入的密码，明文传入
	 * @param target 数据库中存储的密码，密文，不可逆
	 * @param salt 数据库中存储的盐值，密文，可逆
	 * @return
	 */
	public static Boolean verify(String input, String target, String salt){
		salt = new String(Base64.decodeBase64(salt));
		String password = encode(input, salt);
		if (target.equals(password)){
			return true;
		}
		return false;
	}

	/**
	 * 针对明文进行 base64 可逆加密
	 * @author eko.zhan
	 * @date 2019/4/13 17:15
	 * @param token
	 * @return java.lang.String
	 */
	public static String encodeBase64(String token) {
		return new String(Base64Utils.encode(token.getBytes()));
	}
	/**
	 * 针对密文进行 base64 解密
	 * @author eko.zhan
	 * @date 2019/4/13 17:17
	 * @param raw
	 * @return java.lang.String
	 */
	public static String decodeBase64(String raw) {
        return new String(Base64Utils.decode(raw.getBytes()));
	}

}

