package com.yd.yourdoctorandroid.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListIDFavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectIDFavorite;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.GetListPendingChatService;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.IDPending;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.MainPendingResponse;
import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.networks.models.Login;
import com.yd.yourdoctorandroid.networks.services.LoginService;
import com.yd.yourdoctorandroid.services.TimeOutChatService;
import com.yd.yourdoctorandroid.utils.Config;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String USER_INFO = "USER_INFO";
    private final String prefname = "my_data";
    @BindView(R.id.tv_signup)
    TextView tvSignUp;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;

    @BindView(R.id.forgotpass)
    com.yd.yourdoctorandroid.custormviews.MyTextView forgotPass;

    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_sign_in)
    CircularProgressButton btnLogin;
    @BindView(R.id.remember)
    CheckBox cbRememder;
    private Unbinder unbinder;

    int countSuccessInitialization;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        unbinder = ButterKnife.bind(this, view);
        tvSignUp = (TextView) view.findViewById(R.id.tv_signup);
        //LoadDefaultModel.getInstance();
        countSuccessInitialization = 0;
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new RulesRegisterFragment(), R.id.fl_auth, true, true);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputPhoneNumberFragment inputPhoneNumberFragment = new InputPhoneNumberFragment();
                inputPhoneNumberFragment.setIsForgetPassword(true);
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(), inputPhoneNumberFragment, R.id.fl_auth, true, true);

            }
        });
    }

    private boolean onValidate() {
        boolean isValidate = true;
        String regex = "(09|01[2|6|8|9])+([0-9]{8})\\b";
        String phone = edPhone.getText().toString();
        String password = edPassword.getText().toString();

        if (phone.isEmpty() || !phone.matches(regex)) {
            isValidate = false;
            tilPhone.setError(getResources().getString(R.string.ERROR0001));
        } else {
            isValidate = true;
            tilPhone.setError(null);
        }

        if (password.isEmpty()) {
            isValidate = false;
            tilPassword.setError(getResources().getString(R.string.empty_password));
        } else {
            isValidate = true;
            tilPassword.setError(null);
        }

        return isValidate;
    }

    private void onLogin() {
        if (!onValidate()) {
            return;
        }
        btnLogin.startAnimation();
        disableAll();
        String phone = edPhone.getText().toString();
        String password = edPassword.getText().toString();
        Login login = new Login(phone, password);

        LoginService loginService = RetrofitFactory.getInstance().createService(LoginService.class);
        loginService.register(login).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, final Response<AuthResponse> response) {

                if (response.code() == 200 || response.code() == 201) {
                    if(response.body().getPatient().getRole() == 1 && response.body().getPatient().getStatus() == 1){
                        SharedPrefs.getInstance().put(JWT_TOKEN, response.body().getJwtToken());
                        Log.e("tokenLogin: ",SharedPrefs.getInstance().get(JWT_TOKEN,String.class));
                        if(SharedPrefs.getInstance().get(USER_INFO, Patient.class) != null){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefs.getInstance().get(USER_INFO, Patient.class).getId());
                        }
                        SharedPrefs.getInstance().put(USER_INFO, response.body().getPatient());
                        FirebaseMessaging.getInstance().subscribeToTopic(response.body().getPatient().getId());
                        LoadDefaultModel.getInstance().registerServiceCheckNetwork(getActivity().getApplicationContext());
                        loadFavoriteDoctor(SharedPrefs.getInstance().get(USER_INFO,Patient.class));
                    }
                    else {
                        if(response.body().getPatient().getRole() != 1){
                            Toast.makeText(getContext(),"Tài khoản của bạn không phải là tài khoản bệnh nhân!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(),"Tài khoản đang bị khóa, mọi thắc mắc xin liện hệ đến tổng đài!", Toast.LENGTH_LONG).show();
                        }
                        btnLogin.revertAnimation();
                    }

                }else if(response.code() == 404){
                    enableAll();
                        Toast.makeText(getActivity(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    btnLogin.revertAnimation();
                }
                else {
                    enableAll();
                    try{
                        CommonErrorResponse commonErrorResponse = parseToCommonError(response);
                        if (commonErrorResponse.getError() != null) {
                            String error = Utils.getStringResourceByString(getContext(), commonErrorResponse.getError());
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE", error);
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Lỗi Đăng nhập!", Toast.LENGTH_SHORT).show();
                    }

                    btnLogin.revertAnimation();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                if(btnLogin != null) btnLogin.revertAnimation();

                enableAll();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Không có kết nối mạng", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loadFavoriteDoctor(final Patient currentPatient) {
        GetListIDFavoriteDoctor getListIDFavoriteDoctor = RetrofitFactory.getInstance().createService(GetListIDFavoriteDoctor.class);
        getListIDFavoriteDoctor.getMainObjectIDFavorite(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId()).enqueue(new Callback<MainObjectIDFavorite>() {
            @Override
            public void onResponse(Call<MainObjectIDFavorite> call, Response<MainObjectIDFavorite> response) {
                if(response.code() == 200){
                    MainObjectIDFavorite mainObject = response.body();
                    if (mainObject != null) {
                        Patient newPatient = currentPatient;
                        newPatient.setFavoriteDoctors(mainObject.getListIDFavoriteDoctor());
                        SharedPrefs.getInstance().put("USER_INFO", newPatient);
                        //LoadDefaultModel.getInstance().loadAllChatPending(currentPatient.getId());
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
                }
                btnLogin.revertAnimation();
            }

            @Override
            public void onFailure(Call<MainObjectIDFavorite> call, Throwable t) {
                Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                btnLogin.revertAnimation();
            }
        });

    }

    private void enableAll() {
        this.edPassword.setEnabled(true);
        this.edPhone.setEnabled(true);
    }

    private void disableAll() {
        this.edPassword.setEnabled(false);
        this.edPhone.setEnabled(false);
    }

    public CommonErrorResponse parseToCommonError(Response<AuthResponse> response) {
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse();
        Gson gson = new GsonBuilder().create();
        try {
            commonErrorResponse = gson.fromJson(response.errorBody().string(), CommonErrorResponse.class);
        } catch (IOException e) {
            // handle failure to read error
        }
        return commonErrorResponse;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void savingPreferences() {
        SharedPreferences pre = this.getActivity().getSharedPreferences(prefname, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        String user = edPhone.getText().toString();
        String pwd = edPassword.getText().toString();
        boolean bchk = cbRememder.isChecked();
        if (!bchk) {
            editor.clear();
        } else {
            editor.putString("user", user);
            editor.putString("pwd", pwd);
            editor.putBoolean("checked", bchk);
        }
        editor.commit();
    }

    public void restoringPreferences() {
        SharedPreferences pre = this.getActivity().getSharedPreferences(prefname, MODE_PRIVATE);
        boolean bchk = pre.getBoolean("checked", false);
        if (bchk) {
            String user = pre.getString("user", "");
            String pwd = pre.getString("pwd", "");
            edPhone.setText(user);
            edPassword.setText(pwd);
        }
        cbRememder.setChecked(bchk);
    }

    @Override
    public void onResume() {
        super.onResume();
        restoringPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savingPreferences();
    }
}
