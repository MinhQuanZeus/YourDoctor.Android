package com.yd.yourdoctorandroid.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.models.VideoCallSession;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.ratingService.MainResponRating;
import com.yd.yourdoctorandroid.networks.ratingService.RatingRequest;
import com.yd.yourdoctorandroid.networks.ratingService.RatingService;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoneVideoCallFragment extends Fragment {

    private int timeCall;
    private VideoCallSession videoCallSession;
    private TypeAdvisory typeAdvisory;
    private Patient patient;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.rb_rate_doctor)
    RatingBar ratingBar;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_ok)
    CircularProgressButton btnOk;
    Unbinder unbinder;

    public DoneVideoCallFragment setVideoCallSession(VideoCallSession videoCallSession, int timeCall, TypeAdvisory typeAdvisory) {
        this.timeCall = timeCall;
        this.videoCallSession = videoCallSession;
        this.typeAdvisory = typeAdvisory;
        return this;
    }

    public DoneVideoCallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done_video_call, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        patient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        int min = timeCall / 60;
        int sec = timeCall % 60;
        int money = (int) (timeCall * typeAdvisory.getPrice());
        String message = "Bạn vừa kết thúc cuộc trò chuyện với bác sĩ "
                + videoCallSession.getCalleeName() + " trong " + min + "min" + sec + "s. \n"
                + "Tài khoản của quý khách bị trừ -" + money
                + "\n Vui lòng đánh giá bác sĩ " + videoCallSession.getCalleeName() + "!";
        tvDone.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRating();
            }
        });
    }

    private void onRating() {
        btnOk.startAnimation();
        if (ratingBar.getRating() > 0) {
            RatingRequest ratingRequest = new RatingRequest();
            ratingRequest.setComment(etComment.getText().toString());
            ratingRequest.setDoctorId(videoCallSession.getCalleeId());
            ratingRequest.setPatientId(patient.getId());
            ratingRequest.setRating(ratingBar.getRating() + "");

            RatingService ratingService = RetrofitFactory.getInstance().createService(RatingService.class);
            ratingService.ratingService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), ratingRequest).enqueue(new Callback<MainResponRating>() {
                @Override
                public void onResponse(Call<MainResponRating> call, Response<MainResponRating> response) {
                    btnOk.revertAnimation();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }

                @Override
                public void onFailure(Call<MainResponRating> call, Throwable t) {
                    btnOk.revertAnimation();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            });
        }else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
