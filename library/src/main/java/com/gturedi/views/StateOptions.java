package com.gturedi.views;

import android.view.View;

import java.io.Serializable;

/**
 * Created by twiceYuan on 2017/3/6.
 *
 * Common State Options
 */
public abstract class StateOptions implements Serializable {

    View stateView;

    protected StateOptions(View stateView) {
        this.stateView = stateView;
    }

    protected abstract void init();

    public View rootView() {
        return stateView;
    }
}
