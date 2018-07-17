package com.yd.yourdoctorandroid.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorChoiceAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.networks.models.Specialist;
import com.yd.yourdoctorandroid.networks.models.TypeAdvisory;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @BindView(R.id.btn_date)
    Button btndate;

    @BindView(R.id.btn_time)
    Button btntime;

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

    Calendar cal;
    Date dateFinish;
    Date hourFinish;
    DoctorChoiceAdapter doctorChoiceAdapter;
    Doctor doctorChoice;

    ArrayList<Specialist> spectlists;

    ArrayList<TypeAdvisory> typeAdvisories;

    String[] arrTypeChatAdvisories;
    String[] arrTypeVideoCallAdvisories;
    String arrayspecialists[];


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
        btndate.setOnClickListener(this);
        btntime.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_choose_Doctor.setOnClickListener(this);

        spectlists = new ArrayList<Specialist>();
        typeAdvisories = new ArrayList<TypeAdvisory>();
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

        getDefaultInfor();

        setUpSpecialists();
        sp_speclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //TODO
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

        setUpTypeAdvisory();
        sp_typeChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //TODO
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

    }

    private void setUpSpecialists() {

        spectlists = (ArrayList<Specialist>)  LoadDefaultModel.getInstance().getSpecialists();

        arrayspecialists = new String[spectlists.size()];

        for (int i = 0; i < arrayspecialists.length; i++) {
            arrayspecialists[i] = spectlists.get(i).getName();
        }

        setSpinerSpecialist(arrayspecialists);

    }

    private void setUpTypeAdvisory() {

        typeAdvisories = (ArrayList<TypeAdvisory>) LoadDefaultModel.getInstance().getTypeAdvisories();
        ArrayList<TypeAdvisory> arrlistChatType = new ArrayList<>();
        ArrayList<TypeAdvisory> arrlistVideoCall = new ArrayList<>();
        for (TypeAdvisory typeAdvisory: typeAdvisories) {
            if(typeAdvisory.getName().contains("Video")){
                arrlistVideoCall.add(typeAdvisory);
            }else {
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
    }

    private void setSpinerSpecialist(String[] arrSpecilists){
        ArrayAdapter<String> adapterSpeclist = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        arrSpecilists
                );

        adapterSpeclist.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sp_speclist.setAdapter(adapterSpeclist);

    };

    private void setSpinerTypeAdvisory(String[] arrTypeChatAdvisories){
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

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_choose_chat:
                if (((RadioButton) v).isChecked()) {
                    rl_chat.setVisibility(View.VISIBLE);
                    setSpinerTypeAdvisory(arrTypeChatAdvisories);
                    //  rl_videocall.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.rb_choose_video_call:
                if (((RadioButton) v).isChecked()) {
                    rl_chat.setVisibility(View.INVISIBLE);
                    setSpinerTypeAdvisory(arrTypeVideoCallAdvisories);
                    // rl_videocall.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_date: {
                showDatePickerDialog();
                break;
            }
            case R.id.btn_time: {
                showTimePickerDialog();
                break;
            }
            case R.id.btn_choose_Doctor: {
                showDialogChooseDoctor();
                break;
            }
            case R.id.btn_post: {
                //showDialogChooseDoctor();
            }

        }
    }

    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                btndate.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);

                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish = cal.getTime();
            }
        };

        if (btndate.getText().toString().compareToIgnoreCase("Ngày") != 0) {

        }
        String s = btndate.getText().toString() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                getContext(),
                callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày hoàn thành");
        pic.show();
    }


    public void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {
                String s = hourOfDay + ":" + minute;
                int hourTam = hourOfDay;
                if (hourTam > 12)
                    hourTam = hourTam - 12;
                btntime.setText
                        (hourTam + ":" + minute + (hourOfDay > 12 ? " PM" : " AM"));
                btntime.setTag(s);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                hourFinish = cal.getTime();
            }
        };

        String s = btntime.getTag().toString() + "";
        String strArr[] = s.split(":");
        Log.d("Anhle", s);
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                getContext(),
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }

    public void getDefaultInfor() {
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String strDate = dft.format(cal.getTime());
        //hiển thị lên giao diện
        btndate.setText(strDate);
        //Định dạng giờ phút am/pm
        dft = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String strTime = dft.format(cal.getTime());
        //đưa lên giao diện
        btntime.setText(strTime);
        //lấy giờ theo 24 để lập trình theo Tag
        dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        btntime.setTag(dft.format(cal.getTime()));

    }

    public void showDialogChooseDoctor() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.choose_doctor_dialog);
        dialog.setTitle("Lựa Chọn Bác Sĩ của bạn");

        // set the custom dialog components - text, image and button
        Button btn_cancel_choose = dialog.findViewById(R.id.btn_cancel_choose_doctor);
        Button btn_ok_choose = dialog.findViewById(R.id.btn_ok_choose_doctor);
        RecyclerView rv_list_doctor = dialog.findViewById(R.id.rv_list_doctor);

        final Doctor doctorChoose;
        List<Doctor> chosenDoctorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doctor doctor = new Doctor();
            doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
            doctor.setFirstName("Le");
            doctor.setLastName("Anh");
            doctor.setMiddleName("The");
            doctor.setCurrentRating((float) 3.3);
            chosenDoctorList.add(doctor);
        }

        doctorChoiceAdapter = new DoctorChoiceAdapter(chosenDoctorList, getContext(), dialog);
        rv_list_doctor.setAdapter(doctorChoiceAdapter);
        rv_list_doctor.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rv_list_doctor.addItemDecoration(dividerItemDecoration);
        // if button is clicked, close the custom dialog
        btn_cancel_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }
}

