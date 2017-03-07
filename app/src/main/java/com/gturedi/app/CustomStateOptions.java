package com.gturedi.app;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gturedi.views.StateOptions;

@SuppressWarnings("ALL")
public class CustomStateOptions extends StateOptions {

    @Override
    protected int layoutId() {
        return R.layout.custom_state;
    }

    @Override
    protected void init(View rootView) {
        Switch switchTest = (Switch) rootView.findViewById(R.id.switch_test);
        switchTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getAttachedStf().showContent();
                }
            }
        });
    }
}