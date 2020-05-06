package org.sse.cbc.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import org.sse.cbc.car.domain.TrafficOrder;

import java.util.ArrayList;
import java.util.List;

import ng.max.slideview.SlideView;

public class StartActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener {

    private TrafficOrder trafficOrder;

    private SlideView slideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        trafficOrder = (TrafficOrder) intent.getSerializableExtra("order");
        String name = intent.getStringExtra("name");
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        slideView = findViewById(R.id.slideView);

        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Toast.makeText(slideView.getContext(), "正在获取地址中...", Toast.LENGTH_LONG).show();
            }
        });

        if (trafficOrder != null) {
            LatLonPoint latLng = new LatLonPoint(trafficOrder.destinationLatitude,
                    trafficOrder.destinationLongitude);
            geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(latLng, 100, GeocodeSearch.AMAP));

            SlideView slideView = findViewById(R.id.slideView);
            slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideView slideView) {
                    Poi start = new Poi("起点", new LatLng(trafficOrder.driverLatitude,
                            trafficOrder.driverLongitude), "");
                    Poi end = new Poi("终点", new LatLng(trafficOrder.destinationLatitude,
                            trafficOrder.destinationLongitude), "");
                    List<Poi> passenger = new ArrayList<>();
                    passenger.add(new Poi("乘客位置", new LatLng(trafficOrder.passengerLatitude,
                            trafficOrder.passengerLongitude), ""));

                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                            new AmapNaviParams(start, passenger, end,
                                    AmapNaviType.DRIVER, AmapPageType.NAVI), null);
                }
            });
        } else {
            TextView view = findViewById(R.id.destination_name);
            if (name == null) {
                view.setText("无识别结果");
            } else {
                view.setText("识别结果: " + name);
                geocodeSearch.getFromLocationNameAsyn(new GeocodeQuery(name, "shanghai"));
            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000 && regeocodeResult != null) {
            TextView view = findViewById(R.id.destination_name);
            view.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if ( i == 1000 ) {
            TextView view = findViewById(R.id.destination_name);
            view.setText(geocodeResult.getGeocodeAddressList().get(0).getFormatAddress());

            final LatLonPoint destination = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
            slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideView slideView) {
                    Poi start = new Poi("起点", new LatLng(31.36185, 121.25021), "");
                    Poi end = new Poi("终点", new LatLng(destination.getLatitude(),
                            destination.getLongitude()), "");
                    List<Poi> passenger = new ArrayList<>();

                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                            new AmapNaviParams(start, passenger, end,
                                    AmapNaviType.DRIVER, AmapPageType.NAVI), null);
                }
            });
        }

    }
}
