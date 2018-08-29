package com.yd.yourdoctorandroid.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.YourDoctorApplication;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.adapters.DoctorVideoCallAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.DoctorSocketOnline;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.models.TypeCall;
import com.yd.yourdoctorandroid.models.VideoCallSession;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.doctorsvideocall.DoctorsBySpecialist;
import com.yd.yourdoctorandroid.networks.doctorsvideocall.GetDoctorsBySpecialistService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.utils.RxScheduler;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
public class VideoCallFragment extends Fragment {
    public static final String TAG = "VideoCallFragment";

    private Specialist specialist;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_doctors)
    RecyclerView rvDoctors;
    @BindView(R.id.iv_special)
    ImageView ivSpecialist;
    @BindView(R.id.rl_blank_user)
    RelativeLayout rlBlankUser;

    private List<Doctor> doctorList = new ArrayList<>();
    private List<Doctor> onlineDoctors = new ArrayList<>();
    private List<DoctorSocketOnline> doctorSocketOnlines = new ArrayList<DoctorSocketOnline>();
    DoctorVideoCallAdapter doctorVideoCallAdapter;
    private Unbinder unbinder;
    private Patient userInfo;
    private Socket socket;
    private List<TypeAdvisory> typeAdvisories;
    private TypeAdvisory videoCallTypeAdvisory;
    private int remainTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_call, container, false);
        init(view);
        loadData();
        loadTypeAdvisory();
        setupSocket();
        return view;
    }

    private void setupSocket() {
        socket = YourDoctorApplication.self().getSocket();
        socket.on("getDoctorOnline", getDoctorOnline);
        socket.connect();
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", "register");
            obj.put("userId", this.userInfo.getId());
            obj.put("name", this.userInfo.getFullName());
            obj.put("avatar", this.userInfo.getAvatar());
            obj.put("type", 1);
            socket.emit("register", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener getDoctorOnline = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            RxScheduler.runOnUi(o -> {
                try {
                    JSONObject data = new JSONObject(args[0].toString());
                    Log.d(TAG, data.toString());
                    Iterator<String> iter = data.keys();
                    doctorSocketOnlines = new ArrayList<DoctorSocketOnline>();
                    onlineDoctors = new ArrayList<>();
                    Log.d(TAG, doctorSocketOnlines.toString());
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {
                            String doctorId = data.getString(key);
                            doctorSocketOnlines.add(new DoctorSocketOnline(doctorId, key));
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                    if (doctorList != null && doctorList.size() > 0 && doctorSocketOnlines != null && doctorSocketOnlines.size() > 0) {
                        for (Doctor doctor : doctorList) {
                            for (DoctorSocketOnline doctorSocketOnline : doctorSocketOnlines) {
                                if (doctor.getDoctorId().equals(doctorSocketOnline.getDoctorId())) {
                                    doctor.setOnline(true);
                                    onlineDoctors.add(doctor);
                                } else {
                                    doctor.setOnline(false);
                                }
                            }
                        }
                    } else if (doctorList != null && doctorList.size() > 0) {
                        for (Doctor doctor : doctorList) {
                            doctor.setOnline(false);
                        }
                    }
                    Collections.sort(onlineDoctors, new Comparator<Doctor>() {
                        @Override
                        public int compare(Doctor o1, Doctor o2) {
                            int c;
                            c = (o1.isFavorite() == o2.isFavorite()) ? 0 : (o1.isFavorite() ? -1 : 0);
                            if (c == 0) {
                                c = o1.getCurrentRating() == o2.getCurrentRating() ? 0 : o1.getCurrentRating() > o2.getCurrentRating() ? -1 : 0;
                            }
                            return c;
                        }
                    });
                    if (onlineDoctors.size() > 0) {
                        rlBlankUser.setVisibility(View.GONE);
                    } else {
                        rlBlankUser.setVisibility(View.VISIBLE);
                    }
                    doctorVideoCallAdapter.swap(onlineDoctors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        userInfo = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        rlBlankUser.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(this.specialist.getName());
        Picasso.with(getContext()).load(specialist.getImage()).into(ivSpecialist);
        setUpDoctorList();
    }

    private void setUpDoctorList() {
        doctorVideoCallAdapter = new DoctorVideoCallAdapter(onlineDoctors, getContext());
        doctorVideoCallAdapter.setOnItemClickListner(new DoctorVideoCallAdapter.onItemClickListner() {
            @Override
            public void onCall(String calleeId, String calleename, String calleeAvatar) {
                makeCall(calleeId, calleename, calleeAvatar);
            }
        });
        rvDoctors.setAdapter(doctorVideoCallAdapter);
        rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvDoctors.addItemDecoration(dividerItemDecoration);
    }


    public VideoCallFragment setSpecialist(Specialist specialist) {
        if (specialist != null) {
            this.specialist = specialist;
        } else {
            this.specialist = new Specialist();
            this.specialist.setId("5b6d4e316f0e1b129ca48588");
            this.specialist.setName("Dinh dưỡng");
            this.specialist.setImage("https://s3-ap-southeast-1.amazonaws.com/yourdoctor-assets-public/dinhduong.png");
        }
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void loadData() {
        GetDoctorsBySpecialistService getDoctorsBySpecialistService = RetrofitFactory.getInstance().createService(GetDoctorsBySpecialistService.class);
        getDoctorsBySpecialistService.getDoctorsBySpecialist(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), specialist.getId(), userInfo.getId()).enqueue(new Callback<DoctorsBySpecialist>() {
            @Override
            public void onResponse(Call<DoctorsBySpecialist> call, Response<DoctorsBySpecialist> response) {
                DoctorsBySpecialist mainObject = response.body();

                doctorList = new ArrayList<>();
                if (response.code() == 200 && mainObject != null) {
                    List<Doctor> doctorsBySpecialist = mainObject.getDoctorList();
                    if (doctorsBySpecialist != null && doctorsBySpecialist.size() > 0) {
                        for (Doctor doctorRecomment : doctorsBySpecialist) {
                            Doctor doctor = new Doctor();
                            doctor.setAvatar(doctorRecomment.getAvatar());
                            doctor.setFirstName(doctorRecomment.getFirstName());
                            doctor.setLastName(doctorRecomment.getLastName());
                            doctor.setMiddleName(doctorRecomment.getMiddleName());
                            doctor.setCurrentRating((float) doctorRecomment.getCurrentRating());
                            doctor.setDoctorId(doctorRecomment.getDoctorId());
                            doctorList.add(doctor);
                        }
                        socket.emit("getDoctorOnline");
                    }
                }
            }

            @Override
            public void onFailure(Call<DoctorsBySpecialist> call, Throwable t) {

            }
        });
    }

    public void makeCall(String calleeId, String calleename, String calleeAvatar) {
        VideoCallSession videoCallSession = new VideoCallSession(userInfo.getId(), userInfo.getFullName(), calleeId, calleename, calleeAvatar, TypeCall.CALL);
        ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new CallingFragment().setVideoCallSession(videoCallSession, specialist, remainTime, videoCallTypeAdvisory), R.id.fl_video_call, false, true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadTypeAdvisory() {
        GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
        getAllTypesAdvisoryService.getMainObjectTypeAdvisories(SharedPrefs.getInstance().get("JWT_TOKEN", String.class)).enqueue(new Callback<MainObjectTypeAdivosry>() {
            @Override
            public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
                if (response.code() == 200) {
                    MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
                    typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
                    if (typeAdvisories != null && typeAdvisories.size() > 0) {
                        for (TypeAdvisory typeAdvisory : typeAdvisories) {
                            if (typeAdvisory.getName().toLowerCase().contains("video call")) {
                                videoCallTypeAdvisory = typeAdvisory;
                                float userMoney = userInfo.getRemainMoney();
                                remainTime = (int) (userMoney / videoCallTypeAdvisory.getPrice());
                                int min = remainTime / 60;
                                int sec = remainTime % 60;
                                Toast.makeText(getActivity(), "Video call có giá là " + videoCallTypeAdvisory.getPrice() + "đ/s. Bạn có " + userMoney + "đ trong tải khoản, bạn có thể thực hiện cuộc gọi trong " + min + "m" + sec + "s", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
            }
        });
    }
}
