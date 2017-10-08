/**
 * 
 */
package com.kss.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author linrq
 *
 */
public class PropertiesUtil {
	public static Properties findProperties(String filename)throws IOException{
        Properties p = new Properties();        
        URL url = null;
		
        url = PropertiesUtil.class.getResource(filename);  
		if (url == null){
			// not found in package -> try from absolute path
			url = PropertiesUtil.class.getResource("/" + filename);
		}		
		if (url == null) {
			//if not found in app classpath: try what we always did:
			//lookup as a system resource, without extra prefix
			url = PropertiesUtil.class.getClassLoader().getSystemResource(filename);
		}                          
        if (url == null){
        	throw new IOException();
        }
      
        InputStream in = url.openStream();
        try{
        	p.load(in);
        }finally{
        	in.close();
        }
        return p;
    }
}
