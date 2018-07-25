package com.yd.yourdoctorandroid.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.ChatAdpater;
import com.yd.yourdoctorandroid.adapters.DoctorCertificationAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getChatHistory.GetChatHistoryService;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainObjectChatHistory;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainRecord;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.SpecialistDetail;
import com.yd.yourdoctorandroid.networks.models.Certification;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.models.Patient;
import com.yd.yourdoctorandroid.networks.models.Record;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rc_chat)
    RecyclerView recyclerView;

    @BindView(R.id.btn_image)
    ImageView btn_image;

    @BindView(R.id.btn_send)
    Button btnChat;

    @BindView(R.id.edit_message)
    EditText mEditText;

    @BindView(R.id.tb_main_chat)
    Toolbar tb_main_chat;

    @BindView(R.id.iv_done)
    ImageView iv_done;

    @BindView(R.id.iv_info)
    ImageView iv_info;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private List<Record> recordsChat;
    //private final String URL_SERVER = "https://your-doctor-test2.herokuapp.com";

    private AlertDialog alertDialog;

    private final String URL_SERVER = "http://192.168.124.100:3000";

    private Socket mSocket;
    private ChatAdpater chatApapter;
    private MainObjectChatHistory mainObject;

    String chatHistoryID;
    Patient currentPaitent;
    Doctor doctorChoice;
    String doctorChoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        iv_done.setOnClickListener(this);
        iv_info.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btnChat.setOnClickListener(this);

        currentPaitent = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        Intent intent = getIntent();


        chatHistoryID = intent.getStringExtra("chatHistoryId");
        doctorChoiceId = intent.getStringExtra("doctorChoiceId");
        alertDialog = new AlertDialog.Builder(ChatActivity.this).create();
        Log.e("chat activity ", chatHistoryID);
        Log.e("Doctor Choice ", doctorChoiceId);
        //doctorChoice = (Doctor) intent.getSerializableExtra("doctorChoice");
//
        recordsChat = new ArrayList<>();
        chatApapter = new ChatAdpater(getApplicationContext(), recordsChat, currentPaitent.getId());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatApapter);
        tb_main_chat.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tb_main_chat.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tb_main_chat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToMainActivity();
            }
        });


        try {
            mSocket = IO.socket(URL_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Không kết nối được server chat", Toast.LENGTH_LONG).show();
        }

        mSocket.connect();

        mSocket.emit("createRoom", chatHistoryID);

        mSocket.emit("addUser", currentPaitent.getId());

        mSocket.on("newMessage", onNewMessage);

        mSocket.on("errorUpdate", onErrorUpdate);

        mSocket.on("finishConversation", onFinishMessage);

        mSocket.on("conversationDone", onConversationDone);

        loadDoctorChoice(doctorChoiceId);

    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    private void loadChatDisplay() {
        GetChatHistoryService getChatHistoryService = RetrofitFactory.getInstance().createService(GetChatHistoryService.class);
        getChatHistoryService.getChatHistory(chatHistoryID).enqueue(new Callback<MainObjectChatHistory>() {
            @Override
            public void onResponse(Call<MainObjectChatHistory> call, Response<MainObjectChatHistory> response) {
                mainObject = response.body();
                if (response.code() == 200 && mainObject != null) {
                    List<MainRecord> mainRecords = mainObject.getObjConversation().getRecords();

                    for (MainRecord mainRecord : mainRecords) {
                        Record record = new Record();
                        record.setRecorderID(mainRecord.getRecorderID());
                        record.setType(mainRecord.getType());
                        record.setValue(mainRecord.getValue());
                        record.setCreatedAt(mainRecord.getCreated());

                        if (record.getRecorderID().equals(doctorChoice.getDoctorId())) {
                            record.setName(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                            record.setAvatar(doctorChoice.getAvatar());
                        }
                        recordsChat.add(record);
                    }
                    chatApapter.notifyDataSetChanged();
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectChatHistory> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể tải được cuộc trò chuyện", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadDoctorChoice(String doctorChoiceId) {
        GetDoctorDetailService getDoctorDetailService = RetrofitFactory.getInstance().createService(GetDoctorDetailService.class);
        getDoctorDetailService.getMainObjectDoctorDetail(doctorChoiceId).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {
                MainObjectDetailDoctor mainObject = response.body();

                doctorChoice = new Doctor();

                doctorChoice.setDoctorId(mainObject.getInformationDoctor().get(0).getDoctorId().get_id());
                doctorChoice.setFirstName(mainObject.getInformationDoctor().get(0).getDoctorId().getFirstName());
                doctorChoice.setMiddleName(mainObject.getInformationDoctor().get(0).getDoctorId().getMiddleName());
                doctorChoice.setLastName(mainObject.getInformationDoctor().get(0).getDoctorId().getLastName());
                doctorChoice.setAddress(mainObject.getInformationDoctor().get(0).getDoctorId().getAddress());
                doctorChoice.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
                doctorChoice.setBirthday(mainObject.getInformationDoctor().get(0).getDoctorId().getBirthday());
                doctorChoice.setPhoneNumber(mainObject.getInformationDoctor().get(0).getDoctorId().getPhoneNumber());
                doctorChoice.setPlaceWorking(mainObject.getInformationDoctor().get(0).getPlaceWorking());
                doctorChoice.setUniversityGraduate(mainObject.getInformationDoctor().get(0).getUniversityGraduate());
                doctorChoice.setYearGraduate(mainObject.getInformationDoctor().get(0).getYearGraduate());
                doctorChoice.setCurrentRating(mainObject.getInformationDoctor().get(0).getCurrentRating());
                doctorChoice.setCertificates((ArrayList<Certification>) mainObject.getInformationDoctor().get(0).getCertificates());

                loadChatDisplay();


            }

            @Override
            public void onFailure(Call<MainObjectDetailDoctor> call, Throwable t) {
                Log.e("Anhle P error ", t.toString());
                progressBar.setVisibility(View.GONE);
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

                }
            });
        }
    };

    private Emitter.Listener onErrorUpdate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                }
            });
        }
    };

