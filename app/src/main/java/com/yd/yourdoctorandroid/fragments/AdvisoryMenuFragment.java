package com.yd.yourdoctorandroid.fragments;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;

import com.github.nkzawa.emitter.Emitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.adapters.ChatAdapter;
import com.yd.yourdoctorandroid.adapters.DoctorChoiceAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Record;
import com.yd.yourdoctorandroid.networks.getListRecommentDoctor.DoctorRecommend;
import com.yd.yourdoctorandroid.networks.getListRecommentDoctor.GetListRecommentDoctorService;
import com.yd.yourdoctorandroid.networks.getListRecommentDoctor.MainObjectRecommend;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.networks.postChatHistory.ChatHistory;
import com.yd.yourdoctorandroid.networks.postChatHistory.ChatHistoryResponse;
import com.yd.yourdoctorandroid.networks.postChatHistory.PostChatHistoryService;
import com.yd.yourdoctorandroid.networks.postPaymentHistory.PaymentHistory;
import com.yd.yourdoctorandroid.networks.postPaymentHistory.PaymentResponse;
import com.yd.yourdoctorandroid.networks.postPaymentHistory.PostPaymentHistoryService;
import com.yd.yourdoctorandroid.services.TimeOutChatService;
import com.yd.yourdoctorandroid.utils.Config;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvisoryMenuFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_post)
    Button btn_post;

    @BindView(R.id.tv_name_specialist_choice)
    TextView tvNameSpecialistChoice;

    @BindView(R.id.sp_typeChat)
    Spinner sp_typeChat;

    @BindView(R.id.et_question)
    EditText et_question;

    @BindView(R.id.btn_choose_Doctor)
    Button btn_choose_Doctor;

    @BindView(R.id.iv_item_doctor_chosen)
    ImageView iv_item_doctor_chosen;

    @BindView(R.id.tv_name_doctor_chosen)
    TextView tv_name_doctor_chosen;

    @BindView(R.id.rb_doctorChosen)
    RatingBar rb_doctorChosen;

    @BindView(R.id.tb_main)
    Toolbar tb_main;

    @BindView(R.id.pb_ranking)
    ProgressBar progressBar;

    @BindView(R.id.iv_status_menu)
    ImageView iv_status_menu;

    Unbinder butterKnife;

    DoctorChoiceAdapter doctorChoiceAdapter;
    Doctor doctorChoice;
    Specialist specialistChoice;
    TypeAdvisory typeAdvisoryChoice;

    ArrayList<TypeAdvisory> typeAdvisories;

    private ArrayList<TypeAdvisory> arrlistChatType;
    String[] arrTypeChatAdvisories;

    boolean isChat;

    Patient currentPatient;

    int countProcess;

    Button btn_cancel_choose;
    Button btn_ok_choose;
    RecyclerView rv_list_doctor;

    private List<Doctor> doctorListRecommend;

    public AdvisoryMenuFragment() {
        // Required empty public constructor
    }

    public void setData(Specialist specialist) {
        this.specialistChoice = specialist;
    }

    public void setDoctorChoice(Doctor doctorChoice) {
        this.doctorChoice = doctorChoice;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advisory_menu, container, false);
        // EventBus.getDefault().register(this);
        setupUI(view);
        return view;
    }


    private void setupUI(View view) {
        butterKnife = ButterKnife.bind(AdvisoryMenuFragment.this, view);
        if(specialistChoice != null){
            tvNameSpecialistChoice.setText(specialistChoice.getName());

        }
        btn_post.setOnClickListener(this);
        btn_choose_Doctor.setOnClickListener(this);

        countProcess = 0;
        typeAdvisories = new ArrayList<>();
        isChat = true;
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        tb_main.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tb_main.setTitle(R.string.logo_text_menu_advisory);
        tb_main.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tb_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });
        if (doctorChoice != null) {
            btn_choose_Doctor.setVisibility(View.INVISIBLE);
            tvNameSpecialistChoice.setText("");

            rb_doctorChosen.setVisibility(View.VISIBLE);
            iv_status_menu.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(doctorChoice.getAvatar()).transform(new CropCircleTransformation()).into(iv_item_doctor_chosen);
            tv_name_doctor_chosen.setText(doctorChoice.getFullName());
            rb_doctorChosen.setRating(doctorChoice.getCurrentRating());
            if (doctorChoice.isOnline())
                iv_status_menu.setImageResource(R.drawable.circle_green_line);
        }

        setUpTypeAdvisory();
        sp_typeChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                typeAdvisoryChoice = arrlistChatType.get(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

        if (!SocketUtils.getInstance().checkIsConnected()) {
            Toast.makeText(getContext(), "Không kết nối được máy chủ", Toast.LENGTH_LONG).show();
        } else {
            SocketUtils.getInstance().getSocket().on("getDoctorOnline", GetDoctorOnline);
        }
    }

    private void setUpTypeAdvisory() {
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
        typeAdvisories = (ArrayList<TypeAdvisory>) LoadDefaultModel.getInstance().getTypeAdvisories();

        if (typeAdvisories == null) {
            GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
            getAllTypesAdvisoryService.getMainObjectTypeAdvisories(SharedPrefs.getInstance().get("JWT_TOKEN", String.class)).enqueue(new Callback<MainObjectTypeAdivosry>() {
                @Override
                public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
                    if (response.code() == 200) {
                        MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
                        typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
                        LoadDefaultModel.getInstance().setTypeAdvisories(typeAdvisories);

                        arrlistChatType = new ArrayList<>();
                        for (TypeAdvisory typeAdvisory : typeAdvisories) {
                            if (typeAdvisory.getDescription().contains("Chat")) {
                                arrlistChatType.add(typeAdvisory);
                            }
                        }

                        arrTypeChatAdvisories = new String[arrlistChatType.size()];
                        for (int i = 0; i < arrTypeChatAdvisories.length; i++) {
                            arrTypeChatAdvisories[i] = arrlistChatType.get(i).getName();
                        }
                        setSpinerTypeAdvisory(arrTypeChatAdvisories);
                        typeAdvisoryChoice = typeAdvisories.get(0);

                    } else if (response.code() == 401) {
                        Utils.backToLogin(getActivity().getApplicationContext());
                    }
                    if(progressBar != null){
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
                    if(progressBar != null){
                        progressBar.setVisibility(View.GONE);
                    }
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            arrlistChatType = new ArrayList<>();
            for (TypeAdvisory typeAdvisory : typeAdvisories) {
                if (typeAdvisory.getDescription().contains("Chat")) {
                    arrlistChatType.add(typeAdvisory);
                }
            }

            arrTypeChatAdvisories = new String[arrlistChatType.size()];
            for (int i = 0; i < arrTypeChatAdvisories.length; i++) {
                arrTypeChatAdvisories[i] = arrlistChatType.get(i).getName();
            }
            setSpinerTypeAdvisory(arrTypeChatAdvisories);
            typeAdvisoryChoice = typeAdvisories.get(0);
            if(progressBar != null){
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void setSpinerTypeAdvisory(String[] arrTypeChatAdvisories) {
        ArrayAdapter<String> adapterTypeAdvisories = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        arrTypeChatAdvisories
                );

        adapterTypeAdvisories.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sp_typeChat.setAdapter(adapterTypeAdvisories);
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_Doctor: {
                showDialogChooseDoctor();
                break;
            }
            case R.id.btn_post: {
                if (doctorChoice == null) {
                    Toast.makeText(getContext(), "Bạn cần chọn bác sĩ trước !!!", Toast.LENGTH_LONG).show();
                } else if (et_question.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Bạn cần nhập nội dung câu hỏi !!!", Toast.LENGTH_LONG).show();
                } else if (typeAdvisoryChoice.getPrice() > currentPatient.getRemainMoney()) {
                    Toast.makeText(getContext(), "Số tiền của bạn không đủ để thực hiện cuộc tư vấn !", Toast.LENGTH_LONG).show();
                } else {

                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Xác nhận tạo cuộc tư vấn")
                            .setMessage("Cuộc tư vấn " + typeAdvisoryChoice.getName() + " với BS." + doctorChoice.getFullName() + ". Phí là " + typeAdvisoryChoice.getPrice() + " đ ?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    handlePostRequest();
                                }

                            })
                            .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
                break;
            }

        }
    }

    private void handlePostRequest() {
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }


        PaymentHistory paymentHistoryPatient =
                new PaymentHistory(currentPatient.getId(),
                        typeAdvisoryChoice.getPrice(),
                        currentPatient.getRemainMoney() - typeAdvisoryChoice.getPrice(),
                        typeAdvisoryChoice.getId(),
                        doctorChoice.getDoctorId(),
                        1);


        PostPaymentHistoryService postPaymentHistoryService = RetrofitFactory.getInstance().createService(PostPaymentHistoryService.class);
        postPaymentHistoryService.addPaymentHistory(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), paymentHistoryPatient).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                Toast.makeText(getContext(), "code la" + response.code(), Toast.LENGTH_LONG).show();
                if (response.code() == 200) {
                    PaymentResponse paymentResponse = (PaymentResponse) response.body();
                    ChatHistory chatHistory = new ChatHistory();
                    chatHistory.setContentTopic(et_question.getText().toString());
                    chatHistory.setPatientId(currentPatient.getId());
                    chatHistory.setDoctorId(doctorChoice.getDoctorId());
                    chatHistory.setStatus(1);
                    chatHistory.setTypeAdvisoryID(typeAdvisoryChoice.getId());
                    chatHistory.setPaymentPatientID(paymentResponse.getPaymentsHistory());
                    postHistoryChat(chatHistory);
                    //progressBar.setVisibility(View.GONE);
                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                } else {
                    Toast.makeText(getContext(), "Đã có lỗi xảy ra 1", Toast.LENGTH_LONG).show();
                    if(progressBar != null){
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("anh le error", " remove");
            }
        });

    }

    private void postHistoryChat(ChatHistory chatHistory) {
        PostChatHistoryService postChatHistoryService = RetrofitFactory.getInstance().createService(PostChatHistoryService.class);
        postChatHistoryService.addChatHistory(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), chatHistory).enqueue(new Callback<ChatHistoryResponse>() {
            @Override
            public void onResponse(Call<ChatHistoryResponse> call, Response<ChatHistoryResponse> response) {
                if (response.code() == 200) {
                    ChatHistoryResponse chatHistoryResponse = response.body();
                    currentPatient.setRemainMoney(currentPatient.getRemainMoney() - typeAdvisoryChoice.getPrice());
                    SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                    EventBus.getDefault().post(new EventSend(1));
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("chatHistoryId", chatHistoryResponse.getChatHistory().get_id());
                    intent.putExtra("doctorChoiceId", doctorChoice.getDoctorId());
                    getActivity().startActivity(intent);

                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                } else {
                    Toast.makeText(getContext(), "Đã có lỗi xảy ra 2", Toast.LENGTH_LONG).show();
                    if(progressBar != null){
                        progressBar.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ChatHistoryResponse> call, Throwable t) {
                Log.e("anh le error", " remove");
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    Dialog dialog;
    private ProgressBar pbChoose;
    private TextView tvErrorChoiceInfo;


    public void showDialogChooseDoctor() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_doctor_dialog);
        dialog.setTitle("Lựa Chọn Bác Sĩ của bạn");

        btn_cancel_choose = dialog.findViewById(R.id.btn_cancel_choose_doctor);
        btn_ok_choose = dialog.findViewById(R.id.btn_ok_choose_doctor);
        rv_list_doctor = dialog.findViewById(R.id.rv_list_doctor);
        tvErrorChoiceInfo = dialog.findViewById(R.id.tv_error_choice_info);
        pbChoose = dialog.findViewById(R.id.pb_choose);
        if(tvErrorChoiceInfo != null) tvErrorChoiceInfo.setVisibility(View.GONE);
        if(pbChoose != null)pbChoose.setVisibility(View.VISIBLE);

        GetListRecommentDoctorService getListRecommentDoctorService = RetrofitFactory.getInstance().createService(GetListRecommentDoctorService.class);
        getListRecommentDoctorService.getListRecommentDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), specialistChoice.getId(), currentPatient.getId()).enqueue(new Callback<MainObjectRecommend>() {
            @Override
            public void onResponse(Call<MainObjectRecommend> call, Response<MainObjectRecommend> response) {
                MainObjectRecommend mainObject = response.body();
                doctorListRecommend = new ArrayList<>();
                if (response.code() == 200 && mainObject != null) {
                    List<DoctorRecommend> doctorRecomments = mainObject.getDoctorList();
                    if (doctorRecomments != null && doctorRecomments.size() > 0) {
                        for (DoctorRecommend doctorRecomment : doctorRecomments) {
                            Doctor doctor = new Doctor();
                            doctor.setAvatar(doctorRecomment.getAvatar());
                            doctor.setFirstName(doctorRecomment.getFirstName());
                            doctor.setLastName(doctorRecomment.getLastName());
                            doctor.setMiddleName(doctorRecomment.getMiddleName());
                            doctor.setCurrentRating((float) doctorRecomment.getCurrentRating());
                            doctor.setDoctorId(doctorRecomment.getDoctorId());
                            doctor.setOnline(false);
                            doctorListRecommend.add(doctor);
                            //progressBar.setVisibility(View.GONE);

                        }
                        SocketUtils.getInstance().getSocket().emit("getDoctorOnline");
                    }else {
                        if(tvErrorChoiceInfo != null){
                            tvErrorChoiceInfo.setVisibility(View.VISIBLE);
                            tvErrorChoiceInfo.setText("Không có tìm thấy bác sĩ nào với yêu cầu của bạn!");
                        }

                    }
                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                }else {
                    if(tvErrorChoiceInfo != null){
                        tvErrorChoiceInfo.setVisibility(View.VISIBLE);
                        tvErrorChoiceInfo.setText("Không thể tải được dữ liệu!");
                    }
                }
                if(pbChoose != null) pbChoose.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectRecommend> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                if(pbChoose != null){
                    pbChoose.setVisibility(View.GONE);
                }
            }
        });

        btn_cancel_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(doctorChoiceAdapter != null){
                    doctorChoice = doctorChoiceAdapter.getdoctorChoice();
                }

                if (doctorChoice != null) {
                    rb_doctorChosen.setVisibility(View.VISIBLE);
                    iv_status_menu.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(doctorChoice.getAvatar()).transform(new CropCircleTransformation()).into(iv_item_doctor_chosen);
                    tv_name_doctor_chosen.setText(doctorChoice.getFullName());
                    rb_doctorChosen.setRating(doctorChoice.getCurrentRating());
                    if (doctorChoice.isOnline())
                        iv_status_menu.setImageResource(R.drawable.circle_green_line);
                } else {
                    if(doctorChoiceAdapter != null) Toast.makeText(getContext(), "Bạn chưa chọn bác sĩ nào", Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public Emitter.Listener GetDoctorOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // here you check the value of getActivity() and break up if needed
            if (getActivity() == null) {
                if(pbChoose != null){
                    pbChoose.setVisibility(View.GONE);
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> listIDDoctorOnline = new ArrayList<>();
                        try {
                            JSONArray data = new JSONArray(args[0].toString());

                            for (int i = 0; i < data.length(); i++) {
                                listIDDoctorOnline.add(data.get(i).toString());
                            }

                            if (doctorListRecommend != null) {
                                for (Doctor doctor : doctorListRecommend) {
                                    for (String idDoctorOnline : listIDDoctorOnline) {
                                        if (doctor.getDoctorId().equals(idDoctorOnline)) {
                                            doctor.setOnline(true);
                                            //doctorChoiceAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                }
                                //sort online
                                Collections.sort(doctorListRecommend);
                                doctorChoiceAdapter = new DoctorChoiceAdapter(doctorListRecommend, getContext(), dialog);
                                rv_list_doctor.setAdapter(doctorChoiceAdapter);
                                rv_list_doctor.setLayoutManager(new LinearLayoutManager(getContext()));
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                                rv_list_doctor.addItemDecoration(dividerItemDecoration);
                            }
                            if(pbChoose != null){
                                pbChoose.setVisibility(View.GONE);
                            }

                            //dialog.show();


                        } catch (Exception e) {
                            Log.e("loiListOnlone : ", e.toString());
                            if(pbChoose != null){
                                pbChoose.setVisibility(View.GONE);
                            }
                            //dialog.show();
                        }
                    }
                });
            }


        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //butterKnife.unbind();
        // EventBus.getDefault().unregister(this);
    }
}

