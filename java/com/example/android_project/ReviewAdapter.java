package com.example.android_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {

    private ArrayList<ReviewData> arrayList;
    private Context context;
    private final int REVISE_NUM = 1000;
    private final int DELETE_NUM = 2000;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff, String ID, String Content);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public ReviewAdapter(ArrayList<ReviewData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ReviewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.CustomViewHolder holder, int position) {
//        holder.Review_image.setImageURI(Uri.parse(arrayList.get(position).getReview_image()));
//        holder.Review_id.setText(arrayList.get(position).getReview_id());
//        holder.Review_content.setText(arrayList.get(position).getReview_content());
//

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getReview_image())
                .into(holder.Review_image);
        holder.Review_id.setText(arrayList.get(position).getReview_id());
        holder.Review_content.setText(arrayList.get(position).getReview_content());
        holder.RatingBar.setRating(arrayList.get(position).getReview_rating());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView Review_image;
        protected TextView Review_id;
        protected TextView Review_content;
        protected TextView Revise_btn;
        protected TextView Delete_btn;
        protected RatingBar RatingBar;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Review_image = (ImageView) itemView.findViewById(R.id.review_image);
            this.Review_id = (TextView) itemView.findViewById(R.id.review_id);
            this.Review_content = (TextView) itemView.findViewById(R.id.review_content);
            this.Revise_btn = (TextView) itemView.findViewById(R.id.revise_button);
            this.Delete_btn = (TextView) itemView.findViewById(R.id.delete_button);
            this.RatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            Revise_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position,REVISE_NUM, Review_id.getText().toString(), arrayList.get(position).getReview_content());
                        }
                    }
                }
            });

            Delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position,DELETE_NUM, Review_id.getText().toString(), Review_content.getText().toString());
                        }
                    }
                }
            });
        }
    }
}
