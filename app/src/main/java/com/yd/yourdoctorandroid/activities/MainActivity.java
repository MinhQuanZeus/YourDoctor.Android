package com.yd.yourdoctorandroid.activities;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
import com.yd.yourdoctorandroid.fragments.AdvisoryMenuFragment;
import com.yd.yourdoctorandroid.fragments.DoctorFavoriteListFragment;
import com.yd.yourdoctorandroid.fragments.DoctorProfileFragment;
import com.yd.yourdoctorandroid.fragments.DoctorRankFragment;
import com.yd.yourdoctorandroid.fragments.UserProfileFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.FavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListDoctorFavoriteService;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListIDFavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectFavoriteList;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectIDFavorite;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.models.Patient;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.fab_question)
    FloatingActionButton fab_question;

    @BindView(R.id.draw_layout_main)
    DrawerLayout draw_layout_main;

    @BindView(R.id.nav_view_menu)
    NavigationView navigationView_main;

    @BindView(R.id.toolbar)
    Toolbar tb_main;

    @BindView(R.id.pb_main)
    ProgressBar pb_main;

    Patient currentPatient;

    ImageView iv_ava_user;
    TextView tv_name_user;
    TextView tv_money_user;

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


        View headerView = navigationView_main.inflateHeaderView(R.layout.nav_header_main);
        iv_ava_user = headerView.findViewById(R.id.iv_ava_user);
        tv_name_user = headerView.findViewById(R.id.tv_name_user);
        tv_money_user = headerView.findViewById(R.id.tv_money_user);

        setSupportActionBar(tb_main);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        if(currentPatient != null){
            tv_name_user.setText(currentPatient.getfName() + " " + currentPatient.getmName() == null? "" : currentPatient.getmName() + " " + currentPatient.getlName());
            Picasso.with(this).load(currentPatient.getAvatar().toString()).transform(new CropCircleTransformation()).into(iv_ava_user);
            tv_money_user.setText(currentPatient.getRemainMoney() + "" );
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, draw_layout_main, tb_main, R.string.app_name, R.string.app_name);
        draw_layout_main.setDrawerListener(toggle);
        toggle.syncState();

        navigationView_main.setNavigationItemSelectedListener(this);

        fab_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ScreenManager.openFragment(getSupportFragmentManager(), new AdvisoryMenuFragment(), R.id.rl_container, true, true);
            }
        });

        tb_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_layout_main.openDrawer(GravityCompat.START);
            }
        });


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notifications_none_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_history_black_24dp));

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
        pb_main.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {

        if (draw_layout_main.isDrawerOpen(GravityCompat.START)) {
            draw_layout_main.closeDrawer(GravityCompat.START);
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
                draw_layout_main.openDrawer(GravityCompat.START);
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
            case R.id.nav_ranking_docto_main: {
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorRankFragment(), R.id.rl_container, true, true);
                break;
            }
            case R.id.nav_logout_main: {
                //Test
                ScreenManager.openFragment(getSupportFragmentManager(), new DoctorProfileFragment(), R.id.rl_container, true, true);
                break;

            }
        }

        draw_layout_main.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab_question.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fab_question.setVisibility(View.INVISIBLE);
    }
}




