package com.pig4cloud.pig.common.core.exception;

import lombok.NoArgsConstructor;

/**
 * @author lengleng
 * @date 2018年06月22日16:22:03
 * 403 授权拒绝
 */
@NoArgsConstructor
public class PigDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PigDeniedException(String message) {
		super(message);
	}

	public PigDeniedException(Throwable cause) {
		super(cause);
	}

	public PigDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public PigDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
