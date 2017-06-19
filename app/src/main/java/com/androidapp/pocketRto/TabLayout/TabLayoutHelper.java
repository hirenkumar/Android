package com.androidapp.pocketRto.TabLayout;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class TabLayoutHelper {

    public interface IconPagerAdapter {
        @DrawableRes
        int getPageTitleIconRes(int position);
        @Nullable
        Drawable getPageTitleIconDrawable(int position);
    }

    public static void setupWithViewPager(TabLayout tabLayout,ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        setTabsFromPagerAdapter(tabLayout, viewPager.getAdapter());

    }

    public static void setTabsFromPagerAdapter(TabLayout tabLayout, PagerAdapter pagerAdapter) {
        if (!(pagerAdapter instanceof IconPagerAdapter)) {
            tabLayout.setTabsFromPagerAdapter(pagerAdapter);
            return;
        }
        final ColorStateList tabColors = tabLayout.getTabTextColors();
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            Drawable icon = ((IconPagerAdapter) pagerAdapter).getPageTitleIconDrawable(i);
            if (icon == null) {
                final int iconRes = ((IconPagerAdapter) pagerAdapter).getPageTitleIconRes(i);
                icon = ResourcesCompat.getDrawable(tabLayout.getResources(), iconRes, null);
            }
            icon = DrawableCompat.wrap(icon);
            DrawableCompat.setTintList(icon, tabColors);
            tabLayout.addTab(tabLayout.newTab()
                    .setIcon(icon));

        }
    }
}