package com.github.mobj.util.regex;

import com.github.mobj.util.exception.RegexpIllegalException;
import com.github.mobj.util.exception.TypeNotMatchException;
import com.github.mobj.util.exception.UninitializedException;

/**
 * @author luoruihua
 * @date 2020/12/12
 */
public interface Node {

    String getExpression();

    String random() throws UninitializedException, RegexpIllegalException;

    boolean test();

    void init() throws RegexpIllegalException, TypeNotMatchException;

    boolean isInitialized();
}
