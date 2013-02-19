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
<<<<<<< HEAD

* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www.action4java.org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.11
=======
>>>>>>> 6d4a61903dc378e26c8f97b46a5d9d1ef091e353
 */


package org.action4java.core.map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
* JTreeMap try to adopt a <b>hashes of hashes implementation</b> like the perl language offers as elementary type
* like the array [] specifier in java
* <br/>
* <br/>
* Where perl defines e.g. <i>$phone=$address->{'country'}->{'city'}->{'person'}->{'phone'}</i> you can use 
* <i>String phone=address.get("country:city:person:phone")</i> in JTreeMap. The delimiter is free but ":" is used
* as default. To scan all persons for city you just 
* call <i>List &lt;String&gt; persons = address.getList("country:city");</i> and to get a list of all entries of a person you just 
* enter <i>List &lt;String&gt; personDatas = address.getList("country:city:person");</i>
* <br/>
* <br/>
* This simplifies the construction of complex data container without the need to define a bunch of classes. 
* This could be used for rapid prototyping, and you can create the classes afterwards, if they were reused. If not you can avoid 
* the every-shit-a-class-paradigm and the freedom to access everything by free defined dynamic names. 
* <br/>
* <br/>
* JTreeMap consists of a generic Hashmap together with a HashMap of Lists, which are used to define hierarchical subsets of the "big" hashmap.
* <br/>
* <br/>
* Since JTreeMap is a java object there are some methods added to perlMap which exceed the ability of hashes of hashes in perl. 
* The best alternative name would be TreeMap, but unfortunately its already used by java itself.
* <br/>
* <br/>
* The JTreeMap class accepts no nulls. 
* The JTreeMap class uses Lists so its guaranteed, that the order of the hash keys will be constant.
* Furthermore it implements different order getters, so this will simplify scans through the operation
* <br />
* <br />
* Since JTreeMap just uses basically two instances of HashMaps, 
* all things for performance for for the basic operations (get and put), 
* and iterations depend are the same as for for HashMaps, it depends on initial capacity and load factor. 
* For certain operation feel free to change those values. I've made no performance tests at the moment.
<<<<<<< HEAD

=======
*
* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www.action4java.org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.11
>>>>>>> 6d4a61903dc378e26c8f97b46a5d9d1ef091e353

*/



public class JTreeMap <T> {
	private static final Log logger = LogFactory.getLog(JTreeMap.class);
	
	private HashMap <String,T>            map      = new HashMap <String,T> ();
	private HashMap <String,List<String>> list     = new HashMap <String,List<String>> ();
	private HashMap <String,List<Integer>> listInt = new HashMap <String,List<Integer>> ();
	
	private String  delimiter     = ":";
	private String pointer        ="";
	private Pattern fieldPattern  = Pattern.compile(":");
	private Pattern listPattern   = Pattern.compile(":[^:]+$");
	private Pattern childPattern  = Pattern.compile(".*:");
	
	/**
	 * A empty constructor just inialize an empty root list entry
	 */
	
	public JTreeMap() {
		//initialize the root list which is always available
		list.put("",new ArrayList<String>());
	}
	
	/**
	 * Reinit all values
	 */
	public void clearAll() {
		map      = new HashMap <String,T> ();
		list     = new HashMap <String,List<String>> ();
	}
	
	/**
	 * Remove all map values beside params
	 */
	public void clearAllExceptParams() {
		HashMap <String,T>            mapBuffer     = new HashMap <String,T> ();
		HashMap <String,List<String>> listBuffer    = new HashMap <String,List<String>> ();
		for (String key:this.map.keySet()) {
			if (key.startsWith("params" + this.delimiter))  {
				mapBuffer.put(key,this.map.get(key));
			}
		}
		for (String key:list.keySet()) {
			if (key.startsWith("params" + this.delimiter))  {
				listBuffer.put(key,this.list.get(key));
			}
		}
		this.map=mapBuffer;
		this.list=listBuffer;
	}
	
	/**
	 * Remove all map values from this.pointer + endPointer
	 */
	public void clearPointer(String endPointer) {
		HashMap <String,T>            mapBuffer     = new HashMap <String,T> ();
		HashMap <String,List<String>> listBuffer    = new HashMap <String,List<String>> ();

		for (String key:map.keySet()) {
			if (!key.startsWith(this.pointer + this.delimiter +  endPointer))  {
				mapBuffer.put(key,this.map.get(key));
			}
		}
		for (String key:list.keySet()) {
			if (!key.startsWith(this.pointer + this.delimiter +  endPointer))  {
				listBuffer.put(key,this.list.get(key));
			}
		}
		this.map=mapBuffer;
		this.list=listBuffer;
	}
	
