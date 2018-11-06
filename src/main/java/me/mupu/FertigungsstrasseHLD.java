package me.mupu;

//import quancom.UsbOptoRel32; // RELEASE
import me.mupu._testUsbInterface.UsbOptoRel32; // DEBUG
import me.mupu.interfaces.*;

import java.io.IOException;

import static me.mupu.interfaces.bitpos.IOutput.*;
import static me.mupu.interfaces.bitpos.IInput.*;

public class FertigungsstrasseHLD implements IKran, IMMehrspindelmaschine, IMBohrmaschine, IMFraesmaschine, IMSchieber {

    // DEBUG
    public static FertigungsstrasseHLD instance;
    public final UsbOptoRel32 usbInterface;
    public int output = 0;

    // RELEASE
//    private static FertigungsstrasseHLD instance;
//    private final UsbOptoRel32 usbInterface;
//    private int output = 0;

    /**
     * Oeffnet USB-Interface. Wirft ggf. fehler.
     */
    private FertigungsstrasseHLD() {

        usbInterface = new UsbOptoRel32();

        if (!usbInterface.open())
            throw new RuntimeException("Quancom Verbindung konnte nicht hergestellt werden!");

        // todo initial state
    }


    public static IKran getKran() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    public static IMBohrmaschine getBohrmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    public static IMFraesmaschine getFraesmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    public static IMMehrspindelmaschine getMehrspindelmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    public static IMSchieber getSchieber() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }


    /**
     * Setzt die Bits.
     */
    private void setOutputBit(int mask) {
        this.output |= mask;
    }


    /**
     * Setzt die Bits zurueck.
     */
    private void resetOutputBit(int mask) {
        this.output &= ~mask;
    }


    /**
     * Liesst den input vom USB-Interface.
     *
     * @return Daten als int
     */
    private int read() {
        try {
            return usbInterface.digitalIn();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Interface nicht mehr erreichbar!");
        }
    }


    /**
     * Gibt den output ueber das USB-Interface aus.
     */
    private void write() {
        try {
            usbInterface.digitalOut(output);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Interface nicht mehr erreichbar!");
        }
    }

    // todo add synchronized to every set method
    // todo bei set noch inputs checken (runtimeexceptions werfen und maschine ausschalten)
    // todo warning ausgeben wenn zb: magnet an ist und er nochmals angemacht werden soll
    // todo eventuell error bei falschen I_.. zb: I_21 | I_22 nur als warnung machen <- zimmer fragen
    /************************
     *  IMSchieber
     ************************/
    @Override
    public synchronized void setStatusEinlegestationS(Q_EinlegestationS neuerStatus) {
        if (neuerStatus == Q_EinlegestationS.AUS) {
            resetOutputBit(Q_1 | Q_2);

        } else if (neuerStatus == Q_EinlegestationS.VOR) {
            resetOutputBit(Q_2);
            setOutputBit(Q_1);

        } else if (neuerStatus == Q_EinlegestationS.RUECK) {
            resetOutputBit(Q_1);
            setOutputBit(Q_2);

        }
        write();
    }

    @Override
    public boolean istBelegtS() {
        return (read() & I_01) == 0;
    }

    @Override
    public boolean istInAusgangslageS() {
        return (read() & I_02) == 0;
    }

    @Override
    public boolean istBandpositionS() {
        return (read() & I_03) == 0;
    }


    /************************
     *  IKran
     ************************/
    @Override
    public synchronized void setStatusMotorXAchseK(Q_XAchseK neuerStatus) {
        if (neuerStatus == Q_XAchseK.AUS) {
            resetOutputBit(Q_18 | Q_19);

        } else if (neuerStatus == Q_XAchseK.LINKS) {
            resetOutputBit(Q_18);
            setOutputBit(Q_19);

        } else if (neuerStatus == Q_XAchseK.RECHTS) {
            resetOutputBit(Q_19);
            setOutputBit(Q_18);

        }
        write();
    }

    @Override
    public I_XAchseK getStatusMotorXAchseK() {
        if ((read() & (I_17 | I_18)) == (I_17 | I_18))
            return I_XAchseK.DAZWISCHEN;
        else if ((read() & I_17) == 0)
            return I_XAchseK.RECHTS;
        else if ((read() & I_18) == 0)
            return I_XAchseK.LINKS;
        else {
            setStatusMotorXAchseK(Q_XAchseK.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusMotorYAchseK(Q_YAchseK neuerStatus) {
        if (neuerStatus == Q_YAchseK.AUS) {
            resetOutputBit(Q_20 | Q_21);

        } else if (neuerStatus == Q_YAchseK.VOR) {
            resetOutputBit(Q_21);
            setOutputBit(Q_20);

        } else if (neuerStatus == Q_YAchseK.ZURUECK) {
            resetOutputBit(Q_20);
            setOutputBit(Q_21);

        }
        write();
    }

    @Override
    public I_YAchseK getStatusMotorYAchseK() {
        if ((read() & (I_19 | I_20)) == (I_19 | I_20))
            return I_YAchseK.DAZWISCHEN;
        else if ((read() & I_19) == 0)
            return I_YAchseK.VORNE;
        else if ((read() & I_20) == 0)
            return I_YAchseK.HINTEN;
        else {
            setStatusMotorYAchseK(Q_YAchseK.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusMotorZAchseK(Q_ZAchseK neuerStatus) {
        if (neuerStatus == Q_ZAchseK.AUS) {
            resetOutputBit(Q_22 | Q_23);

        } else if (neuerStatus == Q_ZAchseK.HOCH) {
            resetOutputBit(Q_23);
            setOutputBit(Q_22);

        } else if (neuerStatus == Q_ZAchseK.RUNTER) {
            resetOutputBit(Q_22);
            setOutputBit(Q_23);

        }
        write();
    }

    @Override
    public I_ZAchseK getStatusMotorZAchseK() {
        if ((read() & (I_21 | I_22)) == (I_21 | I_22))
            return I_ZAchseK.DAZWISCHEN;
        else if ((read() & I_21) == 0)
            return I_ZAchseK.OBEN;
        else if ((read() & I_22) == 0)
            return I_ZAchseK.UNTEN;
        else {
            setStatusMotorZAchseK(Q_ZAchseK.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusMagnetK(Q_MagnetK neuerStatus) {
        if (neuerStatus == Q_MagnetK.AUS) {
            resetOutputBit(Q_24);

        } else if (neuerStatus == Q_MagnetK.AN) {
            setOutputBit(Q_24);

        }
        write();
    }

    @Override
    public I_EinlegestationK istEinlegestationBelegtK() {
        return (read() & I_01) == 0 ? I_EinlegestationK.BELEGT : I_EinlegestationK.NICHT_BELEGT;
    }

    @Override
    public I_AusschleussbahnK istAusschleussbahnBelegtK() {
        return (read() & I_14) == 0 ? I_AusschleussbahnK.BELEGT : I_AusschleussbahnK.NICHT_BELEGT;
    }

    @Override
    public boolean initiatorXAchseK() {
        return (read() & I_28) == 0;
    }

    @Override
    public boolean initiatorYAchseK() {
        return (read() & I_29) == 0;
    }

    @Override
    public boolean initiatorZAchseK() {
        return (read() & I_30) == 0;
    }



    /************************
     *  IMBohrmaschine
     ************************/
    @Override
    public void setStatusHubBm(Q_HubBm neuerStatus) {
        if (neuerStatus == Q_HubBm.AUS) {
            resetOutputBit(Q_3 | Q_4);

        } else if (neuerStatus == Q_HubBm.AUF) {
            resetOutputBit(Q_4);
            setOutputBit(Q_3);

        } else if (neuerStatus == Q_HubBm.AB) {
            resetOutputBit(Q_3);
            setOutputBit(Q_4);

        }
        write();
    }

    @Override
    public I_HubBm getStatusHubBm() {
        if ((read() & (I_04 | I_05)) == (I_04 | I_05))
            return I_HubBm.DAZWISCHEN;
        else if ((read() & I_04) == 0)
            return I_HubBm.UNTEN;
        else if ((read() & I_05) == 0)
            return I_HubBm.OBEN;
        else {
            setStatusHubBm(Q_HubBm.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusWerkzeugAntriebBm(WerkzeugAntriebBm neuerStatus) {
        if (neuerStatus == WerkzeugAntriebBm.AUS) {
            resetOutputBit(Q_5);

        } else if (neuerStatus == WerkzeugAntriebBm.AN) {
            setOutputBit(Q_5);

        }
        write();
    }

    @Override
    public void setStatusBandBm(BandBm neuerStatus) {
        if (neuerStatus == BandBm.AUS) {
            resetOutputBit(Q_15);

        } else if (neuerStatus == BandBm.AN) {
            setOutputBit(Q_15);

        }
        write();
    }

    @Override
    public boolean istBandstartBm() {
        return (read() & I_13) == 0;
    }

    @Override
    public boolean InitiatorBm() {
        return (read() & I_25) == 0;
    }



    /************************
     *  IMFraesmaschine
     ************************/
    @Override
    public void setStatusHubF(Q_HubF neuerStatus) {
        if (neuerStatus == Q_HubF.AUS) {
            resetOutputBit(Q_10 | Q_11);

        } else if (neuerStatus == Q_HubF.AUF) {
            resetOutputBit(Q_11);
            setOutputBit(Q_10);

        } else if (neuerStatus == Q_HubF.AB) {
            resetOutputBit(Q_10);
            setOutputBit(Q_11);

        }
        write();
    }

    @Override
    public I_HubF getStatusHubF() {
        if ((read() & (I_09 | I_10)) == (I_09 | I_10))
            return I_HubF.DAZWISCHEN;
        else if ((read() & I_09) == 0)
            return I_HubF.OBEN;
        else if ((read() & I_10) == 0)
            return I_HubF.UNTEN;
        else {
            setStatusHubF(Q_HubF.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusQuerschlittenF(QuerschlittenF neuerStatus) {
        if (neuerStatus == QuerschlittenF.AUS) {
            resetOutputBit(Q_12 | Q_13);

        } else if (neuerStatus == QuerschlittenF.VOR) {
            resetOutputBit(Q_12);
            setOutputBit(Q_13);

        } else if (neuerStatus == QuerschlittenF.RUECK) {
            resetOutputBit(Q_13);
            setOutputBit(Q_12);

        }
        write();
    }

    @Override
    public void setStatusWerkzeugAntriebF(WerkzeugAntriebF neuerStatus) {
        if (neuerStatus == WerkzeugAntriebF.AN)
            setOutputBit(Q_14);

        else if (neuerStatus == WerkzeugAntriebF.AUS)
            resetOutputBit(Q_14);

        write();
    }

    @Override
    public void setStatusBandF(BandF neuerStatus) {
        if (neuerStatus == BandF.AN)
            setOutputBit(Q_17);
        else if (neuerStatus == BandF.AUS)
            resetOutputBit(Q_17);
        write();
    }

    @Override
    public boolean istBandpositionF() {
        return (read() & I_11) == 0;
    }

    @Override
    public boolean istStaenderpositionF() {
        return (read() & I_12) == 0;
    }

    @Override
    public boolean initiatorF() {
        return (read() & I_27) == 0;
    }


    /************************
     *  IMMehrspindelmaschine
     ************************/

    @Override
    public void setStatusHubM(Q_HubM neuerStatus) {
        if (neuerStatus == Q_HubM.AUS) {
            resetOutputBit(Q_6 | Q_7);

        } else if (neuerStatus == Q_HubM.AUF) {
            resetOutputBit(Q_7);
            setOutputBit(Q_6);

        } else if (neuerStatus == Q_HubM.AB) {
            resetOutputBit(Q_6);
            setOutputBit(Q_7);

        }
        write();
    }

    @Override
    public I_HubM getStatusHubM() {
        if ((read() & (I_06 | I_07)) == (I_06 | I_07))
            return I_HubM.DAZWISCHEN;
        else if ((read() & I_06) == 0)
            return I_HubM.OBEN;
        else if ((read() & I_07) == 0)
            return I_HubM.UNTEN;
        else {
            setStatusHubM(Q_HubM.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setStatusRevolverdrehungM(RevolverdrehungM neuerStatus) {
        if (neuerStatus == RevolverdrehungM.AUS)
            resetOutputBit(Q_8);
        else if (neuerStatus == RevolverdrehungM.AN)
            setOutputBit(Q_8);
        write();
    }

    @Override
    public void setStatusWerkzeugAntriebM(WerkzeugAntriebM neuerStatus) {
        if (neuerStatus == WerkzeugAntriebM.AUS)
            resetOutputBit(Q_9);
        else if (neuerStatus == WerkzeugAntriebM.AN)
            setOutputBit(Q_9);
        write();
    }

    @Override
    public void setStatusBandM(BandM neuerStatus) {
        if (neuerStatus == BandM.AUS)
            resetOutputBit(Q_16);
        else if (neuerStatus == BandM.AN)
            setOutputBit(Q_16);
        write();
    }

    @Override
    public boolean revolverPositionMelderM() {
        return (read() & I_08) == 0;
    }

    @Override
    public I_AusschleussbahnM istAusschleussbahnBelegtM() {
        return (read() & I_14) == 0 ? I_AusschleussbahnM.BELEGT : I_AusschleussbahnM.NICHT_BELEGT;
    }

    @Override
    public boolean initiatorM() {
        return (read() & I_26) == 0;
    }
}
