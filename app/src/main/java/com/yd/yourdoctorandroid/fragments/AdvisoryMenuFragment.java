package com.yd.yourdoctorandroid.fragments;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.ChatActivity;
import com.yd.yourdoctorandroid.adapters.DoctorChoiceAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
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
import com.yd.yourdoctorandroid.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
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

    @BindView(R.id.rb_choose_chat)
    RadioButton rb_choose_chat;

    @BindView(R.id.rb_choose_video_call)
    RadioButton rb_choose_video_call;

    @BindView(R.id.rl_chat)
    RelativeLayout rl_chat;

    @BindView(R.id.btn_post)
    Button btn_post;

    @BindView(R.id.sp_speclist)
    Spinner sp_speclist;

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

    Unbinder butterKnife;

    DoctorChoiceAdapter doctorChoiceAdapter;
    Doctor doctorChoice;
    Specialist specialistChoice;
    TypeAdvisory typeAdvisoryChoice;

    ArrayList<Specialist> spectlists;

    ArrayList<TypeAdvisory> typeAdvisories;

    String[] arrTypeChatAdvisories;
    String[] arrTypeVideoCallAdvisories;
    String arrayspecialists[];

    boolean isChat;

    Patient currentPatient;

    int countProcess;

    Button btn_cancel_choose;
    Button btn_ok_choose;
    RecyclerView rv_list_doctor;

    public AdvisoryMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advisory_menu, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        butterKnife = ButterKnife.bind(AdvisoryMenuFragment.this, view);
        rb_choose_chat.setOnClickListener(this);
        rb_choose_video_call.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_choose_Doctor.setOnClickListener(this);

        countProcess = 0;
        spectlists = new ArrayList<Specialist>();
        typeAdvisories = new ArrayList<TypeAdvisory>();
        isChat = true;
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb_main);
        final ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle(R.string.logo_text_menu_advisory);

        tb_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());

            }
        });


        setUpSpecialists();
        sp_speclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                for (int i = 0; i < spectlists.size(); i++) {
                    if (spectlists.get(i).getName().equals(arrayspecialists[pos])) {
                        specialistChoice = spectlists.get(i);
                        break;
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

        setUpTypeAdvisory();
        sp_typeChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (isChat) {
                    for (int i = 0; i < typeAdvisories.size(); i++) {
                        if (typeAdvisories.get(i).getName().equals(arrTypeChatAdvisories[pos])) {
                            typeAdvisoryChoice = typeAdvisories.get(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < typeAdvisories.size(); i++) {
                        if (typeAdvisories.get(i).getName().equals(arrTypeVideoCallAdvisories[pos])) {
                            typeAdvisoryChoice = typeAdvisories.get(i);
                            break;
                        }
                    }
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });


    }

    private void setUpSpecialists() {
        spectlists = (ArrayList<Specialist>) LoadDefaultModel.getInstance().getSpecialists();

        if (spectlists == null) {
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {

                    if (response.code() == 200) {
                        Log.e("AnhLe", "success: " + response.body());
                        MainObjectSpecialist mainObjectSpecialist = response.body();
                        spectlists = (ArrayList<Specialist>) mainObjectSpecialist.getListSpecialist();
                        arrayspecialists = new String[spectlists.size()];

                        for (int i = 0; i < arrayspecialists.length; i++) {
                            arrayspecialists[i] = spectlists.get(i).getName();
                        }

                        setSpinerSpecialist(arrayspecialists);
                        specialistChoice = spectlists.get(0);
                        checkInvisible();

                    } else if (response.code() == 401) {
                        Utils.backToLogin(getContext());
                    }

                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    checkInvisible();
                }
            });

        } else {
            arrayspecialists = new String[spectlists.size()];

            for (int i = 0; i < arrayspecialists.length; i++) {
                arrayspecialists[i] = spectlists.get(i).getName();
            }

            setSpinerSpecialist(arrayspecialists);
            specialistChoice = spectlists.get(0);
            checkInvisible();
        }

    }

    private void setUpTypeAdvisory() {

        typeAdvisories = (ArrayList<TypeAdvisory>) LoadDefaultModel.getInstance().getTypeAdvisories();

        if (typeAdvisories == null) {
            GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
            getAllTypesAdvisoryService.getMainObjectTypeAdvisories(SharedPrefs.getInstance().get("JWT_TOKEN", String.class)).enqueue(new Callback<MainObjectTypeAdivosry>() {
                @Override
                public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
                    if (response.code() == 200) {
                        Log.e("AnhLe", "success: " + response.body());
                        MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
                        typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
                        ArrayList<TypeAdvisory> arrlistChatType = new ArrayList<>();
                        ArrayList<TypeAdvisory> arrlistVideoCall = new ArrayList<>();
                        for (TypeAdvisory typeAdvisory : typeAdvisories) {
                            if (typeAdvisory.getName().contains("Video")) {
                                arrlistVideoCall.add(typeAdvisory);
                            } else {
                                arrlistChatType.add(typeAdvisory);
                            }
                        }

                        arrTypeChatAdvisories = new String[arrlistChatType.size()];
                        for (int i = 0; i < arrTypeChatAdvisories.length; i++) {
                            arrTypeChatAdvisories[i] = arrlistChatType.get(i).getName();
                        }

                        arrTypeVideoCallAdvisories = new String[arrlistVideoCall.size()];
                        for (int i = 0; i < arrTypeVideoCallAdvisories.length; i++) {
                            arrTypeVideoCallAdvisories[i] = arrlistVideoCall.get(i).getName();
                        }
                        setSpinerTypeAdvisory(arrTypeChatAdvisories);
                        typeAdvisoryChoice = typeAdvisories.get(0);
                        checkInvisible();

                    } else if (response.code() == 401) {
                        Utils.backToLogin(getContext());
                    }


                }

                @Override
                public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    checkInvisible();
                }
            });
        } else {
            ArrayList<TypeAdvisory> arrlistChatType = new ArrayList<>();
            ArrayList<TypeAdvisory> arrlistVideoCall = new ArrayList<>();
            for (TypeAdvisory typeAdvisory : typeAdvisories) {
                if (typeAdvisory.getName().contains("Video")) {
                    arrlistVideoCall.add(typeAdvisory);
                } else {
                    arrlistChatType.add(typeAdvisory);
                }
            }

            arrTypeChatAdvisories = new String[arrlistChatType.size()];
            for (int i = 0; i < arrTypeChatAdvisories.length; i++) {
                arrTypeChatAdvisories[i] = arrlistChatType.get(i).getName();
            }

            arrTypeVideoCallAdvisories = new String[arrlistVideoCall.size()];
            for (int i = 0; i < arrTypeVideoCallAdvisories.length; i++) {
                arrTypeVideoCallAdvisories[i] = arrlistVideoCall.get(i).getName();
            }
            setSpinerTypeAdvisory(arrTypeChatAdvisories);
            typeAdvisoryChoice = typeAdvisories.get(0);
            checkInvisible();
        }


    }

    private void checkInvisible() {
        countProcess++;
        if (countProcess == 2) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setSpinerSpecialist(String[] arrSpecilists) {
        ArrayAdapter<String> adapterSpeclist = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        arrSpecilists
                );

        adapterSpeclist.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sp_speclist.setAdapter(adapterSpeclist);

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
        progressBar.setVisibility(View.GONE);

    }

    ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_choose_chat:
                if (((RadioButton) v).isChecked()) {
                    isChat = true;
                    rl_chat.setVisibility(View.VISIBLE);
                    setSpinerTypeAdvisory(arrTypeChatAdvisories);
                    //  rl_videocall.setVisibility(View.INVISIBLE);
                    if (typeAdvisories.size() != 0) {
                        for (int i = 0; i < typeAdvisories.size(); i++) {
                            if (typeAdvisories.get(i).getName().equals(arrTypeChatAdvisories[0])) {
                                typeAdvisoryChoice = typeAdvisories.get(i);
                                break;
                            }
                        }
                    }
                }

                break;
            case R.id.rb_choose_video_call:
                if (((RadioButton) v).isChecked()) {
                    isChat = false;
                    rl_chat.setVisibility(View.INVISIBLE);
                    setSpinerTypeAdvisory(arrTypeVideoCallAdvisories);
                    // rl_videocall.setVisibility(View.VISIBLE);
                    if (typeAdvisories.size() != 0) {
                        for (int i = 0; i < typeAdvisories.size(); i++) {
                            if (typeAdvisories.get(i).getName().equals(arrTypeVideoCallAdvisories[0])) {
                                typeAdvisoryChoice = typeAdvisories.get(i);
                                break;
                            }
                        }
                    }
                }
                break;
            case R.id.btn_choose_Doctor: {
                showDialogChooseDoctor();
                break;
            }
            case R.id.btn_post: {
                // Toast.makeText(getContext(),"specialist " + specialistChoice.getName() + ", " + " Typespecialist " + typeAdvisoryChoice.getName() , Toast.LENGTH_LONG).show();
                if (doctorChoice == null) {
                    Toast.makeText(getContext(), "Bạn cần chọn bác sĩ trước !!!", Toast.LENGTH_LONG).show();
                } else if (isChat && et_question.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Bạn cần nhập nội dung câu hỏi !!!", Toast.LENGTH_LONG).show();
                } else {
                    handlePostRequest();
                }
                break;
            }

        }
    }

    private void handlePostRequest() {
        progressBar.setVisibility(View.VISIBLE);

        if (isChat) {
            PaymentHistory paymentHistoryPatient =
                    new PaymentHistory(currentPatient.getId(),
                            typeAdvisoryChoice.getPrice(),
                            currentPatient.getRemainMoney() - typeAdvisoryChoice.getPrice(),
                            typeAdvisoryChoice.getId(),
                            doctorChoice.getDoctorId(),
                            1);


            Log.e("Payment Patient ", paymentHistoryPatient.toString());
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
                        Utils.backToLogin(getContext());
                    } else {
                        Toast.makeText(getContext(), "Đã có lỗi xảy ra 1", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<PaymentResponse> call, Throwable t) {
                    Log.e("anh le error", " remove");
                    //progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    private void postHistoryChat(ChatHistory chatHistory) {
        PostChatHistoryService postChatHistoryService = RetrofitFactory.getInstance().createService(PostChatHistoryService.class);
        postChatHistoryService.addChatHistory(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), chatHistory).enqueue(new Callback<ChatHistoryResponse>() {
            @Override
            public void onResponse(Call<ChatHistoryResponse> call, Response<ChatHistoryResponse> response) {
                if (response.code() == 200) {
                    ChatHistoryResponse chatHistoryResponse = (ChatHistoryResponse) response.body();

                    currentPatient.setRemainMoney(currentPatient.getRemainMoney() - typeAdvisoryChoice.getPrice());
                    SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                    progressBar.setVisibility(View.GONE);
                    Intent intentTimeOut = new Intent(getContext(), TimeOutChatService.class);
                    intentTimeOut.putExtra("idChat", chatHistoryResponse.getChatHistory().get_id());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 234324243, intentTimeOut, 0);
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                            + (Config.TIME_OUT_CHAT_CONVERSATION * 1000), pendingIntent);

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("chatHistoryId", chatHistoryResponse.getChatHistory().get_id());
                    intent.putExtra("doctorChoiceId", doctorChoice.getDoctorId());
                    getActivity().startActivity(intent);

                } else if (response.code() == 401) {
                    Utils.backToLogin(getContext());
                } else {
                    Toast.makeText(getContext(), "Đã có lỗi xảy ra 2", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ChatHistoryResponse> call, Throwable t) {
                Log.e("anh le error", " remove");
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public void showDialogChooseDoctor() {
        progressBar.setVisibility(View.VISIBLE);
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_doctor_dialog);
        dialog.setTitle("Lựa Chọn Bác Sĩ của bạn");

        // set the custom dialog components - text, image and button
        btn_cancel_choose = dialog.findViewById(R.id.btn_cancel_choose_doctor);
        btn_ok_choose = dialog.findViewById(R.id.btn_ok_choose_doctor);
        rv_list_doctor = dialog.findViewById(R.id.rv_list_doctor);


        GetListRecommentDoctorService getListRecommentDoctorService = RetrofitFactory.getInstance().createService(GetListRecommentDoctorService.class);
        getListRecommentDoctorService.getListRecommentDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), specialistChoice.getId(), currentPatient.getId()).enqueue(new Callback<MainObjectRecommend>() {
            @Override
            public void onResponse(Call<MainObjectRecommend> call, Response<MainObjectRecommend> response) {
                MainObjectRecommend mainObject = response.body();
                List<Doctor> doctorList = new ArrayList<>();
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
                            doctorList.add(doctor);
                        }
                        doctorChoiceAdapter = new DoctorChoiceAdapter(doctorList, getContext(), dialog);
                        rv_list_doctor.setAdapter(doctorChoiceAdapter);
                        rv_list_doctor.setLayoutManager(new LinearLayoutManager(getContext()));
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                        rv_list_doctor.addItemDecoration(dividerItemDecoration);


                    }
                } else if (response.code() == 401) {
                    Utils.backToLogin(getContext());
                }


                progressBar.setVisibility(View.GONE);
                dialog.show();
            }

            @Override
            public void onFailure(Call<MainObjectRecommend> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                dialog.show();
            }
        });


        // if button is clicked, close the custom dialog
        btn_cancel_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });

        btn_ok_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorChoice = doctorChoiceAdapter.getdoctorChoice();
                if (doctorChoice != null) {

                    Picasso.with(getContext()).load(doctorChoice.getAvatar()).transform(new CropCircleTransformation()).into(iv_item_doctor_chosen);
                    tv_name_doctor_chosen.setText(doctorChoice.getFirstName() + " " + doctorChoice.getMiddleName() + " " + doctorChoice.getLastName());
                    rb_doctorChosen.setRating(doctorChoice.getCurrentRating());
                }
                dialog.dismiss();

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }
}

