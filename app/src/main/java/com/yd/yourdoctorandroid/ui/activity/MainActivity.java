package com.yd.yourdoctorandroid.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;

import vn.demonganluong.R;
import vn.demonganluong.api.SendOrderRequest;
import vn.demonganluong.bean.SendOrderBean;
import vn.demonganluong.ui.BaseActivity;
import vn.demonganluong.utils.Commons;
import vn.demonganluong.utils.Constant;

public class MainActivity extends BaseActivity implements View.OnClickListener, SendOrderRequest.SendOrderRequestOnResult {

    private EditText editFullName;
    private EditText editAmount;
    private EditText editEmail;
    private EditText editPhoneNumber;
    private EditText editAddress;
    private Button btnSendOrder;

    private ScrollView scrollView;
    private ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        editFullName = (EditText) findViewById(R.id.activity_main_editFullName);
        editAmount = (EditText) findViewById(R.id.activity_main_editAmount);
        editEmail = (EditText) findViewById(R.id.activity_main_editEmail);
        editPhoneNumber = (EditText) findViewById(R.id.activity_main_editPhoneNumber);
        editAddress = (EditText) findViewById(R.id.activity_main_editAddress);
        btnSendOrder = (Button) findViewById(R.id.activity_main_btnSendOrder);

        scrollView = (ScrollView) findViewById(R.id.activity_main_scrollView);
        progressView = (ProgressView) findViewById(R.id.activity_main_progressView);

        if (!Commons.checkInternetConnection(getApplicationContext())) {
            showErrorDialog(getString(R.string.error_disconnect), true);
        }

        btnSendOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.activity_main_btnSendOrder:
                String fullName = editFullName.getText().toString();
                String amount = editAmount.getText().toString();
                String email = editEmail.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String address = editAddress.getText().toString();

                if (!fullName.equalsIgnoreCase("")) {
                    if (!amount.equalsIgnoreCase("") && Integer.valueOf(amount) >= 2000) {
                        if (!email.equalsIgnoreCase("")) {
                            if (!phoneNumber.equalsIgnoreCase("")) {
                                if (!address.equalsIgnoreCase("")) {
                                    sendOrderObject(fullName, amount, email, phoneNumber, address);
                                } else {
                                    showErrorDialog(getString(R.string.error_address), false);
                                }
                            } else {
                                showErrorDialog(getString(R.string.error_mobile), false);
                            }
                        } else {
                            showErrorDialog(getString(R.string.error_email), false);
                        }
                    } else {
                        showErrorDialog(getString(R.string.error_amount), false);
                    }
                } else {
                    showErrorDialog(getString(R.string.error_name_order), false);
                }

                break;
        }
    }

    private void sendOrderObject(String fullName, String amount, String email, String phoneNumber, String address) {
        SendOrderBean sendOrderBean = new SendOrderBean();
        sendOrderBean.setFunc("sendOrder");
        sendOrderBean.setVersion("1.0");
        sendOrderBean.setMerchantID(Constant.MERCHANT_ID);
        sendOrderBean.setMerchantAccount("nhant@peacesoft.net");
        sendOrderBean.setOrderCode("123456DEMO");
        sendOrderBean.setTotalAmount(Integer.valueOf(amount));
        sendOrderBean.setCurrency("vnd");
        sendOrderBean.setLanguage("vi");
        sendOrderBean.setReturnUrl(Constant.RETURN_URL);
        sendOrderBean.setCancelUrl(Constant.CANCEL_URL);
        sendOrderBean.setNotifyUrl(Constant.NOTIFY_URL);
        sendOrderBean.setBuyerFullName(fullName);
        sendOrderBean.setBuyerEmail(email);
        sendOrderBean.setBuyerMobile(phoneNumber);
        sendOrderBean.setBuyerAddress(address);

        String checksum = getChecksum(sendOrderBean);
        sendOrderBean.setChecksum(checksum);

        SendOrderRequest sendOrderRequest = new SendOrderRequest();
        sendOrderRequest.execute(getApplicationContext(), sendOrderBean);
        sendOrderRequest.getSendOrderRequestOnResult(this);
        scrollView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    private String getChecksum(SendOrderBean sendOrderBean) {
        String stringSendOrder = sendOrderBean.getFunc() + "|" +
                sendOrderBean.getVersion() + "|" +
                sendOrderBean.getMerchantID() + "|" +
                sendOrderBean.getMerchantAccount() + "|" +
                sendOrderBean.getOrderCode() + "|" +
                sendOrderBean.getTotalAmount() + "|" +
                sendOrderBean.getCurrency() + "|" +
                sendOrderBean.getLanguage() + "|" +
                sendOrderBean.getReturnUrl() + "|" +
                sendOrderBean.getCancelUrl() + "|" +
                sendOrderBean.getNotifyUrl() + "|" +
                sendOrderBean.getBuyerFullName() + "|" +
                sendOrderBean.getBuyerEmail() + "|" +
                sendOrderBean.getBuyerMobile() + "|" +
                sendOrderBean.getBuyerAddress() + "|" +
                Constant.MERCHANT_PASSWORD;
        String checksum = Commons.md5(stringSendOrder);

        return checksum;
    }

    @Override
    public void onSendOrderRequestOnResult(boolean result, String data) {
        if (result == true) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    String tokenCode = objResult.getString("token_code");
                    String checkoutUrl = objResult.getString("checkout_url");

                    Intent intentCheckout = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intentCheckout.putExtra(CheckOutActivity.TOKEN_CODE, tokenCode);
                    intentCheckout.putExtra(CheckOutActivity.CHECKOUT_URL, checkoutUrl);
                    startActivity(intentCheckout);
                    finish();
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                    showErrorDialog(Commons.getCodeError(getApplicationContext(), responseCode), false);
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void showErrorDialog(String message, final boolean isExit) {
        final Dialog mSuccessDialog = new Dialog(MainActivity.this);
        mSuccessDialog.setContentView(R.layout.dialog_success);
        mSuccessDialog.setCancelable(false);
        mSuccessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mSuccessDialog.getWindow().setGravity(Gravity.CENTER);

        TextView txtContent = (TextView) mSuccessDialog.findViewById(R.id.dialog_success_txtContent);
        txtContent.setText(message);
        Button btnClose = (Button) mSuccessDialog.findViewById(R.id.dialog_success_btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuccessDialog.dismiss();
                if (isExit) {
                    finish();
                }
            }
        });

        mSuccessDialog.show();
    }
}
