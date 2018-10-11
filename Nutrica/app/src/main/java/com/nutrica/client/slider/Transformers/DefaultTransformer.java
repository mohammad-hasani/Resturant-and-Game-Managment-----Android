package com.nutrica.client.slider.Transformers;

import android.view.View;

import com.daimajia.slider.library.Transformers.*;

public class DefaultTransformer extends com.daimajia.slider.library.Transformers.BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