	/**
	 * Remove all map values from this.pointer + endPointer
	 */
	public void clear(String pointer) {
		HashMap <String,T>            mapBuffer     = new HashMap <String,T> ();
		HashMap <String,List<String>> listBuffer    = new HashMap <String,List<String>> ();

		for (String key:map.keySet()) {
			if (!key.startsWith(this.pointer))  {
				mapBuffer.put(key,this.map.get(key));
			}
		}
		for (String key:list.keySet()) {
			if (!key.startsWith(this.pointer ))  {
				listBuffer.put(key,this.list.get(key));
			}
		}
		this.map=mapBuffer;
		this.list=listBuffer;
	}
	/**
	 * Sets a pointer to allow "lazy" putter and setter
	 * @param pointer
	 * @return
	 */
	public void setPointer (String pointer) {
		this.pointer=pointer;
	}
	
	/**
	 * Gets a pointer to allow "lazy" putter and setter
	 * @param pointer
	 * @return
	 */
	public String getPointer () {
		return this.pointer;
	}
	
	/**
	 * Adds a string to actual pointer to allow "lazy" putter and setter
	 * @param pointer
	 * @return
	 */
	public void addPointer (String addPointer) {
		this.pointer=this.pointer + this.delimiter + addPointer;
	}
	/**
	 * A constructor with a self defined separator just inialize an empty root list entry
	 * The separator could not be changed after the instanciation of a JTreeMap object, since it makes no sense
	 */
	public JTreeMap(String delimiter) {
		if (delimiter==null) {
			logger.warn("Delimiter could not be null using default :" );
			return;
		}
		if (delimiter.equals("")) {
			logger.warn("Delimiter could not be empty String using default : " );
			return;
		}
		list.put("",new ArrayList<String>());
		this.delimiter=delimiter;
		fieldPattern = Pattern.compile(delimiter);
		listPattern  = Pattern.compile(delimiter + "[^" + delimiter + "]+$");
		childPattern = Pattern.compile(".*" + delimiter);
	}
	
	public void init() {
		list.put("",new ArrayList<String>());
	}

	public void setDelimiter(String delimiter) {
		this.delimiter=delimiter;
	}
	
	public String getDelimiter() {
		return this.delimiter;
	}
	/**
	 * Get the parent-list of a key 
	 * if key is list1:list2:list3 it will return list1:list2
	 * its private until I see some benefit to make it public
	 * @param key
	 * @return returns string before the last delimiter
	 */
	private String getParentList(String key) {
		if (!key.contains(delimiter)) {
			return "";
		}
		Matcher match   = listPattern.matcher(key);
		String  listKey = match.replaceFirst("");
		return listKey;
	}
	
	/**
	 * Get the last sibling child of a key
	 * if key is list1:list2:list3 it will return list3
	 * its private unil I see som benefit to make it public
	 * @param key
	 * @return the last entry after the last delimiter
	 */
	private String getChildKey(String key) {
		Matcher match  = childPattern.matcher(key);
		String listKey = match.replaceFirst("");
		return listKey;
	}

	/** 
	 * Checks if a key is set for a value. 
	 * @param modelKey
	 * @return
	 */
	public boolean isValueKey (String key) {
		if (this.map.get(key)!=null) {
			return true;
		}
		return false;
	}
	
	/** 
	 * Checks if a key is set for a list. 
	 * @param modelKey
	 * @return
	 */
	public boolean isListKey (String key) {
		if (this.list.get(key)!=null) {
			return true;
		}
		return false;
	}

	/**
	 * Is the pointer defined for a value or a list
	 * @return
	 */
	public boolean is (String key) {
		return (isListKey(key)||isValueKey(key));
		
	}	
	

	/** Returns a List of keys defined by pointer
	 * 
	 * @param modelKey
	 * @param specifier
	 * @return
	 */
	public List <String> getMapList(String pointer) {
		logger.debug("Get List for " + pointer);
		List <String> list = new ArrayList <String>();
		if (isListKey(pointer)) {		
			logger.debug("List type for " + pointer);
			list.addAll( this.list.get(pointer));
			return list;	
		}
		return list;
	}
	
	/** Returns a List from a the pointer value
	 * @return List from pointer value
	 */
	public List <String> getMapList() {
		return this.getMapList(this.pointer);
	}

<<<<<<< HEAD
	/**
	 * 
	 * @param key
	 * @return
	 */
=======
	
>>>>>>> 6d4a61903dc378e26c8f97b46a5d9d1ef091e353
	
