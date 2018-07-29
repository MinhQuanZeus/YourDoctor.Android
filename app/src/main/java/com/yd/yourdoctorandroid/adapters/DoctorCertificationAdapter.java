package com.yd.yourdoctorandroid.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.List;

public class DoctorCertificationAdapter extends RecyclerView.Adapter<DoctorCertificationAdapter.DoctorCertificationViewHolder> {
    private List<Certification> chosenCertificationList;
    private Context context;
    private View.OnClickListener onClickListener;
    Certification certificationChoice;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public DoctorCertificationAdapter(List<Certification> chosenCertificationList, Context context) {

        this.context = context;
        this.chosenCertificationList = chosenCertificationList;

    }

    public Certification getCertificationChoice() {
        return certificationChoice;
    }

    @NonNull
    @Override
    public DoctorCertificationAdapter.DoctorCertificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_certification, parent, false);
        view.setOnClickListener(onClickListener);
        return new DoctorCertificationAdapter.DoctorCertificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorCertificationAdapter.DoctorCertificationViewHolder holder, int position) {
        holder.setData(chosenCertificationList.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                certificationChoice = holder.getcertificationModel();

                ZoomImageViewUtils.zoomImage(context,certificationChoice.getPathImage());

            }
        });
    }

    @Override
    public int getItemCount() {
        return chosenCertificationList.size();
    }

    public class DoctorCertificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivCertificationProfile;
        TextView tvCertificationProfile;
        private ItemClickListener itemClickListener;
        private Certification certificationModel;

        public DoctorCertificationViewHolder(View itemView) {
            super(itemView);
            ivCertificationProfile = itemView.findViewById(R.id.ivCertificationProfile);
            tvCertificationProfile = itemView.findViewById(R.id.tvCertificationProfile);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Certification certificationModel) {

            this.certificationModel = certificationModel;
            if (certificationModel != null) {
                ZoomImageViewUtils.loadImageManual(context,certificationModel.getPathImage(),ivCertificationProfile);
                tvCertificationProfile.setText(certificationModel.getName());

            }
        }


        public Certification getcertificationModel() {
            return certificationModel;
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


