package com.yd.yourdoctorandroid.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.nhancv.npermission.NPermission;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.YourDoctorApplication;
import com.yd.yourdoctorandroid.adapters.ChatAdapter;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
import com.yd.yourdoctorandroid.adapters.SpecialistAdapter;
import com.yd.yourdoctorandroid.adapters.SpecialistChoiceAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.fragments.AboutUsFragment;
import com.yd.yourdoctorandroid.fragments.AdvisoryMenuFragment;
import com.yd.yourdoctorandroid.fragments.DoctorFavoriteListFragment;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.fragments.DoctorRankFragment;
import com.yd.yourdoctorandroid.fragments.UserProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.services.TimeOutChatService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_PERMISSION_CODE = 1;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.fab_question)
    FloatingActionButton fabQuestion;

    @BindView(R.id.fab_chat)
    FloatingActionButton fabChat;

    @BindView(R.id.fab_video_call)
    FloatingActionButton fabVideoCall;

    @BindView(R.id.draw_layout_main)
    DrawerLayout drawerViewMenu;

    @BindView(R.id.nav_view_menu)
    NavigationView navigationViewMain;

    @BindView(R.id.toolbar)
    Toolbar tbMain;

    @BindView(R.id.pb_main)
    ProgressBar pbMain;

    Patient currentPatient;

    ImageView ivAvaUser;
    ImageView ivAvaUserBackGroud;
    TextView tvNameUser;


    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void setupUI() {
        ButterKnife.bind(this);
        if (!checkPermission()) {
            requestPermission();
        }
        View headerView = navigationViewMain.inflateHeaderView(R.layout.nav_header_main);
        ivAvaUser = headerView.findViewById(R.id.iv_ava_user);
        tvNameUser = headerView.findViewById(R.id.tv_name_user);
        ivAvaUserBackGroud = headerView.findViewById(R.id.iv_ava_user_back_groud);
        // Animation
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        setSupportActionBar(tbMain);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        if(currentPatient != null){
            tvNameUser.setText(currentPatient.getFullName());
            ZoomImageViewUtils.loadImageManual(getApplicationContext(),currentPatient.getAvatar().toString(),ivAvaUserBackGroud);
            ZoomImageViewUtils.loadCircleImage(getApplicationContext(),currentPatient.getAvatar().toString(),ivAvaUser);
            tvNameUser.setText(currentPatient.getFullName());
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerViewMenu, tbMain, R.string.app_name, R.string.app_name);
        drawerViewMenu.setDrawerListener(toggle);
        toggle.syncState();

        navigationViewMain.setNavigationItemSelectedListener(this);

        fabQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChooseSpecialist(true);

            }
        });
        fabVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChooseSpecialist(false);

            }
        });

        tbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerViewMenu.openDrawer(GravityCompat.START);
            }
        });


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notifications_none_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_book_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_history_black_24dp));

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //tabLayout.setScrollPosition(1,0f,true);
        tabLayout.getTabAt(1).select();
        viewPager.setCurrentItem(1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(pbMain != null) pbMain.setVisibility(View.GONE);

    }

    Dialog dialogChooseSpecialist;

    private Button btnCancelChoose;
    private Button btnOkChoose;
    private RecyclerView rvListSpecilist;
    private ArrayList<Specialist> specialists;
    private Specialist specialistChoice;
    private ProgressBar pbChoose;
    private SpecialistChoiceAdapter specialistChoiceAdapter;
    public void showDialogChooseSpecialist(Boolean isChat) {
        dialogChooseSpecialist = new Dialog(MainActivity.this);

        dialogChooseSpecialist.setContentView(R.layout.choose_doctor_dialog);
        dialogChooseSpecialist.setTitle("Lựa Chọn Chuyên khoa bạn muốn tư vấn");

        // set the custom dialog components - text, image and button
        btnCancelChoose = dialogChooseSpecialist.findViewById(R.id.btn_cancel_choose_doctor);
        btnOkChoose = dialogChooseSpecialist.findViewById(R.id.btn_ok_choose_doctor);
        rvListSpecilist = dialogChooseSpecialist.findViewById(R.id.rv_list_doctor);
        pbChoose = dialogChooseSpecialist.findViewById(R.id.pb_choose);
        if(pbChoose != null) pbChoose.setVisibility(View.VISIBLE);

        specialists = (ArrayList<Specialist>) LoadDefaultModel.getInstance().getSpecialists();
        if(specialists == null){
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public  void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                    if(response.code() == 200){
                        MainObjectSpecialist mainObjectSpecialist = response.body();
                        specialists = (ArrayList<Specialist>) mainObjectSpecialist.getListSpecialist();
                        specialistChoiceAdapter = new SpecialistChoiceAdapter(specialists,getApplicationContext(),dialogChooseSpecialist);
                        rvListSpecilist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        rvListSpecilist.setAdapter(specialistChoiceAdapter);
                        LoadDefaultModel.getInstance().setSpecialists(specialists);
                        }else{
                        Toast.makeText(getApplicationContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();

                    }
                    if(pbChoose != null) pbChoose.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    if(pbChoose != null) pbChoose.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Kết nốt mạng có vấn đề , không thể tải được các chuyên khoa!", Toast.LENGTH_LONG).show();
                }
            });

        }else {
            specialistChoiceAdapter = new SpecialistChoiceAdapter(specialists,getApplicationContext(),dialogChooseSpecialist);
            rvListSpecilist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            rvListSpecilist.setAdapter(specialistChoiceAdapter);
            if(pbChoose != null) pbChoose.setVisibility(View.GONE);
            dialogChooseSpecialist.show();
        }



        btnCancelChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooseSpecialist.dismiss();
            }
        });

        btnOkChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialistChoice = specialistChoiceAdapter.getSpecialistChoice();
                if(specialistChoice != null){
                    if(isChat){
                        animateFAB();
                        AdvisoryMenuFragment advisoryMenuFragment = new AdvisoryMenuFragment();
                        advisoryMenuFragment.setData(specialistChoice);
                        ScreenManager.openFragment(getSupportFragmentManager(), advisoryMenuFragment, R.id.rl_container, true, true);
                    }else {
                        animateFAB();
                        Intent intent = new Intent(MainActivity.this, VideoCallActivity.class);
                        intent.putExtra("idSpecialistChoice", specialistChoice.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    dialogChooseSpecialist.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"Bạn chưa lựa chọn chuyên ngành nào", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogChooseSpecialist.show();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSend eventSend) {
        if(eventSend.getType() == 1){
            currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
            if(currentPatient != null){
                tvNameUser.setText(currentPatient.getFullName());
                ivAvaUserBackGroud.setImageResource(R.drawable.your_doctor_logo);
                ivAvaUser.setImageResource(R.drawable.your_doctor_logo);
                ZoomImageViewUtils.loadImageManual(getApplicationContext(),currentPatient.getAvatar().toString(),ivAvaUserBackGroud);
                ZoomImageViewUtils.loadCircleImage(getApplicationContext(),currentPatient.getAvatar().toString(),ivAvaUser);
            }
        }
    }


    public void animateFAB(){

        if(isFabOpen){

            fabQuestion.startAnimation(rotate_backward);
            fabVideoCall.startAnimation(fab_close);
            fabChat.startAnimation(fab_close);
            fabVideoCall.setClickable(false);
            fabChat.setClickable(false);
            isFabOpen = false;
        } else {

            fabQuestion.startAnimation(rotate_forward);
            fabVideoCall.startAnimation(fab_open);
            fabChat.startAnimation(fab_open);
            fabVideoCall.setClickable(true);
            fabChat.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerViewMenu.isDrawerOpen(GravityCompat.START)) {
            drawerViewMenu.closeDrawer(GravityCompat.START);
        } else {
            handleLogOut();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerViewMenu.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item == null) return false;
        switch (item.getItemId()) {
            case R.id.nav_favorite_doctor_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorFavoriteListFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_profile_main: {

                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setData(tvNameUser,ivAvaUser,ivAvaUserBackGroud);

                ScreenManager.openFragment(getSupportFragmentManager(), userProfileFragment, R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_ranking_doctor_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorRankFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_about_us: {
                ScreenManager.openFragment(getSupportFragmentManager(), new AboutUsFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_logout_main: {
                //Test
                handleLogOut();
                //ScreenManager.openFragment(getSupportFragmentManager(), new DoctorProfileFragment(), R.id.rl_container, true, true);
                break;

            }
        }

        drawerViewMenu.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogOut(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn có chắc muốn thoát khỏi hệ thống không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //YourDoctorApplication.self().getSocket().close();
                        YourDoctorApplication.self().getSocket().disconnect();
                        Utils.backToLogin(getApplicationContext());
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setupUI();
        fabQuestion.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fabQuestion.setVisibility(View.INVISIBLE);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        requestPermission();
                    }
                }
                break;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{RECORD_AUDIO, CAMERA}, REQUEST_PERMISSION_CODE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }
}




