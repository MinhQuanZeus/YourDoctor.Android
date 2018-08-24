package com.yd.yourdoctorandroid.activities;

import android.Manifest;
import android.app.AlarmManager;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.nhancv.npermission.NPermission;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.fragments.AboutUsFragment;
import com.yd.yourdoctorandroid.fragments.AdvisoryMenuFragment;
import com.yd.yourdoctorandroid.fragments.DoctorFavoriteListFragment;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.fragments.DoctorRankFragment;
import com.yd.yourdoctorandroid.fragments.UserProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.services.TimeOutChatService;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NPermission.OnPermissionResult {

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
    TextView tvNameUser;
    TextView tvMoneyUser;

    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private NPermission nPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        EventBus.getDefault().register(this);
        Log.d("MainActivity", "USER_INFO");
        Log.d("MainActivity", SharedPrefs.getInstance().get("USER_INFO", Patient.class).toString());
        Log.d("MainActivity", SharedPrefs.getInstance().get("JWT_TOKEN", String.class));
    }

    private void setupUI() {
        ButterKnife.bind(this);
        if (!checkPermission()) {
            Log.d("MAIN", "request checkPermission");
            nPermission = new NPermission(false);
            nPermission.requestPermission(this, Manifest.permission.CAMERA);
            nPermission.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        }
        View headerView = navigationViewMain.inflateHeaderView(R.layout.nav_header_main);
        ivAvaUser = headerView.findViewById(R.id.iv_ava_user);
        tvNameUser = headerView.findViewById(R.id.tv_name_user);
        tvMoneyUser = headerView.findViewById(R.id.tv_money_user);
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
            Picasso.with(this).load(currentPatient.getAvatar().toString()).into(ivAvaUser);
            tvMoneyUser.setText(currentPatient.getRemainMoney() + "" );
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
                animateFAB();
               ScreenManager.openFragment(getSupportFragmentManager(), new AdvisoryMenuFragment(), R.id.rl_container, true, true);
            }
        });
        fabVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
                Intent intent = new Intent(MainActivity.this, VideoCallActivity.class);
                startActivity(intent);
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
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pbMain.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSend eventSend) {
        if(eventSend.getType() == 1){
            currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
            if(currentPatient != null){
                tvNameUser.setText(currentPatient.getFullName());
                Picasso.with(this).load(currentPatient.getAvatar().toString()).into(ivAvaUser);
                tvMoneyUser.setText(currentPatient.getRemainMoney() + "" );
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
            super.onBackPressed();
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        // Handle navigation view item clicks here.
        if (item == null) return false;
        switch (item.getItemId()) {
            case R.id.nav_create_advisory_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new AdvisoryMenuFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_favorite_doctor_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorFavoriteListFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_exchange_money_main: {
                break;
            }
            case R.id.nav_profile_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new UserProfileFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_ranking_doctor_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorRankFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_logout_main: {
                //Test
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorProfileFragment(), R.id.rl_container, true, true);
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
        Log.e("MainActivityRe","backTo resume");
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        if(currentPatient != null){
            tvNameUser.setText(currentPatient.getFullName());
            ZoomImageViewUtils.loadImageManual(getApplicationContext(),currentPatient.getAvatar().toString(),ivAvaUser );
            //Picasso.with(this).load(currentPatient.getAvatar().toString()).into(ivAvaUser);
            tvMoneyUser.setText(currentPatient.getRemainMoney() + "" );
        }
        //setupUI();
        fabQuestion.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fabQuestion.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MAIN", "request");
        nPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String s, boolean b) {
        switch (s) {
            case Manifest.permission.CAMERA:
                if (!b) {
                    nPermission.requestPermission(this, Manifest.permission.CAMERA);
                }
                break;
            default:
                break;
        }
    }
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}




