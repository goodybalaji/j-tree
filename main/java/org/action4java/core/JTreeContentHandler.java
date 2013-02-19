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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;


import org.action4java.core.map.JTreeMapString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
* A XML-Loader for JTreeMap to map any xml structure to JTree map at a certain pointer which allows to load several xml files
*
* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www.j-tree.org<br/>
* date 28.3.2012<br/>
* @author Werner Diwischek
* @version 0.1
*/



public class JTreeContentHandler implements ContentHandler  {
	private Logger logger = Logger.getLogger(this.getClass());
	private String currentValue;
	JTreeMapString map=null;
	String pointer="";
	
	public JTreeContentHandler () {
	}
	
	public JTreeContentHandler (JTreeMapString map,String pointer) {
		this.map=map;
		this.pointer=pointer;
	}
	
  public void characters(char[] ch, int start, int length)  throws SAXException {
    currentValue = new String(ch, start, length);
  }

  // Methode wird aufgerufen wenn der Parser zu einem Start-Tag kommt
  public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException {
	  HashMap <String,String> attsMap=new HashMap <String, String>();
	  for (int i=0;i<atts.getLength();i++) {
		  String key=atts.getQName(i);
		  String value=atts.getValue(i);
		  attsMap.put(key, value);
	  }
	  HashMap <String,String> contentMap=new HashMap <String, String>();
	  pointer=pointer+map.getDelimiter()+localName;
	  map.put(pointer, contentMap);
	  map.put(pointer + map.getDelimiter() + "attributes", attsMap);
	  
  }
  // Methode wird aufgerufen wenn der Parser zu einem End-Tag kommt
   	public void endElement(String uri, String localName, String qName) throws SAXException {
		  map.put(pointer,currentValue);
		  pointer.replaceAll(map.getDelimiter() + localName + "$", "");
    }
      
   	
   	  public void setDocumentLocator(Locator locator) {  }
      public void startDocument()                                       throws SAXException {}
      public void endDocument()                                         throws SAXException {}
      
      public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
      public void processingInstruction(String target, String data)     throws SAXException {}
      public void skippedEntity(String name)                            throws SAXException {}
      
      public void startPrefixMapping(String prefix, String uri)         throws SAXException {}
      public void endPrefixMapping(String prefix)                       throws SAXException {}

	


}
