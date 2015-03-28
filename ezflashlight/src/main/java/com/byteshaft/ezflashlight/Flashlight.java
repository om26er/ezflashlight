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

package com.byteshaft.ezflashlight;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Flashlight extends ContextWrapper implements SurfaceHolder.Callback {

    private Camera mCamera = null;
    private CameraSurfaceHelpers mCameraSurfaceHelpers = null;
    private FlashlightHelpers mFlashlightHelpers = null;
    private ArrayList<CameraStateChangeListener> mListeners = null;
    private CameraStateListenerHelpers mCameraStateListenerHelpers = null;

    public Flashlight(Context context) {
        super(context);
        mListeners = new ArrayList<>();
        mCameraStateListenerHelpers = new CameraStateListenerHelpers();
        mCameraSurfaceHelpers = new CameraSurfaceHelpers(this);
    }

    public void setOnCameraStateChangedListener(CameraStateChangeListener listener) {
        mListeners.add(listener);
    }

    public synchronized void initializeCamera() {
        try {
            mCamera = Camera.open();
            mFlashlightHelpers = new FlashlightHelpers(mCamera);
            mCameraStateListenerHelpers.dispatchEventOnCameraOpened(mListeners);
        } catch (RuntimeException e) {
            mCameraStateListenerHelpers.dispatchEventOnCameraBusy(mListeners);
            return;
        }
        mCameraSurfaceHelpers.setupCameraPreviewForTorch();
    }

    public synchronized void turnOn() {
        mFlashlightHelpers.setCameraModeTorch(true);
        mFlashlightHelpers.startCameraPreview(true);
        FlashlightGlobals.setIsFlashlightOn(true);
        mCameraStateListenerHelpers.dispatchEventOnFlashlightTurnedOn(mListeners);
    }

    public synchronized void turnOff() {
        mFlashlightHelpers.setCameraModeTorch(false);
        mFlashlightHelpers.startCameraPreview(false);
        FlashlightGlobals.setIsFlashlightOn(false);
        mCameraStateListenerHelpers.dispatchEventOnFlashlightTurnedOff(mListeners);
    }

    public synchronized void releaseAllResources() {
        if (FlashlightGlobals.isFlashlightOn()) {
            turnOff();
        }
        clearResources();
    }

    private void clearResources() {
        if (mCamera != null) {
            mFlashlightHelpers.releaseCamera();
        }
        mCameraSurfaceHelpers.destroyDummyViewSurface();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mFlashlightHelpers.setCameraPreviewDisplay(CameraSurfaceHelpers.getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCameraStateListenerHelpers.dispatchEventOnCameraViewSetup(mListeners);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        FlashlightGlobals.setIsResourceOccupied(false);
    }
}
