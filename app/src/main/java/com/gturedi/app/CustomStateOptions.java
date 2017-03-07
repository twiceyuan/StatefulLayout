package com.gturedi.app;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gturedi.views.StateOptions;

public class CustomStateOptions extends StateOptions {

    OnOpenListener mOnOpenListener;

    public void setOnOpenListener(OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }

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
                if (isChecked && mOnOpenListener != null) {
                    mOnOpenListener.open();
                }
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnOpenListener {
        void open();
    }
}