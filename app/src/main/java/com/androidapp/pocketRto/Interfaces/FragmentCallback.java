package com.androidapp.pocketRto.Interfaces;

import android.support.annotation.StringRes;

public interface FragmentCallback
{
    public void showFab();
    public void hideFab();
    public void showSnackBar(@StringRes int message, @StringRes int button, int pos, int action);
}

