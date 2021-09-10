package com.roll.codemao.app.data.api.model.event;

/**
 * Created by cretin on 2018/3/27.
 * eventbus的通知类 当登录状态改变的时候触发页面修改状态
 */

public class LoginStateChange {
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_LOGOUT = 2;
    private int type;

    public LoginStateChange() {
    }

    public LoginStateChange(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