	private boolean checkTree(String key) {
		logger.debug("setMapListValue: " + key);
		String [] keys = fieldPattern.split(key);
		String thisKey="";
		String nextKey="";
		for (int i=0;i<keys.length;i++) {
			logger.debug("Check tree value for " + i + " " + thisKey + " : " + nextKey + " ");
			if (thisKey.equals("")) {
				nextKey = keys[i];
			}
			else {
				nextKey = thisKey + this.delimiter +keys[i];
			}
			// Nothing defined at all --> create everything from the scratch
			if (this.isValueKey(thisKey))  {
				return false;
			}
			thisKey=nextKey;
		}
		return true;
	}
<<<<<<< HEAD
	
	/**
	 * 
	 * @param key
	 */
=======
>>>>>>> 6d4a61903dc378e26c8f97b46a5d9d1ef091e353

	private void createTree(String key) {
		logger.debug("setMapListValue: " + key);
		String [] keys = fieldPattern.split(key);
		String thisKey="";
		String nextKey="";
		for (int i=0;i<keys.length;i++) {
			logger.debug("Check tree value for " + i + " " + thisKey + " : " + nextKey + " ");
			if (thisKey.equals("")) {
				nextKey = keys[i];
			}
			else {
				nextKey = thisKey + this.delimiter +keys[i];
			}
			// Nothing defined at all --> create everything from the scratch
			if (!this.isListKey(thisKey))  {
				List <String> newList = new ArrayList <String>();
				newList.add(keys[i]);
				this.list.put(thisKey, newList);
			}
			else {
				// List already exists
				// Now check if a list for nextKey is defined --> tree
				if (!isListKey(nextKey)) {
					List <String> existingList=this.list.get(thisKey);
					existingList.add(keys[i]);
					this.list.put(thisKey,existingList);
				}
			}
			thisKey=nextKey;
		}

	}

	/**
	 * Gets a single Value from key from the map
	 * @param key
	 * @return
	 */
	public T get(String key) {
		if (isValueKey(key)) {
			return map.get(key);
		}
		logger.debug("Key " + key + " is not a map value");
		return null;
	}
<<<<<<< HEAD
	
	/**
	 * Added for file system like method syntax
	 * @param key
	 * @return
	 */
	public T read(String key) throws KeyNotFoundException {
		if (!isValueKey(key)) {
			throw new KeyNotFoundException("Key not found: " + key);
		}
		return get(key);
	}
=======
>>>>>>> 6d4a61903dc378e26c8f97b46a5d9d1ef091e353

	
	/**
	 * Puts a single value in JTreeMap under the key. 
	 * @param key
	 * @param value
	 */
	
	public void put(String key,T value) {		
	
		// no null key allowed
		if (key==null) {
			logger.warn("Null keys won't be added for ");
			return;
		}
		
		// no null value allowed
		if (value==null) {
			logger.warn("Null values won't be added for " + key);
			return;
		}
		
		// Key already set --> just change values
		if (this.isListKey(key)) {
			logger.warn("The key " + key + " is a list key");
			return;
		}
		
		// Key already set --> just change values and return
		if (this.isValueKey(key)) {
			this.map.put(key, value);
			return;
		}
		
		// if parentListKey is not defined nothing will be done. 
		String parentListKey=this.getParentList(key);
		logger.debug("parentListKey=" + parentListKey + " for " + key);
		if (!this.isListKey(parentListKey)) {
			logger.debug("No parent list " + parentListKey + " found for " + key + " ");
			return;
		}

		// parent list defined and of a leave type --> ready to insert
		// Creates a new list entry and put value on the map
		String childKey=this.getChildKey(key);
		logger.debug("ChildKey=" + childKey);
		List <String> parentList=this.getMapList(parentListKey);
		parentList.add(childKey);
		this.list.put(parentListKey, parentList);
		map.put(key, value);
	}
	

	
	/**
	 * Stores the value in the map with an key as combination of  instance pointer and Integer
	 * @param key
	 * @param value
	 */
	
	public void putPointer(String key,T value) {		
		if (key==null) {
			logger.warn("Strings is null");
			return;
		}
		if (this.pointer.equals("")) {
			logger.warn("pointer is empty for " + key);
			return;
		}
		String targetSpecifier=this.pointer+this.delimiter + key;
		this.put(targetSpecifier, value);
	}
	
	/**
	 * Stores the value in the map with an key as combination of  instance pointer and Integer
	 * @param key
	 * @param value
	 */
	
	public void putPointer(Integer key,T value) {		
		if (key==null) {
			logger.warn("Strings is null");
			return;
		}
		if (this.pointer.equals("")) {
			logger.warn("pointer is empty for " + key);
			return;
		}
		String targetSpecifier=this.pointer+this.delimiter + key.toString();
		this.put(targetSpecifier, value);
	}
	

	
	/**
	 * Gets value from a key the key. 
	 * @param key
	 * @param value
	 */
	
