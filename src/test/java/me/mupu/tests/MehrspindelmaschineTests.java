//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
//import me.mupu.enums.motorbewegungen.EMotorstatus;
//import me.mupu.enums.sensoren.ESensorZAchse;
//import me.mupu.enums.sensoren.ESensorstatus;
//import me.mupu.interfaces.maschinen.IMMehrspindelmaschine;
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
//class MehrspindelmaschineTests {
//
//    private IMMehrspindelmaschine mehrspindelmaschine;
//    private UsbOptoRel32 usb;
//    private FertigungsstrasseHLD f;
//
//    @BeforeAll
//    void beforeAll() {
//        mehrspindelmaschine = FertigungsstrasseHLD.getMehrspindelmaschine();
//
//        f = FertigungsstrasseHLD.instance;
//
//        usb = FertigungsstrasseHLD.instance.usbInterface;
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
//    void setMotorstatusHubM() {
//        // AUS
//        f.output = Q_6 | Q_7; // setzte bits
//        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUS);
//        assertEquals(f.output & (Q_6 | Q_7), 0);
//
//
//        // AUF
//        beforeEach();
//        f.output = Q_7;
//        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF);
//        assertEquals(f.output & (Q_6 | Q_7), Q_6);
//
//
//        // AB
//        beforeEach();
//        f.output = Q_6;
//        mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB);
//        assertEquals(f.output & (Q_6 | Q_7), Q_7);
//
//
//
//
//        // GRENZE OBEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_06);
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AUF));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE UNTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_07);
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusHubM(EMotorbewegungZAchse.AB));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusHubM(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getPositionHubM() {
//        // DAZWISCHEN
//        assertEquals(mehrspindelmaschine.getPositionHubM(), ESensorZAchse.DAZWISCHEN);
//
//        // OBEN
//        beforeEach();
//        usb.setIn(I_06);
//        assertEquals(mehrspindelmaschine.getPositionHubM(), ESensorZAchse.OBEN);
//
//        // UNTEN
//        beforeEach();
//        usb.setIn(I_07);
//        assertEquals(mehrspindelmaschine.getPositionHubM(), ESensorZAchse.UNTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_06 | I_07);
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.getPositionHubM());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusRevolverdrehungM() {
//        // AN
//        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AN);
//        assertEquals(f.output & Q_8, Q_8);
//
//        // AUS
//        beforeEach();
//        mehrspindelmaschine.setMotorstatusRevolverdrehungM(EMotorstatus.AUS);
//        assertEquals(f.output & Q_8, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusRevolverdrehungM(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusWerkzeugAntriebM() {
//        // AN
//        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AN);
//        assertEquals(f.output & Q_9, Q_9);
//
//        // AUS
//        beforeEach();
//        mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(EMotorstatus.AUS);
//        assertEquals(f.output & Q_9, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusWerkzeugAntriebM(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setMotorstatusBandM() {
//        // AN
//        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AN);
//        assertEquals(f.output & Q_16, Q_16);
//
//        // AUS
//        beforeEach();
//        mehrspindelmaschine.setMotorstatusBandM(EMotorstatus.AUS);
//        assertEquals(f.output & Q_16, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> mehrspindelmaschine.setMotorstatusBandM(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void istRevolverAufPositionM() {
//        // AN
//        usb.setIn(I_08);
//        assertEquals(mehrspindelmaschine.istRevolverAufPositionM(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(mehrspindelmaschine.istRevolverAufPositionM(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void istFraesmaschineBelegtM() {
//        // AN
//        usb.setIn(I_27);
//        assertEquals(mehrspindelmaschine.istFraesmaschineBelegtM(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(mehrspindelmaschine.istFraesmaschineBelegtM(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorM() {
//        // AN
//        usb.setIn(I_26);
//        assertEquals(mehrspindelmaschine.initiatorM(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(mehrspindelmaschine.initiatorM(), ESensorstatus.KEIN_SIGNAL);
//    }
//}
