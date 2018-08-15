package com.yd.yourdoctorandroid.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;

import vn.demonganluong.R;
import vn.demonganluong.api.CheckOrderRequest;
import vn.demonganluong.bean.CheckOrderBean;
import vn.demonganluong.ui.BaseActivity;
import vn.demonganluong.utils.Commons;
import vn.demonganluong.utils.Constant;

/**
 * Created by DucChinh on 6/14/2016.
 */
public class CheckOrderActivity extends BaseActivity implements CheckOrderRequest.CheckOrderRequestOnResult {

    public static final String TOKEN_CODE = "token_code";

    private TextView txtData;
    private ProgressView mProgressView;

    private String mTokenCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkorder);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTokenCode = extras.getString(TOKEN_CODE, "");
        }

        initView();
    }

    private void initView() {
        txtData = (TextView) findViewById(R.id.activity_checkorder_txtData);
        txtData.setMovementMethod(new ScrollingMovementMethod());

        mProgressView = (ProgressView) findViewById(R.id.activity_checkorder_progressView);

        checkOrderObject();
    }

    private void checkOrderObject() {
        CheckOrderBean checkOrderBean = new CheckOrderBean();
        checkOrderBean.setFunc("checkOrder");
        checkOrderBean.setVersion("1.0");
        checkOrderBean.setMerchantID(Constant.MERCHANT_ID);
        checkOrderBean.setTokenCode(mTokenCode);

        String checksum = getChecksum(checkOrderBean);
        checkOrderBean.setChecksum(checksum);

        CheckOrderRequest checkOrderRequest = new CheckOrderRequest();
        checkOrderRequest.execute(getApplicationContext(), checkOrderBean);
        checkOrderRequest.getCheckOrderRequestOnResult(this);
        txtData.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    private String getChecksum(CheckOrderBean checkOrderBean) {
        String stringSendOrder = checkOrderBean.getFunc() + "|" +
                checkOrderBean.getVersion() + "|" +
                checkOrderBean.getMerchantID() + "|" +
                checkOrderBean.getTokenCode() + "|" +
                Constant.MERCHANT_PASSWORD;
        String checksum = Commons.md5(stringSendOrder);

        return checksum;
    }

    @Override
    public void onBackPressed() {
        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMain);
        finish();
    }

    @Override
    public void onCheckOrderRequestOnResult(boolean result, String data) {
        if (result == true) {
            try {
                JSONObject objResult = new JSONObject(data);
                String responseCode = objResult.getString("response_code");
                if (responseCode.equalsIgnoreCase("00")) {
                    String response_code = objResult.getString("response_code");
                    String receiver_email = objResult.getString("receiver_email");
                    String order_code = objResult.getString("order_code");
                    int total_amount = objResult.getInt("total_amount");
                    String currency = objResult.getString("currency");
                    String language = objResult.getString("language");
                    String return_url = objResult.getString("return_url");
                    String cancel_url = objResult.getString("cancel_url");
                    String notify_url = objResult.getString("notify_url");
                    String buyer_full_name = objResult.getString("buyer_fullname");
                    String buyer_email = objResult.getString("buyer_email");
                    String buyer_mobile = objResult.getString("buyer_mobile");
                    String buyer_address = objResult.getString("buyer_address");
                    int transaction_id = objResult.getInt("transaction_id");
                    int transaction_status = objResult.getInt("transaction_status");
                    int transaction_amount = objResult.getInt("transaction_amount");
                    String transaction_currency = objResult.getString("transaction_currency");
                    int transaction_escrow = objResult.getInt("transaction_escrow");

                    String dataCheckOrder =
                            "response_code:  " + response_code + "\n\n" +
                                    "receiver_email:  " + receiver_email + "\n\n" +
                                    "order_code:  " + order_code + "\n\n" +
                                    "total_amount:  " + total_amount + "\n\n" +
                                    "currency:  " + currency + "\n\n" +
                                    "language:  " + language + "\n\n" +
                                    "return_url:  " + return_url + "\n\n" +
                                    "cancel_url:  " + cancel_url + "\n\n" +
                                    "notify_url:  " + notify_url + "\n\n" +
                                    "buyer_full_name:  " + buyer_full_name + "\n\n" +
                                    "buyer_email:  " + buyer_email + "\n\n" +
                                    "buyer_mobile:  " + buyer_mobile + "\n\n" +
                                    "buyer_address:  " + buyer_address + "\n\n" +
                                    "transaction_id:  " + transaction_id + "\n\n" +
                                    "transaction_status:  " + transaction_status + "\n\n" +
                                    "transaction_amount:  " + transaction_amount + "\n\n" +
                                    "transaction_currency:  " + transaction_currency + "\n\n" +
                                    "transaction_escrow:  " + transaction_escrow + "\n\n";

                    txtData.setText(dataCheckOrder);
                    txtData.setVisibility(View.VISIBLE);
                    mProgressView.setVisibility(View.GONE);
                } else {
                    mProgressView.setVisibility(View.GONE);
                    showErrorDialog(Commons.getCodeError(getApplicationContext(), responseCode), false);
                }
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    private void showErrorDialog(String message, final boolean isExit) {
        final Dialog mSuccessDialog = new Dialog(CheckOrderActivity.this);
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
