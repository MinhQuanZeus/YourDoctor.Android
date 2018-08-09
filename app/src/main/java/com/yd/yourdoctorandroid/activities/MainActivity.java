package com.yd.yourdoctorandroid.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
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

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.fabQuestion)
    FloatingActionButton fabQuestion;

    @BindView(R.id.drawLayoutMain)
    DrawerLayout drawerViewMenu;

    @BindView(R.id.navViewMenu)
    NavigationView navigationViewMain;

    @BindView(R.id.toolbar)
    Toolbar tbMain;

    @BindView(R.id.pbMain)
    ProgressBar pbMain;

    Patient currentPatient;

    ImageView ivAvaUser;
    TextView tvNameUser;
    TextView tvMoneyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();

        Log.d("MainActivity", "USER_INFO");
        Log.d("MainActivity", SharedPrefs.getInstance().get("USER_INFO", Patient.class).toString());
        Log.d("MainActivity", SharedPrefs.getInstance().get("JWT_TOKEN", String.class));
    }

    private void setupUI() {
        ButterKnife.bind(this);

        View headerView = navigationViewMain.inflateHeaderView(R.layout.nav_header_main);
        ivAvaUser = headerView.findViewById(R.id.ivAvaUser);
        tvNameUser = headerView.findViewById(R.id.tvNameUser);
        tvMoneyUser = headerView.findViewById(R.id.tvMoneyUser);

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
                ScreenManager.openFragment(getSupportFragmentManager(), new AdvisoryMenuFragment(), R.id.rlContainer, true, true);
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
            case R.id.navCreateAdvisoryMain: {
                ScreenManager.openFragment(getSupportFragmentManager(), new AdvisoryMenuFragment(), R.id.rlContainer, true, true);
                break;
            }
            case R.id.navFavoriteDoctorMain: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorFavoriteListFragment(), R.id.rlContainer, true, true);
                break;
            }
            case R.id.navExchangeMoneyMain: {
                break;
            }
            case R.id.navProfileMain: {
                ScreenManager.openFragment(getSupportFragmentManager(), new UserProfileFragment(), R.id.rlContainer, true, true);
                break;
            }
            case R.id.navRankingDoctoMain: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorRankFragment(), R.id.rlContainer, true, true);
                break;
            }
            case R.id.navLogoutMain: {
                //Test
                handleLogOut();
                //ScreenManager.openFragment(getSupportFragmentManager(), new DoctorProfileFragment(), R.id.rlContainer, true, true);
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
}




