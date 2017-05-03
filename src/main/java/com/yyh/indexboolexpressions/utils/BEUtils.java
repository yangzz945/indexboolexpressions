/**
 * 
 */
package com.yyh.indexboolexpressions.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.yyh.indexboolexpressions.Assignment;
import com.yyh.indexboolexpressions.Conjunction;

/**
 * @author yyh
 *
 */
public class BEUtils {

	public static Conjunction assignmentStr2Conjunction(String assignStrs) {
		Conjunction c1=new Conjunction();
		List<Assignment> assignList=new ArrayList<Assignment>();
		for(String assignStr:assignStrs.split("&")) {
			String[] strs=assignStr.split(":");
			assignList.add(new Assignment(strs[0],"1".equals(strs[1]),strs[2]));
		//assignList.add(new Assignment("state",true,"NY"));
		}
 		c1.setAssignList(assignList);
 		return c1;
	}
	
	public static Object strDeserial2Obj(String serStr) {
		ByteArrayInputStream byteArrayInputStream=null;
		ObjectInputStream objectInputStream=null;
		Object obj=null;
		try {
		String redStr = java.net.URLDecoder.decode(serStr, "UTF-8"); 
        byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1")); 
        objectInputStream = new ObjectInputStream(byteArrayInputStream);  
        obj = objectInputStream.readObject();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(null!=objectInputStream) {
				try {
					objectInputStream.close();
				}catch(IOException  ioEx) {
					ioEx.printStackTrace();
				}
			}
			if(null!=byteArrayInputStream) {
				try {
					byteArrayInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return obj;
	}
	
	public static String objSerial2String(Object obj) {
		ByteArrayOutputStream byteArrayOutputStream=null;
		ObjectOutputStream objectOutputStream=null;
		String serStr="";
		try {
			byteArrayOutputStream = new ByteArrayOutputStream(); 
	        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream); 
	        objectOutputStream.writeObject(obj);   
	        serStr = byteArrayOutputStream.toString("ISO-8859-1"); 
	        serStr = java.net.URLEncoder.encode(serStr, "UTF-8"); 
	        objectOutputStream.close(); 
	        byteArrayOutputStream.close(); 
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(null!=objectOutputStream) {
				try {
					objectOutputStream.close();
				}catch(IOException  ioEx) {
					ioEx.printStackTrace();
				}
			}
			if(null!=byteArrayOutputStream) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return serStr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
