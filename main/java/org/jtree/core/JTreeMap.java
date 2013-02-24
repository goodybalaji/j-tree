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

* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www.action4java.org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.11
=======
 */


package org.jtree.core;


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

=======
*
* Please include a hint to this site to all the stuff you change or generate<br/>
* http://www..org<br/>
* date 10.3.2012<br/>
* @author Werner Diwischek
* @version 0.11


*/



public class JTreeMap <T> {
	private static final Log logger = LogFactory.getLog(JTreeMap.class);

	private HashMap <String,T>            valuesMap      = new HashMap <String,T> ();
	private HashMap <String,List<String>> directoriesMap = new HashMap <String,List<String>> ();

	private boolean force         = false;
	private String  delimiter     = null;
	private String  pointer       = null;
	private Pattern fieldPattern  = null;
	private Pattern listPattern   = null;
	private Pattern childPattern  = null;

	/**
	 * A empty constructor just inialize an empty root list entry
	 */

	public JTreeMap() {
		this(":");
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
		this.delimiter = delimiter;
		this.pointer   = delimiter;
		directoriesMap.put(this.delimiter, new ArrayList<String>());

		fieldPattern   = Pattern.compile(delimiter);
		listPattern    = Pattern.compile(delimiter + "[^" + delimiter + "]+$");
		childPattern   = Pattern.compile("(.*)" +  delimiter);

	}


	public void setForce(boolean force) {
		this.force=force;
	}

	public String getDelimiter() {
		return this.delimiter;
	}




	// VALUE TYPE THINGS

	@Deprecated
	public T get(String key) {
		try {return read(this.getDirectoryCompatibility(key));} catch (Exception e) {logger.error(e);}
		return null;
	}
	/**
	 * Reads a value from key
	 * @param key
	 * @return
	 */
	public T read(String key) throws KeyNotFoundException, DirectoryExistsException {
		key = getDirectoryAbsolute(key);
		if (isListKey(key)) {
			throw new DirectoryExistsException("Key is a directory. Cant read value for " + key);
		}
		if (!isValueKey(key)) {               
			throw new KeyNotFoundException("Key not found: " + key);
		}
		return this.valuesMap.get(key);
	}


	@Deprecated
	public T getPointer(String key) {
		try {return read(key);} catch (Exception e) {logger.error(e);}
		return null;
	}

	/**
	 * Gets a whole hash under the key if its of leave type
	 * @param key defines the name where to get the list to access the JTreeMap values
	 * @param valuesMap defines leaf keys and values
	 */
	public HashMap <String,T>         readMap (String key,boolean absoluteFlag) throws DirectoryNotFoundException, DirectoryNotEmptyException {
		if (!isListKey(key)) {
			throw new DirectoryNotFoundException("Could not find " + key);
		}
		logger.debug("Test" + key);
		List <String> listKeys = this.directoriesMap.get(key);
		HashMap <String,T> result= new HashMap<String,T>();

		for (String setKey:listKeys) {
			String mapperKey = key + ":" + setKey;
			if (this.isValueKey(mapperKey)) {
				if (absoluteFlag) {
					result.put(mapperKey, valuesMap.get(mapperKey));
				}
				else {
					result.put(setKey, valuesMap.get(mapperKey));
				}
			}
		}
    	return result;
	}

	@Deprecated
	public HashMap <String,T>         getHashMap(String key) {
		try {return this.readMap(key,true);} catch (Exception e) {logger.error(e);}
		return null;
	}

	@Deprecated
	// Returns only the relative path
	public HashMap <String,T>         getHashMapFlat(String key) {
		try {return this.readMap(key,false);} catch (Exception e) {logger.error(e);}
		return null;
	}

	/**
	 * Inserts a single value
	 * @param key
	 * @return
	 */
	public void insert(String directory, String key, T value,boolean force) throws DirectoryNotFoundException, DirectoryNotEmptyException {
		if (value==null) {
			logger.warn("value for " + directory + " " + key + " is null -- doing nothing");
			return;
		}
		if (directory==null) {
			logger.warn("directory is null");
			return;
		}
		if (directory.equals("")) {
			logger.warn("directory is empty");
			return;
		}
		if (key==null) {
		   logger.warn("key is null");
		   return;
		}
		if (key.equals("")) {
			logger.warn("key is empty");
			return;
		}
		directory = getDirectoryAbsolute(directory);

		if (!isListKey(directory)) {
			if (force) {
				this.mkdirs(directory);
			}
			else {
				throw new DirectoryNotFoundException("Key is a directory. Cant read value for " + key);
			}
		}

		String completeKey=directory + this.delimiter + key;
		if (!this.directoriesMap.get(directory).contains(key)) {
			this.directoriesMap.get(directory).add(key);
		}
		valuesMap.put(completeKey, value);
	}


