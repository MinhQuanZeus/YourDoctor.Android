package com.yd.yourdoctorandroid.activities;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.ChatAdpater;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getChatHistory.GetChatHistoryService;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainObjectChatHistory;
import com.yd.yourdoctorandroid.networks.getChatHistory.MainRecord;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getLinkImageService.GetLinkeImageService;
import com.yd.yourdoctorandroid.networks.getLinkImageService.MainGetLink;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Record;
import com.yd.yourdoctorandroid.utils.ImageUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

    private List<Record> recordsChat;
    private final String URL_SERVER = "https://your-doctor-test2.herokuapp.com";
    public static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private AlertDialog alertDialog;

    //private final String URL_SERVER = "http://192.168.124.109:3000";

    private Socket mSocket;
    private ChatAdpater chatApapter;
    private MainObjectChatHistory mainObject;

    ImageUtils imageUtils;

    String chatHistoryID;
    Patient currentPaitent;
    Doctor doctorChoice;
    String doctorChoiceId;

    private int typeChatCurrent;

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

        imageUtils = new ImageUtils(this);

        typeChatCurrent = 1;
        currentPaitent = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        Intent intent = getIntent();
        chatHistoryID = intent.getStringExtra("chatHistoryId");
        doctorChoiceId = intent.getStringExtra("doctorChoiceId");
        alertDialog = new AlertDialog.Builder(ChatActivity.this).create();
        Log.e("chat activity ", chatHistoryID);
        Log.e("Doctor Choice ", doctorChoiceId);

        recordsChat = new ArrayList<>();
        chatApapter = new ChatAdpater(this, recordsChat, currentPaitent.getId());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatApapter);

        tbMainChat.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbMainChat.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbMainChat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToMainActivity();
            }
        });


//        try {
//            mSocket = IO.socket(URL_SERVER);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            new AlertDialog.Builder(this)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle("Lỗi kết nối server")
//                    .setMessage("Không thể kết nối server, Bạn muốn thử kết nối lại không?")
//                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            try {
//                                mSocket = IO.socket(URL_SERVER);
//                            } catch (URISyntaxException e1) {
//                                Toast.makeText(getApplicationContext(), "Không kết nối được server chat", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                    })
//                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).show();
//        }

        //SocketUtils.getInstance().getSocket().connect();

        //SocketUtils.getInstance().getSocket().emit("createRoom", chatHistoryID);


        SocketUtils.getInstance().getSocket().emit("joinRoom", chatHistoryID);

        SocketUtils.getInstance().getSocket().on("newMessage", onNewMessage);

        SocketUtils.getInstance().getSocket().on("errorUpdate", onErrorUpdate);

        SocketUtils.getInstance().getSocket().on("finishConversation", onFinishMessage);

        SocketUtils.getInstance().getSocket().on("conversationDone", onConversationDone);

        loadDoctorChoice(doctorChoiceId);

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
                    List<MainRecord> mainRecords = mainObject.getObjConversation().getRecords();

                    for (MainRecord mainRecord : mainRecords) {
                        Record record = new Record();
                        record.setRecorderID(mainRecord.getRecorderID());
                        record.setType(mainRecord.getType());
                        record.setValue(mainRecord.getValue());
                        try{
                            record.setCreatedAt(Utils.convertTime(Long.parseLong(mainRecord.getCreated())));
                        }catch (Exception e){
                            record.setCreatedAt(new Date().toString());
                        }


                        if (record.getRecorderID().equals(doctorChoice.getDoctorId())) {
                            record.setName(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                            record.setAvatar(doctorChoice.getAvatar());
                        }
                        recordsChat.add(record);
                    }
                    chatApapter.notifyDataSetChanged();
                } else if (response.code() == 401) {
                    Utils.backToLogin(getApplicationContext());
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
        getDoctorDetailService.getMainObjectDoctorDetail(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), doctorChoiceId).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {

                if (response.code() == 200) {
                    MainObjectDetailDoctor mainObject = response.body();

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

                    loadChatDisplay();


                } else if (response.code() == 401) {
                    Utils.backToLogin(getApplicationContext());
                }

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
                    progressBar.setVisibility(View.GONE);

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
                    progressBar.setVisibility(View.GONE);

                }
            });
        }
    };

    private Emitter.Listener onFinishMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    Log.e("emitt anh le", message);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

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
                        chatApapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }
    };

    private void backToMainActivity() {
        // mSocket.emit("disconnect");
        //mSocket.disconnect();
        SocketUtils.getInstance().getSocket().emit("leaveRoom", chatHistoryID);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDone: {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận việc kết thúc cuộc tư vấn")
                        .setMessage("Bạn có chắc chắn muốn kết thúc cuộc tư vấn không?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SocketUtils.getInstance().getSocket().emit("doneConversation", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID);
                                progressBar.setVisibility(View.VISIBLE);

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
            case R.id.ivInfo: {
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
        }
    }

    private void handleSendMessageChat() {
        progressBar.setVisibility(View.VISIBLE);
        if (typeChatCurrent == 1) {
            if (mEditText.getText().equals("")) {
                Toast.makeText(this, "Bạn nên nhập tin nhắn trước", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            } else {
                SocketUtils.getInstance().getSocket().emit("sendMessage", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID, 1, mEditText.getText().toString());
                mEditText.setText("");
            }

        } else {
            if (imageUtils.getImageUpload() == null) {
                progressBar.setVisibility(View.GONE);
                return;
            }

            GetLinkeImageService getLinkeImageService = RetrofitFactory.getInstance().createService(GetLinkeImageService.class);
            getLinkeImageService.uploadImageToGetLink(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), imageUtils.getImageUpload()).enqueue(new Callback<MainGetLink>() {
                @Override
                public void onResponse(Call<MainGetLink> call, Response<MainGetLink> response) {
                    Log.e("linkImage", response.body().getFilePath());

                    if (response.code() == 200) {
                        Log.e("linkSuccess", response.body().getFilePath());
                        MainGetLink mainObject = response.body();
                        SocketUtils.getInstance().getSocket().emit("sendMessage", currentPaitent.getId(), doctorChoice.getDoctorId(), chatHistoryID, 2, mainObject.getFilePath());
                        setTypeChat(1);
                    }else if (response.code() == 401) {
                        Utils.backToLogin(getApplicationContext());
                    } else {
                        Log.e("not200", "anhle");
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MainGetLink> call, Throwable t) {
                    Log.e("Anhlelink", t.toString());
                    progressBar.setVisibility(View.GONE);
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
                Log.e("loiAnhLe", "Cannot get a selected photo from the gallery.", e);
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

