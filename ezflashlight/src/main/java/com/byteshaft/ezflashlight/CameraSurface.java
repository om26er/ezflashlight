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
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraSurface extends ContextWrapper {

    private Flashlight mFlashlight = null;
    private WindowManager mWindowManager = null;
    private SurfaceView mDummyPreview = null;

    public CameraSurface(Flashlight flashlight) {
        super(flashlight);
        mFlashlight = flashlight;
    }

    void create() {
        mDummyPreview = getDummySurface();
        SurfaceHolder holder = getHolderForPreview(mDummyPreview);
        setColorForPreview(mDummyPreview);
        setupCompatibilityForPreHoneycombDevices(holder);
        addCallbackForHolder(holder);
        createSystemOverlayForPreview(mDummyPreview);
        FlashlightGlobals.setIsResourceOccupied(true);
    }

    void destroy() {
        if (mWindowManager != null && mDummyPreview != null) {
            mWindowManager.removeView(mDummyPreview);
            mDummyPreview = null;
        }
    }

    private SurfaceView getDummySurface() {
        return new SurfaceView(this);
    }

    private void setColorForPreview(SurfaceView preview) {
        preview.setBackgroundColor(Color.BLACK);
    }

    private SurfaceHolder getHolderForPreview(SurfaceView preview) {
        return preview.getHolder();
    }

    @SuppressWarnings("deprecation")
    private void setupCompatibilityForPreHoneycombDevices(SurfaceHolder holder) {
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void addCallbackForHolder(SurfaceHolder holder) {
        holder.addCallback(mFlashlight);
    }

    private void createSystemOverlayForPreview(SurfaceView previewForCamera) {
        mWindowManager = getWindowManager();
        WindowManager.LayoutParams params = getCustomWindowManagerParameters();
        mWindowManager.addView(previewForCamera, params);
    }

    private WindowManager getWindowManager() {
        return (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    private WindowManager.LayoutParams getCustomWindowManagerParameters() {
        final int ONE_PIXEL = 1;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = ONE_PIXEL;
        params.width = ONE_PIXEL;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.BOTTOM | Gravity.END;
        return params;
    }
}
