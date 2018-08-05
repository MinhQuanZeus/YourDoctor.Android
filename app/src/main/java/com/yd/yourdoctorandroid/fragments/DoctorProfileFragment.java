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
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.AddFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.FavoriteRequest;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.RemoveFavoriteDoctorService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.GetDoctorDetailService;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.SpecialistDetail;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

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

    @BindView(R.id.ivAvaDoctor)
    ImageView ivAvaDoctor;

    @BindView(R.id.tvNameDoctor)
    TextView tvNameDoctor;

    @BindView(R.id.rbDoctorRanking)
    RatingBar rbDoctorRanking;

    @BindView(R.id.tbBackFromProfileDoctor)
    Toolbar tbBackFromProfileDoctor;

    @BindView(R.id.ivChatWithDoctor)
    ImageView ivChatWithDoctor;

    @BindView(R.id.ivVideoCallWithDoctor)
    ImageView ivVideoCallWithDoctor;

    @BindView(R.id.ivReportWithDoctor)
    ImageView ivReportWithDoctor;

    @BindView(R.id.rlCertificationDoctor)
    RecyclerView rlCertificationDoctor;

    @BindView(R.id.tvIntroduceDoctor)
    TextView tvIntroduceDoctor;

    @BindView(R.id.pbProfileDoctor)
    ProgressBar pbProfileDoctor;

    @BindView(R.id.fabFavorite)
    FloatingActionButton fabFavorite;

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
        ((AppCompatActivity)getActivity()).setSupportActionBar(tbBackFromProfileDoctor);
        final ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Trang cá nhân bác sĩ");

        ivChatWithDoctor.setOnClickListener(this);
        ivReportWithDoctor.setOnClickListener(this);
        ivVideoCallWithDoctor.setOnClickListener(this);
        fabFavorite.setOnClickListener(this);
        rbDoctorRanking.setOnClickListener(this);

        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        tbBackFromProfileDoctor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });


        GetDoctorDetailService getDoctorDetailService = RetrofitFactory.getInstance().createService(GetDoctorDetailService.class);
        getDoctorDetailService.getMainObjectDoctorDetail(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),doctorID).enqueue(new Callback<MainObjectDetailDoctor>() {
            @Override
            public void onResponse(Call<MainObjectDetailDoctor> call, Response<MainObjectDetailDoctor> response) {
                if(response.code() == 200){
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
                    currentDoctor.setCertificates((ArrayList<Certification>) mainObject.getInformationDoctor().get(0).getCertificates());

                    tvNameDoctor.setText(currentDoctor.getFirstName() + " " +currentDoctor.getMiddleName() + " " +currentDoctor.getLastName());
                    //rb_doctorranking.setMax(5);
                    Log.e("rating ", currentDoctor.getCurrentRating() + " ");
                    rbDoctorRanking.setRating(currentDoctor.getCurrentRating());


                    String specialist = " " ;
                    for (SpecialistDetail specialistDetail: mainObject.getInformationDoctor().get(0).getIdSpecialist()) {
                        //specialist.concat( specialistDetail.getName() + ", ");
                        specialist = specialist + specialistDetail.getName() + ", ";
                    }

                    specialist = specialist.substring(0,specialist.length() -2);

                    ZoomImageViewUtils.loadCircleImage(getContext(),currentDoctor.getAvatar(),ivAvaDoctor);
                    Resources res = getResources();
                    String text = String.format(res.getString(R.string.introduce_doctor_text), currentDoctor.getBirthday(), currentDoctor.getAddress(), specialist, currentDoctor.getUniversityGraduate()
                            ,currentDoctor.getYearGraduate(), currentDoctor.getPlaceWorking());
                    tvIntroduceDoctor.setText(text);


                    if(currentPatient.getFavoriteDoctors() == null || currentPatient.getFavoriteDoctors().size() == 0){
                        fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                    }else {
                        for(String idDoctor: currentPatient.getFavoriteDoctors()){
                            if(idDoctor.compareToIgnoreCase(currentDoctor.getDoctorId())==0){
                                fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                isFavorite = true;
                            }
                        }
                    }

                    rlCertificationDoctor.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    rlCertificationDoctor.setFocusable(false);
                    List<Certification> certificationList = currentDoctor.getCertificates();

                    //Test
                    for(int i = 0; i < certificationList.size() ; i++){
                        certificationList.get(i).setPathImage("http://bacsilanda.com.vn/wp-content/uploads/2016/11/Giay-chung-nhan-y-hoc-va-vat-ly-tri-lieu.jpg");
                    }
                    //Test

                    DoctorCertificationAdapter doctorCertificationAdapter = new DoctorCertificationAdapter(certificationList,getContext());
                    rlCertificationDoctor.setAdapter(doctorCertificationAdapter);
                    pbProfileDoctor.setVisibility(View.GONE);
                }else if(response.code() == 401){
                    Utils.backToLogin(getContext());
                }


            }

            @Override
            public void onFailure(Call<MainObjectDetailDoctor> call, Throwable t) {
                Log.e("Anhle P error ", t.toString());
                pbProfileDoctor.setVisibility(View.GONE);
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
            case R.id.ivChatWithDoctor:{

                break;
            }
            case R.id.ivReportWithDoctor:{
                break;
            }
            case R.id.ivVideoCallWithDoctor:{
                break;
            }
            case R.id.fabFavorite:{
                handleFabFavorite();
                break;
            }
            case R.id.rbDoctorRanking:{
                break;
            }
        }
    }

    private void handleFabFavorite(){
        pbProfileDoctor.setVisibility(View.VISIBLE);
        if(isFavorite){
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.setDoctorId(currentDoctor.getDoctorId());
            favoriteRequest.setPatientId(currentPatient.getId());
            RemoveFavoriteDoctorService removeFavoriteDoctorService = RetrofitFactory.getInstance().createService(RemoveFavoriteDoctorService.class);
            removeFavoriteDoctorService.addFavoriteDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    if(response.code() == 200){
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.remove(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                        pbProfileDoctor.setVisibility(View.GONE);
                    }else if(response.code() == 401){
                        Utils.backToLogin(getContext());
                    }

                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " remove");
                    pbProfileDoctor.setVisibility(View.GONE);
                }
            });

        }else {
            FavoriteRequest favoriteRequest = new FavoriteRequest();
            favoriteRequest.setDoctorId(currentDoctor.getDoctorId());
            favoriteRequest.setPatientId(currentPatient.getId());
            AddFavoriteDoctorService addFavoriteDoctorService = RetrofitFactory.getInstance().createService(AddFavoriteDoctorService.class);
            addFavoriteDoctorService.addFavoriteDoctor(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),favoriteRequest).enqueue(new Callback<MainResponseFavorite>() {
                @Override
                public void onResponse(Call<MainResponseFavorite> call, Response<MainResponseFavorite> response) {
                    Log.e("Anh le doctor  ", "post submitted to API." + response.body().toString());
                    if(response.code() == 200) {
                        List<String> listFavorite = currentPatient.getFavoriteDoctors();
                        listFavorite.add(currentDoctor.getDoctorId());
                        currentPatient.setFavoriteDoctors(listFavorite);
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFavorite = true;
                        Log.e("Anh le doctor p ", "post submitted to API." + response.body().toString());
                        pbProfileDoctor.setVisibility(View.GONE);
                    }else if(response.code() == 401){
                        Utils.backToLogin(getContext());
                    }
                }

                @Override
                public void onFailure(Call<MainResponseFavorite> call, Throwable t) {
                    Log.e("anh le error", " adding " +  t.toString());
                    pbProfileDoctor.setVisibility(View.GONE);
                }
            });

        }
    }
}
