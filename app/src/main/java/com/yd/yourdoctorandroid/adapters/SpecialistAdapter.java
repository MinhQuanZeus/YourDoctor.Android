package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.fragments.SpecilistInfoDetailFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.List;

public class SpecialistAdapter extends RecyclerView.Adapter<SpecialistAdapter.SpecialistViewHolder> {
    private List<Specialist> specialists;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public SpecialistAdapter(List<Specialist> specialists, Context context) {

        this.context = context;
        this.specialists = specialists;

    }

    @NonNull
    @Override
    public SpecialistAdapter.SpecialistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_specialist, parent, false);
        view.setOnClickListener(onClickListener);
        return new SpecialistAdapter.SpecialistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpecialistAdapter.SpecialistViewHolder holder, int position) {
        holder.setData(specialists.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                SpecilistInfoDetailFragment specilistInfoDetailFragment = new SpecilistInfoDetailFragment();
                specilistInfoDetailFragment.setData(holder.getSpecialist().getId());
                ScreenManager.openFragment(((FragmentActivity) context).getSupportFragmentManager(), specilistInfoDetailFragment, R.id.rlContainer, true, true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return specialists.size();
    }

    public class SpecialistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivSpecialist;
        TextView tvNameSpecialist;
        private ItemClickListener itemClickListener;
        private Specialist specialist;

        public SpecialistViewHolder(View itemView) {
            super(itemView);
            ivSpecialist = itemView.findViewById(R.id.iv_specialist);
            tvNameSpecialist = itemView.findViewById(R.id.tv_name_specialist);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public Specialist getSpecialist() {
            return specialist;
        }

        public void setData(Specialist specialist) {

            this.specialist = specialist;
            if (specialist != null) {
                ZoomImageViewUtils.loadImageManual(context, specialist.getImage(), ivSpecialist);
                tvNameSpecialist.setText(specialist.getName());
            }
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
