package com.byteshaft.referencetorch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import byteshaft.com.ezflashlight.CameraInitializationListener;
import byteshaft.com.ezflashlight.Flashlight;
import byteshaft.com.ezflashlight.FlashlightGlobals;


public
class MainActivity extends ActionBarActivity implements ToggleButton.OnCheckedChangeListener,
        CameraInitializationListener {

    private Flashlight mFlashlight = null;
    private ToggleButton mFlashlightToggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlashlightToggle = (ToggleButton) findViewById(R.id.flashlightToggle);
        mFlashlightToggle.setOnCheckedChangeListener(this);
        mFlashlight = new Flashlight(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!FlashlightGlobals.isResourceOccupied()) {
            mFlashlight.setOnCameraStateChangeListener(this);
            mFlashlight.initializeCamera();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FlashlightGlobals.isFlashlightOn()) {
            mFlashlight.turnOff();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!FlashlightGlobals.isFlashlightOn()) {
            mFlashlight.releaseAllResources();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!FlashlightGlobals.isFlashlightOn()) {
            mFlashlight.releaseAllResources();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.flashlightToggle:
                if (isChecked) {
                    mFlashlight.turnOn();
                } else {
                    mFlashlight.turnOff();
                }
                break;
        }
    }

    @Override
    public void onCameraOpened() {
        mFlashlightToggle.setEnabled(true);
    }

    @Override
    public void onCameraViewSetup() {

    }

    @Override
    public void onCameraBusy() {
        Toast busyToast = Toast.makeText(getApplicationContext(),
                "Flashlight resource is busy", Toast.LENGTH_LONG);
        busyToast.show();
    }

    @Override
    public void onFlashlightOn() {

    }

    @Override
    public void onFlashlightOff() {

    }
}
