package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.models.Doctor;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DoctorVideoCallAdapter {

    private Context context;


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
            tvName = (TextView) itemView.findViewById(R.id.tv_name_doctor);
        }

        public void setData(Doctor doctorModel) {
            this.doctorModel = doctorModel;
            if (doctorModel != null) {
                Picasso.with(context).load(doctorModel.getAvatar()).transform(new CropCircleTransformation()).into(ivAvatar);
                ivCall.setImageResource(R.drawable.ic_video_call_black_24dp);
                tvName.setText(doctorModel.getFirst_name() + " " + doctorModel.getLast_name());
                rbRating.setRating(doctorModel.getCurrent_rating());
                // view.setTag(musicTypeModel);
            }
        }

    }
}
