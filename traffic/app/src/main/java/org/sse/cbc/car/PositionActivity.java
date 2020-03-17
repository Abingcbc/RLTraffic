package org.sse.cbc.car;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.concurrent.CopyOnWriteArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PositionActivity extends AppCompatActivity {

    private MapView mapView;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

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

        recyclerView = findViewById(R.id.order);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        orderAdapter = new OrderAdapter(this, new CopyOnWriteArrayList<String>());
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(orderAdapter);
        animationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < 100; i++) {
            orderAdapter.add("order", i);
        }

    }
}
