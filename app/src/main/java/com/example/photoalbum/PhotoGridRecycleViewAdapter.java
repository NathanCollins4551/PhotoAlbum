package com.example.photoalbum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotoGridRecycleViewAdapter extends RecyclerView.Adapter<PhotoGridRecycleViewAdapter.RecyclerViewHolder> {

    private ArrayList<PhotoGridRecyclerData> items;
    private Context mcontext;
    private OnClickListener onClickListener;


    public PhotoGridRecycleViewAdapter(ArrayList<PhotoGridRecyclerData> recyclerDataArrayList, Context mcontext) {
        this.items = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_grid_card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int pos) {
        // Set the data to textview and imageview.
        PhotoGridRecyclerData recyclerData = items.get(holder.getAdapterPosition());
        holder.courseIV.setImageBitmap(recyclerData.getImgBitmap());
        //holder.courseIV.setImageResource(recyclerData.getImgID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getAdapterPosition());
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
        void onClick(int position);
    }

    // View Holder Class to handle Recycler View.
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView courseIV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVcourseIVPhoto);
        }
    }
}

