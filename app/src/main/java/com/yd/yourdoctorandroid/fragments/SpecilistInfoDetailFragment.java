package com.yd.yourdoctorandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorCertificationAdapter;
import com.yd.yourdoctorandroid.adapters.SpecialistAdapter;
import com.yd.yourdoctorandroid.events.ItemClickListener;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Certification;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getDetailSpecialist.GetSpecialistDetailService;
import com.yd.yourdoctorandroid.networks.getDetailSpecialist.MainObjectSpecialistDetail;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecilistInfoDetailFragment extends Fragment {

    @BindView(R.id.tb_detail_specilist)
    Toolbar tb_detail_specilist;
    @BindView(R.id.iv_specilist_detail)
    ImageView iv_specilist_detail;
    @BindView(R.id.tv_name_specilist)
    TextView tv_name_specilist;
    @BindView(R.id.tv_description_specialist)
    TextView tv_description_specialist;
    @BindView(R.id.rlQuestions)
    RecyclerView rlQuestions;
    @BindView(R.id.pb_detail_specialist)
    ProgressBar pbDetailSpecialist;

    Specialist specialistChoice;
    String idSpecialistChoice;

    QuestionAdapter questionAdapter;


    public SpecilistInfoDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specilist_info_detail, container, false);
        ButterKnife.bind(this, view);
        setUpUI();
        return view;
    }

    public void setData(String idSpecialist) {
        this.idSpecialistChoice = idSpecialist;
    }

    private void setUpUI() {
        tv_description_specialist.setMovementMethod(new ScrollingMovementMethod());
        tb_detail_specilist.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tb_detail_specilist.setTitle("Thông tin chuyên môn");
        tb_detail_specilist.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tb_detail_specilist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());
            }
        });
        rlQuestions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rlQuestions.setFocusable(false);


        GetSpecialistDetailService getSpecialistDetailService = RetrofitFactory.getInstance().createService(GetSpecialistDetailService.class);
        getSpecialistDetailService.getSpecialistDetailService(idSpecialistChoice).enqueue(new Callback<MainObjectSpecialistDetail>() {
            @Override
            public  void onResponse(Call<MainObjectSpecialistDetail> call, Response<MainObjectSpecialistDetail> response) {
                if(response.code() == 200){
                    Log.e("specialistDetailA", "success: " + response.body().getObjSpecialist().toString());
                    MainObjectSpecialistDetail mainObjectSpecialist = response.body();
                    specialistChoice = mainObjectSpecialist.getObjSpecialist();
                    ZoomImageViewUtils.loadImageManual(getContext(),specialistChoice.getImage(),iv_specilist_detail);
                    tv_name_specilist.setText(specialistChoice.getName());
                    tv_description_specialist.setText(specialistChoice.getDescription());
                    questionAdapter = new QuestionAdapter(specialistChoice.getListQuestion());
                    rlQuestions.setAdapter(questionAdapter);
                    pbDetailSpecialist.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainObjectSpecialistDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                pbDetailSpecialist.setVisibility(View.GONE);
            }
        });

    }

}

class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<String> questions;

    public QuestionAdapter(List<String> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_specilist_question, parent, false);
        return new QuestionAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionAdapter.QuestionViewHolder holder, int position) {
        holder.setData(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
        }

        public void setData(String question) {
            tvQuestion.setText(question);
        }
    }

}
