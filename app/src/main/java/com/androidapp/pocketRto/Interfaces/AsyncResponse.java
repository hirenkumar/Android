package com.androidapp.pocketRto.Interfaces;

import com.androidapp.pocketRto.Models.Vehicle;

public interface AsyncResponse
{
    void processFinish(Vehicle vehicle, int statuscode);
}