//    private void testDialoag(){
//        new AlertDialog.Builder(getApplicationContext())
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Xác nhận việc kết th")
//                .setMessage("Are you sure you want to close this activity?")
//                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //mSocket.emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);
//                        dialog.dismiss();
//                    }
//
//                })
//                .show();
//    }

    private Emitter.Listener onFinishMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    Log.e("emitt anh le", message);
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                    new AlertDialog.Builder(getApplicationContext())
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setTitle("Xác nhận việc kết th")
//                            .setMessage("Are you sure you want to close this activity?")
//                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //mSocket.emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);
//                                    dialog.dismiss();
//                                }
//
//                            })
//                            .show();

                }
            });
        }
    };


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String message;
                    message = data.optString("data");
                    //Log.e("emitt anh le", message);

                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        Record record = new Record();
                        record.setRecorderID(jsonObject.getString("senderID"));
                        record.setValue(jsonObject.getString("value"));
                        record.setType(Integer.parseInt(jsonObject.getString("type")));

                        record.setCreatedAt(jsonObject.getString("createTime"));
                        if (record.getRecorderID().equals(doctorChoice.getDoctorId())) {
                            record.setAvatar(doctorChoice.getAvatar());
                            record.setName(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                        }

                        recordsChat.add(record);
                        chatApapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private void backToMainActivity() {
        // mSocket.emit("disconnect");
        mSocket.disconnect();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_done: {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận việc kết thúc cuộc tư vấn")
                        .setMessage("Bạn có chắc chắn muốn kết thúc cuộc tư vấn không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                               mSocket.emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);

                            }

                        })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

                break;

            }
//                alertDialog.dismiss();
//                alertDialog.setTitle("Xác nhận việc kết thúc cuộc trò chuyện");
//                //mainObject.getObjConversation().getContentTopic();
//                alertDialog.setMessage("Bạn có chắc muốn kết thúc cuộc trò chuyện không ?");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hủy",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                progressBar.setVisibility(View.VISIBLE);
//                                mSocket.emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);


                                //For test
//                                alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
//                                alertDialog.setTitle("Xác nhận việc kết thúc cuộc trò chuyện");
//                                //mainObject.getObjConversation().getContentTopic();
//                                alertDialog.setMessage("Bạn có chắc muốn kết thúc cuộc trò chuyện không ?");
//                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hủy",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                progressBar.setVisibility(View.VISIBLE);
//                                                mSocket.emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);
//                                            }
//                                        });
//                                alertDialog.show();



            case R.id.iv_info: {
                alertDialog.dismiss();
                alertDialog = new AlertDialog.Builder(ChatActivity.this).create();
                alertDialog.setTitle("Chat với BS " + doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                //mainObject.getObjConversation().getContentTopic();
                alertDialog.setMessage("Nội dung : " + mainObject.getObjConversation().getContentTopic());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                break;
            }
            case R.id.btn_image: {
                break;

            }
            case R.id.btn_send: {
                mSocket.emit("sendMessage", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID, 1, mEditText.getText().toString());
                mEditText.setText("");
                break;
            }
        }
    }
}

