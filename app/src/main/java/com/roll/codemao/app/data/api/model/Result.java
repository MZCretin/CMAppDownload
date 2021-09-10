package com.roll.codemao.app.data.api.model;

import com.roll.codemao.R;
import com.roll.codemao.utils.ResUtils;

/**
 * Created by cretin on 16/10/27.
 */

public class Result<T> {
    private String message;
    private int code;
    private T data;

    public Result<T> createSuccess(T t) {
        this.data = t;
        this.code = 1;
        this.message = ResUtils.getString(R.string.data_obtain_success);
        return this;
    }

    public Result<T> createFail(String msg) {
        this.data = null;
        this.code = 0;
        this.message = msg;
        return this;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
