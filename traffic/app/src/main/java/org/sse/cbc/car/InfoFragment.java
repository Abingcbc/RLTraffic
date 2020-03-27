package org.sse.cbc.car;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InfoFragment extends Fragment {

    private String infoType;
    private String info;
    private String infoUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoType = getArguments().getString("infoType");
        info = getArguments().getString("info");
        infoUnit = getArguments().getString("infoUnit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView)getView().findViewById(R.id.info_type)).setText(infoType);
        ((TextView)getView().findViewById(R.id.info_content)).setText(info);
        ((TextView)getView().findViewById(R.id.info_unit)).setText(infoUnit);
    }
}
