package com.hyst.bus.util;

import android.Manifest;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.hyst.bus.R;
import com.hyst.bus.model.event.SetPointEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by Administrator on 2018/1/17.
 */

public class LocationUtil {
    //定位
    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;

    public static void setLocation(final Context context, final String tag) {
        //初始化定位
        mlocationClient = new AMapLocationClient(context);
        //配置定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);

        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        //设置定位回调监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getAddress();
                    aMapLocation.getAoiName();
                    SetPointEvent event = new SetPointEvent();
                    event.setTag(tag);
                    event.setContent(aMapLocation.getAoiName());
                    event.setLatLonPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    EventBus.getDefault().post(event);
                } else if(aMapLocation.getErrorCode() == 12){
                    List<PermissionItem> permissionItems = new ArrayList<>();
                    permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
                    HiPermission.create(context)
                            .permissions(permissionItems)
                            .style(R.style.PermissionDefaultBlueStyle)
                            .checkMutiPermission(new PermissionCallback() {
                                @Override
                                public void onClose() {

                                }

                                @Override
                                public void onFinish() {
                                    mlocationClient.startLocation();
                                }

                                @Override
                                public void onDeny(String permission, int position) {

                                }

                                @Override
                                public void onGuarantee(String permission, int position) {

                                }
                            });

                }
            }
        });
    }

}
