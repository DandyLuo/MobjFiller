package com.github.mobj.util.exception;

/**
 * @author luoruihua
 * @date 2020/12/12
 */
public class UninitializedException extends Exception {

    public UninitializedException() {
        super();
    }

    public UninitializedException(String message) {
        super(message);
    }
}