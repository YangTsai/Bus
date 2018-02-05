package com.hyst.bus.util;

import android.Manifest;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.hyst.bus.R;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.cache.LocationCache;
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

public class LocationUtil implements AMapLocationListener {
    //定位
    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;
    //
    private static LocationUtil locationUtil;
    public static Context mContext;
    //
    private String tag;
    private boolean isCallBack;

    public static LocationUtil getIns(Context context) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil();
        }
        mContext = context;
        return locationUtil;
    }

    /**
     * 设置定位参数，启动定位
     *
     * @param tag        定位发起标签（默认传入类名）
     * @param isCallBack 是否回调
     */
    public void setLocation(String tag, boolean isCallBack) {
        this.tag = tag;
        this.isCallBack = isCallBack;
        boolean connected = NetWorkUtil.isNetworkConnected(mContext);
        if (!connected) {
            ToastUtil.show(mContext, "请打开网络连接");
            return;
        }
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
        }
        if (mLocationOption == null) {
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
        }
        mlocationClient.startLocation();
        ProgressUtil.showDlg(mContext, "正在定位...");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        ProgressUtil.cancleDlg();
        final SetPointEvent event = new SetPointEvent();
        event.setTag(tag);
        if (aMapLocation.getErrorCode() == 0) {
            //将定位信息存入本地
            LocationCache locationCache = new LocationCache();
            locationCache.setAoiName(aMapLocation.getAoiName());
            locationCache.setAddress(aMapLocation.getAddress());
            locationCache.setCityCode(aMapLocation.getCityCode());
            locationCache.setCityName(aMapLocation.getCity());
            locationCache.setLatitude(aMapLocation.getLatitude());
            locationCache.setLongitude(aMapLocation.getLongitude());
            ACache.get(mContext).put(Constant.LOCATION_CONFIG, locationCache);
            //将定位信息返回给发起定位的地方
            if (isCallBack) {
                event.setContent(aMapLocation.getAoiName());
                event.setaMapLocation(aMapLocation);
                event.setLatLonPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                EventBus.getDefault().post(event);
            }
        } else if (aMapLocation.getErrorCode() == 12) {
            ACache.get(mContext).remove(Constant.LOCATION_CONFIG);
            List<PermissionItem> permissionItems = new ArrayList<>();
            permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
            HiPermission.create(mContext)
                    .permissions(permissionItems)
                    .style(R.style.PermissionDefaultBlueStyle)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            EventBus.getDefault().post(event);
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
        mlocationClient.stopLocation();
    }


    /**
     * 获取当前位置信息
     *
     * @return
     */
    public LocationCache getCurrentLocation() {
        LocationCache locationCache = (LocationCache) ACache.get(mContext).getAsObject(Constant.LOCATION_CONFIG);
        if (locationCache == null) {
            locationCache = new LocationCache();
            locationCache.setCityName(Constant.DEFAULT_CITY);
            locationCache.setLatitude(Constant.DEFAULT_SHANTOU.latitude);
            locationCache.setLongitude(Constant.DEFAULT_SHANTOU.longitude);
        }
        return locationCache;
    }
}
