package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Doctor;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DoctorChoiceAdapter extends RecyclerView.Adapter<DoctorChoiceAdapter.DoctorChoiceViewHolder> {
    private List<Doctor> chosenDoctorList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public DoctorChoiceAdapter(List<Doctor> chosenDoctorList, Context context) {

        //this.chosenDoctorList = chosenDoctorList;
        this.context = context;

        //for test
        chosenDoctorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doctor doctor = new Doctor();
            doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
            doctor.setFirst_name("Le");
            doctor.setLast_name("Anh");
            doctor.setCurrent_rating((float) 3.3);
            chosenDoctorList.add(doctor);
        }


        this.chosenDoctorList = chosenDoctorList;

    }

    @NonNull
    @Override
    public DoctorChoiceAdapter.DoctorChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_doctor_choose, parent, false);
        view.setOnClickListener(onClickListener);
        return new DoctorChoiceAdapter.DoctorChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorChoiceAdapter.DoctorChoiceViewHolder holder, int position) {
        holder.setData(chosenDoctorList.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                // EventBus.getDefault().post(new OnClickTopSong(holder.getTopSongModel()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return chosenDoctorList.size();
    }

    public class DoctorChoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView iv_item_doctor_chosen;
        TextView tv_name_doctor_chosen;
        RatingBar rb_doctorChosen;
        private ItemClickListener itemClickListener;
        private Doctor doctorModel;

        public DoctorChoiceViewHolder(View itemView) {
            super(itemView);
            iv_item_doctor_chosen = itemView.findViewById(R.id.iv_item_doctor_chosen);
            tv_name_doctor_chosen = itemView.findViewById(R.id.tv_name_doctor_chosen);
            rb_doctorChosen = itemView.findViewById(R.id.rb_doctorChosen);
            //this.view = itemView;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Doctor doctorModel) {
            //  imageView.setBackgroundResource(musicTypeModel.getIdImage());
            this.doctorModel = doctorModel;
            if (doctorModel != null) {
                Picasso.with(context).load(doctorModel.getAvatar()).transform(new CropCircleTransformation()).into(iv_item_doctor_chosen);
                tv_name_doctor_chosen.setText(doctorModel.getFirst_name() + " " + doctorModel.getLast_name());
                rb_doctorChosen.setRating(doctorModel.getCurrent_rating());
                // view.setTag(musicTypeModel);
            }
        }


        public Doctor getdoctorModel() {
            return doctorModel;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}
