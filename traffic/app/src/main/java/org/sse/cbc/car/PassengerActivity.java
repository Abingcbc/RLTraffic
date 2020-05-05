package org.sse.cbc.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sse.cbc.car.domain.Info;
import org.sse.cbc.car.domain.TrafficOrder;
import org.sse.cbc.car.speechrecognizerlib.speech.util.JsonParser;
import org.sse.cbc.car.utils.JWebSocketClient;
import org.sse.cbc.car.utils.SharedPreferencesUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PassengerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private MapView mapView;
    private JWebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        viewPager = findViewById(R.id.info_cards);
        List<Info> infoArrayList = new ArrayList<>();
        infoArrayList.add(new Info("天气","晴",""));
        infoArrayList.add(new Info("温度","25","c"));
        infoArrayList.add(new Info("速度","77","km/s"));
        infoArrayList.add(new Info("风速","36","km/s"));
        InfoPagerAdapter pagerAdapter = new InfoPagerAdapter(infoArrayList, getLayoutInflater());
        viewPager.setAdapter(pagerAdapter);


        recyclerView = findViewById(R.id.order_passenger);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        orderAdapter = new OrderAdapter(this,
                new CopyOnWriteArrayList<String>(), 1);
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(orderAdapter);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter.add("智能再平衡模式", 0);
        orderAdapter.add("司机再平衡模式", 1);
        orderAdapter.add("再平衡推荐地点", 2);

        SharedPreferencesUtils utils = new SharedPreferencesUtils(this);
        if (utils.isPassenger()) {
            recyclerView.setVisibility(View.INVISIBLE);
            findViewById(R.id.balance).setVisibility(View.INVISIBLE);
        } else {
            initWebSocket();
        }

        mapView = findViewById(R.id.passenger_map);
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

        ImageView microphone = findViewById(R.id.voice);
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerActivity.this, VoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
    }

    void initWebSocket() {
        URI uri = URI.create("ws://"+Constant.hostname+"/order/"+ System.currentTimeMillis());
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                TrafficOrder trafficOrder = gson.fromJson(message, TrafficOrder.class);
                Intent intent = new Intent(PassengerActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("order", trafficOrder);
                this.close();
                startActivity(intent);
            }
        };
        client.connect();
    }
}
