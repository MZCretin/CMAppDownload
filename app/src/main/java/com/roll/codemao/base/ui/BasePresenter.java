package com.roll.codemao.base.ui;

import com.roll.codemao.app.data.api.service.ApiService;
import com.roll.codemao.base.ui.helper.HttpHelper;

import org.jetbrains.annotations.Nullable;

/**
 * Created by jichengzhi on 2017/4/21.
 */

public interface BasePresenter {

    void start();

    void onDestroy();

    void onResume();

    void bind(@Nullable HttpHelper httpHelper, @Nullable ApiService apiService);
}
