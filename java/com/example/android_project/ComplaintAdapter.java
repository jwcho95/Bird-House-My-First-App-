package com.example.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.CustomViewHolder> {

    private ArrayList<ComplaintData> arrayList;
    private Context context;
    private final int REVISE_NUM = 1001;
    private final int DELETE_NUM = 2001;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff, String ID, String Content, String Title);
    }

    // 리스너 객체 참조를 저장하는 변수
    private ComplaintAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public ComplaintAdapter(ArrayList<ComplaintData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ComplaintAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getComplaint_image())
                .into(holder.complaintImage);
        holder.complaintId.setText(arrayList.get(position).getComplaint_id());
        holder.complaintTitle.setText(arrayList.get(position).getComplaint_title());
        holder.complaintContent.setText(arrayList.get(position).getComplaint_content());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView complaintImage;
        protected TextView complaintId;
        protected TextView complaintTitle;
        protected TextView complaintContent;
        protected TextView reviseBtn;
        protected TextView deleteBtn;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.complaintImage = (ImageView) itemView.findViewById(R.id.complaint_image);
            this.complaintId = (TextView) itemView.findViewById(R.id.complaint_id);
            this.complaintTitle = (TextView) itemView.findViewById(R.id.complaint_title);
            this.complaintContent = (TextView) itemView.findViewById(R.id.complaint_content);
            this.reviseBtn = (TextView) itemView.findViewById(R.id.reviseButton);
            this.deleteBtn = (TextView) itemView.findViewById(R.id.deleteButton);

            reviseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position,REVISE_NUM, complaintId.getText().toString(), arrayList.get(position).getComplaint_content(), arrayList.get(position).getComplaint_title());
                        }
                    }
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position,DELETE_NUM, complaintId.getText().toString(), complaintContent.getText().toString(),complaintTitle.getText().toString());
                        }
                    }
                }
            });
        }
    }
}
