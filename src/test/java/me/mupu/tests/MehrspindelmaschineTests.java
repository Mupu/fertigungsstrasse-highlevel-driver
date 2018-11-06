package me.mupu.tests;

import me.mupu.FertigungsstrasseHLD;
import me.mupu._testUsbInterface.UsbOptoRel32;
import me.mupu.interfaces.maschinen.IMMehrspindelmaschine;
import me.mupu.interfaces.maschinen.IMMehrspindelmaschine.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static me.mupu.interfaces.bitpos.IInput.*;
import static me.mupu.interfaces.bitpos.IOutput.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MehrspindelmaschineTests {

    private IMMehrspindelmaschine mehrspindelmaschine;
    private UsbOptoRel32 usb;
    private FertigungsstrasseHLD f;

    @BeforeAll
    void beforeAll() {
        mehrspindelmaschine = FertigungsstrasseHLD.getMehrspindelmaschine();

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
    void setStatusHubM() {
        // AUS
        f.output = Q_6 | Q_7; // setzte bits
        mehrspindelmaschine.setStatusHubM(Q_HubM.AUS);
        assertEquals(f.output & (Q_6 | Q_7), 0);


        // AUF
        beforeEach();
        f.output = Q_7;
        mehrspindelmaschine.setStatusHubM(Q_HubM.AUF);
        assertEquals(f.output & (Q_6 | Q_7), Q_6);


        // AB
        beforeEach();
        f.output = Q_6;
        mehrspindelmaschine.setStatusHubM(Q_HubM.AB);
        assertEquals(f.output & (Q_6 | Q_7), Q_7);




        // GRENZE OBEN
        beforeEach();
        usb.setIn(Q_6);
        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setStatusHubM(Q_HubM.AUF));


        // GRENZE UNTEN
        beforeEach();
        usb.setIn(Q_7);
        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setStatusHubM(Q_HubM.AB));
    }

    @Test
    void getStatusHubM() {
        // DAZWISCHEN
        assertEquals(mehrspindelmaschine.getStatusHubM(), I_HubM.DAZWISCHEN);

        // OBEN
        beforeEach();
        usb.setIn(I_06);
        assertEquals(mehrspindelmaschine.getStatusHubM(), I_HubM.OBEN);

        // UNTEN
        beforeEach();
        usb.setIn(I_07);
        assertEquals(mehrspindelmaschine.getStatusHubM(), I_HubM.UNTEN);

        // ERROR
        beforeEach();
        usb.setIn(I_06 | I_07);
        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.getStatusHubM());
    }

    @Test
    void setStatusRevolverdrehungM() {
        // AN
        mehrspindelmaschine.setStatusRevolverdrehungM(RevolverdrehungM.AN);
        assertEquals(f.output & Q_8, Q_8);

        // AUS
        beforeEach();
        mehrspindelmaschine.setStatusRevolverdrehungM(RevolverdrehungM.AUS);
        assertEquals(f.output & Q_8, 0);
    }

    @Test
    void setStatusWerkzeugAntriebM() {
        // AN
        mehrspindelmaschine.setStatusWerkzeugAntriebM(WerkzeugAntriebM.AN);
        assertEquals(f.output & Q_9, Q_9);

        // AUS
        beforeEach();
        mehrspindelmaschine.setStatusWerkzeugAntriebM(WerkzeugAntriebM.AUS);
        assertEquals(f.output & Q_9, 0);
    }

    @Test
    void setStatusBandM() {
        // AN
        mehrspindelmaschine.setStatusBandM(BandM.AN);
        assertEquals(f.output & Q_16, Q_16);

        // AUS
        beforeEach();
        mehrspindelmaschine.setStatusBandM(BandM.AUS);
        assertEquals(f.output & Q_16, 0);
    }

    @Test
    void revolverPositionMelderM() {
        // AN
        usb.setIn(I_08);
        assertTrue(mehrspindelmaschine.revolverPositionMelderM());

        // AUS
        beforeEach();
        assertFalse(mehrspindelmaschine.revolverPositionMelderM());
    }

    @Test
    void istAusschleussbahnBelegtM() {
        // AN
        usb.setIn(I_14);
        assertEquals(mehrspindelmaschine.istAusschleussbahnBelegtM(), I_AusschleussbahnM.BELEGT);

        // AUS
        beforeEach();
        assertEquals(mehrspindelmaschine.istAusschleussbahnBelegtM(), I_AusschleussbahnM.NICHT_BELEGT);
    }

    @Test
    void initiatorM() {
        // AN
        usb.setIn(I_27);
        assertTrue(mehrspindelmaschine.initiatorM());

        // AUS
        beforeEach();
        assertFalse(mehrspindelmaschine.initiatorM());
    }
}
