package org.sse.cbc.car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;

import ng.max.slideview.SlideView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SlideView slideView = findViewById(R.id.slideView);
        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Poi start = new Poi("", new LatLng(31.28280538, 121.21507645), "");
                Poi end = new Poi("", new LatLng(31.28246613, 121.50639653), "");
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(),
                        new AmapNaviParams(start, null, end,
                                AmapNaviType.DRIVER, AmapPageType.NAVI), null);
            }
        });
    }
}
