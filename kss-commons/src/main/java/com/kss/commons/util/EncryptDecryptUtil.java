/**
 * 
 */
package com.kss.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linrq
 *
 */
public class EncryptDecryptUtil {	
	private static Logger log = LoggerFactory.getLogger(EncryptDecryptUtil.class);
	private static String key = "@Q3f$6Ulem7*Hj5W";
	public static String encryptPwd(String pwd){
		String lenStr = pwd.length() > 9 ? String.valueOf(pwd.length()) : "0" + pwd.length();
		if(pwd.length() < 60){			
			String newPwd = lenStr + pwd + CommonUtil.getRandomStr(64-pwd.length()-2);
			return DESEncryptUtil.encrypt(newPwd, key);
			
		}else{
			return DESEncryptUtil.encrypt(lenStr + pwd, key);
		}
	}
	
	public static String decryptPwd(String enStr){
		String dePwd = DESEncryptUtil.decrypt(enStr, key);
		int len = Integer.valueOf(dePwd.substring(0, 2));
		return dePwd.substring(2, 2+len);
	}
	
	public static void main(String[] args) {
		//DDSAdvice test = new DDSAdvice();
		String enPwd = encryptPwd("a2R56xV0");
		log.debug("加密后：" + enPwd);
		String dePwd = decryptPwd("Or8O99D93AM9cpgPKNU42wxgwqRJ90lbtSikCKS8ubmChXJ/URJU8cCSJz2g9u4y/" +
				"9i+ygTJdfifGwvU3hDYjOTXLbraQNqT");
		log.debug("A 解密后" + dePwd);
		String dePwd1 = decryptPwd("irvuFO+gN31DePWEDTrg+G5qs+cLXILQho2b3/g6co8K/AF0vZCNByXr5lMy0gra" +
				"M9PZ1w9oA+S97m1iTYWfGOTXLbraQNqT");
		log.debug("B 解密后" + dePwd1);
	}

}
