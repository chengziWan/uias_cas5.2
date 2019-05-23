package com.example.cas.exception;

import javax.security.auth.login.AccountExpiredException;

/**
 * @author wancc----自定义异常
 * @version 2019年5月16日 下午4:08:35创建
 */
public class CustomException extends AccountExpiredException {
	private static final long serialVersionUID = 1L;

    public CustomException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CustomException(String msg) {
        super(msg);
        System.out.println("CustomException.........."+msg);
        // TODO Auto-generated constructor stub
    }
}
