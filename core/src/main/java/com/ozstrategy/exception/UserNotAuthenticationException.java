package com.ozstrategy.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by lihao on 12/30/14.
 */
public class UserNotAuthenticationException extends AuthenticationException {
    public UserNotAuthenticationException(String msg) {
        super(msg);
    }
}