	/**
	 * Inserts a single value with no creation of directories
	 * @param key
	 * @return
	 */
	public void insert(String directory, String key, T value) throws DirectoryNotFoundException, DirectoryNotEmptyException {
		insert(directory,key,value,this.force);
	}

	/**
	 * Compatibility - adapter
	 * @param key
	 * @param value
	 * @throws DirectoryNotFoundException
	 */
	public void insert(String key,T value) throws DirectoryNotFoundException, DirectoryNotEmptyException {
		String parentDirectory=this.getParentDirectory(key);
		String childKey=this.getChildKey(key);
		logger.debug(parentDirectory + " --> " + childKey);
		insert(parentDirectory, childKey, value, this.force);
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

	public void insert(String directory,List <T> entryList) throws DirectoryNotEmptyException, DirectoryNotFoundException {
		List <String> entries = this.directoriesMap.get( directory);
		int entryNum=0;
		if (entries!=null) {
			//List <String> entries = new List <String>();
			entryNum=entries.size();
		}
		for (T value:entryList) {
			entryNum++;
			this.insert(directory,new Integer(entryNum).toString(),value,this.force);
		}
	}

	@Deprecated
	public void put(String key,T value) {
		try {this.insert(this.getDirectoryCompatibility(key),value);} catch (Exception e) {logger.error(e);}
	}
	@Deprecated
	public void putPointer(String key,T value) {
		try {this.insert(key,value);} catch (Exception e) {logger.error(e);}
	}
	@Deprecated
	public void putPointer(Integer key,T value) {
		try {this.insert(key.toString(),value);} catch (Exception e) {logger.error(e);}
	}



	/**
	 * Insert a whole hash under the key. Its the way to create a new list entry
	 * @param directory defines the name where to get the list to access the JTreeMap values
	 * @param values defines leaf keys and values
	 */
	public void insert(String directory, HashMap<String,T> values , boolean force) throws DirectoryNotFoundException, DirectoryNotEmptyException, DirectoryExistsException, KeyExistsException {
		if (values==null) {
			logger.warn("values for " + directory + " is null -- doing nothing");
			return;
		}
		if (directory==null) {
			logger.warn("directory is null");
			return;
		}
		if (directory.equals("")) {
			logger.warn("directory is empty");
			return;
		}
		directory = getDirectoryAbsolute(directory);
		String parentDirectory = this.getParentDirectory(directory);

		if (!isListKey(parentDirectory)) {
			if (force) {
				this.mkdirs(parentDirectory);
			}
			else {
				throw new DirectoryNotFoundException("Directory " + directory + " has no parent defined: " + parentDirectory);
			}
		}
		if (!isListKey(directory)) {
			this.mkdir(directory);
		}
		String localKey ="";
		for (String key:values.keySet()) {
			this.insert(directory, key, values.get(key), force);
		}
	}

	/**
	 * Inserts a HashMap with no creation of directories
	 * @param key
	 * @return
	 */
	public void insert(String directory, HashMap<String,T> values) throws DirectoryNotFoundException, DirectoryNotEmptyException, DirectoryExistsException, KeyExistsException {
		insert(directory,values,this.force);
	}

   @Deprecated
	public void put(String newKey,HashMap <String,T> newMap) {
		try {insert(this.getDirectoryCompatibility(newKey),newMap,true);} catch (Exception e) {logger.error(e);}
	}



	// DIRECTORY THINGS

	/**
	 * Added for file system like method syntax
	 * @param directory
	 * @return
	 */
	public void mkdir (String directory) throws DirectoryNotFoundException, DirectoryExistsException, KeyExistsException {
		this.mkdir(directory,false);
	}
	/**
	 * Adds directory
	 * Create all child directories if not exits
	 * @param directory
	 * @return
	 */
	public void mkdir (String directory,boolean force) throws DirectoryNotFoundException, DirectoryExistsException, KeyExistsException {
		directory = getDirectoryAbsolute(directory);
		if (this.isValueKey(directory)) {
			throw new KeyExistsException("This key is already a value entry: " + directory);
		}
		else if (this.isListKey(directory)) {
			throw new DirectoryExistsException("This key is already a directory entry: " + directory);
		}
		String childKey = this.getChildKey(directory);
		String parentList = this.getParentDirectory(directory);
		logger.debug("ChildKey " + childKey + " from directory " + directory);
		if (!force)    {
			if (!parentList.equals("")) {
				if (!this.isListKey(parentList)) {
					throw new DirectoryNotFoundException("The parentList " + parentList + " to create directory " + directory + " does not exist. Use -f option to create the whole directory!");
				}
			}
		}
		else {
			if (!parentList.equals("")) {
				 this.mkdirs(parentList);
			}
		}
		logger.debug("add " + directory + " directory: " + parentList + " " + childKey);

		this.directoriesMap.put(directory, new ArrayList<String>());
		this.directoriesMap.get(parentList).add(childKey);
	}

	/**
	 * Make all directories of the path
	 * @param directory
	 */
	private void mkdirs (String directory) {
		if (directory==null) {
			logger.warn("Directory is null");
			return;
		}
		if (directory.equals("")) {
			logger.warn("Directory is empty doing nothing");
			return;
		}
		directory = directory.replaceAll("^"+this.delimiter,"");
		String[] paths = directory.split(this.delimiter);
		String pathStep =":";
		String previousDir=":";
		for (String path:paths) {
			if (pathStep.equals(":")) {//first entry
				pathStep+=path;
			}
			else {
				pathStep+=":"+path;
			}
			if (this.isListKey(pathStep)) {
				continue;
			}
			logger.debug("add " + pathStep + "directory: " + previousDir + " " + path);
			this.directoriesMap.put(pathStep, new ArrayList<String>());
			this.directoriesMap.get(previousDir).add(path);
			previousDir=pathStep;
		}
	}

	@Deprecated
	private void createTree(String key) {
		try {mkdirs(key);} catch (Exception e) {logger.error(e);}
	 }

	/**
	 * Sets the pointer to allow "lazy" putter and setter
	 * @param key
	 * @return
	 */
	public void cd(String directory) throws DirectoryNotFoundException {
	   if (directory==null) {
			logger.warn("directory is null");
			return;
	   }
	   directory = getDirectoryAbsolute(directory);
	   if (!isListKey(directory)) {
			throw new DirectoryNotFoundException("Key is a directory. Cant read value for " );
	   }
	   this.pointer=directory;
	}
	@Deprecated
	public void setPointer (String pointer) {
		logger.info("Pointer before " + this.pointer + " " + pointer);
		try {cd(this.getDirectoryCompatibility(pointer));} catch (Exception e) {logger.error(e);}
		logger.info("Pointer afterwards " + this.pointer + " " + pointer);
	}
	@Deprecated
	public void addPointer (String pointer) {
		logger.info("Pointer before " + this.pointer + " " + pointer);
		try {cd(this.pointer + this.delimiter + pointer);} catch (Exception e) {logger.error(e);}
		logger.info("Pointer afterwards " + this.pointer + " " + pointer);
	}

	/**
	 * Gets the actual pointer to allow "lazy" putter and setter
	 * @param pointer
	 * @return
	 */
	public String pwd() {
		 return this.pointer;
	}
	@Deprecated
	public String getPointer () {
		return this.pwd();
	}


	/**
	 * Returns a List of keys defined by instance var pointer
	 * @param directory
	 * @return
	 */
	public List <String> ls () throws DirectoryNotFoundException, DirectoryNotEmptyException {
		 return ls(this.pointer);
	}

	/**
		 * Returns a List of keys defined by directory
		 * @param directory
		 * @return
		 */
	public List <String> ls(String directory) throws DirectoryNotFoundException, DirectoryNotEmptyException {
		return ls(directory,true);
	}
	/**
	 * Returns a List of keys defined by directory
	 * @param directory
	 * @return
	 */
	public List <String> ls(String directory,boolean absoluteFlag) throws DirectoryNotFoundException, DirectoryNotEmptyException {
	if (directory==null) {
		throw new DirectoryNotFoundException("directory is null");
	}
	String absoluteDirectory = getDirectoryAbsolute(directory);
	logger.debug("directory ls " + absoluteDirectory);
	if (this.isValueKey(absoluteDirectory)) {
		throw new DirectoryNotFoundException("Key is a value not a directory. Use read to get the value " + absoluteDirectory);
	}
	if (!isListKey(absoluteDirectory)) {
		throw new DirectoryNotFoundException("Not a directory." + absoluteDirectory);
	}
	if (!absoluteFlag) {
		return (this.directoriesMap.get(absoluteDirectory));
	}
	if (directory.startsWith(this.delimiter)) {
			List <String> myList = this.directoriesMap.get(absoluteDirectory);
			List <String> resultList = new ArrayList <String>();
			for (String key:myList) {
				key=absoluteDirectory + this.delimiter + key;
				resultList.add(key);
			}
			return resultList;
	}
	return (this.directoriesMap.get(absoluteDirectory));
	
}
	@Deprecated
	public List <String> getMapList(String pointer) {
		try {return ls(this.getDirectoryCompatibility(pointer),false);} catch (Exception e) {logger.error(e);}
		return null;
	}
	
	@Deprecated
	public List <String> getMapListAbsolute(String pointer) {
		try {return ls(this.getDirectoryCompatibility(pointer),true);} catch (Exception e) {logger.error(e);}
		return null;
	}
	@Deprecated
	public List <String> getMapList() {
		try {return this.ls(this.pointer,false);} catch (Exception e) {logger.error(e);}
		return null;
	}


	// rmdir
	//==================

	/**
	 * remove all entries of a data directory
	 * @param directory
	 * @return
	 */
	public void rmdir(String directory,boolean force) throws DirectoryNotFoundException, DirectoryNotEmptyException, DirectoryExistsException, KeyNotFoundException {
		if (directory==null) {
			logger.warn("directory is null");
			return;
		}
		directory = getDirectoryAbsolute(directory);
		if (!isListKey(directory)) {
			throw new DirectoryNotFoundException("Key is a directory. Use rm to delete value for " + directory);
		}
		if (!force) {
			List <String> contentDir = ls(directory);
			if (contentDir.size()!=0) {
				throw new DirectoryNotEmptyException("Directory is not empty " + directory + " with " + contentDir.size());
			}
		}
        HashMap <String,List<String>> bufferMap = new HashMap <String,List<String>>();
		// remove sublists
		for (String key:this.directoriesMap.keySet())  {
			  if (key.startsWith(directory)) {
                  String parentDirectory = this.getParentDirectory(key);
                  if (isListKey(parentDirectory)) {
                      String childKey = this.getChildKey(directory);
                      bufferMap.put(parentDirectory, this.removeListEntry(parentDirectory, childKey));
                  }
				  continue;
			  }
              bufferMap.put(key,this.directoriesMap.get(key));
		}
        this.directoriesMap=bufferMap;

		// remove remove entries
        HashMap<String,T> bufferValue = new HashMap<String,T>();
        for (String key:this.valuesMap.keySet())  {
			if (key.startsWith(directory)) {
                String parentDirectory = this.getParentDirectory(key);
                if (isListKey(parentDirectory)) {
                    String childKey = this.getChildKey(directory);
                    bufferMap.put(parentDirectory, this.removeListEntry(parentDirectory, childKey));
                }
                continue;
            }
            bufferValue.put(key,this.valuesMap.get(key));
		}
        this.directoriesMap=bufferMap;
        this.valuesMap=bufferValue;
	 }

	/**
	 * remove all entries of a data directory
	 * @param directory
	 * @return
	 */
	public void rmdir(String directory) throws DirectoryNotFoundException, DirectoryNotEmptyException, DirectoryExistsException, KeyNotFoundException {
		 rmdir(directory,this.force);
	}

	@Deprecated
	public void clearAllExceptParams() {
		try{rmdir(pointer); }catch (Exception e) {logger.error(e);}
	}

	@Deprecated
	public void clearPointer(String endPointer) {
		try{rmdir(pointer); }catch (Exception e) {logger.error(e);}
	}
	@Deprecated
	public void clear(String pointer) {
		try{rmdir(pointer); }catch (Exception e) {logger.error(e);}
	}

    /**
     * Removes a single value
     * @param key
     * @throws DirectoryExistsException
     * @throws KeyNotFoundException
     */
    public void rm (String key) throws DirectoryExistsException, KeyNotFoundException, DirectoryNotFoundException {
        key = getDirectoryAbsolute(key);
        if (!this.isValueKey(key)) {
            throw new KeyNotFoundException("This key does not exist: " + key);
        }
        if (this.isListKey(key)) {
            throw new DirectoryExistsException("This key is already a directory entry: " + key);
        }
        String parentDirectory = this.getParentDirectory(key);
        if (this.isListKey(key)) {
            throw new DirectoryNotFoundException("Parentdirectory " + parentDirectory + " not found for " + key);
        }
        String childKey = this.getChildKey(key);
        this.valuesMap.remove(key);
        this.directoriesMap.put(parentDirectory, removeListEntry(parentDirectory, childKey));
    }

    private List <String> removeListEntry(String parentDirectory,String childKey) {
        List <String> stripList = new ArrayList <String> ();
        for (String entry:this.directoriesMap.get(parentDirectory)) {
            if (entry.equals(childKey)) {continue;}
            stripList.add(entry);
        }
        return stripList;
    }

   /**
	 * Reinit all values
	 */
	public void format()  {
		valuesMap      = new HashMap <String,T> ();
		directoriesMap     = new HashMap <String,List<String>> ();
	}
	@Deprecated
	public void clearAll() {
		this.format();
	}







	/**
	 * Gets the whole hash stored in JTreeMap. Useful for extension to get access. 
	 * @param key defines the name where to get the list to access the JTreeMap values
	 * @param valuesMap defines leaf keys and values
	 */
	public HashMap <String,T> getMap () {
		return this.valuesMap;
	}


	// Private checkers


	/**
	 * Get the parent-list of a key
	 * if key is list1:list2:list3 it will return list1:list2
	 * its private until I see some benefit to make it public
	 * @param key
	 * @return returns string before the last delimiter
	 */
	private String getParentDirectory(String key) {
		if (!key.contains(delimiter)) {
			return ":";
		}
		Matcher match   = listPattern.matcher(key);
		String parentDirectory = match.replaceFirst("");
		if (parentDirectory.equals("")) {
			return this.delimiter;
		}
		return parentDirectory;
	}

	/**
	 * Get the last sibling child of a key
	 * if key is list1:list2:list3 it will return list3
	 * its private unil I see som benefit to make it public
	 * @param key
	 * @return the last entry after the last delimiter
	 */
	private String getChildKey(String key) {
		if (key==null) {
			logger.warn("key " + key + " is null?! ");
			return "";
		}
		if (key.equals("")) {
			logger.warn("key " + key + " is empty?! ");
			return "";
		}
		if (!key.contains(this.delimiter)) {
			logger.debug("key " + key + " is root! ");
			return "";
		}
		Matcher match  = childPattern.matcher(key);
		String childKey = match.replaceFirst("");
		return childKey;
	}

	/**
	 * Checks if a key is set for a value.
	 * @param modelKey
	 * @return
	 */
	public boolean isValueKey (String key) {
		if (this.valuesMap.get(key)!=null) {
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
		if (this.directoriesMap.get(key)!=null) {
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


	/**
	 * Checks if the directory is absolute or not and returns absolute form
	 * @param directory
	 * @return
	 */

	private String getDirectoryAbsolute(String directory)  {
		if (directory==null) {
			return this.pointer;
		}
		if (directory.startsWith(this.delimiter)) {
			return directory;
		}
		if (!this.pointer.equals(":")) {
			return this.pointer + this.delimiter + directory;
		}
		else {
			return this.pointer + directory;
		}
	 }

	/**
	 * Checks compatibility if the directory is absolute or not and returns absolute form
	 * @param directory
	 * @return
	 */

	private String getDirectoryCompatibility(String directory)  {
		logger.info("set compatibility " + this.pointer +  " --- "  + directory);
		if (directory==null) {
			return this.pointer;
		}
		if (directory.startsWith(this.delimiter)) {
			logger.info("return absolute directory " + directory);
			return directory;
		}
		if (directory.contains(this.delimiter)) {
			return this.delimiter + directory;
		}
		if (!this.pointer.equals(":")) {
			return this.pointer + this.delimiter + directory;
		}
		else {
			return this.pointer + directory;
		}
	}




	/**
	 * Checks the directory. Not used actually
	 * @param key
	 * @return
	 */

	private boolean checkDirectory(String key) {
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
}
