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
import com.yd.yourdoctorandroid.models.Notification;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {
    private List<Notification> notifications;
    private Context context;
    private View.OnClickListener onClickListener;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public NotificationAdapter(Context context) {

        this.context = context;
        this.notifications = new ArrayList<>();
    }

    public List<Notification> getDoctors() {
        return notifications;
    }

    public void setDoctors(List<Notification> doctors) {
        this.notifications = doctors;
    }


    @NonNull
    @Override
    public NotificationAdapter.NotificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationAdapter.NotificationAdapterViewHolder viewHolder = null;
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
    private NotificationAdapter.NotificationAdapterViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        NotificationAdapter.NotificationAdapterViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_doctor_favorite, parent, false);
        viewHolder = new NotificationAdapter.NotificationAdapterViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationAdapterViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:

                holder.setData(notifications.get(position), position);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       // DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                       // doctorProfileFragment.setDoctorID(holder.getdoctorModel().getDoctorId());
                       // ScreenManager.openFragment(((FragmentActivity) context).getSupportFragmentManager(), doctorProfileFragment, R.id.rlContainer, true, true);
                    }
                });
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == notifications.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(Notification mc) {
        notifications.add(mc);
        notifyItemInserted(notifications.size() - 1);
    }

    public void addAll(List<Notification> mcList) {
        for (Notification mc : mcList) {
            add(mc);
        }
    }

    public void remove(Notification city) {
        int position = notifications.indexOf(city);
        if (position > -1) {
            notifications.remove(position);
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
        add(new Notification());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = notifications.size() - 1;
        Notification item = getItem(position);

        if (item != null) {
            notifications.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public int getItemCount() {
        return notifications == null ? 0 : notifications.size();
    }

    public class NotificationAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvNumberRank;
        TextView tvNameDoctorFavorite;
        ImageView ivItemDoctorFavorite;
        RatingBar rbDoctorFavorite;
        TextView tvNameDoctorSpecialist;
        private ItemClickListener itemClickListener;
        private Notification doctorModel;

        public NotificationAdapterViewHolder(View itemView) {
            super(itemView);
            tvNumberRank = itemView.findViewById(R.id.tvNumberRank);
            tvNameDoctorFavorite = itemView.findViewById(R.id.tvNameDoctorFavorite);
            ivItemDoctorFavorite = itemView.findViewById(R.id.ivItemDoctorFavorite);
            rbDoctorFavorite = itemView.findViewById(R.id.rbDoctorFavorite);
            tvNameDoctorSpecialist = itemView.findViewById(R.id.tvNameDoctorSpecialist);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Notification doctorModel, int positon) {

            this.doctorModel = doctorModel;

            if (doctorModel != null) {
                if (context == null) Log.d("Anhle", "context bi null");
//                ZoomImageViewUtils.loadCircleImage(context,doctorModel.getAvatar(),ivItemDoctorFavorite);
//                tvNameDoctorFavorite.setText(doctorModel.getFullName());
//                rbDoctorFavorite.setRating(doctorModel.getCurrentRating());
//                tvNumberRank.setText((positon + 1) + "");
                String specialistText = "";
                //TODO
//                ArrayList<Specialist> specialists = (ArrayList<Specialist>) LoadDefaultModel.getInstance().getSpecialists();
//                for(String idSpecialist : doctorModel.getIdSpecialist()){
//                    for(Specialist specialist : specialists){
//                        if(specialist.getId().equals(idSpecialist)){
//                            specialistText = specialistText + specialist.getName() + ", ";
//                        }
//                    }
//                }
                tvNameDoctorSpecialist.setText(specialistText);
            }
        }


        public Notification getdoctorModel() {
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

    protected class LoadingVH extends NotificationAdapterViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}
