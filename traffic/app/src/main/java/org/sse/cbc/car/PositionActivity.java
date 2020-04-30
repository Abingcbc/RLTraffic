package org.sse.cbc.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;

import org.sse.cbc.car.domain.TrafficOrder;
import org.sse.cbc.car.utils.JWebSocketClient;

import java.net.URI;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PositionActivity extends AppCompatActivity {

    private MapView mapView;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private JWebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        mapView = findViewById(R.id.position_map);
        mapView.onCreate(savedInstanceState);
        AMap aMap = null;
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.car));
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

//        mLocationListener = new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                if (aMapLocation != null) {
//                    if (aMapLocation.getErrorCode() == 0) {
//                        speedTextView.setText("当前速度："+ aMapLocation.getSpeed() +"米/秒");
//                        altitudeTextView.setText("当前海拔："+aMapLocation.getAltitude()+"米");
//                    }else {
//                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError","location Error, ErrCode:"
//                                + aMapLocation.getErrorCode() + ", errInfo:"
//                                + aMapLocation.getErrorInfo());
//                    }
//                }
//            }
//        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setInterval(1000);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

        recyclerView = findViewById(R.id.order_position);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        orderAdapter = new OrderAdapter(this,
                new CopyOnWriteArrayList<String>(), 0);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(orderAdapter);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter.add("智能再平衡模式", 0);
        orderAdapter.add("司机再平衡模式", 1);
        orderAdapter.add("再平衡推荐地点", 2);

        initWebSocket();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        client.close();
    }

    void initWebSocket() {
        URI uri = URI.create("ws://"+Constant.hostname+"/order/"+ System.currentTimeMillis());
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                TrafficOrder trafficOrder = gson.fromJson(message, TrafficOrder.class);
                Intent intent = new Intent(PositionActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order", trafficOrder);
                this.close();
                startActivity(intent);
            }
        };
        client.connect();
    }
}
