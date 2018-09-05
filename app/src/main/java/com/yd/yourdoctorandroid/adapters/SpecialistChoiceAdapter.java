package com.yd.yourdoctorandroid.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.List;

public class SpecialistChoiceAdapter extends RecyclerView.Adapter<SpecialistChoiceAdapter.SpecialistChoiceViewHolder> {
    private List<Specialist> chosenSpecialist;
    private Context context;
    private View.OnClickListener onClickListener;
    Dialog dialog;
    Specialist specialistChoice;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public SpecialistChoiceAdapter(List<Specialist> chosenSpecialist, Context context, Dialog dialog) {

        this.context = context;
        this.chosenSpecialist = chosenSpecialist;
        this.dialog = dialog;

    }

    public Specialist getSpecialistChoice() {
        return specialistChoice;
    }

    @NonNull
    @Override
    public SpecialistChoiceAdapter.SpecialistChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_specialist, parent, false);
        view.setOnClickListener(onClickListener);
        return new SpecialistChoiceAdapter.SpecialistChoiceViewHolder(view);
    }

    View previousView;

    @Override
    public void onBindViewHolder(@NonNull final SpecialistChoiceAdapter.SpecialistChoiceViewHolder holder, int position) {
        holder.setData(chosenSpecialist.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                if (previousView != null) {
                    previousView.setBackground(ContextCompat.getDrawable(context, R.color.primary_text));
                }
                previousView = view;

                view.setBackground(ContextCompat.getDrawable(context, R.color.colorPrimary));
                specialistChoice = holder.getSpecialistModel();

            }
        });
    }

    @Override
    public int getItemCount() {
        return chosenSpecialist.size();
    }

    public class SpecialistChoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivSpecialist;
        TextView tvNameSpecialist;
        private ItemClickListener itemClickListener;
        private Specialist specialistModel;

        public SpecialistChoiceViewHolder(View itemView) {
            super(itemView);
            ivSpecialist = itemView.findViewById(R.id.iv_specialist);
            tvNameSpecialist = itemView.findViewById(R.id.tv_name_specialist);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Specialist specialist) {
            this.specialistModel = specialist;
            if (specialistModel != null) {
                ZoomImageViewUtils.loadCircleImage(context,specialistModel.getImage(),ivSpecialist);
                tvNameSpecialist.setText(specialist.getName());
            }
        }

        public Specialist getSpecialistModel() {
            return specialistModel;
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