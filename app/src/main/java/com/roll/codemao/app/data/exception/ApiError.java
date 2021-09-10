package com.roll.codemao.app.data.exception;

/**
 * Created by grubber on 2017/1/6.
 */
public class ApiError {
    private Long _code = -1L;
    private String _msg;

    public Long getCode() {
        return _code;
    }

    public String getMsg() {
        return _msg;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "_code=" + _code +
                ", _msg='" + _msg + '\'' +
                '}';
    }
}
