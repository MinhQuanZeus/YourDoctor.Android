package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.networks.models.PhoneVerification;
import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;
import com.yd.yourdoctorandroid.networks.services.PhoneVerificationCodeService;
import com.yd.yourdoctorandroid.utils.NetworkUtils;
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
public class VerifyCodePhoneNumberFragment extends Fragment {

    private String phoneNumber;

    @BindView(R.id.btn_next)
    CircularProgressButton btnNext;
    @BindView(R.id.pv_code)
    PinView pvCode;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    private Unbinder unbinder;

    public VerifyCodePhoneNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_code_phone_number, container, false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_main);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle(R.string.verify_phone_number);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        unbinder = ButterKnife.bind(this, view);
        tvPhoneNumber.setText(phoneNumber);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
        pvCode.setAnimationEnable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean onValidate() {
        boolean isValid = true;
        String code = pvCode.getText().toString();
        if (code.isEmpty() || code.length() < 4) {
            isValid = false;
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getResources().getText(R.string.ERROR0002));
        } else {
            isValid = true;
            tvError.setVisibility(View.GONE);
        }

        return isValid;
    }

    private void onSubmit() {
        if (!onValidate()) {
            return;
        }
        btnNext.startAnimation();
        pvCode.setEnabled(false);
        String code = pvCode.getText().toString();
        PhoneVerification phoneVerification = new PhoneVerification(phoneNumber, code);

        PhoneVerificationCodeService phoneVerificationCodeService = RetrofitFactory.getInstance().createService(PhoneVerificationCodeService.class);
        phoneVerificationCodeService.register(phoneVerification).enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                btnNext.revertAnimation();
                if (response.code() == 200) {
                    ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new RegisterFragment().setPhoneNumber(phoneNumber), R.id.fl_auth, true, true);
                } else {
                    pvCode.setEnabled(true);
                    CommonErrorResponse commonErrorResponse = NetworkUtils.parseToCommonError(response);
                    if (commonErrorResponse.getError() != null) {
                        String error = Utils.getStringResourceByString(getContext(), commonErrorResponse.getError());
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE" , error);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                pvCode.setEnabled(true);
                btnNext.revertAnimation();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                }

                Log.d("RESPONSE", t.getMessage());
            }
        });
    }

    public VerifyCodePhoneNumberFragment setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

}
