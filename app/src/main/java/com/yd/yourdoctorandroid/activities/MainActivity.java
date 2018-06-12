package com.yd.yourdoctorandroid.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.design.widget.TabLayout;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    RelativeLayout tb_main;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fab_question;
    ImageView iv_ava_user;
    TextView tv_name_user;
    TextView tv_money_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
    }

    private void setupUI() {
        tb_main = findViewById(R.id.tb_main);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fab_question = findViewById(R.id.fab_question);
        iv_ava_user = findViewById(R.id.iv_ava_user);
        tv_name_user = findViewById(R.id.tv_name_user);
        tv_money_user = findViewById(R.id.tv_money_user);
        fab_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ScreenManager.openFragment(getSupportFragmentManager(),new AdvisoryMenuFragment(),R.id.rl_container,true);
               Intent intent = new Intent(MainActivity.this,AdvisoryMenuActivity.class);

// Các dữ liệu gửi kèm


// Yêu cầu start Activity chỉ định trong Intent.
// (Không cần phản hồi từ Activity được gọi)
                startActivity(intent);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notifications_none_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_people_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_black_24dp));

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.icon_selected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.icon_unselected), PorterDuff.Mode.SRC_IN);
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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}



