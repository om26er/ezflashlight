package byteshaft.com.ezflashlight;

public class FlashlightGlobals {

    private static boolean isFlashlightOn = false;
    private static boolean isResourceOccupied = false;

    static void setIsFlashlightOn(boolean on) {
        isFlashlightOn = on;
    }

    public static boolean isFlashlightOn() {
        return isFlashlightOn;
    }

    static void setIsResourceOccupied(boolean occupied) {
        isResourceOccupied = occupied;
    }

    public static boolean isResourceOccupied() {
        return isResourceOccupied;
    }
}
