package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Notification;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

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
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

    }

    @NonNull
    private NotificationAdapter.NotificationAdapterViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        NotificationAdapter.NotificationAdapterViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_notification, parent, false);
        viewHolder = new NotificationAdapter.NotificationAdapterViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.NotificationAdapterViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:

                holder.setData(notifications.get(position));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        switch (holder.getNotification().getType()){
                            case 1 : {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("chatHistoryId", holder.getNotification().getStorageId());
                                intent.putExtra("doctorChoiceId", holder.getNotification().getSenderId().getId());
                                context.startActivity(intent);
                                break;
                            }
                            case 2 :{
                                //TODO
                            }
                        }
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


    public void add(Notification notification) {
        notifications.add(notification);
        notifyItemInserted(notifications.size() - 1);
    }

    public void addAll(List<Notification> notifications) {
        for (Notification notification : notifications) {
            add(notification);
        }
    }

    public void remove(Notification notification) {
        int position = notifications.indexOf(notification);
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
        ImageView ivNotification;
        TextView tvTitleNotification;
        TextView tvContentNotification;
        TextView tvTime;
        private ItemClickListener itemClickListener;
        private Notification notification;

        public NotificationAdapterViewHolder(View itemView) {
            super(itemView);
            ivNotification = itemView.findViewById(R.id.iv_notification);
            tvTitleNotification = itemView.findViewById(R.id.tv_title_notification);
            tvContentNotification = itemView.findViewById(R.id.tv_content_notification);
            tvTime = itemView.findViewById(R.id.tv_time);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(Notification notification) {

            this.notification = notification;

            if (notification != null) {
                if (context != null){
                    ZoomImageViewUtils.loadCircleImage(context,notification.getSenderId().getAvatar(),ivNotification);
                    //ivNotification.setImageDrawable(context.getResources().getDrawable(R.drawable.your_doctor_logo));
                    tvContentNotification.setText(notification.getMessage());
                    tvTime.setText(Utils.convertTime(notification.getCreatedAt()));
                    switch (notification.getType()){
                        case 1:{
                            tvTitleNotification.setText("Thông báo chat từ BS." + notification.getNameSender() );
                            break;
                        }
                        case 2:{
                            tvTitleNotification.setText("Thông báo video call từ BS." + notification.getNameSender() );
                            break;
                        }
                        case 3:{
                            tvTitleNotification.setText("Thông báo thanh toán từ BS." + notification.getNameSender() );
                            break;
                        }
                        case 5:{
                            tvTitleNotification.setText("Thông báo báo cáo người dùng");
                            break;
                        }
                    }

                }

            }
        }


        public Notification getNotification() {
            return notification;
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
