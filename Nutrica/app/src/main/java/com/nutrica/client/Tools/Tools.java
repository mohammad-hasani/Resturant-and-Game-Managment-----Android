package com.nutrica.client.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import com.nutrica.client.Enum.Enums;
import com.nutrica.client.G;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by root on 18/09/2017.
 */

public class Tools {
    public static void copyDB(Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream;
        try {
            inputStream = assetManager.open("db/game.db");
            HelperIO.copyFile(inputStream, G.FILE_DATABASE);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Drawable[] readImagesFromAssets(Context context, String folder) {
        try {
            // to reach asset
            AssetManager assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] images = assetManager.list(folder);
            // to keep all image
            Drawable[] drawables = new Drawable[images.length];
            // the loop read all image in dogs folder and  aa
            for (int i = 0; i < images.length; i++) {
                InputStream inputStream = context.getAssets().open(folder + "/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;
            }
            return drawables;
        } catch (IOException e) {
            // you can print error or log.
        }
        return null;
    }

    public static Drawable readImagesFromAssetsFromId(Context context, String folder, int id) {
        try {
            // to reach asset
            AssetManager assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] images = assetManager.list(folder);
            InputStream inputStream = context.getAssets().open(folder + "/" + images[id]);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            return drawable;
        } catch (IOException e) {
            // you can print error or log.
        }
        return null;
    }
}
