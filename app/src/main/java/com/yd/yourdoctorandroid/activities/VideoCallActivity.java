package com.yd.yourdoctorandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nhancv.npermission.NPermission;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.CallingFragment;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.fragments.VideoCallFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.models.TypeCall;
import com.yd.yourdoctorandroid.models.VideoCallSession;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCallActivity extends AppCompatActivity {
    private String idSpecialist;
    private String idDoctor;
    private String idDoctorName;
    private String idDoctorAvatar;
    private ProgressBar pbVideoCallMain;
    private Patient currentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        pbVideoCallMain =  findViewById(R.id.pb_video_call_main);
        Intent intent = getIntent();
        idSpecialist = intent.getStringExtra("idSpecialistChoice");
        idDoctor = intent.getStringExtra("idDoctor");
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        if(idDoctor == null){
            pbVideoCallMain.setVisibility(View.GONE);
            for (Specialist specialist: LoadDefaultModel.getInstance().getSpecialists()) {
                if(specialist.getId().equals(idSpecialist)){
                    ScreenManager.openFragment(getSupportFragmentManager(), new VideoCallFragment().setSpecialist(specialist), R.id.fl_video_call, false, true);
                    break;
                }
            }
        }else {

             idDoctorName = intent.getStringExtra("idDoctorName");
             idDoctorAvatar = intent.getStringExtra("idDoctorAvatar");
            loadSpecialist();

        }

    }

    ArrayList<TypeAdvisory> typeAdvisories;
    TypeAdvisory videoCallType;
    int remainTime;
    ArrayList<Specialist> specialists;
    String specialistIdChoice;
    Specialist specialistChoice;

    private void loadSpecialist() {
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
                        Toast.makeText(getApplicationContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();
                        if(pbVideoCallMain != null) pbVideoCallMain.setVisibility(View.GONE);
                        onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    if(pbVideoCallMain != null) pbVideoCallMain.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();
                    onBackPressed();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setUpTypeAdvisory() {

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
                        Toast.makeText(getApplicationContext(), "Video call có giá là " + videoCallType.getPrice() + "đ/s. Bạn có " + userMoney + "đ trong tải khoản, bạn có thể thực hiện cuộc gọi trong " + min + "m" + sec + "s", Toast.LENGTH_LONG).show();
                        makeCall();
                    } else if (response.code() == 401) {
                        Utils.backToLogin(getApplicationContext());
                    }else {
                        onBackPressed();
                    }
                    if(pbVideoCallMain != null) pbVideoCallMain.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
                    if(pbVideoCallMain != null) pbVideoCallMain.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            });
        } else {
            if(pbVideoCallMain != null) pbVideoCallMain.setVisibility(View.GONE);
            for (TypeAdvisory typeAdvisory : typeAdvisories) {
                if (typeAdvisory.getName().contains("Video")) {
                    videoCallType = typeAdvisory;
                }
            }
            float userMoney = currentPatient.getRemainMoney();
            remainTime = (int) (userMoney / videoCallType.getPrice());
            int min = remainTime / 60;
            int sec = remainTime % 60;
            Toast.makeText(getApplicationContext(), "Video call có giá là " + videoCallType.getPrice() + "đ/s. Bạn có " + userMoney + "đ trong tải khoản, bạn có thể thực hiện cuộc gọi trong " + min + "m" + sec + "s", Toast.LENGTH_LONG).show();
            makeCall();

        }
    }

    public void makeCall() {
        if(currentPatient == null) onBackPressed();
        VideoCallSession videoCallSession = new VideoCallSession(currentPatient.getId(), currentPatient.getFullName(), idDoctor, idDoctorName, idDoctorAvatar, TypeCall.CALL);
        ScreenManager.openFragment(getSupportFragmentManager(), new CallingFragment().setVideoCallSession(videoCallSession, specialistChoice, remainTime, videoCallType), R.id.fl_video_call, false, true);
    }

}
