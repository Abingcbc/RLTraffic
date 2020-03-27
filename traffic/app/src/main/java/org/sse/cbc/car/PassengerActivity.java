package org.sse.cbc.car;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

import org.sse.cbc.car.domain.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import github.chenupt.springindicator.SpringIndicator;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PassengerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private MapView mapView;

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

        SpringIndicator indicator = findViewById(R.id.info_indicator);
        indicator.setViewPager(viewPager);


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
    }
}
