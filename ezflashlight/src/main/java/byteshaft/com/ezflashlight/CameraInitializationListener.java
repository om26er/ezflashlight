package byteshaft.com.ezflashlight;

public interface CameraInitializationListener {

    public void onCameraOpened();

    public void onCameraViewSetup();

    public void onCameraBusy();

    public void onFlashlightOn();

    public void onFlashlightOff();

}