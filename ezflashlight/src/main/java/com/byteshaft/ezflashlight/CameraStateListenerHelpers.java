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

import java.util.ArrayList;

public class CameraStateListenerHelpers {

    void dispatchEventOnCameraOpened(ArrayList<CameraStateChangeListener>  listeners) {
        for (CameraStateChangeListener listener : listeners) {
            listener.onCameraOpened();
        }
    }

    void dispatchEventOnCameraViewSetup(ArrayList<CameraStateChangeListener>  listeners) {
        for (CameraStateChangeListener listener : listeners) {
            listener.onCameraViewSetup();
        }
    }

    void dispatchEventOnCameraBusy(ArrayList<CameraStateChangeListener>  listeners) {
        for (CameraStateChangeListener listener : listeners) {
            listener.onCameraBusy();
        }
    }

    void dispatchEventOnFlashlightTurnedOn(ArrayList<CameraStateChangeListener>  listeners) {
        for (CameraStateChangeListener listener : listeners) {
            listener.onFlashlightTurnedOn();
        }
    }

    void dispatchEventOnFlashlightTurnedOff(ArrayList<CameraStateChangeListener>  listeners) {
        for (CameraStateChangeListener listener : listeners) {
            listener.onFlashlightTurnedOff();
        }
    }
}