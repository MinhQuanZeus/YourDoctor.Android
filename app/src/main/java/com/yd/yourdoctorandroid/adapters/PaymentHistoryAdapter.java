package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.ObjectPaymentResponse;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder> {
    private List<ObjectPaymentResponse> objectPaymentResponses;
    private Context context;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public PaymentHistoryAdapter(Context context) {

        this.context = context;
        this.objectPaymentResponses = new ArrayList<>();
    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.PaymentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PaymentHistoryAdapter.PaymentHistoryViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_process, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

    }

    @NonNull
    private PaymentHistoryAdapter.PaymentHistoryViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        PaymentHistoryAdapter.PaymentHistoryViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_payment_history, parent, false);
        viewHolder = new PaymentHistoryAdapter.PaymentHistoryViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentHistoryAdapter.PaymentHistoryViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:
                holder.setData(objectPaymentResponses.get(position));
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == objectPaymentResponses.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(ObjectPaymentResponse objectPaymentResponse) {
        objectPaymentResponses.add(objectPaymentResponse);
        notifyItemInserted(objectPaymentResponses.size() - 1);
    }

    public void addAll(List<ObjectPaymentResponse> objectPaymentResponses) {
        for (ObjectPaymentResponse objectPaymentResponse : objectPaymentResponses) {
            add(objectPaymentResponse);
        }
    }

    public void remove(ObjectPaymentResponse objectPaymentResponse) {
        int position = objectPaymentResponses.indexOf(objectPaymentResponse);
        if (position > -1) {
            objectPaymentResponses.remove(position);
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
        add(new ObjectPaymentResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = objectPaymentResponses.size() - 1;
        ObjectPaymentResponse item = getItem(position);

        if (item != null) {
            objectPaymentResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ObjectPaymentResponse getItem(int position) {
        return objectPaymentResponses.get(position);
    }

    @Override
    public int getItemCount() {
        return objectPaymentResponses == null ? 0 : objectPaymentResponses.size();
    }

    public class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPaymentHistory;
        TextView tvTitlePayment;
        TextView tvContentPayment;
        TextView tvTimePayment;
        private ItemClickListener itemClickListener;
        private ObjectPaymentResponse objectPaymentResponse;

        public PaymentHistoryViewHolder(View itemView) {
            super(itemView);
            ivPaymentHistory = itemView.findViewById(R.id.iv_payment_history);
            tvTitlePayment = itemView.findViewById(R.id.tv_title_payment);
            tvContentPayment = itemView.findViewById(R.id.tv_content_payment);
            tvTimePayment = itemView.findViewById(R.id.tv_time_payment);


        }

        public void setData(ObjectPaymentResponse objectPaymentResponse) {

            this.objectPaymentResponse = objectPaymentResponse;

            if (objectPaymentResponse != null && context != null) {
                try {
                    ZoomImageViewUtils.loadCircleImage(context, objectPaymentResponse.getFromUser().getAvatar(), ivPaymentHistory);
                    //ivNotification.setImageDrawable(context.getResources().getDrawable(R.drawable.your_doctor_logo));
                    tvTitlePayment.setText("Tư vấn " + objectPaymentResponse.getTypeAdvisoryID().getName()
                            + " bởi BS." + objectPaymentResponse.getFromUser().getFullName());
                } catch (Exception e) {

                }
                tvContentPayment.setText("Phí thanh toán: " + Utils.formatStringNumber(objectPaymentResponse.getAmount()) + " VND\n"
                        + "Số dư hiện tại: " + Utils.formatStringNumber(objectPaymentResponse.getRemainMoney()) + " VND");
                tvTimePayment.setText("Thời gian: " + Utils.convertTime(objectPaymentResponse.getUpdatedAt()));


            }
        }


        public ObjectPaymentResponse getObjectPaymentResponse() {
            return objectPaymentResponse;
        }

    }

    protected class LoadingVH extends PaymentHistoryViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}

