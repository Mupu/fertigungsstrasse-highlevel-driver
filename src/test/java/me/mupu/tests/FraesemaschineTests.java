//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
//import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
//import me.mupu.enums.motorbewegungen.EMotorstatus;
//import me.mupu.enums.sensoren.ESensorYAchse;
//import me.mupu.enums.sensoren.ESensorZAchse;
//import me.mupu.enums.sensoren.ESensorstatus;
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
//    void setMotorstatusHubF() {
//        // AUS
//        f.output = Q_10 | Q_11; // setzte bits
//        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUS);
//        assertEquals(f.output & (Q_10 | Q_11), 0);
//
//
//        // AUF
//        beforeEach();
//        f.output = Q_11;
//        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF);
//        assertEquals(f.output & (Q_10 | Q_11), Q_10);
//
//
//        // AB
//        beforeEach();
//        f.output = Q_10;
//        fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AB);
//        assertEquals(f.output & (Q_10 | Q_11), Q_11);
//
//
//
//
//        // GRENZE OBEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_09);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AUF));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE UNTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_10);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusHubF(EMotorbewegungZAchse.AB));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusHubF(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionHubF() {
//        // DAZWISCHEN
//        assertEquals(fraesmaschine.getPositionHubF(), ESensorZAchse.DAZWISCHEN);
//
//        // OBEN
//        beforeEach();
//        usb.setIn(I_09);
//        assertEquals(fraesmaschine.getPositionHubF(), ESensorZAchse.OBEN);
//
//        // UNTEN
//        beforeEach();
//        usb.setIn(I_10);
//        assertEquals(fraesmaschine.getPositionHubF(), ESensorZAchse.UNTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_09 | I_10);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.getPositionHubF());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusQuerschlittenF() {
//        // AUS
//        f.output = Q_12 | Q_13; // setzte bits
//        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
//        assertEquals(f.output & (Q_12 | Q_13), 0);
//
//
//        // VOR
//        beforeEach();
//        f.output = Q_12;
//        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR);
//        assertEquals(f.output & (Q_12 | Q_13), Q_13);
//
//
//        // RUECK
//        beforeEach();
//        f.output = Q_13;
//        fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK);
//        assertEquals(f.output & (Q_12 | Q_13), Q_12);
//
//
//
//
//        // GRENZE VORNE
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_11);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.VOR));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE HINTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_12);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusQuerschlittenF(EMotorbewegungYAchse.ZURUECK));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusQuerschlittenF(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionQuerschlittenF() {
//        // DAZWISCHEN
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), ESensorYAchse.DAZWISCHEN);
//
//        // BANDPOSITION
//        beforeEach();
//        usb.setIn(I_11);
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), ESensorYAchse.VORNE);
//
//        // STAENDERPOSITION
//        beforeEach();
//        usb.setIn(I_12);
//        assertEquals(fraesmaschine.getPositionQuerschlittenF(), ESensorYAchse.HINTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_11 | I_12);
//        assertThrows(RuntimeException.class, () -> fraesmaschine.getPositionQuerschlittenF());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusWerkzeugAntriebF() {
//        // AN
//        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AN);
//        assertEquals(f.output & Q_14, Q_14);
//
//        // AUS
//        beforeEach();
//        fraesmaschine.setMotorstatusWerkzeugAntriebF(EMotorstatus.AUS);
//        assertEquals(f.output & Q_14, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusWerkzeugAntriebF(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusBandF() {
//        // AN
//        fraesmaschine.setMotorstatusBandF(EMotorstatus.AN);
//        assertEquals(f.output & Q_17, Q_17);
//
//        // AUS
//        beforeEach();
//        fraesmaschine.setMotorstatusBandF(EMotorstatus.AUS);
//        assertEquals(f.output & Q_17, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> fraesmaschine.setMotorstatusBandF(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void istAusschleussbahnBelegtF() {
//        // AN
//        usb.setIn(I_14);
//        assertEquals(fraesmaschine.istAusschleussbahnBelegtF(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(fraesmaschine.istAusschleussbahnBelegtF(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorF() {
//        // AN
//        usb.setIn(I_27);
//        assertEquals(fraesmaschine.initiatorF(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(fraesmaschine.initiatorF(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//}
