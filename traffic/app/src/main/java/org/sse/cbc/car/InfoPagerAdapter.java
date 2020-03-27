package org.sse.cbc.car;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.sse.cbc.car.domain.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2020-03-27
 */
public class InfoPagerAdapter extends PagerAdapter {

    private List<Info> infoList;
    private LayoutInflater inflater;
    private List<View> infoViewList;

    InfoPagerAdapter(List<Info> infoList, LayoutInflater inflater) {
        this.infoList = infoList;
        this.inflater = inflater;
        infoViewList = new ArrayList<>();
        for (Info info: infoList) {
            infoViewList.add(inflater.inflate(R.layout.fragment_info, null));
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(infoViewList.get(position));
        Info cur = infoList.get(position);
        View curView = infoViewList.get(position);
        ((TextView)curView.findViewById(R.id.info_type)).setText(cur.infoType);
        ((TextView)curView.findViewById(R.id.info_content)).setText(cur.infoContent);
        ((TextView)curView.findViewById(R.id.info_unit)).setText(cur.infoUnit);
        return curView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(infoViewList.get(position));
    }

    @Override
    public int getCount() {
        return infoViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
