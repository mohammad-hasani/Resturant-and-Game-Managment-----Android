package com.nutrica.client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nutrica.client.nutrica.R;
import com.nutrica.client.structures.StructAvatar;

import java.util.ArrayList;


public class AdapterImageSelector extends RecyclerView.Adapter<AdapterImageSelector.MyViewHolder>  {

    private ArrayList<StructAvatar> mSpacePhotos;
    private Context mContext;

    public AdapterImageSelector(Context context, ArrayList<StructAvatar> spacePhotos) {
        mContext = context;
        mSpacePhotos = spacePhotos;
    }

    @Override
    public AdapterImageSelector.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.image_selector_item, parent, false);
        AdapterImageSelector.MyViewHolder viewHolder = new AdapterImageSelector.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterImageSelector.MyViewHolder holder, int position) {

        StructAvatar spacePhoto = mSpacePhotos.get(position);
        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(spacePhoto.getUrl())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mSpacePhotos.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
//                SpacePhoto spacePhoto = mSpacePhotos[position];
//                Intent intent = new Intent(mContext, SpacePhotoActivity.class);
//                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
//                startActivity(intent);
            }
        }
    }


}