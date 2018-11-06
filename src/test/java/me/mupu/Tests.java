package me.mupu;

import me.mupu._testUsbInterface.UsbOptoRel32;

import static me.mupu.interfaces.bitpos.IInput.*;

public class Tests {

    public static void main(String[] args) {
        new Tests();
    }

    public Tests() {
        FertigungsstrasseHLD.getBohrmaschine(); // init instance of fertigungsstrassehld
        FertigungsstrasseHLD f = FertigungsstrasseHLD.instance;
        UsbOptoRel32 usb = FertigungsstrasseHLD.instance.usbInterface;

        /**
         * SCHIEBER
         */
        usb.setIn(0); // alles aus = ~0
        usb.setIn(I_01);
        System.out.println(f.istBelegtS());


    }

}
