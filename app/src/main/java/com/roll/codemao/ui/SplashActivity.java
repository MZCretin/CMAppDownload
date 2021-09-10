package com.roll.codemao.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cretin.tools.fanpermission.FanPermissionListener;
import com.cretin.tools.fanpermission.FanPermissionUtils;
import com.roll.codemao.R;
import com.roll.codemao.base.ui.RootActivity;

import org.jetbrains.annotations.Nullable;


/**
 * Created by sks on 2016/4/7.
 */
public class SplashActivity extends RootActivity {

    @Override
    protected void initView(@Nullable View view, @Nullable Bundle savedInstanceState) {
        FanPermissionUtils.with(this)
                //添加所有你需要申请的权限
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //添加权限申请回调监听 如果申请失败 会返回已申请成功的权限列表，用户拒绝的权限列表和用户点击了不再提醒的永久拒绝的权限列表
                .setPermissionsCheckListener(new FanPermissionListener() {
                    @Override
                    public void permissionRequestSuccess() {
                        ((TextView) findViewById(R.id.tv_splash)).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1000);
                    }

                    @Override
                    public void permissionRequestFail(String[] grantedPermissions, String[] deniedPermissions, String[] forceDeniedPermissions) {

                    }
                })
                //生成配置
                .createConfig()
                //配置是否强制用户授权才可以使用，当设置为true的时候，如果用户拒绝授权，会一直弹出授权框让用户授权
                .setForceAllPermissionsGranted(true)
                //配置当用户点击了不再提示的时候，会弹窗指引用户去设置页面授权，这个参数是弹窗里面的提示内容
                .setForceDeniedPermissionTips("请前往设置->应用->【" + FanPermissionUtils.getAppName(this) + "】->权限中打开相关权限，否则功能无法正常运行！")
                //构建配置并生效
                .buildConfig()
                //开始授权
                .startCheckPermission();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void refreshPageData() {

    }
}
