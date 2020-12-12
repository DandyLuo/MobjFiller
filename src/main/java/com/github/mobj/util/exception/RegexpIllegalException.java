package com.github.mobj.util.exception;

/**
 * @author luoruihua
 * @date 2020/12/12
 */
public class RegexpIllegalException extends Exception {

    public RegexpIllegalException() {
        super();
    }

    public RegexpIllegalException(String message) {
        super(message);
    }

    public RegexpIllegalException(String regexp, int index) {
        super(String.format("Invalid regular expression: %s, Index: %d", regexp, index));
    }
}
