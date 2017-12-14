package com.android.customtablayout;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Typeface font;
    Integer direction = null;
    final int DIRECTION_LEFT = 1;
    final int DIRECTION_RIGHT = 2;
    int currentTab = 0;
    int nextTab = 0;
    Float startingFrom = null;
    Context context;
    float selectedTab = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        context = this;
    }

    private void initView() {
        font  = Typeface.createFromAsset(getAssets(), "font.otf");
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        List<String> tabHeaders = new ArrayList<>();
        tabHeaders.add("Sports");
        tabHeaders.add("Technologies");
        tabHeaders.add("Design");
        tabHeaders.add("Politics");
        tabHeaders.add("Movies");
        tabHeaders.add("TV Shows");
        tabHeaders.add("Automobiles");
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragments(tabHeaders);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        for(int i = 0;i<tabHeaders.size();i++){
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
            tv.setText(tabHeaders.get(i));
            tv.setTypeface(font, Typeface.BOLD);
            if(i == 0)
                tv.setTextColor(getResources().getColor(R.color.black));
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(direction == null){
                    if((position + positionOffset) > selectedTab){
                        direction = DIRECTION_RIGHT;
                        currentTab = (int) selectedTab;
                        nextTab = (int) (selectedTab + 1);
                    } else if((position + positionOffset) < selectedTab){
                        direction = DIRECTION_LEFT;
                        nextTab = (int) selectedTab;
                        currentTab = position;
                    }
                }
                if(positionOffset == 0.0){
                    startingFrom = null;
                    direction = null;
                    selectedTab = position;
                } else {
                    try {
                        int colorVal = (int) argbEvaluator.evaluate(positionOffset,
                                Color.parseColor("#000000"), Color.parseColor("#bbbbbb"));
                        int colorVal2 = (int) argbEvaluator.evaluate(positionOffset,
                                Color.parseColor("#bbbbbb"), Color.parseColor("#000000"));
                        TextView tv = (TextView) tabLayout.getTabAt(currentTab).getCustomView();
                        tv.setTextColor(colorVal);
                        TextView previousTextView = (TextView) tabLayout.getTabAt(nextTab).getCustomView();
                        previousTextView.setTextColor(colorVal2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragments(List<String> tabHeaders) {
            for (String header:tabHeaders) {
                MyFragment myFragment = new MyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("text", header);
                myFragment.setArguments(bundle);
                addFragment(myFragment, header);
            }
        }
    }
}
