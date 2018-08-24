package com.yd.yourdoctorandroid.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getHistoryVideoCall.ObjectHistoryVideo;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.ObjectPaymentResponse;
import com.yd.yourdoctorandroid.networks.reportConversation.ReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.RequestReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.ResponseReportConversation;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                holder.ivReportWithDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reportConversation(holder.getObjectHistoryVideo().getDoctorId().get_id(),
                                holder.getObjectHistoryVideo().getDoctorId().getFullName(),
                                holder.getObjectHistoryVideo().get_id());
                    }
                });

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

    private EditText etReasonReport;
    private ProgressBar pbInforChat;
    private AlertDialog dialogReport;

    private void reportConversation(String idDoctorChoice, String nameDoctorChoice, String idConversation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.report_user_dialog, null);
        etReasonReport = view.findViewById(R.id.et_reason_report);
        pbInforChat = view.findViewById(R.id.pb_report);
        if (pbInforChat != null) {
            pbInforChat.setVisibility(View.GONE);
        }

        builder.setView(view);
        if (nameDoctorChoice != null) {
            builder.setTitle("Báo cáo cuộc tư vấn của BS." + nameDoctorChoice);
        }
        builder.setPositiveButton("Báo cáo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogReport = builder.create();
        dialogReport.show();
        dialogReport.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pbInforChat != null) {
                    pbInforChat.setVisibility(View.VISIBLE);
                }
                if (etReasonReport.getText().toString().equals("")) {
                    Toast.makeText(context, "Bạn phải nhập lý do", Toast.LENGTH_LONG).show();
                    if (pbInforChat != null) {
                        pbInforChat.setVisibility(View.GONE);
                    }
                } else {
                    RequestReportConversation reportRequest = new RequestReportConversation(SharedPrefs.getInstance().get("USER_INFO", Patient.class).getId(),
                            idDoctorChoice, etReasonReport.getText().toString().trim(), idConversation, 2);

                    ReportConversation reportConversation = RetrofitFactory.getInstance().createService(ReportConversation.class);
                    reportConversation.reportConversations(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), reportRequest).enqueue(new Callback<ResponseReportConversation>() {
                        @Override
                        public void onResponse(Call<ResponseReportConversation> call, Response<ResponseReportConversation> response) {
                            if (response.code() == 200 && response.body().isSuccess()) {
                                etReasonReport.setText("");
                                Toast.makeText(context, "Báo cáo cuộc tư vấn thành công", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 401) {
                                Utils.backToLogin(context.getApplicationContext());
                            } else {
                                Toast.makeText(context.getApplicationContext(), "Báo cáo không thành công", Toast.LENGTH_LONG).show();
                            }

                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                            dialogReport.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseReportConversation> call, Throwable t) {
                            Toast.makeText(context.getApplicationContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }
        });

    }

    public class VideoCallHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVideoHistory;
        TextView tvTitleVideoHistory;
        TextView tvContentVideoHistory;
        TextView tvTimeVideoCall;
        LinearLayout ivReportWithDoctor;
        private ObjectHistoryVideo objectHistoryVideo;

        public VideoCallHistoryViewHolder(View itemView) {
            super(itemView);
            ivVideoHistory = itemView.findViewById(R.id.iv_video_history);
            tvTitleVideoHistory = itemView.findViewById(R.id.tv_title_video_history);
            tvContentVideoHistory = itemView.findViewById(R.id.tv_content_video_history);
            tvTimeVideoCall = itemView.findViewById(R.id.tv_time_video_call);
            ivReportWithDoctor = itemView.findViewById(R.id.ivReportWithDoctor);

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
                        + " với BS." + objectHistoryVideo.getDoctorId().getFullName() + " đã kết thúc");
                tvTimeVideoCall.setText("Bắt đầu: " + Utils.convertTime(objectHistoryVideo.getTimeStart()) +"\n"
                        + "Kết thúc: " + Utils.convertTime(objectHistoryVideo.getTimeEnd()));


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
