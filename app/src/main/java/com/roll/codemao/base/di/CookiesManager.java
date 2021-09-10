package com.roll.codemao.base.di;

/**
 * Created by cretin on 16/11/3.
 */

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自动管理Cookies
 */
public class CookiesManager implements CookieJar {

    public CookiesManager(Context context) {
        cookieStore = new PersistentCookieStore(context);
    }

    private final PersistentCookieStore cookieStore;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for ( Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

    public void removeCookies() {
        if (cookieStore != null)
            cookieStore.removeAll();
    }

    public PersistentCookieStore getCookieStore() {
        return cookieStore;
    }
}