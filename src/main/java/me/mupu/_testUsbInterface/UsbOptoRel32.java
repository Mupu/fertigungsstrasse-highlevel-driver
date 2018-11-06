package me.mupu._testUsbInterface;

import java.io.IOException;

public class UsbOptoRel32 {

    public boolean open() {
        return true;
    }

    public int in = 0;

    public int digitalIn() throws IOException {
        return in;
    }

    public void setIn(int in) {
        this.in = ~in;
    }

    public void digitalOut(int data) throws IOException {
    }
}
