//package me.mupu._testUsbInterface;
//
//import me.mupu.FertigungsstrasseHLD;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//
//public class UsbOptoRel32 {
//
//    public boolean open() {
//        return true;
//    }
//
//    public void close() {
//    }
//
//    public int in = 0;
//    public int out = 0;
//
//    public int digitalIn() throws IOException {
//        return in;
//    }
//
//    public void setIn(int in) {
//        this.in = ~in;
//
//        // instantly update
//        FertigungsstrasseHLD.instance.input = ~in;
//    }
//
//    public void digitalOut(int data) throws IOException {
//        out = data;
//    }
//}
