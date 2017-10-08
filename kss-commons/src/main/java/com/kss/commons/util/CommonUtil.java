package com.kss.commons.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linrq
 *
 */
public class CommonUtil {
	private static final  byte[] HEX = "0123456789ABCDEF".getBytes();
	private static final char[] RANDOM_SEEND = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	

	/**
	 * 取当前系统时间,格式:yyyyMMddHHmmss，例如20080808122135
	 * @author huangyj 2010-05-10
	 * @return
	 */
	public static String getNow() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return sDateFormat.format(new Date());
	}

	/**
	 * 取格式化时间,格式:yyyyMMddHHmmss，例如20080808122135
	 * @author huangyj 2010-05-10
	 * @return
	 */
	public static String getFormatTimeString(Date date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return sDateFormat.format(date);
	}

	/**
	 * 从字节数组到十六进制字符串转换
	 * @author huangyj 2010-05-10
	 * @param b
	 * @return
	 */
	public static String byteTostr(byte[] b) {

		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = HEX[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = HEX[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 从十六进制字符串到字节数组转换
	 * @author huangyj 2010-05-10
	 * @param hexstr
	 * @return byte[]
	 */
	public static byte[] strTobyte(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char cA = hexstr.charAt(j++);
			char cB = hexstr.charAt(j++);
			b[i] = (byte) ((parseInt(cA) << 4) | parseInt(cB));
		}
		return b;
	}

	/**
	 * @author huangyj 2010-05-10
	 * @param c
	 * @return
	 */
	private static int parseInt(char c) {
//		if (c >= 'a')
//			return (c - 'a' + 10) & 0x0f;
//		if (c >= 'A')
//			return (c - 'A' + 10) & 0x0f;
//		return (c - '0') & 0x0f;
		return Integer.parseInt(String.valueOf(c), 16);
	}
	
	/**
	 * 判断抛出的异常是否是ORACLE的存储过程失效异常
	 * @author huangyj 2010-05-10
	 * @param e
	 * @return
	 */
	public static boolean isPackageDiscarded(Exception e){
		if(e==null){
			return false;
		}
		//调用JDBC的SQLException异常
		if(e instanceof SQLException){
			SQLException ex = (SQLException)e;
			return "72000".equals(ex.getSQLState()) && ex.getErrorCode() == 4068; 
		}
		//可能是由spring抛出的封装后的异常
		/*if(e instanceof SQLException){
			
			SQLException ex = ((UncategorizedSQLException)e).getSQLException();
			return "72000".equals(ex.getSQLState()) && ex.getErrorCode() == 4068; 
		}*/
		return false;
	}
	
	public static String getRandomStr(int len){
		Random random = new Random(System.currentTimeMillis());
		char[] randChars = new char[len];
		
		for(int i=0; i<len; i++){		
			randChars[i] = RANDOM_SEEND[Math.abs(random.nextInt() % (RANDOM_SEEND.length-1))];			
		}
		
		return new String(randChars);
	}
	
	public static String getPatternAttr(String str,String attr) {
		String temp = null;
		Pattern pattern = Pattern.compile(attr + "(.*?)" + attr);
		if(str != null){
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				temp = matcher.group(1).trim();
			}
		}
		return temp;
	}
	
	public static String getExceptionDetail(Throwable e, int maxLength){
    	StringWriter sw = new StringWriter();        
        e.printStackTrace(new PrintWriter(sw));
        String exDetail =  sw.toString();
        
        if(maxLength >0 && exDetail.length() > maxLength){
        	exDetail = exDetail.substring(0, maxLength); 
        }
        
        return exDetail;
    }
	
	public static Throwable getLastCause(Throwable e){
		Throwable prevEx = e;
		Throwable ex = prevEx.getCause();
		while(ex != null){
			prevEx = ex;
			ex = ex.getCause();
		}
		return prevEx;
	}
	
}
