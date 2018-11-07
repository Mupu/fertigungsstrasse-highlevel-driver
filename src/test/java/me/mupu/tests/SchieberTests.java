//package me.mupu.tests;
//
//import me.mupu.FertigungsstrasseHLD;
//import me.mupu._testUsbInterface.UsbOptoRel32;
//import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
//import me.mupu.enums.sensoren.ESensorXAchse;
//import me.mupu.enums.sensoren.ESensorstatus;
//import org.junit.jupiter.api.*;
//
//import me.mupu.interfaces.maschinen.IMSchieber;
//
//import static me.mupu.interfaces.bitpos.IInput.*;
//import static me.mupu.interfaces.bitpos.IOutput.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class SchieberTests {
//
//    private IMSchieber schieber;
//    private UsbOptoRel32 usb;
//    private FertigungsstrasseHLD f;
//
//    @BeforeAll
//    void beforeAll() {
//        schieber = FertigungsstrasseHLD.getSchieber();
//
//        f = FertigungsstrasseHLD.instance;
//
//        usb = FertigungsstrasseHLD.instance.usbInterface;
//
//        System.out.println("******************" +
//                        "\n* SCHIEBER-TESTS *" +
//                        "\n******************"
//        );
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
//    void setMotorstatusSchieberS() {
//        // AUS
//        f.output = Q_1 | Q_2; // setzte bits
//        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.AUS);
//        assertEquals(f.output & (Q_1 | Q_2), 0);
//
//
//        // VOR
//        beforeEach();
//        f.output = Q_2;
//        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.RECHTS);
//        assertEquals(f.output & (Q_1 | Q_2), Q_1);
//
//
//        // RUECK
//        beforeEach();
//        f.output = Q_1;
//        schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.LINKS);
//        assertEquals(f.output & (Q_1 | Q_2), Q_2);
//
//
//        // GRENZE RECHTS(VOR)
//        beforeEach();
//        usb.setIn(I_03); // bandposition
//        assertThrows(RuntimeException.class, () -> schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.RECHTS));
//
//        // GRENZE LINKS(RUECK)
//        beforeEach();
//        usb.setIn(I_02); // ausgangslage
//        assertThrows(RuntimeException.class, () -> schieber.setMotorstatusSchieberS(EMotorbewegungXAchse.LINKS));
//    }
//
//    @Test
//    void getPositionSchieberS() {
//        // DAZWISCHEN
//        assertEquals(schieber.getPositionSchieberS(), ESensorXAchse.DAZWISCHEN);
//
//        // RECHTS
//        beforeEach();
//        usb.setIn(I_03);
//        assertEquals(schieber.getPositionSchieberS(), ESensorXAchse.RECHTS);
//
//        // LINKS
//        beforeEach();
//        usb.setIn(I_02);
//        assertEquals(schieber.getPositionSchieberS(), ESensorXAchse.LINKS);
//
//        // ERROR
//        beforeEach();
//        usb.setIn(I_02 | I_03);
//        assertThrows(RuntimeException.class, () -> schieber.getPositionSchieberS());
//    }
//
//    @Test
//    void istEinlegestationBelegtS() {
//        // true
//        usb.setIn(I_01);
//        assertEquals(schieber.istEinlegestationBelegtS(), ESensorstatus.SIGNAL);
//
//        // false
//        beforeEach();
//        assertEquals(schieber.istEinlegestationBelegtS(), ESensorstatus.KEIN_SIGNAL);
//    }
//
//    @Test
//    void istUebergabestelleVorBohrmaschineBelegtS() {
//
//        // istEinlegestationBelegtS
//        usb.setIn(I_13);
//        assertEquals(schieber.istUebergabestelleVorBohrmaschineBelegtS(), ESensorstatus.SIGNAL);
//
//        // false
//        beforeEach();
//        assertEquals(schieber.istUebergabestelleVorBohrmaschineBelegtS(), ESensorstatus.KEIN_SIGNAL);
//
//    }
//
//
//
//}
