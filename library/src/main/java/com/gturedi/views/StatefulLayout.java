package com.gturedi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Android layout to show most common state templates like loading, empty, error etc. To do that all you need to is
 * wrap the target area(view) with StatefulLayout. For more information about usage look
 * <a href="https://github.com/gturedi/StatefulLayout#usage">here</a>
 */
public class StatefulLayout extends LinearLayout {

    private static final String  MSG_ONE_CHILD        = "StatefulLayout must have one child!";
    private static final boolean DEFAULT_ANIM_ENABLED = true;
    private static final int     DEFAULT_IN_ANIM      = android.R.anim.fade_in;
    private static final int     DEFAULT_OUT_ANIM     = android.R.anim.fade_out;

    private          boolean animationEnabled;
    @AnimRes private int     inAnimation;
    @AnimRes private int     outAnimation;

    private View        content;
    private FrameLayout stContainer;

    public StatefulLayout(Context context) {
        this(context, null);
    }

    public StatefulLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.stfStatefulLayout, 0, 0);
        animationEnabled = array.getBoolean(R.styleable.stfStatefulLayout_stfAnimationEnabled, DEFAULT_ANIM_ENABLED);
        inAnimation = array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation, DEFAULT_IN_ANIM);
        outAnimation = array.getResourceId(R.styleable.stfStatefulLayout_stfOutAnimation, DEFAULT_OUT_ANIM);
        array.recycle();
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    @AnimRes
    public int getInAnimation() {
        return inAnimation;
    }

    public void setInAnimation(@AnimRes int anim) {
        inAnimation = anim;
    }

    @AnimRes
    public int getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(@AnimRes int anim) {
        outAnimation = anim;
    }

    // content //

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) throw new IllegalStateException(MSG_ONE_CHILD);
        if (isInEditMode()) return; // hide state views in designer
        setOrientation(VERTICAL);
        content = getChildAt(0); // assume first child as content
        LayoutInflater.from(getContext()).inflate(R.layout.stf_template, this, true);
        stContainer = (FrameLayout) findViewById(R.id.stContainer);
    }

    // loading //

    public void showContent() {
        if (isAnimationEnabled()) {
            if (stContainer.getVisibility() == VISIBLE) {
                Animation outAnim = createOutAnimation();
                outAnim.setAnimationListener(new StateAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        stContainer.setVisibility(GONE);
                        content.setVisibility(VISIBLE);
                        content.startAnimation(createInAnimation());
                    }
                });
                stContainer.startAnimation(outAnim);
            }
        } else {
            stContainer.setVisibility(GONE);
            content.setVisibility(VISIBLE);
        }
    }

    public void showLoading() {
        showLoading(R.string.stfLoadingMessage);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(str(resId));
    }

    // empty //

    public void showLoading(String message) {
        showCustom(new SimpleStateOptions()
                .message(message)
                .loading());
    }

    public void showEmpty() {
        showEmpty(R.string.stfEmptyMessage);
    }

    public void showEmpty(@StringRes int resId) {
        showEmpty(str(resId));
    }

    // error //

    public void showEmpty(String message) {
        showCustom(new SimpleStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty));
    }

    public void showError(OnClickListener clickListener) {
        showError(R.string.stfErrorMessage, clickListener);
    }

    public void showError(@StringRes int resId, OnClickListener clickListener) {
        showError(str(resId), clickListener);
    }

    // offline

    public void showError(String message, OnClickListener clickListener) {
        showCustom(new SimpleStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_error)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener));
    }

    public void showOffline(OnClickListener clickListener) {
        showOffline(R.string.stfOfflineMessage, clickListener);
    }

    public void showOffline(@StringRes int resId, OnClickListener clickListener) {
        showOffline(str(resId), clickListener);
    }

    // location off //

    public void showOffline(String message, OnClickListener clickListener) {
        showCustom(new SimpleStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener));
    }

    public void showLocationOff(OnClickListener clickListener) {
        showLocationOff(R.string.stfLocationOffMessage, clickListener);
    }

    public void showLocationOff(@StringRes int resId, OnClickListener clickListener) {
        showLocationOff(str(resId), clickListener);
    }

    // custom //

    public void showLocationOff(String message, OnClickListener clickListener) {
        showCustom(new SimpleStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_location_off)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener));
    }

    /**
     * Shows custom state for given options. If you do not set buttonClickListener, the button will not be shown
     *
     * @param options customization options
     * @see SimpleStateOptions
     */
    // helper methods //
    public void showCustom(final StateOptions options) {
        stContainer.clearAnimation();
        content.clearAnimation();

        stContainer.post(new Runnable() {
            @Override
            public void run() {
                if (isAnimationEnabled()) {
                    if (stContainer.getVisibility() == GONE) {
                        Animation outAnim = createOutAnimation();
                        outAnim.setAnimationListener(new StateAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                content.setVisibility(GONE);
                                stContainer.setVisibility(VISIBLE);
                                stContainer.startAnimation(createInAnimation());
                            }
                        });
                        content.startAnimation(outAnim);
                        refreshContainer(options);
                    } else {
                        Animation outAnim = createOutAnimation();
                        outAnim.setAnimationListener(new StateAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                refreshContainer(options);
                                stContainer.startAnimation(createInAnimation());
                            }
                        });
                        stContainer.startAnimation(outAnim);
                    }
                } else {
                    content.setVisibility(GONE);
                    stContainer.setVisibility(VISIBLE);
                    refreshContainer(options);
                }
            }
        });
    }

    private void refreshContainer(StateOptions options) {
        stContainer.removeAllViews();
        if (options.stateView == null) {
            options.stateView = LayoutInflater.from(getContext()).inflate(options.layoutId(), stContainer, true);
            options.setAttachedStf(this);
        } else {
            stContainer.addView(options.stateView);
        }
        options.init();
    }

    private String str(@StringRes int resId) {
        return getContext().getString(resId);
    }

    private Animation createInAnimation() {
        return AnimationUtils.loadAnimation(getContext(), inAnimation);
    }

    private Animation createOutAnimation() {
        return AnimationUtils.loadAnimation(getContext(), outAnimation);
    }
}
