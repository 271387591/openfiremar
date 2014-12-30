package com.ozstrategy.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by lihao on 12/30/14.
 */
public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
