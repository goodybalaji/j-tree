/* Copyright 2012 Werner Diwischek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.action4java.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * Some static utility classes....
 * <p>
* Please include a hint to this site to all the stuff you change or generate<br/>

* http://www.j-tree.org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.1
*/
public class Util {
	private static final Log logger = LogFactory.getLog(Util.class);
	private static SimpleDateFormat dayTimeFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
	private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");

	public Util() {
	}

	public static String getDateTime(Date date) {
			return dayTimeFormat.format(date);
	}
	
	public static String getDay(Date date) {
		return dayFormat.format(date);
	}
	
	public static String lowerFirstCharacter (String item) {
		if (item==null) {
			logger.error("String is null");
			return null;
		}
		
		int length=item.length();
		
		if (length==0) {
			logger.error("String is of zero size");
			return item;
		}
		
		String firstChanged    =  item.substring(0, 1).toLowerCase();
		//logger.info("firstChanged " + firstChanged);
		return firstChanged+item.substring(1);
	}
	
	public static String UpperFirstCharacter (String item) {
		if (item==null) {
			logger.error("String is null");
			return null;
		}
		
		int length=item.length();
		
		if (length==0) {
			logger.error("String is of zero size");
			return item;
		}
		
		String firstChanged    =  item.substring(0, 1).toUpperCase();
		//logger.info("firstChanged " + firstChanged);
		return firstChanged+item.substring(1);
	}


	
	public static String readFile (String fileName) {
		String result = "";
		logger.debug("Start reading " + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("File not found " + file.getPath());
			return result;
		}
		if (!file.isFile()) {
			logger.error("File is not a file " + file.getPath() );
			return result;
		}
		try {
			String content="";
			FileInputStream fis = new FileInputStream(fileName); 
			//UnicodeReader ucr = new UnicodeReader(fis,content);
			//logger.info("content " + content);
			//InputStreamReader in = new InputStreamReader(fis, "CP850");
			//InputStreamReader in = new InputStreamReader(fis, "ISO-8859-1");
			InputStreamReader in = new InputStreamReader(fis, "UTF-8");
			StringBuffer buffer= new StringBuffer("");
			BufferedReader br = new BufferedReader(in);
			String line = br.readLine();
			while (line !=null) {
				//logger.info("Line: " + line);
				buffer.append(line + "\n");

				line = br.readLine();
			}

			result=buffer.toString();
			br.close();
			in.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			logger.error("Could not load " + fileName,e);
		} catch (IOException e) {
			logger.error("Could not load " + fileName,e);
		}
		return result;
}	
	
	public static String writeFile (String fileName,String content) {
		String result = "";
		logger.info("Start writing " + fileName);
		File file = new File(fileName);

		try {
			FileWriter reader = new FileWriter(file);
			char[] temp = new char[(int) file.length()];

			reader.write(content);
			reader.close();
		} catch (FileNotFoundException e) {
			logger.error("Could not load " + fileName,e);
		} catch (IOException e) {
			logger.error("Could not load " + fileName,e);
		}

		//logger.info(result);
		return result;
}	
	
	public static boolean isElementary(String type) {
		
		if (type.equals("boolean")){return true;}
		else if (type.equals("Date"))   {return true;}
		
		else if (type.equals("double"))    {return true;}
		else if (type.equals("int"))       {return true;}
		else if (type.equals("long"))      {return true;}
		else if (type.equals("Short"))     {return true;}
		else if (type.equals("String"))    {return true;}
		else if (type.equals("String []")) {return true;}
		return false;
	}

	public static boolean isDirectlyComparable(String type) {
		if (type.equals("Short"))     {return true;}
		else if (type.equals("boolean")){return true;}
		else if (type.equals("double"))    {return true;}
		else if (type.equals("int"))       {return true;}
		else if (type.equals("long"))      {return true;}
		else if (type.equals("Short"))      {return true;}
		return false;
	}
	
	
	
	
	
	

	

}
