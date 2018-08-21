package com.yd.yourdoctorandroid.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.ratingService.MainResponRating;
import com.yd.yourdoctorandroid.networks.ratingService.RatingRequest;
import com.yd.yourdoctorandroid.networks.ratingService.RatingService;
import com.yd.yourdoctorandroid.networks.reportConversation.ReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.RequestReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.ResponseReportConversation;
import com.yd.yourdoctorandroid.networks.reportService.MainResponReport;
import com.yd.yourdoctorandroid.networks.reportService.ReportRequest;
import com.yd.yourdoctorandroid.networks.reportService.ReportService;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmEndChatFragment extends Fragment {

    @BindView(R.id.tbConfirmEnd)
    Toolbar tbConfirmEnd;
    @BindView(R.id.iv_ava_confirm)
    ImageView iv_ava_confirm;
    @BindView(R.id.tv_logo_confirm)
    TextView tv_logo_confirm;
    @BindView(R.id.btn_rate_confirm)
    Button btn_rate_confirm;
    @BindView(R.id.btn_report)
    Button btn_report;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    private Patient currentPatient;
    private Doctor currentDoctor;
    private String message;
    private String chatHistoryID;

    public ConfirmEndChatFragment() {
        // Required empty public constructor
    }

    public void setData(Patient currentPatient, Doctor currentDoctor, String message, String chatHistoryID) {
        this.currentDoctor = currentDoctor;
        this.currentPatient = currentPatient;
        this.message = message;
        this.chatHistoryID = chatHistoryID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_end_chat, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        setUpUI();

        return view;
    }

    private void setUpUI() {

        tbConfirmEnd.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbConfirmEnd.setTitle("Xác nhận Kết thúc tư vấn");
        tbConfirmEnd.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbConfirmEnd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());
            }
        });

        ZoomImageViewUtils.loadCircleImage(getContext(), currentDoctor.getAvatar(), iv_ava_confirm);
        tv_logo_confirm.setText(message);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });

        btn_rate_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRateConfirm();
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportConversation();
            }
        });

    }

    private RatingBar rbRating;
    private ProgressBar pbInfoRating;
    private EditText etCommentRating;
    private AlertDialog dialogReport;

    private void handleRateConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);
        rbRating = view.findViewById(R.id.rb_rating);
        pbInfoRating = view.findViewById(R.id.pb_info_rating);
        etCommentRating = view.findViewById(R.id.et_comment_rating);

        if (pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);

        builder.setView(view);
        if (currentDoctor != null) {
            builder.setTitle("Đánh giá BS." + currentDoctor.getFullName());
        }
        builder.setPositiveButton("Đánh Gía", new DialogInterface.OnClickListener() {
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
                if (pbInfoRating != null) pbInfoRating.setVisibility(View.VISIBLE);

                if (rbRating.getNumStars() == 0) {
                    Toast.makeText(getContext(), "Bạn nên đánh giá ít nhất 0.5 sao!", Toast.LENGTH_LONG).show();
                    if (pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
                } else {
                    //TODO
                    RatingRequest ratingRequest = new RatingRequest();
                    ratingRequest.setComment(etCommentRating.getText().toString());
                    ratingRequest.setDoctorId(currentDoctor.getDoctorId());
                    ratingRequest.setPatientId(currentPatient.getId());
                    ratingRequest.setRating(rbRating.getRating() + "");

                    RatingService ratingService = RetrofitFactory.getInstance().createService(RatingService.class);
                    ratingService.ratingService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), ratingRequest).enqueue(new Callback<MainResponRating>() {
                        @Override
                        public void onResponse(Call<MainResponRating> call, Response<MainResponRating> response) {

                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Đánh giá bác sĩ thành công", Toast.LENGTH_LONG).show();
                                etCommentRating.setText("");
                                rbRating.setRating(0);
                                dialogReport.dismiss();
                            } else if (response.code() == 401) {
                                Utils.backToLogin(getActivity().getApplicationContext());
                            }
                            if (pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<MainResponRating> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
                            if (pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });

    }

    private EditText etReasonReport;
    private ProgressBar pbInforChat;

    private void reportConversation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.report_user_dialog, null);
        etReasonReport = view.findViewById(R.id.et_reason_report);
        pbInforChat = view.findViewById(R.id.pb_report);
        if (pbInforChat != null) {
            pbInforChat.setVisibility(View.GONE);
        }

        builder.setView(view);
        if (currentDoctor != null) {
            builder.setTitle("Báo cáo cuộc tư vấn của BS." + currentDoctor.getFullName());
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
                    Toast.makeText(getContext(), "Bạn phải nhập lý do", Toast.LENGTH_LONG).show();
                    if (pbInforChat != null) {
                        pbInforChat.setVisibility(View.GONE);
                    }
                } else {
                    RequestReportConversation reportRequest = new RequestReportConversation(currentPatient.getId(),
                            currentDoctor.getDoctorId(), etReasonReport.getText().toString().trim(), chatHistoryID, 1);

                    ReportConversation reportConversation = RetrofitFactory.getInstance().createService(ReportConversation.class);
                    reportConversation.reportConversations(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), reportRequest).enqueue(new Callback<ResponseReportConversation>() {
                        @Override
                        public void onResponse(Call<ResponseReportConversation> call, Response<ResponseReportConversation> response) {
                            if (response.code() == 200 && response.body().isSuccess()) {
                                etReasonReport.setText("");
                                Toast.makeText(getContext(), "Báo cáo cuộc tư vấn thành công", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 401) {
                                Utils.backToLogin(getContext());
                            } else {
                                Toast.makeText(getContext(), "Báo cáo không thành công", Toast.LENGTH_LONG).show();
                            }

                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                            dialogReport.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseReportConversation> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }
        });

    }


//    private ProgressBar pbReport;
//    private void handleReportConfirm() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = this.getLayoutInflater();
//        View view = inflater.inflate(R.layout.report_user_dialog, null);
//        etReasonReport = view.findViewById(R.id.et_reason_report);
//        pbReport = view.findViewById(R.id.pb_report);
//        if(pbReport != null) pbReport.setVisibility(View.GONE);
//        builder.setView(view);
//        if (currentDoctor != null) {
//            builder.setTitle("Báo cáo BS." + currentDoctor.getFullName());
//        }
//        builder.setPositiveButton("Báo cáo", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        dialogReport = builder.create();
//        dialogReport.show();
//        dialogReport.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(pbReport != null) pbReport.setVisibility(View.VISIBLE);
//                if (etReasonReport.getText().toString().equals("")) {
//                    Toast.makeText(getContext(), "Bạn phải nhập lý do", Toast.LENGTH_LONG).show();
//                    if(pbReport != null) pbReport.setVisibility(View.GONE);
//                } else {
//                    ReportRequest reportRequest = new ReportRequest();
//                    reportRequest.setIdPersonBeingReported(currentDoctor.getDoctorId());
//                    reportRequest.setIdReporter(currentPatient.getId());
//                    reportRequest.setReason(etReasonReport.getText().toString());
//
//                    ReportService reportService = RetrofitFactory.getInstance().createService(ReportService.class);
//                    reportService.reportService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), reportRequest).enqueue(new Callback<MainResponReport>() {
//                        @Override
//                        public void onResponse(Call<MainResponReport> call, Response<MainResponReport> response) {
//                            Log.e("Anh le doctor  ", "post submitted to API." + response.body().toString());
//                            if (response.code() == 200 && response.body().isSuccess()) {
//                                Toast.makeText(getContext(), "Báo cáo người dùng thành công", Toast.LENGTH_LONG).show();
//                                etReasonReport.setText("");
//                                dialogReport.dismiss();
//                            } else if (response.code() == 401) {
//                                Utils.backToLogin(getActivity().getApplicationContext());
//                            }
//                            if(pbReport != null) pbReport.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onFailure(Call<MainResponReport> call, Throwable t) {
//                            Toast.makeText(getContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
//                            if(pbReport != null) pbReport.setVisibility(View.GONE);
//                        }
//                    });
//
//                }
//            }
//        });
//    }


}
