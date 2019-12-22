/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.domain;

/**
 *
 * We raise this exception in case we can't determine an owner for a specfic log
 * <p>
 * Currently all methods which create Fight object (Reader.readFile,
 * Fight.combineFights and Database) should not have to deal with this error as
 * they are all built to make sure to always set the Owner correctly
 * <p>
 * but seeing as a fight having the correct Owner is a crucial part of the Fight
 * class, and it's function, this exception is made to make sure the correct
 * Owner is set each time a new fight is created
 */
public class NoOwnerException extends Exception {

    /**
     * Creates a new instance of <code>NoOwnerException</code> without detail
     * message.
     */
    public NoOwnerException() {
    }

    /**
     * Constructs an instance of <code>NoOwnerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoOwnerException(String msg) {
        super(msg);
    }
    /**
     * /**
     * Constructs an instance of <code>NoOwnerException</code> with the
     * specified detail message and a Throwable cause.
     *
     * @param msg
     * @param cause 
     */
    public NoOwnerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
