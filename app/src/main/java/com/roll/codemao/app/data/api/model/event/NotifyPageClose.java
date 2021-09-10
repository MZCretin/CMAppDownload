package com.roll.codemao.app.data.api.model.event;

/**
 * Author by cretin, Email mxnzp_life@163.com, Date on 2018/4/23.
 * PS: Not easy to write code, please care for the programmer.
 * Description:通知页面关闭
 */
public class NotifyPageClose {
    private String pageClass;

    public String getPageClass() {
        return pageClass;
    }

    public void setPageClass(String pageClass) {
        this.pageClass = pageClass;
    }

    public NotifyPageClose(String pageClass) {
        this.pageClass = pageClass;
    }
}
