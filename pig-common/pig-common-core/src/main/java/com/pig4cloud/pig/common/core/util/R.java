package com.pig4cloud.pig.common.core.util;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static com.pig4cloud.pig.common.core.constant.CommonConstants.FAIL;
import static com.pig4cloud.pig.common.core.constant.CommonConstants.SUCCESS;


/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
@Builder
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        return new R<T>().setCode(code).setData(data).setMsg(msg);
    }
}

