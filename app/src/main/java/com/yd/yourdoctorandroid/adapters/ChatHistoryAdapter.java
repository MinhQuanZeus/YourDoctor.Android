package com.yd.yourdoctorandroid.adapters;


import android.content.Context;
import android.content.Intent;
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
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.getAllChatHistory.ChatHistoryInfoResponse;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;


public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryViewHolder> {
    private List<ChatHistoryInfoResponse> chatHistoryInfoResponses;
    Patient currentPatient;
    private Context context;
    private View.OnClickListener onClickListener;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public ChatHistoryAdapter(Context context) {

        this.context = context;
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        this.chatHistoryInfoResponses = new ArrayList<>();
    }


    @NonNull
    @Override
    public ChatHistoryAdapter.ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChatHistoryAdapter.ChatHistoryViewHolder viewHolder = null;
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
    private ChatHistoryAdapter.ChatHistoryViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        ChatHistoryAdapter.ChatHistoryViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_chat_history, parent, false);
        viewHolder = new ChatHistoryAdapter.ChatHistoryViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatHistoryAdapter.ChatHistoryViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:
                //DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder movieVH = (DoctorRankingSpecialistAdapter.DoctorRankingSpecialistViewHolder) holder;

                holder.setData(chatHistoryInfoResponses.get(position));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("chatHistoryId", holder.getChatHistoryInfoResponse().get_id());
                        intent.putExtra("doctorChoiceId", holder.getChatHistoryInfoResponse().getDoctorId().get_id());
                        context.startActivity(intent);

                    }
                });
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == chatHistoryInfoResponses.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(ChatHistoryInfoResponse mc) {
        chatHistoryInfoResponses.add(mc);
        notifyItemInserted(chatHistoryInfoResponses.size() - 1);
    }

    public void addAll(List<ChatHistoryInfoResponse> chatHistoryResponses) {
        for (ChatHistoryInfoResponse chatHistoryResponse : chatHistoryResponses) {
            add(chatHistoryResponse);
        }
    }

    public void remove(ChatHistoryInfoResponse chatHistoryResponse) {
        int position = chatHistoryInfoResponses.indexOf(chatHistoryResponse);
        if (position > -1) {
            chatHistoryInfoResponses.remove(position);
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
        add(new ChatHistoryInfoResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = chatHistoryInfoResponses.size() - 1;
        ChatHistoryInfoResponse item = getItem(position);

        if (item != null) {
            chatHistoryInfoResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ChatHistoryInfoResponse getItem(int position) {
        return chatHistoryInfoResponses.get(position);
    }

    @Override
    public int getItemCount() {
        return chatHistoryInfoResponses == null ? 0 : chatHistoryInfoResponses.size();
    }

    public class ChatHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivChatHistory;
        TextView tvTitleNotification;
        TextView tvTimeChatHistory;
        TextView tvStatusChatHistory;
        TextView tvContentHistory;
        private ItemClickListener itemClickListener;
        private ChatHistoryInfoResponse chatHistoryInfoResponse;

        public ChatHistoryViewHolder(View itemView) {
            super(itemView);
            ivChatHistory = itemView.findViewById(R.id.iv_chat_history);
            tvTitleNotification = itemView.findViewById(R.id.tv_title_notification);
            tvTimeChatHistory = itemView.findViewById(R.id.tv_time_chat_history);
            tvStatusChatHistory = itemView.findViewById(R.id.tv_status_chat_history);
            tvContentHistory = itemView.findViewById(R.id.tv_content_history);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(ChatHistoryInfoResponse chatHistoryInfoResponse) {

            this.chatHistoryInfoResponse = chatHistoryInfoResponse;

            if (chatHistoryInfoResponse != null) {
                if (context == null) Log.d("Anhle", "context bi null");
                ZoomImageViewUtils.loadCircleImage(context, chatHistoryInfoResponse.getDoctorId().getAvatar() ,ivChatHistory);
                tvTitleNotification.setText("Cuộc chat với BS." + chatHistoryInfoResponse.getDoctorId().getFullName());
                tvTimeChatHistory.setText("Thời gian tạo: " + Utils.convertTime(chatHistoryInfoResponse.getCreatedAt())
                        +", Tin nhắn cuối cùng: " + Utils.convertTime(chatHistoryInfoResponse.getUpdatedAt())
                );
                tvContentHistory.setText("Nội dung: " +chatHistoryInfoResponse.getContentTopic());
                if(chatHistoryInfoResponse.getStatus() == 1){
                    tvStatusChatHistory.setText("Trạng thái: Chưa Hoàn thành");

                }else {
                    tvStatusChatHistory.setText("Trạng thái: Hoàn thành");
                }

            }
        }


        public ChatHistoryInfoResponse getChatHistoryInfoResponse() {
            return chatHistoryInfoResponse;
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

    protected class LoadingVH extends ChatHistoryViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}

