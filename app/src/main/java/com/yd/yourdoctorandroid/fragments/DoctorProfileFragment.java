package com.yd.yourdoctorandroid.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.YourDoctorApplication;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.activities.VideoCallActivity;
import com.yd.yourdoctorandroid.adapters.DoctorCertificationAdapter;
import com.yd.yourdoctorandroid.adapters.DoctorChoiceAdapter;
import com.yd.yourdoctorandroid.adapters.SpecialistChoiceAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.DoctorSocketOnline;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.models.TypeCall;
import com.yd.yourdoctorandroid.models.VideoCallSession;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.AddFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.FavoriteRequest;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.RemoveFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.SpecialistDetail;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.networks.ratingService.MainResponRating;
import com.yd.yourdoctorandroid.networks.ratingService.RatingRequest;
import com.yd.yourdoctorandroid.networks.ratingService.RatingService;
import com.yd.yourdoctorandroid.networks.reportService.MainResponReport;
import com.yd.yourdoctorandroid.networks.reportService.ReportRequest;
import com.yd.yourdoctorandroid.networks.reportService.ReportService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.RxScheduler;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
public class DoctorProfileFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.ivAvaDoctor)
    ImageView ivAvaDoctor;

    @BindView(R.id.tvNameDoctor)
    TextView tvNameDoctor;

    @BindView(R.id.rbDoctorRanking)
    RatingBar rbDoctorRanking;

    @BindView(R.id.tbBackFromProfileDoctor)
    Toolbar tbBackFromProfileDoctor;

    @BindView(R.id.ivRatingDoctor)
    LinearLayout ivRatingDoctor;

    @BindView(R.id.ivChatWithDoctor)
    LinearLayout ivChatWithDoctor;

    @BindView(R.id.ivVideoCallWithDoctor)
    LinearLayout ivVideoCallWithDoctor;

