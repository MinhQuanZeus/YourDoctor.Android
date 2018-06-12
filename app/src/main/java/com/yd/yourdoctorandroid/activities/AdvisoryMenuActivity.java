package com.yd.yourdoctorandroid.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorChoiceAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class AdvisoryMenuActivity extends AppCompatActivity implements View.OnClickListener {
    RadioButton rb_choose_chat;
    RadioButton rb_choose_video_call;
    RelativeLayout rl_chat;
    Button btn_post;
    Spinner sp_speclist;
    Spinner sp_typeChat;
    EditText et_question;
    TextView txtdate;
    Button btndate;
    TextView txttime;
    Button btntime;

    Calendar cal;
    Date dateFinish;
    Date hourFinish;

    //for test
    String arrspectlist[] = {
            "Đa Khoa",
            "Tim Mạch",
            "Da liễu"};

    String arrtypechat[] = {
            "Ngắn",
            "Dài",
            "Trung bình"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory_menu);
        setupUI();
    }

    private void setupUI() {
        rb_choose_chat = findViewById(R.id.rb_choose_chat);
        rb_choose_video_call = findViewById(R.id.rb_choose_video_call);
        rl_chat = findViewById(R.id.rl_chat);
        //  rl_videocall = findViewById(R.id.rl_videocall);
        btn_post = findViewById(R.id.btn_post);
        sp_speclist = findViewById(R.id.sp_speclist);
        sp_typeChat = findViewById(R.id.sp_typeChat);
        et_question = findViewById(R.id.et_question);
        txtdate = findViewById(R.id.txtdate);
        btndate = findViewById(R.id.btndate);
        txttime = findViewById(R.id.txttime);
        btntime = findViewById(R.id.btntime);

        rb_choose_chat.setOnClickListener(this);
        rb_choose_video_call.setOnClickListener(this);
        btndate.setOnClickListener(this);
        btntime.setOnClickListener(this);
        btn_post.setOnClickListener(this);

        getDefaultInfor();

        ArrayAdapter<String> adapterSpeclist = new ArrayAdapter<String>
                (
                        this,
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
                        this,
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
            case R.id.btndate: {
                showDatePickerDialog();
                break;
            }
            case R.id.btntime: {
                showTimePickerDialog();
                break;
            }
            case R.id.btn_post: {
                showDialogChooseDoctor();
            }

        }
    }


    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                txtdate.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);

                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish = cal.getTime();
            }
        };

        String s = txtdate.getText() + "";
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(
                AdvisoryMenuActivity.this,
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
                txttime.setText
                        (hourTam + ":" + minute + (hourOfDay > 12 ? " PM" : " AM"));
                txttime.setTag(s);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                hourFinish = cal.getTime();
            }
        };

        String s = txttime.getTag() + "";
        String strArr[] = s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time = new TimePickerDialog(
                AdvisoryMenuActivity.this,
                callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }

    public void getDefaultInfor() {
        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        txtdate.setText(strDate);
        dft = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String strTime = dft.format(cal.getTime());
        txttime.setText(strTime);
        dft = new SimpleDateFormat("HH:mm", Locale.getDefault());
        txttime.setTag(dft.format(cal.getTime()));

    }

    public void showDialogChooseDoctor() {
        final Dialog dialog = new Dialog(AdvisoryMenuActivity.this);
        dialog.setContentView(R.layout.choose_doctor_dialog);
        dialog.setTitle("Lựa Chọn Bác Sĩ của bạn");

        // set the custom dialog components - text, image and button
        Button btn_cancel_choose = dialog.findViewById(R.id.btn_cancel_choose_doctor);
        Button btn_ok_choose = dialog.findViewById(R.id.btn_ok_choose_doctor);
        RecyclerView rv_list_doctor = dialog.findViewById(R.id.rv_list_doctor);

        DoctorChoiceAdapter doctorChoiceAdapter = new DoctorChoiceAdapter(null, getApplicationContext());
        rv_list_doctor.setAdapter(doctorChoiceAdapter);
        rv_list_doctor.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
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

            }
        });

        dialog.show();
    }

}
