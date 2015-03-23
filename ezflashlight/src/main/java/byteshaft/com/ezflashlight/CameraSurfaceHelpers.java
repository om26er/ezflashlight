package byteshaft.com.ezflashlight;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraSurfaceHelpers extends ContextWrapper {

    private static SurfaceHolder sSurfaceHolder = null;
    private Flashlight mFlashlight = null;
    private WindowManager mWindowManager = null;
    private SurfaceView mDummyPreview = null;

    public CameraSurfaceHelpers(Flashlight flashlight) {
        super(flashlight);
        mFlashlight = flashlight;
    }

    public static SurfaceHolder getHolder() {
        return sSurfaceHolder;
    }

    protected void setupCameraPreviewForTorch() {
        mDummyPreview = getDummySurface();
        sSurfaceHolder = getHolderForPreview(mDummyPreview);
        setColorForPreview(mDummyPreview);
        setupCompatibilityForPreHoneycombDevices(sSurfaceHolder);
        addCallbackForHolder(sSurfaceHolder);
        createSystemOverlayForPreview(mDummyPreview);
        FlashlightGlobals.setIsResourceOccupied(true);
    }

    protected void destroyDummyViewSurface() {
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
