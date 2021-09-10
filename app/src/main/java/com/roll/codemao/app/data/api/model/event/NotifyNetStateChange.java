package com.roll.codemao.app.data.api.model.event;

/**
 * Author by cretin, Email mxnzp_life@163.com, Date on 2018/5/11.
 * PS: Not easy to write code, please care for the programmer.
 * Description:通知网络变化
 */
public class NotifyNetStateChange {
    public NotifyNetStateChange(boolean state) {
        this.state = state;
    }

    private boolean state;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
