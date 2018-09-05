package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getRuleIntroAPI.GetRuleIntroService;
import com.yd.yourdoctorandroid.networks.getRuleIntroAPI.MainObjectRule;
import com.yd.yourdoctorandroid.networks.getRuleIntroAPI.RuleObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesRegisterFragment extends Fragment {

    @BindView(R.id.tbRuleRegister)
    Toolbar tbRuleRegister;
    @BindView(R.id.iv_rule)
    ImageView ivRule;
    @BindView(R.id.tv_content_rule)
    TextView tvContentRule;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.btn_continue_register)
    Button btnContinueRegister;
    @BindView(R.id.pb_rule)
    ProgressBar pbRule;

    RuleObject currentRuleObject;
    public RulesRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules_register, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,view);
        setUpUi();

        return view;
    }

    private void setUpUi(){
        tbRuleRegister.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbRuleRegister.setTitle("Điều khoản");
        tbRuleRegister.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbRuleRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });

        GetRuleIntroService getRuleIntroService = RetrofitFactory.getInstance().createService(GetRuleIntroService.class);
        getRuleIntroService.getIntroduceAndRule().enqueue(new Callback<MainObjectRule>() {
            @Override
            public void onResponse(Call<MainObjectRule> call, Response<MainObjectRule> response) {
                if (response.code() == 200) {
                    MainObjectRule mainObjectSpecialist = response.body();
                    if(mainObjectSpecialist != null){
                        for (RuleObject ruleObject:mainObjectSpecialist.getObjIntroAndRuleReturn()) {
                            if(ruleObject.getType().equals("patient")){
                                currentRuleObject = ruleObject;
                                tvContentRule.setText(ruleObject.getContent());
                                break;
                            }
                        }
                    }
                }
                pbRule.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectRule> call, Throwable t) {
                Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                pbRule.setVisibility(View.GONE);
            }
        });

        btnContinueRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonNext();
            }
        });

    }

    private void handleButtonNext(){
        if(cbAgree.isChecked()){
            ScreenManager.openFragment(getActivity().getSupportFragmentManager(), new InputPhoneNumberFragment(), R.id.fl_auth, true, true);
        }else {
            Toast.makeText(getContext(),"Bạn nên đánh dấu đồng ý điều khoản để tiếp tục", Toast.LENGTH_LONG).show();
        }
    }



}
