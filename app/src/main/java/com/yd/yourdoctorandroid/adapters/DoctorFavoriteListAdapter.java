package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
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
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Doctor;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DoctorFavoriteListAdapter extends RecyclerView.Adapter<DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder> {
    private List<Doctor> favoriteDoctorList;
    private Context context;
    private View.OnClickListener onClickListener;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public DoctorFavoriteListAdapter(Context context) {

        this.context = context;
        this.favoriteDoctorList = new ArrayList<>();
    }

    public List<Doctor> getDoctors() {
        return favoriteDoctorList;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.favoriteDoctorList = doctors;
    }


    @NonNull
    @Override
    public DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_process, parent, false);
                // v2.setOnClickListener(onClickListener);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

    }

    @NonNull
    private DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_doctor_favorite, parent, false);
        viewHolder = new DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:

                holder.setData(favoriteDoctorList.get(position), position);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                        doctorProfileFragment.setDoctorID(holder.getdoctorModel().getDoctorId());
                        ScreenManager.openFragment(((FragmentActivity) context).getSupportFragmentManager(), doctorProfileFragment, R.id.rl_container, true, true);
                    }
                });
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == favoriteDoctorList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Doctor mc) {
        favoriteDoctorList.add(mc);
        notifyItemInserted(favoriteDoctorList.size() - 1);
    }

    public void addAll(List<Doctor> mcList) {
        for (Doctor mc : mcList) {
            add(mc);
        }
    }

    public void remove(Doctor city) {
        int position = favoriteDoctorList.indexOf(city);
        if (position > -1) {
            favoriteDoctorList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Doctor());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = favoriteDoctorList.size() - 1;
        Doctor item = getItem(position);

        if (item != null) {
            favoriteDoctorList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Doctor getItem(int position) {
        return favoriteDoctorList.get(position);
    }

    @Override
    public int getItemCount() {
        return favoriteDoctorList == null ? 0 : favoriteDoctorList.size();
    }

    public class DoctorFavoriteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_number_rank;
        TextView tv_name_doctor_favorite;
        ImageView iv_item_doctor_favorite;
        RatingBar rb_doctor_favorite;
        TextView tv_name_doctor_specialist;
        private ItemClickListener itemClickListener;
        private Doctor doctorModel;

        public DoctorFavoriteListViewHolder(View itemView) {
            super(itemView);
            tv_number_rank = itemView.findViewById(R.id.tv_number_rank);
            tv_name_doctor_favorite = itemView.findViewById(R.id.tv_name_doctor_favorite);
            iv_item_doctor_favorite = itemView.findViewById(R.id.iv_item_doctor_favorite);
            rb_doctor_favorite = itemView.findViewById(R.id.rb_doctor_favorite);
            tv_name_doctor_specialist = itemView.findViewById(R.id.tv_name_doctor_specialist);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Doctor doctorModel, int positon) {

            this.doctorModel = doctorModel;

            if (doctorModel != null) {
                if (context == null) Log.d("Anhle", "context bi null");

                Picasso.with(context).load(doctorModel.getAvatar()).transform(new CropCircleTransformation()).into(iv_item_doctor_favorite);
                tv_name_doctor_favorite.setText(doctorModel.getFirstName() + " " + doctorModel.getMiddleName() + " " + doctorModel.getLastName());
                rb_doctor_favorite.setRating(doctorModel.getCurrentRating());
                tv_number_rank.setText((positon + 1) + "");
                tv_name_doctor_specialist.setText("Khoa răng hàm mặt");

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

    protected class LoadingVH extends DoctorFavoriteListViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}


