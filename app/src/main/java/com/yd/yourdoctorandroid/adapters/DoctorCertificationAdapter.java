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
import com.yd.yourdoctorandroid.networks.models.Certification;

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

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.certification_dialog);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                PhotoView certification_photo_view = dialog.findViewById(R.id.certification_photo_view);
                Button btn_cancel_from_photo_view = dialog.findViewById(R.id.btn_cancel_from_photo_view);

                Picasso.with(context).load(certificationChoice.getPathImage()).into(certification_photo_view);

                btn_cancel_from_photo_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return chosenCertificationList.size();
    }

    public class DoctorCertificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView iv_certification_profile;
        TextView tv_certification_profile;
        private ItemClickListener itemClickListener;
        private Certification certificationModel;

        public DoctorCertificationViewHolder(View itemView) {
            super(itemView);
            iv_certification_profile = itemView.findViewById(R.id.iv_certification_profile);
            tv_certification_profile = itemView.findViewById(R.id.tv_certification_profile);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Certification certificationModel) {

            this.certificationModel = certificationModel;
            if (certificationModel != null) {
                Picasso.with(context).load(certificationModel.getPathImage()).into(iv_certification_profile);
                tv_certification_profile.setText(certificationModel.getName());

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


