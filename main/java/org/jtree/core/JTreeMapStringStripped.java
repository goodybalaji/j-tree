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

package org.jtree.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
* A String extension of a String JTreeMap with some sorting functionality 
*
* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www.action4java.org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.1
*/



public class JTreeMapStringStripped extends JTreeMapStripped<String> {
	
	public JTreeMapStringStripped() {
		super();
		this.put("params", new HashMap<String,String> ());
	}
	/**
	 * Puts a whole hash under a list of keys. Its the way to create a new list entry
	 * Since the in the generic version the type of the fieldkey is not defined and every key is a string
	 * the method is placed here
	 * @param keyNames is separated with the delimiter used for the JTreeMap
	 * @param root defines the name where to start the list to access the JTreeMap values
	 * @param newMap defines leaf keys and values
	 */
	
	public void put(String root, String keyNames, HashMap <String,String> newMap) {
		
		String [] keyNamesArray=keyNames.split(getDelimiter());
		String pointer=new String (root);
		for (String key:keyNamesArray) {
			
			String mapKey=newMap.get(key);
			if (mapKey==null) {
				System.out.println("Key for keys " + key + " is null " );
				return;
			}
			else if (mapKey.equals("")) {
				//logger.debug("Key for keys " + key + " is empty: " + pointer);
				for(String errkey:newMap.keySet()) {
						//logger.debug("available: " + errkey + " " + newMap.get(errkey));
				}
				return;
			}
			pointer=pointer+this.getDelimiter()+mapKey;
		}
		//logger.debug("add " + root + " " + pointer);
		this.put(pointer, newMap);
	}
	
	/** 
	 * Returns a sorted String of all values stored in the hash
	 * Specially useful when debugging. 
	 * The input value filter is for filter only values matching the pattern. 
	 * @param filter
	 */
	public String printHash(String filter) {
		StringBuffer result=new StringBuffer("");
		List <String> keys=new ArrayList<String>();
		keys.addAll(this.getMap().keySet());
		Collections.sort(keys);
		int counter=0;
		for (String key:keys) {
			if (!filter.equals("")) {
				if (!key.matches(filter)) {
					continue;
				}
			}
			result.append(counter + " " + key + "=" + get(key)+"\n");
			counter++;
		}
		return result.toString();
	}

	

	
	/**
	 * Dispaches call of methods depending on the keyIdentifier. 
	 * In JTreeMap this is empty.
	 * These methods could be defined within a class extend JTreeMapString. 
	 * This dispatch method is called by the JTreeTemplateProcess parsing for expandData attribute.
	 * @param keyIdentifier
	 */
	public void expandData(String keyIdentifier) {
		System.out.println("Expanding empty for default map " + keyIdentifier);

	}
}
