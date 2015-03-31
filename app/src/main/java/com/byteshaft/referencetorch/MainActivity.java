/*
 *
 *  *
 *  *  * (C) Copyright 2015 byteShaft Inc.
 *  *  *
 *  *  * All rights reserved. This program and the accompanying materials
 *  *  * are made available under the terms of the GNU Lesser General Public License
 *  *  * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *  *  * http://www.gnu.org/licenses/lgpl-2.1.html
 *  *  *
 *  *  * This library is distributed in the hope that it will be useful,
 *  *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  *  * Lesser General Public License for more details.
 *  *  
 *
 */

package com.byteshaft.referencetorch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.ezflashlight.FlashlightGlobals;


public class MainActivity extends ActionBarActivity implements ToggleButton.OnCheckedChangeListener,
        CameraStateChangeListener {

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
    protected void onResume() {
        super.onResume();
        if (!FlashlightGlobals.isResourceOccupied()) {
            mFlashlight.setCameraStateChangedListener(this);
            mFlashlight.setupCameraPreview();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFlashlight.releaseAllResources();
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
                    if (!FlashlightGlobals.isResourceOccupied()) {
                        mFlashlight.setupCameraPreview();
                        mFlashlight.turnOn();
                    } else {
                        mFlashlight.turnOn();
                    }
                } else {
                    mFlashlight.turnOff();
                }
                break;
        }
    }

    @Override
    public void onCameraInitialized() {
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
}
