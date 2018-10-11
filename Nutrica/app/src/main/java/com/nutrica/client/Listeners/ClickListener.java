package com.nutrica.client.Listeners;

import android.view.View;

/**
 * Created by root on 6/27/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}