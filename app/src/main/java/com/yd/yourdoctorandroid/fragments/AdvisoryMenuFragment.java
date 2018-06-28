package com.yd.yourdoctorandroid.fragments;


import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yd.yourdoctorandroid.models.Doctor;

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

    @BindView(R.id.iv_back_from_menu_advisory)
    ImageView iv_back_from_menu_advisory;

    Unbinder butterKnife;

    Calendar cal;
    Date dateFinish;
    Date hourFinish;
    DoctorChoiceAdapter doctorChoiceAdapter;
    Doctor doctorChoice;
    //for test
    String arrspectlist[] = {
            "Đa Khoa",
            "Tim Mạch",
            "Da liễu"};

    String arrtypechat[] = {
            "Ngắn",
            "Dài",
            "Trung bình"};

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
        iv_back_from_menu_advisory.setOnClickListener(this);
        getDefaultInfor();

        ArrayAdapter<String> adapterSpeclist = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        arrspectlist
                );


        adapterSpeclist.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sp_speclist.setAdapter(adapterSpeclist);

        sp_speclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //TODO
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });

        ArrayAdapter<String> adapterTypeChat = new ArrayAdapter<String>
                (
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        arrtypechat
                );

        adapterTypeChat.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sp_typeChat.setAdapter(adapterTypeChat);

        sp_typeChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //TODO
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_choose_chat:
                if (((RadioButton) v).isChecked()) {
                    rl_chat.setVisibility(View.VISIBLE);
                    //  rl_videocall.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.rb_choose_video_call:
                if (((RadioButton) v).isChecked()) {
                    rl_chat.setVisibility(View.INVISIBLE);
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
            case R.id.iv_back_from_menu_advisory: {
                ScreenManager.backFragment(getFragmentManager());
                break;
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
            doctor.setFirst_name("Le");
            doctor.setLast_name("Anh");
            doctor.setCurrent_rating((float) 3.3);
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
                    tv_name_doctor_chosen.setText(doctorChoice.getFirst_name() + " " + doctorChoice.getLast_name());
                    rb_doctorChosen.setRating(doctorChoice.getCurrent_rating());
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

