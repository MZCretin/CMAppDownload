package com.roll.codemao.app.data.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

import com.roll.codemao.R;
import com.roll.codemao.app.data.api.model.event.NotifyNetStateChange;
import com.roll.codemao.utils.dialog.CommDialogUtils;

import org.greenrobot.eventbus.EventBus;


public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP ) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            // 获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ( wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected() ) {
//                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new NotifyNetStateChange(true));
            } else if ( wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected() ) {
//                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                showDialog(context);
            } else if ( !wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected() ) {
//                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new NotifyNetStateChange(true));
            } else {
//                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
                showDialog(context);
            }
            //API大于23时使用下面的方式进行网络监听
        } else {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            if ( networks == null || networks.length == 0 ) {
                //网络未连接
                showDialog(context);
            } else {
                //网络已连接
                EventBus.getDefault().post(new NotifyNetStateChange(true));
            }
        }
    }

    private void showDialog(Context context) {
        CommDialogUtils.showCustomToast(context, R.layout.view_toast_login, new CommDialogUtils.ViewLoadListener() {
            @Override
            public void onViewLoad(View view) {
                ((TextView) view.findViewById(R.id.tv_info)).setText("网络似乎不畅...");
            }
        });
    }
}