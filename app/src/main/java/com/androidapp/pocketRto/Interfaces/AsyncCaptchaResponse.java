package com.androidapp.pocketRto.Interfaces;

import android.graphics.Bitmap;

public interface AsyncCaptchaResponse
{
    void processFinish(Bitmap bitmap, int statuscode);
}
