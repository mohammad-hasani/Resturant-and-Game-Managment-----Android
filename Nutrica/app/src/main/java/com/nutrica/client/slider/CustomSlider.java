package com.nutrica.client.slider;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nutrica.client.G;
import com.nutrica.client.structures.StructSlider;

import java.util.ArrayList;
import java.util.Stack;


public class CustomSlider implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    public static ArrayList<StructSlider> structSlider;
    private BaseSliderView.OnSliderClickListener listener;


    public void callSlider(final SliderLayout mDemoSlider, ArrayList<StructSlider> sliderData,
                           BaseSliderView.OnSliderClickListener listener) {
        this.mDemoSlider = mDemoSlider;
        this.structSlider = sliderData;
        this.listener = listener;

        getUrls(structSlider);

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
/*        String name = slider.getBundle().get("extra").toString();
        for (StructSlider s : structSlider) {
            if (name.equals(s.getName())) {
                if (s.getType() == 1) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getData()));
                        G.context.startActivity(browserIntent);
                    } catch (Exception e) {

                    }
                } else if (s.getType() == 2) {

                }
                break;
            }
        }*/
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void getUrls(ArrayList<StructSlider> sliderData) {
        for (StructSlider value : sliderData) {
            int id = value.getId();
            String pictureUrl = value.getUrlPic();
            String name = value.getName();
            int type = value.getType();
            String data = value.getData();

            final TextSliderView textSliderView = new TextSliderView(G.context);
            textSliderView
                    .description(value.getName())
                    .image(value.getUrlPic())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(listener);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", value.getName());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mDemoSlider.addSlider(textSliderView);
                }
            });
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        mDemoSlider.setDuration(4000);

        mDemoSlider.addOnPageChangeListener(CustomSlider.this);
    }
}
