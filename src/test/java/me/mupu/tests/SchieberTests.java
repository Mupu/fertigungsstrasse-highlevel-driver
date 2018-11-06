package me.mupu.tests;

import me.mupu.FertigungsstrasseHLD;
import me.mupu._testUsbInterface.UsbOptoRel32;
import org.junit.jupiter.api.*;

import me.mupu.interfaces.maschinen.IMSchieber;
import me.mupu.interfaces.maschinen.IMSchieber.*;
import static me.mupu.interfaces.bitpos.IInput.*;
import static me.mupu.interfaces.bitpos.IOutput.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchieberTests {

    private IMSchieber schieber;
    private UsbOptoRel32 usb;
    private FertigungsstrasseHLD f;

    @BeforeAll
    void beforeAll() {
        schieber = FertigungsstrasseHLD.getSchieber();

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
    void istBelegtS() {

        // istBelegtS
        usb.setIn(I_01);
        assertTrue(schieber.istBelegtS());

    }

    @Test
    void istInAusgangslageS() {

        // istInAusgagslageS
        usb.setIn(I_02);
        assertTrue(schieber.istInAusgangslageS());

    }

    @Test
    void istBandpositionS() {

        // istBandpositionS
        usb.setIn(I_03);
        assertTrue(schieber.istBandpositionS());

    }

    @Test
    void setStatusEinlegestationS_AUS() {
        // AUS
        f.output = Q_1 | Q_2; // setzte bits
        schieber.setStatusEinlegestationS(Q_EinlegestationS.AUS);
        assertEquals(f.output & (Q_1 | Q_2), 0);


        // VOR
        beforeEach();
        f.output = Q_2;
        schieber.setStatusEinlegestationS(Q_EinlegestationS.VOR);
        assertEquals(f.output & (Q_1 | Q_2), Q_1);


        // RUECK
        beforeEach();
        f.output = Q_1;
        schieber.setStatusEinlegestationS(Q_EinlegestationS.RUECK);
        assertEquals(f.output & (Q_1 | Q_2), Q_2);


        // todo vor gleich nach links ? rueck gleich nach rechts ??
        // GRENZE LINKS(VOR)
        beforeEach();
        usb.setIn(I_02); // ausgangslage
        assertThrows(RuntimeException.class, () -> schieber.setStatusEinlegestationS(Q_EinlegestationS.VOR));

        // GRENZE RECHTS(RUECK)
        beforeEach();
        usb.setIn(I_03); // bandposition
        assertThrows(RuntimeException.class, () -> schieber.setStatusEinlegestationS(Q_EinlegestationS.RUECK));
    }

}
