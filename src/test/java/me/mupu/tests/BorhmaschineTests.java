//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
//import me.mupu.enums.motorbewegungen.EMotorstatus;
//import me.mupu.enums.sensoren.ESensorZAchse;
//import me.mupu.enums.sensoren.ESensorstatus;
//import me.mupu.interfaces.maschinen.IMBohrmaschine;
//import mockit.Deencapsulation;
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
//class BorhmaschineTests {
//
//    private IMBohrmaschine bohrmaschine;
//    private UsbOptoRel32 usb;
//    private FertigungsstrasseHLD f;
//
//    @BeforeAll
//    void beforeAll() {
//        bohrmaschine = FertigungsstrasseHLD.getBohrmaschine();
//
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
//    void setStatusHubB() {
//        // AUS
//        f.output = Q_3 | Q_4; // setzte bits
//        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUS);
//        assertEquals(f.output & (Q_3 | Q_4), 0);
//
//
//        // AUF
//        beforeEach();
//        f.output = Q_4;
//        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUF);
//        assertEquals(f.output & (Q_3 | Q_4), Q_3);
//
//
//        // AB
//        beforeEach();
//        f.output = Q_3;
//        bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AB);
//        assertEquals(f.output & (Q_3 | Q_4), Q_4);
//
//
//
//
//        // GRENZE OBEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_04);
//        assertThrows(RuntimeException.class, () -> bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AUF));
//        assertEquals(f.usbInterface.out, 0);
//
//
//        // GRENZE UNTEN
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_05);
//        assertThrows(RuntimeException.class, () -> bohrmaschine.setMotorstatusHubB(EMotorbewegungZAchse.AB));
//        assertEquals(f.usbInterface.out, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> bohrmaschine.setMotorstatusHubB(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void getStatusHubB() {
//        // DAZWISCHEN
//        assertEquals(bohrmaschine.getPositionHubB(), ESensorZAchse.DAZWISCHEN);
//
//        // OBEN
//        beforeEach();
//        usb.setIn(I_04);
//        assertEquals(bohrmaschine.getPositionHubB(), ESensorZAchse.OBEN);
//
//        // UNTEN
//        beforeEach();
//        usb.setIn(I_05);
//        assertEquals(bohrmaschine.getPositionHubB(), ESensorZAchse.UNTEN);
//
//        // ERROR
//        beforeEach();
//        f.output = Q_1;
//        usb.setIn(I_04 | I_05);
//        assertThrows(RuntimeException.class, () -> bohrmaschine.getPositionHubB());
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setStatusWerkzeugAntriebB() {
//        // AN
//        bohrmaschine.setMotorstatusWerkzeugAntriebB(EMotorstatus.AN);
//        assertEquals(f.output & Q_5, Q_5);
//
//        // AUS
//        beforeEach();
//        bohrmaschine.setMotorstatusWerkzeugAntriebB(EMotorstatus.AUS);
//        assertEquals(f.output & Q_5, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> bohrmaschine.setMotorstatusWerkzeugAntriebB(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void setStatusBandB() {
//        // AN
//        bohrmaschine.setMotorstatusBandB(EMotorstatus.AN);
//        assertEquals(f.output & Q_15, Q_15);
//
//        // AUS
//        beforeEach();
//        bohrmaschine.setMotorstatusBandB(EMotorstatus.AUS);
//        assertEquals(f.output & Q_15, 0);
//
//        // NULL CHECK
//        beforeEach();
//        f.output = Q_1;
//        assertThrows(RuntimeException.class, () -> bohrmaschine.setMotorstatusBandB(null));
//        assertEquals(f.usbInterface.out, 0);
//    }
//
//    @Test
//    void istUebergabestelleVorBohrmaschineBelegtB() {
//        // AN
//        usb.setIn(I_13);
//        assertEquals(bohrmaschine.istUebergabestelleVorBohrmaschineBelegtB(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(bohrmaschine.istUebergabestelleVorBohrmaschineBelegtB(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void istMehrspindelmaschineBelegtB() {
//        // AN
//        usb.setIn(I_26);
//        assertEquals(bohrmaschine.istMehrspindelmaschineBelegtB(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(bohrmaschine.istMehrspindelmaschineBelegtB(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void initiatorB() {
//        // AN
//        usb.setIn(I_25);
//        assertEquals(bohrmaschine.initiatorB(), ESensorstatus.SIGNAL);
//
//        // AUS
//        beforeEach();
//        assertEquals(bohrmaschine.initiatorB(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//}
