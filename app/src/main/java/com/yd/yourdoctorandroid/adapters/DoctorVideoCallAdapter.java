package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DoctorVideoCallAdapter extends RecyclerView.Adapter<DoctorVideoCallAdapter.DoctorVideoCallViewHolder> {

    private List<Doctor> chosenDoctorList;
    private Context context;
    private onItemClickListner onItemClickListner;

    public DoctorVideoCallAdapter(List<Doctor> chosenDoctorList, Context context) {
        this.context = context;
        this.chosenDoctorList = chosenDoctorList;
    }

    public void swap(List list){
        if (chosenDoctorList != null) {
            chosenDoctorList.clear();
            chosenDoctorList.addAll(list);
        }
        else {
            chosenDoctorList = list;
        }
        notifyDataSetChanged();
    }

    public List<Doctor> getChosenDoctorList() {
        return chosenDoctorList;
    }

    public void setOnItemClickListner(DoctorVideoCallAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public DoctorVideoCallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_doctor_video_call, parent, false);
        return new DoctorVideoCallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorVideoCallViewHolder holder, int position) {
        holder.setData(chosenDoctorList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("VideoCallFragment", chosenDoctorList.size() + "");
        return chosenDoctorList.size();
    }


    public class DoctorVideoCallViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private ImageView ivOnlineStatus;
        private ImageView ivCall;
        private TextView tvName;
        private RatingBar rbRating;
        private Doctor doctorModel;

        public DoctorVideoCallViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivOnlineStatus = (ImageView) itemView.findViewById(R.id.iv_status);
            ivCall = (ImageView) itemView.findViewById(R.id.iv_call);
            rbRating = (RatingBar) itemView.findViewById(R.id.rating_bar);
            tvName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
        }

        public void setData(Doctor doctorModel) {
            this.doctorModel = doctorModel;
            if (doctorModel != null) {
                ZoomImageViewUtils.loadCircleImage(context,doctorModel.getAvatar(),ivAvatar);
                ivCall.setImageResource(R.drawable.ic_video_call_black_24dp);
                tvName.setText(doctorModel.getFullName());
                rbRating.setRating(doctorModel.getCurrentRating());
                Log.d("VideoCallFragment", doctorModel.isOnline() + "");
                if(doctorModel.isOnline()) {
                    ivOnlineStatus.setImageResource(R.drawable.circle_green_line);
                } else {
                    ivOnlineStatus.setImageResource(R.drawable.circle_red_line);
                }

                ivCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListner.onCall(doctorModel.getDoctorId(), doctorModel.getFullName(), doctorModel.getAvatar());
                    }
                });
            }
        }

    }

    public interface onItemClickListner{
        void onCall(String calleeId, String calleename, String calleeAvatar);
    }
}
