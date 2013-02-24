package org.jtree.test;

import org.jtree.core.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wdiwischek
 * Date: 22.02.13
 * Time: 00:07
 * To change this template use File | Settings | File Templates.
 */
public class TestMap {
    public static void main(String[]args) throws DirectoryNotEmptyException {
        System.out.println("TEST");
        JTreeMapString map = new JTreeMapString() ;
        System.out.println(
                "\n=======================================================\n" +
                " TEST 0 OLD SETTING \n" +
                "=======================================================");

        map.put("params", new HashMap<String,String>());
        map.put("params:test","TEST2 OLD");
        System.out.println("Now getting value ");
        System.out.println("TEST " + map.get("params:test"));

        try {
            System.out.println(
            "\n=======================================================\n" +
            " TEST 1 NEW SETTING \n" +
            "=======================================================");

            map.mkdir (":test");
            map.mkdir (":test:test2");
            List<String> list = map.getMapList("test");
            System.out.println("Test 'test' size: " + list.size());
            for (String key:list) {
                System.out.println("key1: " + key);
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 2 ADDING SOME DIRECTORIES AND VALUES \n" +
                            "=======================================================");
            map.mkdir(":test:test2:test3");
            map.mkdir (":test:test2:test4");
            map.insert(":test","key","value");
            map.mkdir (":test:test3a");
            map.mkdir (":test:test4b");
            list = map.ls(":test");
            System.out.println("Test 'test' 1 size: " + list.size());
            for (String key:list) {
                System.out.println("key2: " + key);
            }
            System.out.println(
                    "\n=======================================================\n" +
                    " TEST 3 NEW SETTING \n" +
                    "=======================================================");

            list = map.ls(":test:test2");
            System.out.println("Test 'test:test2' 2 size: " + list.size());
            for (String key:list) {
                System.out.println("key3: " + key);
            }

            System.out.println(
                    "\n=======================================================\n" +
                    " TEST 4 NOW TESTING mkdir WITH FORCE \n" +
                    "=======================================================");
            map.mkdir (":testForce:test2:test3:test4",true);
            map.insert (":testForce:test2:test3","keyforce","forceTest");
            list = map.ls(":testForce:test2:test3");
            System.out.println("Test ':testForce:test2:test3  size: " + list.size());
            for (String key:list) {
                System.out.println("key4: " + key);
            }


            System.out.println(
                    "\n=======================================================\n" +
                    " TEST 5 Set HASHMAP WITH FORCE \n" +
                    "=======================================================");

            HashMap <String,String> localMap= new   HashMap <String,String> ();
            localMap.put("localMap1","test1");
            localMap.put("localMap2","test2");
            localMap.put("localMap3","test3");

            map.insert(":map:map1:map2",localMap,true);
            list = map.ls(":map:map1:map2");
            System.out.println("Test ':map:map1:map2  size: " + list.size());
            for (String key:list) {
                System.out.println("key5: " + key + "=" + map.read(key).toString());
            }

            System.out.println(
                    "\n=======================================================\n" +
                    " TEST 6 Set HASHMAP OLD WITH put \n" +
                     "=======================================================");
            map.put("map",localMap);
            list = map.getMapList("map");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key6: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }


            System.out.println(
                    "\n=======================================================\n" +
                    " TEST 7 Set HASHMAP RELATIVE WITH FORCE \n" +
                    "=======================================================");
            map.cd(":map:map1");
            map.insert("map2NEW",localMap);
            list = map.ls("map2NEW");
            System.out.println("Test ':map:map1:map2NEW  size: " + list.size());
            for (String key:list) {
                if (map.isValueKey(key)) {
                    System.out.println("key7: " + key + "=" + map.read(key).toString());
                }
                else {
                    System.out.println("key7 only: " + key);
                }
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 8 Set HASHMAP OLD WITH put \n" +
                            "=======================================================");
            map.put("map2OLD",localMap);
            list = map.getMapList("map2OLD");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key8: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 9 pwd and getPointer \n" +
                            "=======================================================");
            System.out.println("pwd " + map.pwd());
            System.out.println("pwd " + map.getPointer());

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 10 REMOVE A SINLGE ELEMENT WITH rm \n" +
                            "=======================================================");
            map.rm(":map:map1:map2OLD:localMap2");
            list = map.ls("map2OLD");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key10: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }
            System.out.println("VALUE " + map.read(":map:map1:map2OLD:localMap3"));

            try {
                System.out.println("VALUE " + map.read(":map:map1:map2OLD:localMap2"));
            } catch (KeyNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            System.out.println(
                    "\n=======================================================\n" +
                       " TEST 11 REMOVE A DIRECTORY \n" +
                       "=======================================================");
            list = map.ls(":map:map1");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key11a: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }
            try {
                map.rmdir(":map:map1:map2OLD");
            } catch (DirectoryNotEmptyException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            map.rmdir(":map:map1:map2OLD",true);
            System.out.println("Test ':map  size: " + list.size());
            try {
                for (String key:list) {
                    System.out.println("key11b: " + key);
                    if (map.isValueKey(key)) {
                        System.out.println("value " + map.get(key).toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            list = map.ls(":map:map1");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key11c: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 12 ADD EXISTING KEY (SHOULD BE NOT ADDED) \n" +
                            "=======================================================");
            map.insert(":map:map1","testInsert","testValue");
            map.insert(":map:map1","testInsert","testValue");
            list = map.ls(":map:map1");
            System.out.println("Test ':map  size: " + list.size());
            for (String key:list) {
                System.out.println("key12: " + key);
                if (map.isValueKey(key)) {
                    System.out.println("value " + map.get(key).toString());
                }
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 13 READ MAP RELATIVE\n" +
                            "=======================================================");

            HashMap <String,String> myMap = map.readMap(":map:map1",false);
            System.out.println("Test :map  size: " + myMap.size());
            for (String key:myMap.keySet()) {
                System.out.println("key13: " + key + "=" + myMap.get(key));
            }

            System.out.println(
                    "\n=======================================================\n" +
                            " TEST 14 READ MAP ABSOLUTE\n" +
                            "=======================================================");

            myMap = map.readMap(":map:map1",true);
            System.out.println("Test:map  size: " + myMap.size());
            for (String key:myMap.keySet()) {
                System.out.println("key14: " + key + "=" + myMap.get(key));
            }

        } catch (DirectoryNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DirectoryExistsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyExistsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
