package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Record;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.Date;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT_IMAGE = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED_IMAGE = 4;
    String currentUserID;

    private View.OnClickListener onClickListener;

    private Context mContext;
    private List<Record> mMessageList;

    public ChatAdapter(Context context, List<Record> messageList, String currentUserID) {
        this.mContext = context;
        this.mMessageList = messageList;
        this.currentUserID = currentUserID;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        String senderid = mMessageList.get(position).getRecorderID();
        int type = mMessageList.get(position).getType();

        if (senderid.equals(currentUserID) && type == 1) {

            return VIEW_TYPE_MESSAGE_SENT;

        } else if (senderid.equals(currentUserID) && type == 2) {

            return VIEW_TYPE_MESSAGE_SENT_IMAGE;

        } else if (!senderid.equals(currentUserID) && type == 1) {

            return VIEW_TYPE_MESSAGE_RECEIVED;

        } else {

            return VIEW_TYPE_MESSAGE_RECEIVED_IMAGE;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);

            return new SentMessageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);

            return new ReceivedMessageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_SENT_IMAGE) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sender_image, parent, false);

            return new SentMessageImageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED_IMAGE) {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_receiver_image, parent, false);

            return new ReceivedMessageImageHolder(view);
        }
        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Record message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:

                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:

                ((ReceivedMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_SENT_IMAGE:

                ((SentMessageImageHolder) holder).bind(message);
                ((SentMessageImageHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        ZoomImageViewUtils.zoomImage(mContext, message.getValue());
                    }
                });
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED_IMAGE:

                ((ReceivedMessageImageHolder) holder).bind(message);
                ((ReceivedMessageImageHolder) holder).setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        ZoomImageViewUtils.zoomImage(mContext, message.getValue());
                    }
                });
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessageBody);
            timeText = itemView.findViewById(R.id.textMessageTime);
        }

        void bind(Record message) {
            messageText.setText(message.getValue());
            timeText.setText(message.getCreatedAt());
        }
    }

    private class SentMessageImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivMessageBody;
        TextView timeText;
        private ItemClickListener itemClickListener;

        SentMessageImageHolder(View itemView) {
            super(itemView);

            ivMessageBody = itemView.findViewById(R.id.ivMessageBody);
            timeText = itemView.findViewById(R.id.textMessageTime);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(Record message) {
            ZoomImageViewUtils.loadImageManual(mContext, message.getValue(), ivMessageBody);
            timeText.setText(message.getCreatedAt());
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

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessageBody);
            timeText = itemView.findViewById(R.id.textMessageTime);
            profileImage = itemView.findViewById(R.id.imageMessageProfile);


        }

        void bind(Record message) {
            messageText.setText(message.getValue());
            timeText.setText(message.getCreatedAt());
            ZoomImageViewUtils.loadCircleImage(mContext, message.getAvatar(), profileImage);
        }
    }

    private class ReceivedMessageImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivMessageBody;
        TextView timeText;
        ImageView profileImage;
        private ItemClickListener itemClickListener;

        ReceivedMessageImageHolder(View itemView) {
            super(itemView);

            ivMessageBody = itemView.findViewById(R.id.ivMessageBody);

            timeText = itemView.findViewById(R.id.textMessageTime);
            profileImage = itemView.findViewById(R.id.imageMessageProfile);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(Record message) {
            ZoomImageViewUtils.loadImageManual(mContext, message.getValue(), ivMessageBody);
            timeText.setText(message.getCreatedAt());
            ZoomImageViewUtils.loadCircleImage(mContext,message.getAvatar(),profileImage);

            // Insert the profile image from the URL into the ImageView.

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