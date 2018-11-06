package me.mupu;

import me.mupu._testUsbInterface.UsbOptoRel32;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import static me.mupu.interfaces.bitpos.IInput.*;
import static me.mupu.interfaces.bitpos.IOutput.*;

import static me.mupu.interfaces.IKran.*;
import static me.mupu.interfaces.IMBohrmaschine.*;
import static me.mupu.interfaces.IMFraesmaschine.*;
import static me.mupu.interfaces.IMMehrspindelmaschine.*;
import static me.mupu.interfaces.IMSchieber.*;

public class Tests {

    FertigungsstrasseHLD f;
    UsbOptoRel32 usb;

    @Before
    public void init() {
        FertigungsstrasseHLD.getBohrmaschine(); // init instance of fertigungsstrassehld

        f = FertigungsstrasseHLD.instance;
        usb = FertigungsstrasseHLD.instance.usbInterface;

        System.out.println("Reset USB-Port:");
        usb.setIn(0); // alles aus = ~0
    }

    @Test
    public void schieberTest() {

        System.out.println(
                  "******************" +
                "\n* SCHIEBER-TESTS *" +
                "\n******************"
        );
        usb.setIn(I_01);
        System.out.println(f.istBelegtS());

    }

//    @Test
//    public void kranTest() {
//    }

}
