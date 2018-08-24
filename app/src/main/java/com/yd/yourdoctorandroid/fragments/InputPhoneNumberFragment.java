package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.models.PhoneVerification;
import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;
import com.yd.yourdoctorandroid.networks.services.PhoneVerificationService;

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
public class InputPhoneNumberFragment extends Fragment {

    @BindView(R.id.btn_next)
    CircularProgressButton btnNext;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.et_phone_number)
    EditText etPhone;
    private Unbinder unbinder;

    public InputPhoneNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_input_phone_number, container, false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        unbinder = ButterKnife.bind(this, view);
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private boolean onValidate() {
        boolean isValidate = true;
        String regex = "(09|01[2|6|8|9])+([0-9]{8})\\b";
        String phone = etPhone.getText().toString();

        if (phone.isEmpty() || !phone.matches(regex)) {
            isValidate = false;
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getResources().getText(R.string.ERROR0001));
        } else {
            isValidate = true;
            tvError.setVisibility(View.GONE);
        }

        return isValidate;
    }

    private void onSubmit() {
        if (!onValidate()) {
            return;
        }
        etPhone.setEnabled(false);
        final String phone = etPhone.getText().toString();
        btnNext.startAnimation();
        PhoneVerification phoneVerification = new PhoneVerification(phone, null);
        PhoneVerificationService phoneVerificationService = RetrofitFactory.getInstance().createService(PhoneVerificationService.class);
        phoneVerificationService.register(phoneVerification)
                .enqueue(new Callback<CommonSuccessResponse>() {
                    @Override
                    public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                        btnNext.revertAnimation();
                        if (response.code() == 200 || response.code() == 201){
                            ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new VerifyCodePhoneNumberFragment().setPhoneNumber(phone), R.id.fl_auth, true, true);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                        btnNext.revertAnimation();
                        etPhone.setEnabled(true);
                        if (t instanceof SocketTimeoutException) {
                            Toast.makeText(getActivity(), getResources().getText(R.string.error_timeout), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
