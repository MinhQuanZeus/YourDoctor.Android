package com.yd.yourdoctorandroid.activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.ChatAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.fragments.ConfirmEndChatFragment;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getChatHistory.GetChatHistoryService;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainObjectChatHistory;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainRecord;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getLinkImageService.GetLinkImageService;
import com.yd.yourdoctorandroid.networks.getLinkImageService.MainGetLink;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Record;
import com.yd.yourdoctorandroid.networks.reportConversation.ReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.RequestReportConversation;
import com.yd.yourdoctorandroid.networks.reportConversation.ResponseReportConversation;
import com.yd.yourdoctorandroid.utils.ImageUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rcChat)
    RecyclerView recyclerView;

    @BindView(R.id.btnImage)
    ImageView btnImage;

    @BindView(R.id.btnSend)
    Button btnChat;

    @BindView(R.id.editMessage)
    EditText mEditText;

    @BindView(R.id.tbMainChat)
    Toolbar tbMainChat;

    @BindView(R.id.ivDone)
    ImageView ivDone;

    @BindView(R.id.ivInfo)
    ImageView ivInfo;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.rlMessageImage)
    RelativeLayout rlMessageImage;

    @BindView(R.id.ivMessage)
    ImageView ivMessage;

    @BindView(R.id.ivCancel)
    ImageView ivCancel;

    @BindView(R.id.ivReportConversation)
    ImageView ivReportConversation;

    @BindView(R.id.tv_content_question)
    TextView tvContentQuestion;

    //Dialog Info
    private ImageView ivDoctorChat;

    private TextView tvNameDoctorChat;

    private TextView tvBirthDayChat;

    private TextView tvContentChat;

    private Button btnRateChatDoctor;

    private Button btnOkInfoDoctor;

    private TextView tvAddressChat;

    private List<Record> recordsChat;

    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;

    private ChatAdapter chatApapter;
    private MainObjectChatHistory mainObject;

    ImageUtils imageUtils;

    String chatHistoryID;
    Patient currentPaitent;
    Doctor doctorChoice;
    String doctorChoiceId;
    private RecyclerView.LayoutManager layoutManager;

    private int typeChatCurrent;

    private boolean isDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        ivDone.setOnClickListener(this);
        ivInfo.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivReportConversation.setOnClickListener(this);

        imageUtils = new ImageUtils(this);
        isDone = false;
        typeChatCurrent = 1;
        currentPaitent = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        Intent intent = getIntent();
        chatHistoryID = intent.getStringExtra("chatHistoryId");
        doctorChoiceId = intent.getStringExtra("doctorChoiceId");

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        tbMainChat.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbMainChat.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbMainChat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToMainActivity();
            }
        });

        tvContentQuestion.setMovementMethod(new ScrollingMovementMethod());

        try {
            if (!SocketUtils.getInstance().checkIsConnected()) {
                Toast.makeText(getApplicationContext(), "Không có kết nối mạng", Toast.LENGTH_LONG).show();
                return;
            }
            SocketUtils.getInstance().getSocket().emit("joinRoom", chatHistoryID);

            SocketUtils.getInstance().setRoomId(chatHistoryID);

            SocketUtils.getInstance().getSocket().on("newMessage", onNewMessage);

            SocketUtils.getInstance().getSocket().on("errorUpdate", onErrorUpdate);

            SocketUtils.getInstance().getSocket().on("finishConversation", onFinishMessage);

            SocketUtils.getInstance().getSocket().on("conversationDone", onConversationDone);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Không thể kết nối với máy chủ", Toast.LENGTH_LONG).show();
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

        }

        recordsChat = new ArrayList<>();
        chatApapter = new ChatAdapter(this, recordsChat, currentPaitent.getId());
        recyclerView.setAdapter(chatApapter);
        loadDoctorChoice(doctorChoiceId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSend eventSend) {
        if (eventSend.getType() == 2) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            recordsChat = new ArrayList<>();
            chatApapter = new ChatAdapter(this, recordsChat, currentPaitent.getId());
            recyclerView.setAdapter(chatApapter);
            loadDoctorChoice(doctorChoiceId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    private void loadChatDisplay() {
        GetChatHistoryService getChatHistoryService = RetrofitFactory.getInstance().createService(GetChatHistoryService.class);
        getChatHistoryService.getChatHistory(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), chatHistoryID).enqueue(new Callback<MainObjectChatHistory>() {
            @Override
            public void onResponse(Call<MainObjectChatHistory> call, Response<MainObjectChatHistory> response) {
                mainObject = response.body();
                if (response.code() == 200 && mainObject != null) {
                    if (mainObject.getObjConversation().getStatus() == 2) {
                        isDone = true;
                    }
                    tvContentQuestion.setText("Nội dung : " + mainObject.getObjConversation().getContentTopic());

                    List<MainRecord> mainRecords = mainObject.getObjConversation().getRecords();
                    if (mainRecords != null) {
                        for (MainRecord mainRecord : mainRecords) {
                            Record record = new Record();
                            record.setRecorderID(mainRecord.getRecorderID());
                            record.setType(mainRecord.getType());
                            record.setValue(mainRecord.getValue());
                            try {
                                record.setCreatedAt(Utils.convertTime(mainRecord.getCreated()));
                            } catch (Exception e) {
                                record.setCreatedAt(new Date().toString());
                            }

                            if (record.getRecorderID().equals(doctorChoice.getDoctorId())) {
                                record.setName(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                                record.setAvatar(doctorChoice.getAvatar());
                            }
                            recordsChat.add(record);
                        }
                        if (recordsChat.size() > 0) {
                            recyclerView.smoothScrollToPosition(recordsChat.size() - 1);
                        }
                        chatApapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Không thể tải được cuộc chat", Toast.LENGTH_LONG).show();
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                } else if (response.code() == 401) {
                    Utils.backToLogin(getApplicationContext());
                }

                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectChatHistory> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể tải được cuộc trò chuyện", Toast.LENGTH_LONG).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadDoctorChoice(String doctorChoiceId) {
        GetDoctorDetailService getDoctorDetailService = RetrofitFactory.getInstance().createService(GetDoctorDetailService.class);
        getDoctorDetailService.getMainObjectDoctorDetail(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), doctorChoiceId).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {

                if (response.code() == 200) {
                    MainObjectDetailDoctor mainObject = response.body();
                    if(mainObject != null){
                        doctorChoice = new Doctor();

                        doctorChoice.setDoctorId(mainObject.getInformationDoctor().get(0).getDoctorId().get_id());
                        doctorChoice.setFirstName(mainObject.getInformationDoctor().get(0).getDoctorId().getFirstName());
                        doctorChoice.setMiddleName(mainObject.getInformationDoctor().get(0).getDoctorId().getMiddleName());
                        doctorChoice.setLastName(mainObject.getInformationDoctor().get(0).getDoctorId().getLastName());
                        doctorChoice.setAddress(mainObject.getInformationDoctor().get(0).getDoctorId().getAddress());
                        doctorChoice.setAvatar(mainObject.getInformationDoctor().get(0).getDoctorId().getAvatar());
                        doctorChoice.setBirthday(mainObject.getInformationDoctor().get(0).getDoctorId().getBirthday());
                        doctorChoice.setPhoneNumber(mainObject.getInformationDoctor().get(0).getDoctorId().getPhoneNumber());
                        doctorChoice.setPlaceWorking(mainObject.getInformationDoctor().get(0).getPlaceWorking());
                        doctorChoice.setUniversityGraduate(mainObject.getInformationDoctor().get(0).getUniversityGraduate());
                        doctorChoice.setYearGraduate(mainObject.getInformationDoctor().get(0).getYearGraduate());
                        doctorChoice.setCurrentRating(mainObject.getInformationDoctor().get(0).getCurrentRating());
                        doctorChoice.setCertificates((ArrayList<Certification>) mainObject.getInformationDoctor().get(0).getCertificates());
                        tbMainChat.setTitle("Bs." + doctorChoice.getLastName());
                        loadChatDisplay();
                    }

                } else if (response.code() == 401) {
                    Utils.backToLogin(getApplicationContext());
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi tải thông tin bác sĩ", Toast.LENGTH_LONG).show();
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<MainObjectDetailDoctor> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi khi tải thông tin bác sĩ", Toast.LENGTH_LONG).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);

            }
        });
    }

    private Emitter.Listener onConversationDone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    isDone = true;
                }
            });
        }
    };

    private Emitter.Listener onErrorUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    String message = (String) args[0];
                    isDone = true;
                    try {
                        ConfirmEndChatFragment confirmEndChatFragment = new ConfirmEndChatFragment();
                        confirmEndChatFragment.setData(currentPaitent, doctorChoice, message, chatHistoryID);
                        ScreenManager.openFragment(getSupportFragmentManager(), confirmEndChatFragment, R.id.rl_chat, true, true);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    };

    private Emitter.Listener onFinishMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    String message = (String) args[0];
                    isDone = true;
                    try {
                        ConfirmEndChatFragment confirmEndChatFragment = new ConfirmEndChatFragment();
                        confirmEndChatFragment.setData(currentPaitent, doctorChoice, message, chatHistoryID);
                        ScreenManager.openFragment(getSupportFragmentManager(), confirmEndChatFragment, R.id.rl_chat, true, true);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String message;
                    message = data.optString("data");

                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        Record record = new Record();
                        record.setRecorderID(jsonObject.getString("senderID"));
                        record.setValue(jsonObject.getString("value"));
                        record.setType(Integer.parseInt(jsonObject.getString("type")));
                        record.setCreatedAt(Utils.convertTime(Long.parseLong(jsonObject.getString("createTime"))));

                        if (record.getRecorderID().equals(doctorChoice.getDoctorId())) {
                            record.setAvatar(doctorChoice.getAvatar());
                            record.setName(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                        }

                        recordsChat.add(record);
                        if (recordsChat.size() > 0) {
                            recyclerView.smoothScrollToPosition(recordsChat.size() - 1);
                        }
                        chatApapter.notifyDataSetChanged();
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }
    };

    private void backToMainActivity() {
        SocketUtils.getInstance().setRoomId(null);
        SocketUtils.getInstance().getSocket().emit("leaveRoom", chatHistoryID);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDialogConfirmEnd() {
        new AlertDialog.Builder(ChatActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xác nhận việc kết thúc cuộc tư vấn")
                .setMessage("Bạn có chắc chắn muốn kết thúc cuộc tư vấn không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isDone) {
                            if (!SocketUtils.getInstance().checkIsConnected()) {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối máy chủ", Toast.LENGTH_LONG).show();
                                if (progressBar != null) progressBar.setVisibility(View.GONE);
                            } else {
                                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
                                SocketUtils.getInstance().getSocket().emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Cuộc tư vấn đã kết thúc trước đó", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDone: {
                showDialogConfirmEnd();
                break;
            }
            case R.id.ivInfo: {
                showDialogInfo();
                break;
            }
            case R.id.btnImage: {
                imageUtils.displayAttachImageDialog();
                break;

            }
            case R.id.btnSend: {
                handleSendMessageChat();
                break;
            }
            case R.id.ivCancel: {
                setTypeChat(1);
                break;
            }
            case R.id.ivReportConversation: {
                reportConversation();
                break;
            }
        }
    }

    private EditText etReasonReport;
    private ProgressBar pbInforChat;
    private AlertDialog dialogReport;

    private void reportConversation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.report_user_dialog, null);
        etReasonReport = view.findViewById(R.id.et_reason_report);
        pbInforChat = view.findViewById(R.id.pb_report);
        if (pbInforChat != null) {
            pbInforChat.setVisibility(View.GONE);
        }

        builder.setView(view);
        if (doctorChoice != null) {
            builder.setTitle("Báo cáo cuộc tư vấn của BS." + doctorChoice.getFullName());
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
                    Toast.makeText(getApplicationContext(), "Bạn phải nhập lý do", Toast.LENGTH_LONG).show();
                    if (pbInforChat != null) {
                        pbInforChat.setVisibility(View.GONE);
                    }
                } else {
                    RequestReportConversation reportRequest = new RequestReportConversation(currentPaitent.getId(),
                            doctorChoiceId, etReasonReport.getText().toString().trim(), chatHistoryID, 1);

                    ReportConversation reportConversation = RetrofitFactory.getInstance().createService(ReportConversation.class);
                    reportConversation.reportConversations(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), reportRequest).enqueue(new Callback<ResponseReportConversation>() {
                        @Override
                        public void onResponse(Call<ResponseReportConversation> call, Response<ResponseReportConversation> response) {
                            if (response.code() == 200 && response.body().isSuccess()) {
                                etReasonReport.setText("");
                                Toast.makeText(getApplicationContext(), "Báo cáo cuộc tư vấn thành công", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 401) {
                                Utils.backToLogin(getApplicationContext());
                            } else {
                                Toast.makeText(getApplicationContext(), "Bạn đã báo cáo trước đó!", Toast.LENGTH_LONG).show();
                            }

                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                            dialogReport.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseReportConversation> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
                            if (pbInforChat != null) {
                                pbInforChat.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }
        });

    }


    private void showDialogInfo() {
        if(doctorChoice == null || mainObject == null) return;

        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.info_chat_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // set the custom dialog components - text, image and button
        ivDoctorChat = dialog.findViewById(R.id.ivDoctorChat);
        tvNameDoctorChat = dialog.findViewById(R.id.tvNameDoctorChat);
        tvBirthDayChat = dialog.findViewById(R.id.tvBirthDayChat);
        tvContentChat = dialog.findViewById(R.id.tvContentChat);
        btnRateChatDoctor = dialog.findViewById(R.id.btn_rate_chat_doctor);
        btnOkInfoDoctor = dialog.findViewById(R.id.btn_ok_info_doctor);
        tvAddressChat = dialog.findViewById(R.id.tvAddressChat);
        tvContentChat.setMovementMethod(new ScrollingMovementMethod());
        //set up data
        if (doctorChoice != null) {
            ZoomImageViewUtils.loadCircleImage(getApplicationContext(), doctorChoice.getAvatar(), ivDoctorChat);
            tvNameDoctorChat.setText("BS. " + doctorChoice.getFullName());
            tvBirthDayChat.setText("NS: " + doctorChoice.getBirthday());
            tvAddressChat.setText("Địa chỉ" + doctorChoice.getAddress());
        }

        if (mainObject.getObjConversation() != null) {
            tvContentChat.setText("Nội dung câu hỏi là: " + mainObject.getObjConversation().getContentTopic());
        }

        // if button is clicked, close the custom dialog
        btnOkInfoDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRateChatDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorProfileFragment doctorProfileFragment = new DoctorProfileFragment();
                doctorProfileFragment.setDoctorID(doctorChoiceId);
                doctorProfileFragment.setIsFromChat(true);
                ScreenManager.openFragment(getSupportFragmentManager(), doctorProfileFragment, R.id.rl_chat, true, true);
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    private void handleSendMessageChat() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (typeChatCurrent == 1) {
            if (mEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Bạn nên nhập tin nhắn trước", Toast.LENGTH_LONG).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            } else {
                if (!SocketUtils.getInstance().checkIsConnected()) {
                    Toast.makeText(getApplicationContext(), "Không thể kết nối máy chủ", Toast.LENGTH_LONG).show();
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    SocketUtils.getInstance().getSocket().emit("sendMessage", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID, 1, mEditText.getText().toString().trim());
                    mEditText.setText("");
                }

            }

        } else {
            if (imageUtils.getImageUpload() == null) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                return;
            }

            GetLinkImageService getLinkeImageService = RetrofitFactory.getInstance().createService(GetLinkImageService.class);
            getLinkeImageService.uploadImageToGetLink(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), imageUtils.getImageUpload()).enqueue(new Callback<MainGetLink>() {
                @Override
                public void onResponse(Call<MainGetLink> call, Response<MainGetLink> response) {

                    if (response.code() == 200) {
                        Log.e("linkSuccess", response.body().getFilePath());
                        MainGetLink mainObject = response.body();
                        if (!SocketUtils.getInstance().checkIsConnected()) {
                            Toast.makeText(getApplicationContext(), "Không thể kết nối máy chủ", Toast.LENGTH_LONG).show();
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            SocketUtils.getInstance().getSocket().emit("sendMessage", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID, 2, mainObject.getFilePath());
                            setTypeChat(1);
                        }

                    } else if (response.code() == 401) {
                        Utils.backToLogin(getApplicationContext());
                    } else {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainGetLink> call, Throwable t) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void setTypeChat(int typeChat) {
        ivMessage.setImageResource(R.drawable.ic_image_black_24dp);
        imageUtils.clearAll();
        if (typeChat == 1) {
            typeChatCurrent = 1;
            mEditText.setVisibility(View.VISIBLE);
            rlMessageImage.setVisibility(View.GONE);
            mEditText.setText("");
        } else {
            typeChatCurrent = 2;
            mEditText.setVisibility(View.GONE);
            rlMessageImage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getApplicationContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TAKE_PHOTO) {
            File file = new File(imageUtils.getmImagePathToBeAttached());
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageUtils.getmImagePathToBeAttached(), options);
                options.inJustDecodeBounds = false;
                imageUtils.setmImageToBeAttached(BitmapFactory.decodeFile(imageUtils.getmImagePathToBeAttached(), options));
                try {
                    ExifInterface exif = new ExifInterface(imageUtils.getmImagePathToBeAttached());
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotationAngle, (float) imageUtils.getmImageToBeAttached().getWidth() / 2, (float) imageUtils.getmImageToBeAttached().getHeight() / 2);
                    imageUtils.setmImageToBeAttached(Bitmap.createBitmap(imageUtils.getmImageToBeAttached(), 0, 0, options.outWidth, options.outHeight, matrix, true));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.e("loi chup anh", ex.toString());
                }
                file.delete();
            }
        } else if (requestCode == REQUEST_CHOOSE_PHOTO) {
            try {
                Uri uri = data.getData();
                ContentResolver resolver = getContentResolver();
                int rotationAngle = imageUtils.getOrientation(this, uri);
                imageUtils.setmImageToBeAttached(MediaStore.Images.Media.getBitmap(resolver, uri));
                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) imageUtils.getmImageToBeAttached().getWidth() / 2, (float) imageUtils.getmImageToBeAttached().getHeight() / 2);
                imageUtils.setmImageToBeAttached(Bitmap.createBitmap(imageUtils.getmImageToBeAttached(), 0, 0, imageUtils.getmImageToBeAttached().getWidth(), imageUtils.getmImageToBeAttached().getHeight(), matrix, true));
            } catch (IOException e) {
            }
        }
        updateUI();
    }

    public void updateUI() {
        typeChatCurrent = 2;
        mEditText.setVisibility(View.GONE);
        rlMessageImage.setVisibility(View.VISIBLE);
        if (imageUtils.getmImageToBeAttached() != null) {
            ivMessage.setImageBitmap(imageUtils.getmImageToBeAttached());
        } else {
            ivMessage.setImageResource(R.drawable.ic_image_black_24dp);
        }
    }

}

