//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
//import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
//import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
//import me.mupu.enums.motorbewegungen.EMotorstatus;
//import me.mupu.enums.sensoren.ESensorXAchse;
//import me.mupu.enums.sensoren.ESensorYAchse;
//import me.mupu.enums.sensoren.ESensorZAchse;
//import me.mupu.enums.sensoren.ESensorstatus;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import me.mupu.interfaces.maschinen.IKran;
//
//import static me.mupu.interfaces.bitpos.IInput.*;
//import static me.mupu.interfaces.bitpos.IOutput.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class KranTests {
//
//    private IKran kran;
//    private UsbOptoRel32 usb;
//    private FertigungsstrasseHLD f;
//
//    @BeforeAll
//    void beforeAll() {
//        kran = FertigungsstrasseHLD.getKran();
//
//        f = FertigungsstrasseHLD.instance;
//
//        usb = FertigungsstrasseHLD.instance.usbInterface;
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        usb.setIn(0); // reset USBInterface
//        FertigungsstrasseHLD.instance.output = 0;
//    }
//
//
//
//    @Test
//    void setMotorstatusXAchseK() {
//        // AUS
//        f.output = Q_18 | Q_19; // setzte bits
//        kran.setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
//        assertEquals(f.output & (Q_18 | Q_19), 0);
//
//
//        // LINKS
//        beforeEach();
//        f.output = Q_18;
//        kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS);
//        assertEquals(f.output & (Q_18 | Q_19), Q_19);
//
//
//        // RECHTS
//        beforeEach();
//        f.output = Q_19;
//        kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS);
//        assertEquals(f.output & (Q_18 | Q_19), Q_18);
//
//
//
//
//        // GRENZE LINKS
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_18);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusXAchseK(EMotorbewegungXAchse.LINKS));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE RECHTS
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_17);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusXAchseK(EMotorbewegungXAchse.RECHTS));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusXAchseK(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionXAchseK() {
//        // DAZWISCHEN
//        assertEquals(kran.getPositionXAchseK(), ESensorXAchse.DAZWISCHEN);
//
//        // RECHTS
//        beforeEach();
//        usb.setIn(I_17);
//        assertEquals(kran.getPositionXAchseK(), ESensorXAchse.RECHTS);
//
//        // LINKS
//        beforeEach();
//        usb.setIn(I_18);
//        assertEquals(kran.getPositionXAchseK(), ESensorXAchse.LINKS);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_17 | I_18);
//        assertThrows(RuntimeException.class, () -> kran.getPositionXAchseK());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//
//
//
//
//
//    @Test
//    void setMotorstatusYAchseK() {
//        // AUS
//        f.output = Q_20 | Q_21; // setzte bits
//        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);
//        assertEquals(f.output & (Q_20 | Q_21), 0);
//
//
//        // VOR
//        beforeEach();
//        f.output = Q_21;
//        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR);
//        assertEquals(f.output & (Q_20 | Q_21), Q_20);
//
//
//        // ZURUECK
//        beforeEach();
//        f.output = Q_20;
//        kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK);
//        assertEquals(f.output & (Q_20 | Q_21), Q_21);
//
//
//
//
//        // GRENZE VORNE
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_19);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusYAchseK(EMotorbewegungYAchse.VOR));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE HINTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_20);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusYAchseK(EMotorbewegungYAchse.ZURUECK));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusYAchseK(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionYAchseK() {
//        // DAZWISCHEN
//        assertEquals(kran.getPositionYAchseK(), ESensorYAchse.DAZWISCHEN);
//
//        // VORNE
//        beforeEach();
//        usb.setIn(I_19);
//        assertEquals(kran.getPositionYAchseK(), ESensorYAchse.VORNE);
//
//        // HINTEN
//        beforeEach();
//        usb.setIn(I_20);
//        assertEquals(kran.getPositionYAchseK(), ESensorYAchse.HINTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_19 | I_20);
//        assertThrows(RuntimeException.class, () -> kran.getPositionYAchseK());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//
//
//
//
//
//    @Test
//    void setMotorstatusZAchseK() {
//        // AUS
//        f.output = Q_22 | Q_23; // setzte bits
//        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);
//        assertEquals(f.output & (Q_22 | Q_23), 0);
//
//
//        // HOCH
//        beforeEach();
//        f.output = Q_23;
//        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF);
//        assertEquals(f.output & (Q_22 | Q_23), Q_22);
//
//
//        // RUNTER
//        beforeEach();
//        f.output = Q_22;
//        kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB);
//        assertEquals(f.output & (Q_22 | Q_23), Q_23);
//
//
//
//
//        // GRENZE OBEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_21);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AUF));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE UNTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_22);
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusZAchseK(EMotorbewegungZAchse.AB));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusZAchseK(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionZAchseK() {
//        // DAZWISCHEN
//        assertEquals(kran.getPositionZAchseK(), ESensorZAchse.DAZWISCHEN);
//
//        // OBEN
//        beforeEach();
//        usb.setIn(I_21);
//        assertEquals(kran.getPositionZAchseK(), ESensorZAchse.OBEN);
//
//        // UNTEN
//        beforeEach();
//        usb.setIn(I_22);
//        assertEquals(kran.getPositionZAchseK(), ESensorZAchse.UNTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_21 | I_22);
//        assertThrows(RuntimeException.class, () -> kran.getPositionZAchseK());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusMagnetK() {
//        // AN
//        kran.setMotorstatusMagnetK(EMotorstatus.AN);
//        assertEquals(f.output & Q_24, Q_24);
//
//        // AUS
//        beforeEach();
//        kran.setMotorstatusMagnetK(EMotorstatus.AUS);
//        assertEquals(f.output & Q_24, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> kran.setMotorstatusMagnetK(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void istEinlegestationBelegtK() {
//        // BELEGT
//        usb.setIn(I_01);
//        assertEquals(kran.istEinlegestationBelegtK(), ESensorstatus.SIGNAL);
//
//        // NICHT_BELEGT
//        beforeEach();
//        assertEquals(kran.istEinlegestationBelegtK(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void istAusschleussbahnBelegtK() {
//        // BELEGT
//        usb.setIn(I_14);
//        assertEquals(kran.istAusschleussbahnBelegtK(), ESensorstatus.SIGNAL);
//
//        // NICHT_BELEGT
//        beforeEach();
//        assertEquals(kran.istAusschleussbahnBelegtK(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorXAchseK() {
//        // true
//        usb.setIn(I_28);
//        assertEquals(kran.initiatorXAchseK(), ESensorstatus.SIGNAL);
//
//        // false
//        beforeEach();
//        assertEquals(kran.initiatorXAchseK(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorYAchseK() {
//        // true
//        usb.setIn(I_29);
//        assertEquals(kran.initiatorYAchseK(), ESensorstatus.SIGNAL);
//
//        // false
//        beforeEach();
//        assertEquals(kran.initiatorYAchseK(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorZAchseK() {
//        // true
//        usb.setIn(I_30);
//        assertEquals(kran.initiatorZAchseK(), ESensorstatus.SIGNAL);
//
//        // false
//        beforeEach();
//        assertEquals(kran.initiatorZAchseK(), ESensorstatus.KEIN_SIGNAL);
//    }
//}
