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
import com.yd.yourdoctorandroid.networks.getHistoryVideoCall.ObjectHistoryVideo;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.ObjectPaymentResponse;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoCallHistoryAdapter extends RecyclerView.Adapter<VideoCallHistoryAdapter.VideoCallHistoryViewHolder> {
    private List<ObjectHistoryVideo> objectHistoryVideos;
    private Context context;
    private boolean isLoadingAdded = false;

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public VideoCallHistoryAdapter(Context context) {

        this.context = context;
        this.objectHistoryVideos = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoCallHistoryAdapter.VideoCallHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VideoCallHistoryAdapter.VideoCallHistoryViewHolder viewHolder = null;
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
    private VideoCallHistoryAdapter.VideoCallHistoryViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        VideoCallHistoryAdapter.VideoCallHistoryViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_videocall_history, parent, false);
        viewHolder = new VideoCallHistoryAdapter.VideoCallHistoryViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoCallHistoryAdapter.VideoCallHistoryViewHolder holder, int position) {


        switch (getItemViewType(position)) {
            case ITEM:
                holder.setData(objectHistoryVideos.get(position));
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == objectHistoryVideos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(ObjectHistoryVideo objectHistoryVideo) {
        objectHistoryVideos.add(objectHistoryVideo);
        notifyItemInserted(objectHistoryVideos.size() - 1);
    }

    public void addAll(List<ObjectHistoryVideo> objectHistoryVideos) {
        for (ObjectHistoryVideo objectHistoryVideo : objectHistoryVideos) {
            add(objectHistoryVideo);
        }
    }

    public void remove(ObjectHistoryVideo objectHistoryVideo) {
        int position = objectHistoryVideos.indexOf(objectHistoryVideo);
        if (position > -1) {
            objectHistoryVideos.remove(position);
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
        add(new ObjectHistoryVideo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = objectHistoryVideos.size() - 1;
        ObjectHistoryVideo item = getItem(position);

        if (item != null) {
            objectHistoryVideos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ObjectHistoryVideo getItem(int position) {
        return objectHistoryVideos.get(position);
    }

    @Override
    public int getItemCount() {
        return objectHistoryVideos == null ? 0 : objectHistoryVideos.size();
    }

    public class VideoCallHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVideoHistory;
        TextView tvTitleVideoHistory;
        TextView tvContentVideoHistory;
        TextView tvTimeVideoCall;
        private ObjectHistoryVideo objectHistoryVideo;

        public VideoCallHistoryViewHolder(View itemView) {
            super(itemView);
            ivVideoHistory = itemView.findViewById(R.id.iv_video_history);
            tvTitleVideoHistory = itemView.findViewById(R.id.tv_title_video_history);
            tvContentVideoHistory = itemView.findViewById(R.id.tv_content_video_history);
            tvTimeVideoCall = itemView.findViewById(R.id.tv_time_video_call);


        }

        public void setData(ObjectHistoryVideo objectHistoryVideo) {

            this.objectHistoryVideo = objectHistoryVideo;

            if (objectHistoryVideo != null && context != null) {
                try {
                    ZoomImageViewUtils.loadCircleImage(context, objectHistoryVideo.getDoctorId().getAvatar(), ivVideoHistory);
                    tvTitleVideoHistory.setText("Tư vấn video call");
                } catch (Exception e) {

                }
                tvContentVideoHistory.setText("Cuộc tư vấn video call"
                        + " với BS." + objectHistoryVideo.getDoctorId().getFullName() +" đã kết thúc");
                tvTimeVideoCall.setText("Thời gian: " + Utils.convertTime(objectHistoryVideo.getTimeStart())
                +" --- " +  Utils.convertTime(objectHistoryVideo.getTimeEnd()));


            }
        }


        public ObjectHistoryVideo getObjectHistoryVideo() {
            return objectHistoryVideo;
        }

        public void setObjectHistoryVideo(ObjectHistoryVideo objectHistoryVideo) {
            this.objectHistoryVideo = objectHistoryVideo;
        }
    }

    protected class LoadingVH extends VideoCallHistoryViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}
