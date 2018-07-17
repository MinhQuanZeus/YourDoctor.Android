package com.yd.yourdoctorandroid.fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.iv_ava_profile)
    de.hdodenhof.circleimageview.CircleImageView iv_ava_profile;

    @BindView(R.id.btn_edit_profile)
    Button btn_edit_profile;

    @BindView(R.id.btn_change_password)
    Button btn_change_password;

    @BindView(R.id.tb_back_from_profile)
    Toolbar tb_back_from_profile;

    @BindView(R.id.iv_choose_image)
    de.hdodenhof.circleimageview.CircleImageView iv_choose_image;

    @BindView(R.id.ed_firstname)
    EditText ed_firstname;

    @BindView(R.id.ed_lastname)
    EditText ed_lastname;

    @BindView(R.id.ed_address_user)
    EditText ed_address_user;

    @BindView(R.id.tv_birthday_user)
    TextView tv_birthday_user;

    @BindView(R.id.tv_remain_money_profile)
    TextView tv_remain_money_profile;

    @BindView(R.id.tv_phone_number_user)
    TextView tv_phone_number_user;

    @BindView(R.id.ed_middlename)
    EditText ed_middlename;

    EditText et_old_password;

    EditText et_new_password;

    EditText et_confirm_new_password;

    TextView tv_message_change_password;

    ProgressBar progress_change_password;

    AlertDialog dialogChangePassword;


    Patient currentPatient;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupUI(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void setupUI(View view) {

        ButterKnife.bind(this,view);
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);

        Picasso.with(getContext()).load(currentPatient.getAvatar()).transform(new CropCircleTransformation()).into(iv_ava_profile);
        ed_firstname.setText(currentPatient.getfName());
        ed_middlename.setText(currentPatient.getmName());
        ed_lastname.setText(currentPatient.getlName());

        ed_address_user.setText(currentPatient.getAddress());
        tv_remain_money_profile.setText(currentPatient.getRemainMoney()+"");
        tv_birthday_user.setText(currentPatient.getBirthday());
        tv_phone_number_user.setText(currentPatient.getPhoneNumber());

        ed_firstname.setEnabled(false);
        iv_choose_image.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        btn_edit_profile.setOnClickListener(this);
        tv_birthday_user.setOnClickListener(this);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_back_from_profile);
        final ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle(R.string.profile_page);

        tb_back_from_profile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_profile:
                ed_firstname.setEnabled(true);
                ed_lastname.setEnabled(true);
                ed_firstname.setFocusable(true);
                ed_address_user.setEnabled(true);
                iv_choose_image.setVisibility(View.VISIBLE);
                btn_edit_profile.setText("Hoàn thành");

                break;
            case R.id.btn_change_password:
                showDialogChangePassword();
                break;
            case R.id.tv_birthday_user:
                showDatePickerDialog();
                break;
            case R.id.iv_choose_image:
                break;


        }
    }

    private void showDialogChangePassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog, null);
        et_old_password =  view.findViewById(R.id.et_old_password);
        et_new_password = view.findViewById(R.id.et_new_password);
        et_confirm_new_password = view.findViewById(R.id.et_confirm_new_password);
        et_old_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        et_new_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        et_confirm_new_password.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        tv_message_change_password = view.findViewById(R.id.tv_message_change_password);
        progress_change_password = view.findViewById(R.id.progress_change_password);
        builder.setView(view);
        builder.setTitle("Thay đổi mật khẩu");

        builder.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
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
        dialogChangePassword = builder.create();
        dialogChangePassword.show();
        dialogChangePassword.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                String confirm_new_password = et_confirm_new_password.getText().toString();
                if(old_password.isEmpty() || new_password.isEmpty() || confirm_new_password.isEmpty()){
                    tv_message_change_password.setVisibility(View.VISIBLE);
                    //tv_message_change_password.setText("Fields are empty");
                    tv_message_change_password.setText("Có trường bị trống");
                }else if(new_password.length() <8){
                    tv_message_change_password.setVisibility(View.VISIBLE);
                    tv_message_change_password.setText("Độ dài mật khẩu phải trên 8 ký tự");
                }
                else if(confirm_new_password.compareTo(new_password) != 0){
                    tv_message_change_password.setVisibility(View.VISIBLE);
                    tv_message_change_password.setText("Nhập lại mật khẩu mới không khớp ");
                }else {
                    tv_message_change_password.setVisibility(View.GONE);
                    progress_change_password.setVisibility(View.VISIBLE);
                    changePasswordProcess(0,old_password,new_password);
                }


            }
        });
    }

    private void changePasswordProcess(int id , String old_password,String new_password){

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//
//        User user = new User();
//        user.setEmail(email);
//        user.setOld_password(old_password);
//        user.setNew_password(new_password);
//        ServerRequest request = new ServerRequest();
//        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
//        request.setUser(user);
//        Call<ServerResponse> response = requestInterface.operation(request);
//
//        response.enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
//
//                ServerResponse resp = response.body();
//                if(resp.getResult().equals(Constants.SUCCESS)){
//                    progress.setVisibility(View.GONE);
//                    tv_message.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
//
//                }else {
//                    progress.setVisibility(View.GONE);
//                    tv_message.setVisibility(View.VISIBLE);
//                    tv_message.setText(resp.getMessage());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//
//                Log.d(Constants.TAG,"failed");
//                progress.setVisibility(View.GONE);
//                tv_message.setVisibility(View.VISIBLE);
//                tv_message.setText(t.getLocalizedMessage());
//
//            }
//        });
    }

    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                tv_birthday_user.setText(
                        (dayOfMonth) + "/" + (monthOfYear + 1) + "/" + year);
//                cal.set(year, monthOfYear, dayOfMonth);
//                dateFinish = cal.getTime();
            }
        };


        String s = tv_birthday_user.getText().toString() + "";
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
}