	public T getPointer(String key) {		
		if (key==null) {
			logger.warn("String key is null");
			return null;
		}
		if (this.pointer.equals("")) {
			logger.warn("pointer is empty for " + key);
			return null;
		}
		String targetSpecifier=this.pointer+this.delimiter + key;
		return get(targetSpecifier);
	}

	
	
	
	
	
	
	/**
	 * Puts a whole hash under the key. Its the way to create a new list entry
	 * @param newKey defines the name where to get the list to access the JTreeMap values
	 * @param newMap defines leaf keys and values
	 */
	
	public void put(String newKey,HashMap <String,T> newMap) {
		

		// If in the key path is a value already defined
		if (!checkTree(newKey)) {
			logger.warn("Parts of the list " + newKey + "would overwrite key/value pairs. please check");
			return;
		}
		
		// nothing is defined add all.
		// Just create the whole from the scratch
		if (!is(newKey)) {
			logger.debug("Create new list entry and put values in the map for " + newKey);
			List <String> listKeys=new ArrayList <String>();
			listKeys.addAll(newMap.keySet());
			
				
			for (String fieldKey:listKeys) {
				logger.debug("put: " + fieldKey + " " + newMap.get(fieldKey));
				String mapperKey = newKey + ":" + fieldKey;
				this.map.put(mapperKey, newMap.get(fieldKey));	
			}
			this.createTree(newKey);
			this.list.put(newKey, listKeys);
			//this.setLeave(key);
			return;
		}
		
		// list already createed just add extra values
		
		logger.debug("Add new list entries and put values in the map for " + newKey);
		List <String> newKeys=new ArrayList <String>();
		newKeys.addAll(newMap.keySet());
		for (String fieldKey:newKeys) {
			String mapperKey = newKey + ":" + fieldKey;
			logger.debug("put: " +  fieldKey);
			this.put(mapperKey, newMap.get(fieldKey));
		}			
	}
	

	
	
	/**
	 * Gets a whole hash under the key if its of leave type
	 * @param key defines the name where to get the list to access the JTreeMap values
	 * @param map defines leaf keys and values
	 */
	public HashMap <String,T>         getHashMap(String key) {
		HashMap <String,T> result= new HashMap<String,T>();
		if (!isListKey(key)) {
			logger.warn("No list defined for " + key);
			return result;
		}
		logger.debug("Test" + key);
		List <String> listKeys = this.getMapList(key);
		for (String setKey:listKeys) {
			String mapperKey = key + ":" + setKey;
			if (this.isValueKey(mapperKey)) {
				result.put(mapperKey, map.get(mapperKey));
			}
		}

		return result;
	}
	
	/**
	 * Gets a whole hash under the key if its of leave type
	 * @param key defines the name where to get the list to access the JTreeMap values
	 * @param map defines leaf keys and values
	 */
	public HashMap <String,T>         getHashMapFlat(String key) {
		HashMap <String,T> result= new HashMap<String,T>();
		if (!isListKey(key)) {
			logger.warn("No list defined for " + key);
			return result;
		}
		logger.debug("Test" + key);
		List <String> listKeys = this.getMapList(key);
		for (String setKey:listKeys) {
			result.put(setKey, map.get(key+delimiter+setKey));
		}

		return result;
	}	
	
	/**
	 * Gets the whole hash stored in JTreeMap. Useful for extension to get access. 
	 * @param key defines the name where to get the list to access the JTreeMap values
	 * @param map defines leaf keys and values
	 */
	public HashMap <String,T> getMap () {
		return this.map;
	}	
	
	/**
	 * Puts a whole hash under a key created by the pointer String and an Integer. 
	 * Returns the new created key. 
	 * If no entry found under the pointer, the key will be pointer:0
	 * If entries exist it will return pointer:list.size() 
	 * @param newKey defines the name where to get the list to access the JTreeMap values
	 * @param newMap defines leaf keys and values
	 * @return 
	 */
	
	public String addWithIntegerKey(String pointer,HashMap <String,T> newMap) {
		List <String> entries = this.list.get(pointer);
		int entryNum=0;
		if (entries!=null) {
			//List <String> entries = new List <String>();
			entryNum=entries.size();
		}
		Integer newEntry=new Integer(entryNum);
		//entries.add(newEntry.toString());
		//this.list.put(pointer, entries);
		String key=pointer + this.delimiter + newEntry.toString();
		this.put(key, newMap);
		return key;
	}
	


}
