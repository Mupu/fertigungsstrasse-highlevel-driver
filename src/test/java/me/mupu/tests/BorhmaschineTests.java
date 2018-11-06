package me.mupu.tests;

import me.mupu.FertigungsstrasseHLD;
import me.mupu._testUsbInterface.UsbOptoRel32;
import me.mupu.interfaces.maschinen.IMBohrmaschine;
import me.mupu.interfaces.maschinen.IMBohrmaschine.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static me.mupu.interfaces.bitpos.IInput.*;
import static me.mupu.interfaces.bitpos.IOutput.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BorhmaschineTests {

    private IMBohrmaschine bohrmaschine;
    private UsbOptoRel32 usb;
    private FertigungsstrasseHLD f;

    @BeforeAll
    void beforeAll() {
        bohrmaschine = FertigungsstrasseHLD.getBohrmaschine();

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
    void setStatusHubBm() {
        // AUS
        f.output = Q_3 | Q_4; // setzte bits
        bohrmaschine.setStatusHubBm(Q_HubBm.AUS);
        assertEquals(f.output & (Q_3 | Q_4), 0);


        // AUF
        beforeEach();
        f.output = Q_4;
        bohrmaschine.setStatusHubBm(Q_HubBm.AUF);
        assertEquals(f.output & (Q_3 | Q_4), Q_3);


        // AB
        beforeEach();
        f.output = Q_3;
        bohrmaschine.setStatusHubBm(Q_HubBm.AB);
        assertEquals(f.output & (Q_3 | Q_4), Q_4);




        // GRENZE OBEN
        beforeEach();
        usb.setIn(I_04);
        assertThrows(RuntimeException.class, () -> bohrmaschine.setStatusHubBm(Q_HubBm.AUF));


        // GRENZE UNTEN
        beforeEach();
        usb.setIn(I_05);
        assertThrows(RuntimeException.class, () -> bohrmaschine.setStatusHubBm(Q_HubBm.AB));
    }

    @Test
    void getStatusHubBm() {
        // DAZWISCHEN
        assertEquals(bohrmaschine.getStatusHubBm(), I_HubBm.DAZWISCHEN);

        // OBEN
        beforeEach();
        usb.setIn(I_04);
        assertEquals(bohrmaschine.getStatusHubBm(), I_HubBm.OBEN);

        // UNTEN
        beforeEach();
        usb.setIn(I_05);
        assertEquals(bohrmaschine.getStatusHubBm(), I_HubBm.UNTEN);

        // ERROR
        beforeEach();
        usb.setIn(I_04 | I_05);
        assertThrows(RuntimeException.class, () -> bohrmaschine.getStatusHubBm());
    }

    @Test
    void setStatusWerkzeugAntriebBm() {
        // AN
        bohrmaschine.setStatusWerkzeugAntriebBm(WerkzeugAntriebBm.AN);
        assertEquals(f.output & Q_5, Q_5);

        // AUS
        beforeEach();
        bohrmaschine.setStatusWerkzeugAntriebBm(WerkzeugAntriebBm.AUS);
        assertEquals(f.output & Q_5, 0);
    }

    @Test
    void setStatusBandBm() {
        // AN
        bohrmaschine.setStatusBandBm(BandBm.AN);
        assertEquals(f.output & Q_15, Q_15);

        // AUS
        beforeEach();
        bohrmaschine.setStatusBandBm(BandBm.AUS);
        assertEquals(f.output & Q_15, 0);
    }

    @Test
    void istBandstartBm() {
        // AN
        usb.setIn(I_13);
        assertTrue(bohrmaschine.istBandstartBm());

        // AUS
        beforeEach();
        assertFalse(bohrmaschine.istBandstartBm());
    }

    @Test
    void initiatorBm() {
        // AN
        usb.setIn(I_25);
        assertTrue(bohrmaschine.istBandstartBm());

        // AUS
        beforeEach();
        assertFalse(bohrmaschine.istBandstartBm());
    }

}
