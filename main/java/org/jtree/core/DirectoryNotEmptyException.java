package org.jtree.core;

/**
 * Created with IntelliJ IDEA.
 * User: wdiwischek
 * Date: 22.02.13
 * Time: 00:24
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryNotEmptyException extends Exception {
    public DirectoryNotEmptyException(String message) {
        super(message);
    }
}
