/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.domain;

/**
 *
 * We raise this exception in case we can't determine an owner for a specfic log (shouldn't really happen)
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
}
