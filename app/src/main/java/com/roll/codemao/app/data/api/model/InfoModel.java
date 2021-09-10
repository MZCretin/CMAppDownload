package com.roll.codemao.app.data.api.model;

import java.util.List;

/**
 * Created by cretin on 16/10/27.
 */

public class InfoModel {
    /**
     * title : null
     * img : https://www.fangrongjie.com/UF/Uploads/Ad/20161010092531202.jpg
     * disc : null
     * url : https://www.fangrongjie.com/Weixin/Find/detail_notice?id=1521&is_app=1
     */

    private List<NewListBean> new_list;
    /**
     * title :
     * img : https://www.fangrongjie.com/UF/Uploads/Ad/20161010091047230.jpg
     * disc :
     * url : https://www.fangrongjie.com/weixin/Advert/special_guoqing2016?is_app=1
     */

    private List<OldListBean> old_list;

    public List<NewListBean> getNew_list() {
        return new_list;
    }

    public void setNew_list(List<NewListBean> new_list) {
        this.new_list = new_list;
    }

    public List<OldListBean> getOld_list() {
        return old_list;
    }

    public void setOld_list(List<OldListBean> old_list) {
        this.old_list = old_list;
    }

    public static class NewListBean {
        private Object title;
        private String img;
        private Object disc;
        private String url;

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getDisc() {
            return disc;
        }

        public void setDisc(Object disc) {
            this.disc = disc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class OldListBean {
        private String title;
        private String img;
        private String disc;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDisc() {
            return disc;
        }

        public void setDisc(String disc) {
            this.disc = disc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
