package com.kekland.joysticktest;

/**
 * Created by kekland322 on 11/9/17.
 */

public class ControllerData {
    int stickLX;
    int stickLY;

    int stickRX;
    int stickRY;

    int dpadHorizontal;

    public int getDpadHorizontal() {
        return dpadHorizontal;
    }

    public void setDpadHorizontal(int dpadHorizontal) {
        this.dpadHorizontal = dpadHorizontal;
    }

    public int getDpadVertical() {
        return dpadVertical;
    }

    public void setDpadVertical(int dpadVertical) {
        this.dpadVertical = dpadVertical;
    }

    int dpadVertical;

    boolean buttonX;
    boolean buttonB;

    boolean buttonY;
    boolean buttonA;

    boolean buttonRB;
    boolean buttonLB;
    boolean buttonLT;
    boolean buttonRT;

    boolean precisionEnabled;
    boolean moveCatapult = false;

    public boolean isMoveCatapult() {
        return moveCatapult;
    }

    public void setMoveCatapult(boolean moveCatapult) {
        this.moveCatapult = moveCatapult;
    }

    public boolean isPrecisionEnabled() {
        return precisionEnabled;
    }

    public void setPrecisionEnabled(boolean precisionEnabled) {
        this.precisionEnabled = precisionEnabled;
    }

    public ControllerData() {
        setStickLX(0);
        setStickLY(0);
        setStickRX(0);
        setStickRY(0);

        setButtonA(false);
        setButtonB(false);
        setButtonX(false);
        setButtonY(false);
        setButtonLB(false);
        setButtonLT(false);
        setButtonRB(false);
        setButtonRT(false);
    }

    public int getStickLX() {
        return stickLX;
    }

    public void setStickLX(int stickLX) {

        stickLX = smooth(stickLX);
        this.stickLX = stickLX;
    }

    public int getStickLY() {
        return stickLY;
    }

    public void setStickLY(int stickLY) {
        stickLY = smooth(stickLY);
        this.stickLY = stickLY;
    }

    int smooth(int val) {
       if(Math.abs(val) < 30) {
           return 0;
       }
       if(Math.abs(val) > 80) {
           if(val > 0) {
               return 100;
           }
           return -100;
       }
       return val;
    }

    public int getStickRX() {
        return stickRX;
    }

    public void setStickRX(int stickRX) {
        stickRX = smooth(stickRX);
        this.stickRX = stickRX;
    }

    public int getStickRY() {
        return stickRY;
    }

    public void setStickRY(int stickRY) {
        stickRY = smooth(stickRY);
        this.stickRY = stickRY;
    }

    public boolean isButtonX() {
        return buttonX;
    }

    public void setButtonX(boolean buttonX) {
        this.buttonX = buttonX;
    }

    public boolean isButtonB() {
        return buttonB;
    }

    public void setButtonB(boolean buttonB) {
        this.buttonB = buttonB;
    }

    public boolean isButtonY() {
        return buttonY;
    }

    public void setButtonY(boolean buttonY) {
        this.buttonY = buttonY;
    }

    public boolean isButtonA() {
        return buttonA;
    }

    public void setButtonA(boolean buttonA) {
        this.buttonA = buttonA;
    }

    public boolean isButtonRB() {
        return buttonRB;
    }

    public void setButtonRB(boolean buttonRB) {
        this.buttonRB = buttonRB;
    }

    public boolean isButtonLB() {
        return buttonLB;
    }

    public void setButtonLB(boolean buttonLB) {
        this.buttonLB = buttonLB;
    }

    public boolean isButtonLT() {
        return buttonLT;
    }

    public void setButtonLT(boolean buttonLT) {
        this.buttonLT = buttonLT;
    }

    public boolean isButtonRT() {
        return buttonRT;
    }

    public void setButtonRT(boolean buttonRT) {
        this.buttonRT = buttonRT;
    }

    int boolToInt(boolean b) {
        return (b)? 1 : 0;
    }

    @Override
    public String toString() {
        String data = "";

        //Data:
        //StickLeftY;StickRightY;LB1;LB2;RB2;RB1#

        data +=
                getStickLX() + ";" + getStickLY() + ";" +
                        getStickRX() + ";" + getStickRY() + ";" +
                        getDpadHorizontal() + ";" + getDpadVertical() + ";" +
                        boolToInt(isButtonY()) + ";" +
                        boolToInt(isButtonB()) + ";" +
                        boolToInt(isButtonA()) + ";" +
                        boolToInt(isButtonX()) + ";" +
                        boolToInt(isButtonLB()) + ";" +
                        boolToInt(isButtonLT()) + ";" +
                        boolToInt(isButtonRB()) + ";" +
                        boolToInt(isButtonRT()) + ";" +
                        boolToInt(isPrecisionEnabled()) + ";" +
                        //boolToInt(isMoveCatapult()) + ";" +
                        "#";

        //Молодец баха

        return data;
    }

    public String toDetialedString() {
        String data = "";

        //Data:
        //StickLeftY;StickRightY;LB1;LB2;RB2;RB1#

        data += getStickLX() + ";" + getStickLY() + "|" +
                getStickRX() + ";" + getStickRY() + "#";

        //Молодец баха

        return data;
    }
}
