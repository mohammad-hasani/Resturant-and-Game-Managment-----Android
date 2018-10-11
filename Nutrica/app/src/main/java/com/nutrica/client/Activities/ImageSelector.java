package com.nutrica.client.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutrica.client.G;
import com.nutrica.client.Listeners.ClickListener;
import com.nutrica.client.Listeners.RecyclerTouchListener;
import com.nutrica.client.adapters.*;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructAvatar;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 19/09/2017.
 */

public class ImageSelector extends Activity {

    private ArrayList<StructAvatar> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_selector);

        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int id = list.get(position).getId();

                Intent intent = new Intent();
                intent.putExtra("id", id);

                setResult(RESULT_OK, intent);

                ((Activity)ImageSelector.this).finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        list = fillData();
        AdapterImageSelector adapter = new AdapterImageSelector(this, list);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<StructAvatar> fillData() {
        ArrayList<StructAvatar> list = new ArrayList<>();
        AssetManager assetManager = getAssets();
        String[] filesname;
        try {
            filesname = assetManager.list(G.AVATAR_PATH);
        } catch (IOException e) {
            return null;
        }
        for (int i = 0; i < filesname.length; i++) {
            StructAvatar tmp = new StructAvatar();
            tmp.setId(i);
            tmp.setUrl("file:///android_asset/" + G.AVATAR_PATH +"/" + filesname[i]);
            tmp.setName("");
            list.add(tmp);
        }
        return list;
    }
}
