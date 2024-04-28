package com.example.photoalbum;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xw.repo.VectorCompatTextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;



public class AlbumGridRecycleViewAdapter extends RecyclerView.Adapter<AlbumGridRecycleViewAdapter.RecyclerViewHolder> {

    private ArrayList<AlbumGridRecyclerData> items;
    private Context mcontext;
    private OnClickListener onClickListener;


    public AlbumGridRecycleViewAdapter(ArrayList<AlbumGridRecyclerData> recyclerDataArrayList, Context mcontext) {
        this.items = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_grid_card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int pos) {
        // Set the data to textview and imageview.
        AlbumGridRecyclerData recyclerData = items.get(holder.getAdapterPosition());

        holder.vectorCompatTextView.setText(recyclerData.getTitle());
        //holder.vectorCompatTextView.setDrawable

        Drawable newDrawable;

        try {
            InputStream inputStream = mcontext.getContentResolver().openInputStream(recyclerData.getImgUri());
            newDrawable = Drawable.createFromStream(inputStream, recyclerData.getImgUri().toString() );
        } catch (FileNotFoundException e) {
            newDrawable = mcontext.getResources().getDrawable(R.drawable.parrot);
        }

        new VectorCompatTextView.CompoundDrawableConfigBuilder(holder.vectorCompatTextView)
                .setDrawableTop(newDrawable)
                .build();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.itemView, holder.getAdapterPosition(), recyclerData.getAlbum());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View view, int position, Album model);
    }

    // View Holder Class to handle Recycler View.
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private VectorCompatTextView vectorCompatTextView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            vectorCompatTextView = itemView.findViewById(R.id.vectorCompat);
        }
    }
}
