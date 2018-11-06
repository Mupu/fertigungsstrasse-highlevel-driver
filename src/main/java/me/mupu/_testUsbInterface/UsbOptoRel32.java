package me.mupu._testUsbInterface;

import java.io.IOException;

public class UsbOptoRel32 {

    public boolean open() {
        return true;
    }

    private int in = 0;

    public int digitalIn() throws IOException {
        return in;
    }

    public void setIn(int in) {
        System.out.println("set: " + Integer.toBinaryString(~in) + "\n");
        this.in = ~in;
    }

    public void digitalOut(int data) throws IOException {
        System.out.println("out: " + Integer.toBinaryString(data));
    }
}
