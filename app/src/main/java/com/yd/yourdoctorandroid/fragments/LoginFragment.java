package com.yd.yourdoctorandroid.fragments;


import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.networks.models.Login;
import com.yd.yourdoctorandroid.networks.services.LoginService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;
import com.yd.yourdoctorandroid.utils.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

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
public class LoginFragment extends Fragment {

    public static final String JWT_TOKEN = "JWT_TOKEN";
    public static final String USER_INFO = "USER_INFO";
    TextView tvSignUp;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_sign_in)
    CircularProgressButton btnLogin;
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
        LoadDefaultModel.getInstance();
        countSuccessInitialization = 0;
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new InputPhoneNumberFragment(), R.id.fl_auth, true, true);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
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
                    Log.e("Login ", response.body().getJwtToken() );
                    SharedPrefs.getInstance().put(JWT_TOKEN, response.body().getJwtToken());

                    if(SharedPrefs.getInstance().get(USER_INFO, Patient.class) != null){
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefs.getInstance().get(USER_INFO, Patient.class).getId());
                    }
                    SharedPrefs.getInstance().put(USER_INFO, response.body().getPatient());
                    Log.e("idPatient", response.body().getPatient().getId());
                    FirebaseMessaging.getInstance().subscribeToTopic(response.body().getPatient().getId());
                    SocketUtils.getInstance().reConnect();
                    LoadDefaultModel.getInstance().loadFavoriteDoctor(response.body().getPatient(), getActivity(), btnLogin);

                } else {
                    enableAll();
                    CommonErrorResponse commonErrorResponse = parseToCommonError(response);
                    if (commonErrorResponse.getError() != null) {
                        String error = Utils.getStringResourceByString(getContext(), commonErrorResponse.getError());
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", error);
                    }
                    btnLogin.revertAnimation();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                btnLogin.revertAnimation();
                enableAll();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                }
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

}
