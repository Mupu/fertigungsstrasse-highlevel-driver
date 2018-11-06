package me.mupu.tests;

import me.mupu.FertigungsstrasseHLD;
import me.mupu._testUsbInterface.UsbOptoRel32;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import me.mupu.interfaces.IKran;
import me.mupu.interfaces.IKran.*;

import static me.mupu.interfaces.bitpos.IInput.*;
import static me.mupu.interfaces.bitpos.IOutput.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KranTests {

    private IKran kran;
    private UsbOptoRel32 usb;
    private FertigungsstrasseHLD f;

    @BeforeAll
    void beforeAll() {
        kran = FertigungsstrasseHLD.getKran();

        f = FertigungsstrasseHLD.instance;

        usb = FertigungsstrasseHLD.instance.usbInterface;

        System.out.println("******************" +
                "\n* SCHIEBER-TESTS *" +
                "\n******************"
        );
    }

    @BeforeEach
    void beforeEach() {
        usb.setIn(0); // reset USBInterface
        FertigungsstrasseHLD.instance.output = 0;
    }


    @Test
    void setStatusMotorXAchseK() {
        // AUS
        f.output = Q_18 | Q_19; // setzte bits
        kran.setStatusMotorXAchseK(Q_XAchseK.AUS);
        assertEquals(f.output & (Q_18 | Q_19), 0);


        // LINKS
        beforeEach();
        f.output = Q_18;
        kran.setStatusMotorXAchseK(Q_XAchseK.LINKS);
        assertEquals(f.output & (Q_18 | Q_19), Q_19);


        // RECHTS
        beforeEach();
        f.output = Q_19;
        kran.setStatusMotorXAchseK(Q_XAchseK.RECHTS);
        assertEquals(f.output & (Q_18 | Q_19), Q_18);




        // GRENZE LINKS
        beforeEach();
        usb.setIn(I_18);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorXAchseK(Q_XAchseK.LINKS));


        // GRENZE RECHTS
        beforeEach();
        usb.setIn(I_17);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorXAchseK(Q_XAchseK.RECHTS));
    }

    @Test
    void getStatusMotorXAchseK() {
        // DAZWISCHEN
        assertEquals(kran.getStatusMotorXAchseK(), I_XAchseK.DAZWISCHEN);

        // RECHTS
        beforeEach();
        usb.setIn(I_17);
        assertEquals(kran.getStatusMotorXAchseK(), I_XAchseK.RECHTS);

        // LINKS
        beforeEach();
        usb.setIn(I_18);
        assertEquals(kran.getStatusMotorXAchseK(), I_XAchseK.LINKS);

        // ERROR
        beforeEach();
        usb.setIn(I_17 | I_18);
        assertThrows(RuntimeException.class, () -> kran.getStatusMotorXAchseK());
    }






    @Test
    void setStatusMotorYAchseK() {
        // AUS
        f.output = Q_20 | Q_21; // setzte bits
        kran.setStatusMotorYAchseK(Q_YAchseK.AUS);
        assertEquals(f.output & (Q_20 | Q_21), 0);


        // VOR
        beforeEach();
        f.output = Q_21;
        kran.setStatusMotorYAchseK(Q_YAchseK.VOR);
        assertEquals(f.output & (Q_20 | Q_21), Q_20);


        // ZURUECK
        beforeEach();
        f.output = Q_20;
        kran.setStatusMotorYAchseK(Q_YAchseK.ZURUECK);
        assertEquals(f.output & (Q_20 | Q_21), Q_21);




        // GRENZE VORNE
        beforeEach();
        usb.setIn(I_19);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorYAchseK(Q_YAchseK.VOR));


        // GRENZE HINTEN
        beforeEach();
        usb.setIn(I_20);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorYAchseK(Q_YAchseK.ZURUECK));
    }

    @Test
    void getStatusMotorYAchseK() {
        // DAZWISCHEN
        assertEquals(kran.getStatusMotorYAchseK(), I_YAchseK.DAZWISCHEN);

        // VORNE
        beforeEach();
        usb.setIn(I_19);
        assertEquals(kran.getStatusMotorYAchseK(), I_YAchseK.VORNE);

        // HINTEN
        beforeEach();
        usb.setIn(I_20);
        assertEquals(kran.getStatusMotorYAchseK(), I_YAchseK.HINTEN);

        // ERROR
        beforeEach();
        usb.setIn(I_19 | I_20);
        assertThrows(RuntimeException.class, () -> kran.getStatusMotorYAchseK());
    }






    @Test
    void setStatusMotorZAchseK() {
        // AUS
        f.output = Q_22 | Q_23; // setzte bits
        kran.setStatusMotorZAchseK(Q_ZAchseK.AUS);
        assertEquals(f.output & (Q_22 | Q_23), 0);


        // HOCH
        beforeEach();
        f.output = Q_23;
        kran.setStatusMotorZAchseK(Q_ZAchseK.HOCH);
        assertEquals(f.output & (Q_22 | Q_23), Q_22);


        // RUNTER
        beforeEach();
        f.output = Q_22;
        kran.setStatusMotorZAchseK(Q_ZAchseK.RUNTER);
        assertEquals(f.output & (Q_22 | Q_23), Q_23);




        // GRENZE OBEN
        beforeEach();
        usb.setIn(I_21);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorZAchseK(Q_ZAchseK.HOCH));


        // GRENZE UNTEN
        beforeEach();
        usb.setIn(I_22);
        assertThrows(RuntimeException.class, () -> kran.setStatusMotorZAchseK(Q_ZAchseK.RUNTER));
    }

    @Test
    void getStatusMotorZAchseK() {
        // DAZWISCHEN
        assertEquals(kran.getStatusMotorZAchseK(), I_ZAchseK.DAZWISCHEN);

        // OBEN
        beforeEach();
        usb.setIn(I_21);
        assertEquals(kran.getStatusMotorZAchseK(), I_ZAchseK.OBEN);

        // UNTEN
        beforeEach();
        usb.setIn(I_22);
        assertEquals(kran.getStatusMotorZAchseK(), I_ZAchseK.UNTEN);

        // ERROR
        beforeEach();
        usb.setIn(I_21 | I_22);
        assertThrows(RuntimeException.class, () -> kran.getStatusMotorZAchseK());
    }

    @Test
    void setStatusMagnetK() {
        // AN
        kran.setStatusMagnetK(Q_MagnetK.AN);
        assertEquals(f.output & Q_24, Q_24);

        // AUS
        beforeEach();
        kran.setStatusMagnetK(Q_MagnetK.AUS);
        assertEquals(f.output & Q_24, 0);
    }

    @Test
    void istEinlegestationBelegtK() {
        // BELEGT
        usb.setIn(I_01);
        assertEquals(kran.istEinlegestationBelegtK(), I_EinlegestationK.BELEGT);

        // NICHT_BELEGT
        beforeEach();
        assertEquals(kran.istEinlegestationBelegtK(), I_EinlegestationK.NICHT_BELEGT);
    }

    @Test
    void istAusschleussbahnBelegtK() {
        // BELEGT
        usb.setIn(I_14);
        assertEquals(kran.istAusschleussbahnBelegtK(), I_AusschleussbahnK.BELEGT);

        // NICHT_BELEGT
        beforeEach();
        assertEquals(kran.istAusschleussbahnBelegtK(), I_AusschleussbahnK.NICHT_BELEGT);
    }

    @Test
    void initiatorXAchseK() {
        // true
        usb.setIn(I_28);
        assertTrue(kran.initiatorXAchseK());

        // false
        beforeEach();
        assertFalse(kran.initiatorXAchseK());
    }

    @Test
    void initiatorYAchseK() {
        // true
        usb.setIn(I_29);
        assertTrue(kran.initiatorYAchseK());

        // false
        beforeEach();
        assertFalse(kran.initiatorYAchseK());
    }

    @Test
    void initiatorZAchseK() {
        // true
        usb.setIn(I_30);
        assertTrue(kran.initiatorZAchseK());

        // false
        beforeEach();
        assertFalse(kran.initiatorZAchseK());
    }
}
