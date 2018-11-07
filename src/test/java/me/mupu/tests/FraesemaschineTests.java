//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.interfaces.maschinen.IMFraesmaschine;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import static me.mupu.interfaces.bitpos.IInput.*;
//import static me.mupu.interfaces.bitpos.IOutput.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class FraesemaschineTests {
//
//    private IMFraesmaschine fraesmaschine;
//    private UsbOptoRel32 usb;
//    private FertigungsstrasseHLD f;
//
//    @BeforeAll
//    void beforeAll() {
//        fraesmaschine = FertigungsstrasseHLD.getFraesmaschine();
//
//        f = FertigungsstrasseHLD.instance;
//
//        usb = FertigungsstrasseHLD.instance.usbInterface;
//
//
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        usb.setIn(0); // reset USBInterface
//        FertigungsstrasseHLD.instance.output = 0;
//    }
//
//    @Test
//    void setStatusHubF() {
//        // AUS
//        f.output = Q_10 | Q_11; // setzte bits
//        fraesmaschine.setMotorstatusHubF(Q_HubF.AUS);
//        assertEquals(f.output & (Q_10 | Q_11), 0);
//
//
//        // AUF
//        beforeEach();
//        f.output = Q_11;
//        fraesmaschine.setMotorstatusHubF(Q_HubF.AUF);
//        assertEquals(f.output & (Q_10 | Q_11), Q_10);
//
//
//        // AB
//        beforeEach();
//        f.output = Q_10;
//        fraesmaschine.setMotorstatusHubF(Q_HubF.AB);
//        assertEquals(f.output & (Q_10 | Q_11), Q_11);
//
//
//
//
//        // GRENZE OBEN
//        beforeEach();
//        usb.setIn(I_09);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusHubF(Q_HubF.AUF));
//
//
//        // GRENZE UNTEN
//        beforeEach();
//        usb.setIn(I_10);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusHubF(Q_HubF.AB));
//    }
//
//    @Test
//    void getStatusHubF() {
//        // DAZWISCHEN
//        assertEquals(fraesmaschine.getPositionHubF(), I_HubF.DAZWISCHEN);
//
//        // OBEN
//        beforeEach();
//        usb.setIn(I_09);
//        assertEquals(fraesmaschine.getPositionHubF(), I_HubF.OBEN);
//
//        // UNTEN
//        beforeEach();
//        usb.setIn(I_10);
//        assertEquals(fraesmaschine.getPositionHubF(), I_HubF.UNTEN);
//
//        // ERROR
//        beforeEach();
//        usb.setIn(I_09 | I_10);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.getPositionHubF());
//    }
//
//    @Test
//    void setStatusQuerschlittenF() {
//        // AUS
//        f.output = Q_12 | Q_13; // setzte bits
//        fraesmaschine.setMotorstatusQuerschlittenF(Q_QuerschlittenF.AUS);
//        assertEquals(f.output & (Q_12 | Q_13), 0);
//
//
//        // VOR
//        beforeEach();
//        f.output = Q_12;
//        fraesmaschine.setMotorstatusQuerschlittenF(Q_QuerschlittenF.VOR);
//        assertEquals(f.output & (Q_12 | Q_13), Q_13);
//
//
//        // RUECK
//        beforeEach();
//        f.output = Q_13;
//        fraesmaschine.setMotorstatusQuerschlittenF(Q_QuerschlittenF.RUECK);
//        assertEquals(f.output & (Q_12 | Q_13), Q_12);
//
//
//
//
//        // GRENZE VORNE
//        beforeEach();
//        usb.setIn(I_09);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusQuerschlittenF(Q_QuerschlittenF.VOR));
//
//
//        // GRENZE HINTEN
//        beforeEach();
//        usb.setIn(I_10);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusQuerschlittenF(Q_QuerschlittenF.RUECK));
//    }
//
//    @Test
//    void getStatusQuerschlittenF() {
//        // DAZWISCHEN
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), I_QuerschlittenF.DAZWISCHEN);
//
//        // BANDPOSITION
//        beforeEach();
//        usb.setIn(I_11);
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), I_QuerschlittenF.BANDPOSITION);
//
//        // STAENDERPOSITION
//        beforeEach();
//        usb.setIn(I_12);
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), I_QuerschlittenF.STAENDERPOSITION);
//
//        // ERROR
//        beforeEach();
//        usb.setIn(I_11 | I_12);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.getPositionQuerschlittenF());
//    }
//
//    @Test
//    void setStatusWerkzeugAntriebF() {
//        // AN
//        fraesmaschine.setMotorstatusWerkzeugAntriebF(WerkzeugAntriebF.AN);
//        assertEquals(f.output & Q_14, Q_14);
//
//        // AUS
//        beforeEach();
//        fraesmaschine.setMotorstatusWerkzeugAntriebF(WerkzeugAntriebF.AUS);
//        assertEquals(f.output & Q_14, 0);
//    }
//
//    @Test
//    void setStatusBandF() {
//        // AN
//        fraesmaschine.setMotorstatusBandF(BandF.AN);
//        assertEquals(f.output & Q_17, Q_17);
//
//        // AUS
//        beforeEach();
//        fraesmaschine.setMotorstatusBandF(BandF.AUS);
//        assertEquals(f.output & Q_17, 0);
//    }
//
//    @Test
//    void initiatorF() {
//        // AN
//        usb.setIn(I_27);
//        assertTrue(fraesmaschine.initiatorF());
//
//        // AUS
//        beforeEach();
//        assertFalse(fraesmaschine.initiatorF());
//    }
//
//}
