package com.androidapp.pocketRto.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.androidapp.pocketRto.Fragments.OfficeFragment;
import com.androidapp.pocketRto.Fragments.VehicleFragment;
import com.androidapp.pocketRto.Interfaces.FragmentCallback;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.clans.fab.FloatingActionButton;
import com.androidapp.pocketRto.R;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity implements FragmentCallback {
    public static String versionName = "1.0.0";

    public static int currentTab;

    private AppBarLayout appBar;
    private Toolbar toolbar;

    private Fragment vehicleFragment;
    private Fragment officeFragment;


    public final int ACTION_NULL = -1;

    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private FloatingActionButton fab;

    private MaterialTapTargetPrompt prompt;
    public static SharedPreferences sharedPref;

    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        try {
            versionName = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        appBar = (AppBarLayout) findViewById(R.id.top_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customToolbar = getLayoutInflater().inflate(R.layout.custom_toolbar, null);
        toolbar.addView(customToolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinate_layout);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setupFab();


        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.vehicle_tab, R.drawable.ic_directions_car_white_24dp, R.color.md_divider_white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.office_tab, R.drawable.ic_business_white_24dp, R.color.md_divider_white);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.setCurrentItem(0);


        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#BFFFFFFF"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#689F38"));

        // Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setBehaviorTranslationEnabled(true);
        //bottomNavigation.hideBottomNavigation(true);


        vehicleFragment = new VehicleFragment();
        officeFragment = new OfficeFragment();

        showFragment(FragmentName.vehicleFragment);


        currentTab = bottomNavigation.getCurrentItem();


        if (sharedPref.getBoolean(getString(R.string.vehicle_fab_first_run), true) == true) {
            prompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(findViewById(R.id.fab))
                    .setPrimaryText(R.string.tap_target_vehicle_fab_title)
                    .setSecondaryText(R.string.tap_target_vehicle_fab_content)
                    .setBackgroundColourFromRes(R.color.accent)
                    .setFocalColourFromRes(R.color.card_backgroundExpand)
                    .setAutoDismiss(false)
                    .setAutoFinish(false)
                    .setCaptureTouchEventOutsidePrompt(true)
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            if (tappedTarget) {
                                sharedPref.edit().putBoolean(getString(R.string.vehicle_fab_first_run), false).commit();
                                prompt.finish();
                            }
                        }

                        @Override
                        public void onHidePromptComplete() {

                        }
                    }).create();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    prompt.show();
                }
            }, 1000);
        }


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                currentTab = position;
                if (fab.isHidden())
                    fab.show(true);

                if (position == 0) {


                    showFragment(FragmentName.vehicleFragment);
                } else {
                    showFragment(FragmentName.officeFragment);

                    if (sharedPref.getBoolean(getString(R.string.office_fab_first_run), true) == true) {
                        prompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                .setTarget(findViewById(R.id.fab))
                                .setPrimaryText(R.string.tap_target_office_fab_title)
                                .setSecondaryText(R.string.tap_target_office_fab_content)
                                .setBackgroundColourFromRes(R.color.accent)
                                .setFocalColourFromRes(R.color.card_backgroundExpand)
                                .setAutoDismiss(false)
                                .setAutoFinish(false)
                                .setCaptureTouchEventOutsidePrompt(true)
                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                    @Override
                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                        if (tappedTarget) {
                                            sharedPref.edit().putBoolean(getString(R.string.office_fab_first_run), false).commit();
                                            prompt.finish();
                                        }
                                    }

                                    @Override
                                    public void onHidePromptComplete() {

                                    }
                                })
                                .show();
                    }
                }


                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void showFab() {
        fab.show(true);
    }

    @Override
    public void hideFab() {
        fab.hide(true);
    }

    @Override
    public void showSnackBar(@StringRes int message, @StringRes int button, final int pos, final int action) {
        snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        final Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = layout.getLayoutParams();
                if (lp instanceof CoordinatorLayout.LayoutParams) {
                    ((CoordinatorLayout.LayoutParams) lp).setBehavior(new DisableSwipeBehavior());
                    layout.setLayoutParams(lp);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.action_bar));

        if (action != ACTION_NULL) {
            snackbar.setAction(button, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((VehicleFragment) vehicleFragment).removeCardFromList(pos);
                }
            });
        }

        snackbar.show();
    }


    public class DisableSwipeBehavior extends SwipeDismissBehavior<Snackbar.SnackbarLayout> {
        @Override
        public boolean canSwipeDismissView(@NonNull View view) {
            return false;
        }
    }


    private void setupFab() {
        fab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show(true);
            }
        }, 500);

        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        fab.hide(true);

                        Log.i("TT", currentTab + "");
                        switch (currentTab) {

                            case 0: {
                                Log.i("TT", currentTab + "vehicleFragment");
                                ((VehicleFragment) vehicleFragment).fabAction();
                                break;
                            }

                            case 1: {
                                Log.i("TT", currentTab + "officeFragment");
                                ((OfficeFragment) officeFragment).fabAction();
                                break;
                            }
                        }
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return true;
    }


    public enum FragmentName {
        vehicleFragment(0), officeFragment(1);

        public final int value;

        FragmentName(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static FragmentName getNameByCode(int code) {
            for (FragmentName e : FragmentName.values()) {
                if (code == e.value)
                    return e;
            }
            return null;
        }
    }


    public void showFragment(FragmentName fragmentName) {

//        try {
//            exitFragment.unregisterExitReceiver();
//            entranceFragment.unregisterEntranceReceiver();
//        }
//        catch (Exception e){};


        switch (fragmentName) {
            case vehicleFragment:
                Log.v("TTT", "homeFragment");


                if (!vehicleFragment.isAdded()) {
                    Log.v("TTT", "homeFragment if");
                    getSupportFragmentManager().beginTransaction().add(R.id.content_frame_main, vehicleFragment, "Home").commitAllowingStateLoss();
                    getSupportFragmentManager().beginTransaction().show(vehicleFragment).commitAllowingStateLoss();
                } else {
                    Log.v("TTT", "homeFragment else");
                    getSupportFragmentManager().beginTransaction().show(vehicleFragment).commitAllowingStateLoss();
                }

                getSupportFragmentManager().beginTransaction().hide(officeFragment).commitAllowingStateLoss();


                break;

            case officeFragment:


                if (!officeFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().add(R.id.content_frame_main, officeFragment, "Entrance").commitAllowingStateLoss();
                    getSupportFragmentManager().beginTransaction().show(officeFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().show(officeFragment).commitAllowingStateLoss();
                }

                getSupportFragmentManager().beginTransaction().hide(vehicleFragment).commitAllowingStateLoss();


                break;
            default:
                break;
        }
    }


}
