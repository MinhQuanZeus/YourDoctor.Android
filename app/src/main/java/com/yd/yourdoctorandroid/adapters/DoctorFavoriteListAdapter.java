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
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.FavoriteDoctor;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DoctorFavoriteListAdapter extends RecyclerView.Adapter<DoctorFavoriteListAdapter.DoctorFavoriteListViewHolder> {
    private List<FavoriteDoctor> favoriteDoctors;
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
        this.favoriteDoctors = new ArrayList<>();
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

                holder.setData(favoriteDoctors.get(position), position);

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
        return (position == favoriteDoctors.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(FavoriteDoctor mc) {
        favoriteDoctors.add(mc);
        notifyItemInserted(favoriteDoctors.size() - 1);
    }

    public void addAll(List<FavoriteDoctor> mcList) {
        for (FavoriteDoctor mc : mcList) {
            add(mc);
        }
    }

    public void remove(FavoriteDoctor favoriteDoctor) {
        int position = favoriteDoctors.indexOf(favoriteDoctor);
        if (position > -1) {
            favoriteDoctors.remove(position);
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
        add(new FavoriteDoctor());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = favoriteDoctors.size() - 1;
        FavoriteDoctor item = getItem(position);

        if (item != null) {
            favoriteDoctors.remove(position);
            notifyItemRemoved(position);
        }
    }

    public FavoriteDoctor getItem(int position) {
        return favoriteDoctors.get(position);
    }

    @Override
    public int getItemCount() {
        return favoriteDoctors == null ? 0 : favoriteDoctors.size();
    }

    public class DoctorFavoriteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvNumberRank;
        TextView tvNameDoctorFavorite;
        ImageView ivItemDoctorFavorite;
        RatingBar rbDoctorFavorite;
        TextView tvNameDoctorSpecialist;
        private ItemClickListener itemClickListener;
        private FavoriteDoctor favoriteDoctor;

        public DoctorFavoriteListViewHolder(View itemView) {
            super(itemView);
            tvNumberRank = itemView.findViewById(R.id.tvNumberRank);
            tvNameDoctorFavorite = itemView.findViewById(R.id.tvNameDoctorFavorite);
            ivItemDoctorFavorite = itemView.findViewById(R.id.ivItemDoctorFavorite);
            rbDoctorFavorite = itemView.findViewById(R.id.rbDoctorFavorite);
            tvNameDoctorSpecialist = itemView.findViewById(R.id.tvNameDoctorSpecialist);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(FavoriteDoctor favoriteDoctor, int positon) {

            this.favoriteDoctor = favoriteDoctor;

            if (favoriteDoctor != null) {
                ZoomImageViewUtils.loadCircleImage(context,favoriteDoctor.getAvatar(),ivItemDoctorFavorite);
                tvNameDoctorFavorite.setText(favoriteDoctor.getFullName());
                rbDoctorFavorite.setRating(favoriteDoctor.getCurrentRating());
                tvNumberRank.setText((positon + 1) + "");
                //TODO
                tvNameDoctorSpecialist.setText(favoriteDoctor.getSpecialist());
            }
        }


        public FavoriteDoctor getdoctorModel() {
            return favoriteDoctor;
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


