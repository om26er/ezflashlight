package byteshaft.com.ezflashlight;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Flashlight extends ContextWrapper implements SurfaceHolder.Callback {

    private static Camera sCamera = null;
    private CameraSurfaceHelpers helpers = null;
    private ArrayList<CameraInitializationListener> listeners = null;

    public Flashlight(Context context) {
        super(context);
        listeners = new ArrayList<>();
        helpers = new CameraSurfaceHelpers(Flashlight.this);
    }

    public void setOnCameraStateChangeListener(CameraInitializationListener listener) {
        listeners.add(listener);
    }

    public synchronized void initializeCamera() {
        if (sCamera == null) {
            try {
                sCamera = Camera.open();
                for (CameraInitializationListener listener : listeners) {
                    listener.onCameraOpened();
                }
            } catch (RuntimeException e) {
                for (CameraInitializationListener listener : listeners) {
                    listener.onCameraBusy();
                }
                return;
            }
            helpers.setupCameraPreviewForTorch();
        }
    }

    public synchronized void turnOn() {
        setCameraPreviewDisplay();
        setCameraModeTorch(true);
        startCameraPreview(true);
        for (CameraInitializationListener listener : listeners) {
            listener.onFlashlightOn();
        }
        FlashlightGlobals.setIsFlashlightOn(true);
    }

    public synchronized void turnOff() {
        setCameraModeTorch(false);
        startCameraPreview(false);
        for (CameraInitializationListener listener : listeners) {
            listener.onFlashlightOff();
        }
        FlashlightGlobals.setIsFlashlightOn(false);
    }

    public synchronized void releaseAllResources() {
        if (FlashlightGlobals.isFlashlightOn()) {
            turnOff();
        }
        clearResources();
        FlashlightGlobals.setIsResourceOccupied(false);
    }

    private void setCameraPreviewDisplay() {
        try {
            sCamera.setPreviewDisplay(CameraSurfaceHelpers.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraModeTorch(boolean ON) {
        Camera.Parameters mParams = sCamera.getParameters();
        if (ON) {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            sCamera.setParameters(mParams);
        } else {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            sCamera.setParameters(mParams);
        }
    }

    private void startCameraPreview(boolean start) {
        if (start) {
            sCamera.startPreview();
        } else {
            sCamera.stopPreview();
        }
    }

    private void clearResources() {
        releaseCamera();
        helpers.destroyDummyViewSurface();
    }

    private void releaseCamera() {
        if (sCamera != null) {
            sCamera.release();
            sCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        for (CameraInitializationListener listener : listeners) {
            listener.onCameraViewSetup();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
