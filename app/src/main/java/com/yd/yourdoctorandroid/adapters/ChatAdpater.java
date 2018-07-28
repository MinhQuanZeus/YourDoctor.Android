package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.networks.models.Record;

import java.util.Date;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ChatAdpater extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT_IMAGE = 3;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED_IMAGE = 4;
    String currentUserID;

    private Context mContext;
    private List<Record> mMessageList;

    public ChatAdpater(Context context, List<Record> messageList, String currentUserID) {
        this.mContext = context;
        this.mMessageList = messageList;
        this.currentUserID = currentUserID;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        String senderid = mMessageList.get(position).getRecorderID();
        int type = mMessageList.get(position).getType();
        if (senderid.equals(currentUserID) && type == 1) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else if (senderid.equals(currentUserID) && type == 2) {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_SENT_IMAGE;
        } else if (!senderid.equals(currentUserID) && type == 1) {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED_IMAGE;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
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
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED_IMAGE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_receiver_image, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Record message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Record message) {
            messageText.setText(message.getValue());
            Date date = new Date();
            timeText.setText(message.getCreatedAt());
        }
    }

    private class SentMessageImageHolder extends RecyclerView.ViewHolder {
        ImageView iv_message_body;
        TextView timeText;

        SentMessageImageHolder(View itemView) {
            super(itemView);

            iv_message_body = (ImageView) itemView.findViewById(R.id.iv_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Record message) {
            Picasso.with(mContext).load(message.getValue()).into(iv_message_body);
            timeText.setText(message.getCreatedAt());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);

            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Record message) {
            messageText.setText(message.getValue());
            Date date = new Date();
            timeText.setText(message.getCreatedAt());
            Picasso.with(mContext).load(message.getAvatar()).transform(new CropCircleTransformation()).into(profileImage);

            // Insert the profile image from the URL into the ImageView.

        }
    }

    private class ReceivedMessageImageHolder extends RecyclerView.ViewHolder {
        ImageView iv_message_body;
        TextView timeText;
        ImageView profileImage;

        ReceivedMessageImageHolder(View itemView) {
            super(itemView);

            iv_message_body = itemView.findViewById(R.id.iv_message_body);

            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Record message) {
            Picasso.with(mContext).load(message.getValue()).into(iv_message_body);
            Date date = new Date();
            timeText.setText(message.getCreatedAt());
            Picasso.with(mContext).load(message.getAvatar()).transform(new CropCircleTransformation()).into(profileImage);

            // Insert the profile image from the URL into the ImageView.

        }
    }
}