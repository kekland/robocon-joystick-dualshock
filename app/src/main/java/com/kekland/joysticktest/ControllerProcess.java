package com.kekland.joysticktest;

import android.os.Debug;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by okushy on 06.12.2017.
 */

public class ControllerProcess {
    public ControllerData data = new ControllerData();
    Dpad dpad = new Dpad();
    int dir = 1;

    public boolean HandleButtonsDown(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                handled = handleKeyCodes(keyCode, true);
            }
            if (handled) {
                return true;
            }
        }
        return false;
    }

    public boolean HandleButtonsUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                handled = handleKeyCodes(keyCode, false);
            }
            if (handled) {
                return true;
            }
        }
        return false;
    }

    boolean handleKeyCodes(int keyCode, boolean direction) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BUTTON_Y:
                data.setButtonY(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_A:
                data.setButtonA(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_X:
                data.setButtonX(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_B:
                data.setButtonB(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_L1:
                data.setButtonLB(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_R1:
                data.setButtonRB(direction);
                return true;
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
                if(direction) {
                    data.setPrecisionEnabled(!data.isPrecisionEnabled());
                }
                return true;
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                data.setMoveCatapult(direction);
                return true;
        }
        return false;
    }

    public boolean HandleStickMovement(MotionEvent event) {
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            data.setStickLX(Math.round(event.getAxisValue(MotionEvent.AXIS_X) * 100 * dir));
            data.setStickLY(Math.round(event.getAxisValue(MotionEvent.AXIS_Y) * -100 * dir));

            data.setStickRX(Math.round(event.getAxisValue(MotionEvent.AXIS_Z) * 100 * dir));
            data.setStickRY(Math.round(event.getAxisValue(MotionEvent.AXIS_RZ) * -100 * dir));

            float lt = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
            if(lt > 0.5) {
                data.setButtonLT(true);
            }
            else {
                data.setButtonLT(false);
            }

            float rt = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            if(rt > 0.5) {
                data.setButtonRT(true);
            }
            else {
                data.setButtonRT(false);
            }
            float hatX = event.getAxisValue(MotionEvent.AXIS_HAT_X);
            float hatY = event.getAxisValue(MotionEvent.AXIS_HAT_Y);

            data.setDpadHorizontal(Math.round(hatX * 100));
            data.setDpadVertical(-Math.round(hatY * 100));
            return true;
        }
        return false;
    }
}
