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

    private CameraSurface mCameraSurface = null;
    private FlashlightHelpers mFlashlightHelpers = null;
    private ArrayList<CameraStateChangeListener> mListeners = null;
    private CameraStateListenerHelpers mCameraStateListenerHelpers = null;

    public Flashlight(Context context) {
        super(context);
        mListeners = new ArrayList<>();
        mCameraStateListenerHelpers = new CameraStateListenerHelpers();
        mCameraSurface = new CameraSurface(this);
    }

    public void setCameraStateChangedListener(CameraStateChangeListener listener) {
        mListeners.add(listener);
    }

    public void setupCameraPreview() {
        try {
            Camera camera = Camera.open();
            mCameraStateListenerHelpers.dispatchEventOnCameraOpened(mListeners);
            mFlashlightHelpers = new FlashlightHelpers(camera);
            mCameraSurface.create();
        } catch (RuntimeException ignore) {
            mCameraStateListenerHelpers.dispatchEventOnCameraBusy(mListeners);
        }
    }

    public synchronized void turnOn() {
        mFlashlightHelpers.setCameraModeTorch(true);
        FlashlightGlobals.setIsFlashlightOn(true);
    }

    public synchronized void turnOff() {
        mFlashlightHelpers.setCameraModeTorch(false);
        FlashlightGlobals.setIsFlashlightOn(false);
    }

    public void releaseAllResources() {
        if (FlashlightGlobals.isFlashlightOn()) {
            turnOff();
        }
        clearResources();
    }

    private void clearResources() {
        if (mFlashlightHelpers != null) {
            mFlashlightHelpers.releaseCamera();
        }
        mCameraSurface.destroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mFlashlightHelpers.setCameraPreviewDisplay(holder);
        mFlashlightHelpers.startCameraPreview();
        mCameraStateListenerHelpers.dispatchEventOnCameraViewSetup(mListeners);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        FlashlightGlobals.setIsResourceOccupied(false);
    }
}
