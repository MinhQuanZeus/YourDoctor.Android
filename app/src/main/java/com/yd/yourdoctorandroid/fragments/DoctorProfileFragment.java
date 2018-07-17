package com.yd.yourdoctorandroid.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorCertificationAdapter;
import com.yd.yourdoctorandroid.events.OnClickDoctorChosen;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.AddFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.FavoriteRequest;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.RemoveFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.SpecialistDetail;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.DoctorRanking;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.GetDoctorRankingSpecialist;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;
import com.yd.yourdoctorandroid.networks.models.Certification;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
public class DoctorProfileFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.iv_ava_doctor)
    ImageView iv_ava_doctor;

    @BindView(R.id.tv_name_doctor)
    TextView tv_name_doctor;

    @BindView(R.id.rb_doctorranking)
    RatingBar rb_doctorranking;

    @BindView(R.id.tb_back_from_profile_doctor)
    Toolbar tb_back_from_profile_doctor;

    @BindView(R.id.iv_chat_with_doctor)
    ImageView iv_chat_with_doctor;

    @BindView(R.id.iv_videocall_with_doctor)
    ImageView iv_videocall_with_doctor;

    @BindView(R.id.iv_report_with_doctor)
    ImageView iv_report_with_doctor;

    @BindView(R.id.rl_certification_doctor)
    RecyclerView rl_certification_doctor;

    @BindView(R.id.tv_introduce_doctor)
    TextView tv_introduce_doctor;

    @BindView(R.id.pb_profile_doctor)
    ProgressBar pb_profile_doctor;

    @BindView(R.id.fab_favorite)
    FloatingActionButton fab_favorite;

    private boolean isFavorite;

    Doctor currentDoctor;
    Patient currentPatient;

    String doctorID ;
    Unbinder butterKnife;

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

    public void setDoctorID(String doctorID){
        this.doctorID = doctorID;
    }

    private void setupUI(View view){

        butterKnife = ButterKnife.bind(DoctorProfileFragment.this, view);

        isFavorite = false;
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_back_from_profile_doctor);
        final ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Trang cá nhân bác sĩ");

        iv_chat_with_doctor.setOnClickListener(this);
        iv_report_with_doctor.setOnClickListener(this);
        iv_videocall_with_doctor.setOnClickListener(this);
        fab_favorite.setOnClickListener(this);
        rb_doctorranking.setOnClickListener(this);

        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        tb_back_from_profile_doctor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });


        GetDoctorDetailService getDoctorDetailService = RetrofitFactory.getInstance().createService(GetDoctorDetailService.class);
        getDoctorDetailService.getMainObjectDoctorDetail(doctorID).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {
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
                currentDoctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
                currentDoctor.setBirthday(mainObject.getInformationDoctor().get(0).getDoctorId().getBirthday());
                currentDoctor.setPhoneNumber(mainObject.getInformationDoctor().get(0).getDoctorId().getPhoneNumber());
                currentDoctor.setPlaceWorking(mainObject.getInformationDoctor().get(0).getPlaceWorking());
                currentDoctor.setUniversityGraduate(mainObject.getInformationDoctor().get(0).getUniversityGraduate());
                currentDoctor.setYearGraduate(mainObject.getInformationDoctor().get(0).getYearGraduate());
                currentDoctor.setCurrentRating(mainObject.getInformationDoctor().get(0).getCurrentRating());
                currentDoctor.setCertificates((ArrayList<Certification>) mainObject.getInformationDoctor().get(0).getCertificates());

                tv_name_doctor.setText(currentDoctor.getFirstName() + " " +currentDoctor.getMiddleName() + " " +currentDoctor.getLastName());
                //rb_doctorranking.setMax(5);
                Log.e("rating ", currentDoctor.getCurrentRating() + " ");
                rb_doctorranking.setRating(currentDoctor.getCurrentRating());


                String specialist = " " ;
                for (SpecialistDetail specialistDetail: mainObject.getInformationDoctor().get(0).getIdSpecialist()) {
                    //specialist.concat( specialistDetail.getName() + ", ");
                    specialist = specialist + specialistDetail.getName() + ", ";
                }

                specialist = specialist.substring(0,specialist.length() -2);


                Picasso.with(getContext()).load("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg").transform(new CropCircleTransformation()).into(iv_ava_doctor);
                Resources res = getResources();
                String text = String.format(res.getString(R.string.introduce_doctor_text), currentDoctor.getBirthday(), currentDoctor.getAddress(), specialist, currentDoctor.getUniversityGraduate()
                        ,currentDoctor.getYearGraduate(), currentDoctor.getPlaceWorking());
                tv_introduce_doctor.setText(text);


                if(currentPatient.getFavoriteDoctors() == null || currentPatient.getFavoriteDoctors().size() == 0){
                    fab_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    isFavorite = false;
                }else {
                    for(String idDoctor: currentPatient.getFavoriteDoctors()){
                        if(idDoctor.compareToIgnoreCase(currentDoctor.getDoctorId())==0){
                            fab_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            isFavorite = true;
                        }
                    }
                }

                rl_certification_doctor.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rl_certification_doctor.setFocusable(false);
                List<Certification> certificationList = currentDoctor.getCertificates();

                //Test
                for(int i = 0; i < certificationList.size() ; i++){
                    certificationList.get(i).setPathImage("http://bacsilanda.com.vn/wp-content/uploads/2016/11/Giay-chung-nhan-y-hoc-va-vat-ly-tri-lieu.jpg");
                }
                //Test

                DoctorCertificationAdapter doctorCertificationAdapter = new DoctorCertificationAdapter(certificationList,getContext());
                rl_certification_doctor.setAdapter(doctorCertificationAdapter);
                pb_profile_doctor.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectDetailDoctor> call, Throwable t) {
                Log.e("Anhle P error ", t.toString());
                pb_profile_doctor.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }

    @Override
    public void onClick(View view) {
        switch  (view.getId()){
            case R.id.iv_chat_with_doctor:{

                break;
            }
            case R.id.iv_report_with_doctor:{
                break;
            }
            case R.id.iv_videocall_with_doctor:{
                break;
            }
            case R.id.fab_favorite:{
                handleFabFavorite();
                break;
            }
            case R.id.rb_doctorranking:{
                break;
            }
        }
    }

    private void handleFabFavorite(){
        pb_profile_doctor.setVisibility(View.VISIBLE);
        if(isFavorite){
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.doctorId = currentDoctor.getDoctorId();
            favoriteRequest.patientId = currentPatient.getId();
            RemoveFavoriteDoctorService removeFavoriteDoctorService = RetrofitFactory.getInstance().createService(RemoveFavoriteDoctorService.class);
            removeFavoriteDoctorService.addFavoriteDoctor(favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    if(response.code() == 200){
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.remove(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        fab_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                        pb_profile_doctor.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " remove");
                    pb_profile_doctor.setVisibility(View.GONE);
                }
            });

        }else {
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.doctorId = currentDoctor.getDoctorId();
            favoriteRequest.patientId = currentPatient.getId();
            AddFavoriteDoctorService addFavoriteDoctorService = RetrofitFactory.getInstance().createService(AddFavoriteDoctorService.class);
            addFavoriteDoctorService.addFavoriteDoctor(favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    Log.e("Anh le doctor  ", "post submitted to API." + response.body().toString());
                    if(response.code() == 200) {
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.add(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        fab_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFavorite = true;
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        pb_profile_doctor.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " adding " +  t.toString());
                    pb_profile_doctor.setVisibility(View.GONE);
                }
            });

        }
    }
}