//    @BindView(R.id.ivReportWithDoctor)
//    LinearLayout ivReportWithDoctor;

    @BindView(R.id.rlCertificationDoctor)
    RecyclerView rlCertificationDoctor;

    @BindView(R.id.pbProfileDoctor)
    ProgressBar pbProfileDoctor;

    @BindView(R.id.fabFavorite)
    FloatingActionButton fabFavorite;

    //Info
    @BindView(R.id.radio_male)
    RadioButton radioMale;

    @BindView(R.id.radio_female)
    RadioButton radioFemale;

    @BindView(R.id.radio_other)
    RadioButton radioOther;

    @BindView(R.id.ed_birthday)
    EditText edBirthday;

    @BindView(R.id.ed_address)
    EditText edAddress;

    @BindView(R.id.ed_specilist)
    EditText edSpecilist;

    @BindView(R.id.ed_graduate_place)
    EditText edGraduatePlace;

    @BindView(R.id.ed_working_place)
    EditText edWorkingPlace;

    @BindView(R.id.iv_status_doctor)
    de.hdodenhof.circleimageview.CircleImageView iv_status_doctor;

    private EditText etReasonReport;

    private boolean isFavorite;

    Doctor currentDoctor;
    Patient currentPatient;

    String doctorID;
    Unbinder butterKnife;

    private AlertDialog dialogReport;

    private ProgressBar pbReport;


    private boolean isFromChat;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        setupUI(view);
        return view;
    }

    public void setIsFromChat(boolean isFromChat) {
        this.isFromChat = isFromChat;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    private void setupUI(View view) {

        butterKnife = ButterKnife.bind(DoctorProfileFragment.this, view);

        isFavorite = false;
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbBackFromProfileDoctor);
        final ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Trang cá nhân bác sĩ");

        ivChatWithDoctor.setOnClickListener(this);
       // ivReportWithDoctor.setOnClickListener(this);
        ivVideoCallWithDoctor.setOnClickListener(this);
        fabFavorite.setOnClickListener(this);
        ivRatingDoctor.setOnClickListener(this);

        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        tbBackFromProfileDoctor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventSend(2));
                ScreenManager.backFragment(getFragmentManager());
            }
        });

        if (!SocketUtils.getInstance().checkIsConnected()) {
            Toast.makeText(getContext(), "Không kết nối được máy chủ", Toast.LENGTH_LONG).show();
        } else {
            SocketUtils.getInstance().getSocket().on("getDoctorOnline", GetDoctorOnline);
        }

        GetDoctorDetailService getDoctorDetailService = RetrofitFactory.getInstance().createService(GetDoctorDetailService.class);
        getDoctorDetailService.getMainObjectDoctorDetail(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), doctorID).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {
                if (response.code() == 200) {
                    MainObjectDetailDoctor mainObject = response.body();
                    //TEST
                    Log.e("Anhle ProfileDoctor ", doctorID);
                    Log.e("Anhle ProfileDoctor ", response.body().toString());

                    currentDoctor = new Doctor();

                    currentDoctor.setDoctorId(mainObject.getInformationDoctor().get(0).getDoctorId().get_id());
                    currentDoctor.setFirstName(mainObject.getInformationDoctor().get(0).getDoctorId().getFirstName());
                    currentDoctor.setMiddleName(mainObject.getInformationDoctor().get(0).getDoctorId().getMiddleName());
                    currentDoctor.setLastName(mainObject.getInformationDoctor().get(0).getDoctorId().getLastName());
                    currentDoctor.setAddress(mainObject.getInformationDoctor().get(0).getDoctorId().getAddress());
                    currentDoctor.setAvatar(mainObject.getInformationDoctor().get(0).getDoctorId().getAvatar());
                    currentDoctor.setBirthday(mainObject.getInformationDoctor().get(0).getDoctorId().getBirthday());
                    currentDoctor.setPhoneNumber(mainObject.getInformationDoctor().get(0).getDoctorId().getPhoneNumber());
                    currentDoctor.setPlaceWorking(mainObject.getInformationDoctor().get(0).getPlaceWorking());
                    currentDoctor.setUniversityGraduate(mainObject.getInformationDoctor().get(0).getUniversityGraduate());
                    currentDoctor.setYearGraduate(mainObject.getInformationDoctor().get(0).getYearGraduate());
                    currentDoctor.setCurrentRating(mainObject.getInformationDoctor().get(0).getCurrentRating());
                    currentDoctor.setGender(mainObject.getInformationDoctor().get(0).getDoctorId().getGender());
                    currentDoctor.setCertificates((ArrayList<Certification>) mainObject.getInformationDoctor().get(0).getCertificates());

                    tvNameDoctor.setText(currentDoctor.getFirstName() + " " + currentDoctor.getMiddleName() + " " + currentDoctor.getLastName());
                    //rb_doctorranking.setMax(5);
                    Log.e("rating ", currentDoctor.getCurrentRating() + " ");
                    rbDoctorRanking.setRating(currentDoctor.getCurrentRating());

                    specialistIdChoice = mainObject.getInformationDoctor().get(0).getIdSpecialist().get(0).get_id();

                    String specialist = " ";
                    for (SpecialistDetail specialistDetail : mainObject.getInformationDoctor().get(0).getIdSpecialist()) {
                        //specialist.concat( specialistDetail.getName() + ", ");
                        specialist = specialist + specialistDetail.getName() + ", ";
                    }

                    specialist = specialist.substring(0, specialist.length() - 2);
                    edSpecilist.setText(specialist);
                    edSpecilist.setEnabled(false);

                    ZoomImageViewUtils.loadCircleImage(getContext(), currentDoctor.getAvatar(), ivAvaDoctor);

                    Log.e("currentGender ", currentDoctor.getGender() + "");

                    switch (currentDoctor.getGender()) {
                        case 1: {
                            radioMale.setChecked(true);
                            radioFemale.setChecked(false);
                            radioOther.setChecked(false);

                            radioMale.setEnabled(true);
                            radioFemale.setEnabled(false);
                            radioOther.setEnabled(false);
                            break;
                        }
                        case 2: {
                            radioMale.setChecked(false);
                            radioFemale.setChecked(true);
                            radioOther.setChecked(false);

                            radioMale.setEnabled(false);
                            radioFemale.setEnabled(true);
                            radioOther.setEnabled(false);
                            break;
                        }
                        case 3: {
                            radioMale.setChecked(false);
                            radioFemale.setChecked(true);
                            radioOther.setChecked(false);

                            radioMale.setEnabled(false);
                            radioFemale.setEnabled(false);
                            radioOther.setEnabled(true);
                            break;
                        }
                    }

                    edBirthday.setText(currentDoctor.getBirthday());
                    edBirthday.setEnabled(false);


                    edAddress.setText(currentDoctor.getAddress());
                    edAddress.setEnabled(false);


                    edGraduatePlace.setText(currentDoctor.getUniversityGraduate() + " vào năm " + currentDoctor.getYearGraduate());
                    edGraduatePlace.setEnabled(false);

                    edWorkingPlace.setText(currentDoctor.getPlaceWorking());
                    edWorkingPlace.setEnabled(false);


                    if (currentPatient.getFavoriteDoctors() == null || currentPatient.getFavoriteDoctors().size() == 0) {
                        fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                    } else {
                        for (String idDoctor : currentPatient.getFavoriteDoctors()) {
                            if (idDoctor.compareToIgnoreCase(currentDoctor.getDoctorId()) == 0) {
                                fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                isFavorite = true;
                            }
                        }
                    }

                    rlCertificationDoctor.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    rlCertificationDoctor.setFocusable(false);
                    List<Certification> certificationList = currentDoctor.getCertificates();

                    DoctorCertificationAdapter doctorCertificationAdapter = new DoctorCertificationAdapter(certificationList, getContext());
                    rlCertificationDoctor.setAdapter(doctorCertificationAdapter);
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);

                    SocketUtils.getInstance().getSocket().emit("getDoctorOnline");
                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                }


            }

            @Override
            public void onFailure(Call<MainObjectDetailDoctor> call, Throwable t) {
                Log.e("Anhle P error ", t.toString());
                if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
            }
        });

    }

    public Emitter.Listener GetDoctorOnline = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> listIDDoctorOnline = new ArrayList<>();
                        try {
                            JSONArray data = new JSONArray(args[0].toString());
                            for (int i = 0; i < data.length(); i++) {
                                if (currentDoctor.getDoctorId().equals(data.get(i).toString())) {
                                    currentDoctor.setOnline(true);
                                    iv_status_doctor.setImageResource(R.drawable.circle_green_line);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            Log.e("loiListOnlone : ", e.toString());
                            //dialog.show();
                        }
                    }
                });
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivChatWithDoctor: {
                try{
                    if (!isFromChat) {
                        AdvisoryMenuFragment advisoryMenuFragment = new AdvisoryMenuFragment();
                        advisoryMenuFragment.setDoctorChoice(currentDoctor);
                        ScreenManager.openFragment(getFragmentManager(), advisoryMenuFragment, R.id.rl_container, true, true);
                    }
                }catch (Exception e){

                }

                break;
            }
            case R.id.ivVideoCallWithDoctor: {
                try{
                    if (!isFromChat) {
                        if (currentDoctor.isOnline()) {
                            YourDoctorApplication.self().getSocket().connect();
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("id", "register");
                                obj.put("userId", this.currentPatient.getId());
                                obj.put("name", this.currentPatient.getFullName());
                                obj.put("avatar", this.currentPatient.getAvatar());
                                obj.put("type", 1);
                                YourDoctorApplication.self().getSocket().emit("register", obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getContext(), VideoCallActivity.class);
                            intent.putExtra("idSpecialistChoice", specialistIdChoice);
                            intent.putExtra("idDoctor", currentDoctor.getDoctorId());
                            intent.putExtra("idDoctorName", currentDoctor.getFullName());
                            intent.putExtra("idDoctorAvatar", currentDoctor.getAvatar());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            //loadSpecialist();
                        } else {
                            Toast.makeText(getContext(), "Hiện Tại bác sĩ không online", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (Exception e){

                }


                break;
            }
            case R.id.fabFavorite: {
                handleFabFavorite();
                break;
            }
            case R.id.ivRatingDoctor: {
                handleRating();
                break;
            }
        }
    }

    ArrayList<TypeAdvisory> typeAdvisories;
    TypeAdvisory videoCallType;
    int remainTime;
    ArrayList<Specialist> specialists;
    String specialistIdChoice;
    Specialist specialistChoice;

    private void loadSpecialist() {
        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.VISIBLE);
        specialists = (ArrayList<Specialist>) LoadDefaultModel.getInstance().getSpecialists();
        if (specialists == null) {
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                    if (response.code() == 200) {
                        MainObjectSpecialist mainObjectSpecialist = response.body();
                        specialists = (ArrayList<Specialist>) mainObjectSpecialist.getListSpecialist();
                        LoadDefaultModel.getInstance().setSpecialists(specialists);

                        //dialogChooseSpecialist.show();
                        for (Specialist specialist : specialists) {
                            if (specialist.getId().equals(specialistIdChoice)) {
                                specialistChoice = specialist;
                                break;
                            }
                        }
                        setUpTypeAdvisory();
                    } else {
                        Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();
                        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            for (Specialist specialist : specialists) {
                if (specialist.getId().equals(specialistIdChoice)) {
                    specialistChoice = specialist;
                    break;
                }
            }
            setUpTypeAdvisory();
        }
    }


    private void setUpTypeAdvisory() {

        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.VISIBLE);
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

                        for (TypeAdvisory typeAdvisory : typeAdvisories) {
                            if (typeAdvisory.getName().contains("Video")) {
                                videoCallType = typeAdvisory;
                                break;
                            }
                        }
                        float userMoney = currentPatient.getRemainMoney();
                        remainTime = (int) (userMoney / videoCallType.getPrice());
                        int min = remainTime / 60;
                        int sec = remainTime % 60;
                        Toast.makeText(getActivity(), "Video call có giá là " + videoCallType.getPrice() + "đ/s. Bạn có " + userMoney + "đ trong tải khoản, bạn có thể thực hiện cuộc gọi trong " + min + "m" + sec + "s", Toast.LENGTH_LONG).show();
                        makeCall();
                    } else if (response.code() == 401) {
                        Utils.backToLogin(getActivity().getApplicationContext());
                    }
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
            for (TypeAdvisory typeAdvisory : typeAdvisories) {
                if (typeAdvisory.getName().contains("Video")) {
                    videoCallType = typeAdvisory;
                }
            }
            float userMoney = currentPatient.getRemainMoney();
            remainTime = (int) (userMoney / videoCallType.getPrice());
            int min = remainTime / 60;
            int sec = remainTime % 60;
            Toast.makeText(getActivity(), "Video call có giá là " + videoCallType.getPrice() + "đ/s. Bạn có " + userMoney + "đ trong tải khoản, bạn có thể thực hiện cuộc gọi trong " + min + "m" + sec + "s", Toast.LENGTH_LONG).show();
            makeCall();
            if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);

        }
    }

    public void makeCall() {
        VideoCallSession videoCallSession = new VideoCallSession(currentPatient.getId(), currentPatient.getFullName(), currentDoctor.getDoctorId(), currentDoctor.getFullName(), currentDoctor.getAvatar(), TypeCall.CALL);
        ScreenManager.openFragment(getFragmentManager(), new CallingFragment().setVideoCallSession(videoCallSession, specialistChoice, remainTime, videoCallType), R.id.rl_container, false, true);
    }

    private RatingBar rbRating;
    private ProgressBar pbInfoRating;
    private EditText etCommentRating;

    private void handleRating() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);
        rbRating = view.findViewById(R.id.rb_rating);
        pbInfoRating = view.findViewById(R.id.pb_info_rating);
        etCommentRating = view.findViewById(R.id.et_comment_rating);

        if(pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);

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
                if(pbInfoRating != null) pbInfoRating.setVisibility(View.VISIBLE);

                if (rbRating.getRating() == 0) {
                    Toast.makeText(getContext(), "Bạn nên đánh giá ít nhất 0.5 sao!", Toast.LENGTH_LONG).show();
                    if(pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
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
                                currentDoctor.setCurrentRating(response.body().getNewRating());
                                Log.e("ratingProfi", response.body().getNewRating() + "");
                                rbDoctorRanking.setRating(response.body().getNewRating());
                            } else if (response.code() == 401) {
                                Utils.backToLogin(getActivity().getApplicationContext());
                            }

                            if(pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
                            dialogReport.dismiss();
                        }

                        @Override
                        public void onFailure(Call<MainResponRating> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết máy chủ", Toast.LENGTH_LONG).show();
                            if(pbInfoRating != null) pbInfoRating.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });


    }

    private void handleFabFavorite() {
        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.VISIBLE);
        if (isFavorite) {
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.setDoctorId(currentDoctor.getDoctorId());
            favoriteRequest.setPatientId(currentPatient.getId());
            RemoveFavoriteDoctorService removeFavoriteDoctorService = RetrofitFactory.getInstance().createService(RemoveFavoriteDoctorService.class);
            removeFavoriteDoctorService.addFavoriteDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    if (response.code() == 200) {
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.remove(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                    } else if (response.code() == 401) {
                        Utils.backToLogin(getActivity().getApplicationContext());
                    }

                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " remove");
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                }
            });

        } else {
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.setDoctorId(currentDoctor.getDoctorId());
            favoriteRequest.setPatientId(currentPatient.getId());
            AddFavoriteDoctorService addFavoriteDoctorService = RetrofitFactory.getInstance().createService(AddFavoriteDoctorService.class);
            addFavoriteDoctorService.addFavoriteDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    Log.e("Anh le doctor  ", "post submitted to API." + response.body().toString());
                    if (response.code() == 200) {
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.add(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFavorite = true;
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                    } else if (response.code() == 401) {
                        Utils.backToLogin(getActivity().getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " adding " + t.toString());
                    if(pbProfileDoctor != null) pbProfileDoctor.setVisibility(View.GONE);
                }
            });

        }
    }
}
