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

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;


public class DoctorRankingSpecialistAdapter extends RecyclerView.Adapter<DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder> {
    private List<Doctor> rankingDoctorList;
    private Context context;
    private View.OnClickListener onClickListener;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public DoctorRankingSpecialistAdapter(Context context) {

        this.context = context;
        this.rankingDoctorList = new ArrayList<>();
    }

    public List<Doctor> getDoctors() {
        return rankingDoctorList;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.rankingDoctorList = doctors;
    }


    @NonNull
    @Override
    public DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder viewHolder = null;
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
    private DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_doctor_ranking, parent, false);
        viewHolder = new DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:
                //DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder movieVH = (DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder) holder;

                holder.setData(rankingDoctorList.get(position), position);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                        doctorProfileFragment.setDoctorID(holder.getdoctorModel().getDoctorId());
                        ScreenManager.openFragment(((FragmentActivity)context).getSupportFragmentManager(), doctorProfileFragment, R.id.rl_container, true, true);
                    }
                });
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == rankingDoctorList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Doctor mc) {
        rankingDoctorList.add(mc);
        notifyItemInserted(rankingDoctorList.size() - 1);
    }

    public void addAll(List<Doctor> mcList) {
        for (Doctor mc : mcList) {
            add(mc);
        }
    }

    public void remove(Doctor city) {
        int position = rankingDoctorList.indexOf(city);
        if (position > -1) {
            rankingDoctorList.remove(position);
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

        int position = rankingDoctorList.size() - 1;
        Doctor item = getItem(position);

        if (item != null) {
            rankingDoctorList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Doctor getItem(int position) {
        return rankingDoctorList.get(position);
    }

    @Override
    public int getItemCount() {
        return rankingDoctorList == null ? 0 : rankingDoctorList.size();
    }

    public class DoctorRankingSpecialistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvNumberRank;
        TextView tvNameDoctorRanking;
        ImageView ivItemDoctorRanking;
        RatingBar rbDoctorRanking;

        private ItemClickListener itemClickListener;
        private Doctor doctorModel;

        public DoctorRankingSpecialistViewHolder(View itemView) {
            super(itemView);
            tvNumberRank = itemView.findViewById(R.id.tvNumberRank);
            tvNameDoctorRanking = itemView.findViewById(R.id.tvNameDoctorRanking);
            ivItemDoctorRanking = itemView.findViewById(R.id.ivItemDoctorRanking);
            rbDoctorRanking = itemView.findViewById(R.id.rbDoctorRanking);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Doctor doctorModel, int positon) {

            this.doctorModel = doctorModel;

            if (doctorModel != null) {
                if (context == null) Log.d("Anhle", "context bi null");
                ZoomImageViewUtils.loadCircleImage(context,doctorModel.getAvatar(),ivItemDoctorRanking);
                tvNameDoctorRanking.setText(doctorModel.getFullName());
                rbDoctorRanking.setRating(doctorModel.getCurrentRating());
                tvNumberRank.setText((positon + 1) + "");

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

    protected class LoadingVH extends DoctorRankingSpecialistViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}


