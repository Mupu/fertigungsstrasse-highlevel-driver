package me.mupu;

//import quancom.UsbOptoRel32; // RELEASE

import me.mupu._testUsbInterface.UsbOptoRel32; // DEBUG
import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.*;

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

    // todo wenn kein sensor an den 3 maschinen
    // todo flags hinzufügen
    //  1 = kann annehmen 0 = kann nicht annehmen <- setzten die maschinen selber
    // (M1)(M2)(M3) (Ausgabe)
    //  1   1   1   (1) <-- () = ausschleuse (kran muss ihn abholen)
    //  0   1   1   (1)
    //  1   0   1   (1)     nach unten hin die zeit
    //  0   0   1   (1)
    //  0   1   0   (1)
    //  1   0   0   (1)             das ganze ist mit warten bis platz frei ist
    //  1   0   1   (0)             könnte auch simultan teil wegschieben und neues annehmen
    //  1   1   0   (0)
    //  1   1   0   (0)
    //  1   1   1   (0)
    //  1   1   1   (1)

    // todo add synchronized to every set method
    // todo bei set noch inputs checken (runtimeexceptions werfen und maschine ausschalten)
    // todo warning ausgeben wenn zb: magnet an ist und er nochmals angemacht werden soll
    // todo eventuell error bei falschen I_.. zb: I_21 | I_22 nur als warnung machen <- zimmer fragen

    /************************
     *  IMSchieber
     ************************/
    @Override
    public synchronized void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_1 | Q_2);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) { // todo vor = rechts ?
            resetOutputBit(Q_2);
            setOutputBit(Q_1);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) { // todo rueck = links ?
            resetOutputBit(Q_1);
            setOutputBit(Q_2);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionSchieberS() {
        int data = read();
        if ((data & (I_02 | I_03)) == (I_02 | I_03))
            return ESensorXAchse.DAZWISCHEN;
        else if ((data & I_02) == 0)
            return ESensorXAchse.LINKS;
        else if ((data & I_03) == 0)
            return ESensorXAchse.RECHTS;
        else {
            setMotorstatusSchieberS(EMotorbewegungXAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public ESensorstatus istEinlegestationBelegtS() {
        return (read() & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtS() {
        return (read() & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }




    /************************
     *  IKran
     ************************/
    @Override
    public synchronized void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_18 | Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
            resetOutputBit(Q_18);
            setOutputBit(Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_19);
            setOutputBit(Q_18);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionXAchseK() {
        if ((read() & (I_17 | I_18)) == (I_17 | I_18))
            return ESensorXAchse.DAZWISCHEN;
        else if ((read() & I_17) == 0)
            return ESensorXAchse.RECHTS;
        else if ((read() & I_18) == 0)
            return ESensorXAchse.LINKS;
        else {
            setMotorstatusXAchseK(EMotorbewegungXAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_20 | Q_21);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_21);
            setOutputBit(Q_20);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_20);
            setOutputBit(Q_21);

        }
        write();
    }

    @Override
    public ESensorYAchse getPositionYAchseK() {
        if ((read() & (I_19 | I_20)) == (I_19 | I_20))
            return ESensorYAchse.DAZWISCHEN;
        else if ((read() & I_19) == 0)
            return ESensorYAchse.VORNE;
        else if ((read() & I_20) == 0)
            return ESensorYAchse.HINTEN;
        else {
            setMotorstatusYAchseK(EMotorbewegungYAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_22 | Q_23);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_23);
            setOutputBit(Q_22);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_22);
            setOutputBit(Q_23);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionZAchseK() {
        if ((read() & (I_21 | I_22)) == (I_21 | I_22))
            return ESensorZAchse.DAZWISCHEN;
        else if ((read() & (I_21 | I_22)) == I_22)  // 21 geschaltet aber 22 nicht // todo nochmal anschauen
            return ESensorZAchse.OBEN;
        else if ((read() & (I_22 | I_21)) == I_21)  // 22 geschaltet aber 21 nicht
            return ESensorZAchse.UNTEN;
        else {
            setMotorstatusZAchseK(EMotorbewegungZAchse.AUS);
            throw new RuntimeException("Illegale Bits"); // todo fehler bits spezifizieren
        }
    }

    @Override
    public void setMotorstatusMagnetK(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_24);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_24);
        }
        write();
    }

    @Override
    public ESensorstatus istEinlegestationBelegtK() {
        return (read() & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtK() {
        return (read() & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorXAchseK() {
        return (read() & I_28) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorYAchseK() {
        return (read() & I_29) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorZAchseK() {
        return (read() & I_30) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    /************************
     *  IMBohrmaschine
     ************************/
    @Override
    public void setMotorstatusHubBm(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_3 | Q_4);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_4);
            setOutputBit(Q_3);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_3);
            setOutputBit(Q_4);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubBm() {
        if ((read() & (I_04 | I_05)) == (I_04 | I_05))
            return ESensorZAchse.DAZWISCHEN;
        else if ((read() & I_04) == 0)
            return ESensorZAchse.UNTEN;
        else if ((read() & I_05) == 0)
            return ESensorZAchse.OBEN;
        else {
            setMotorstatusHubBm(EMotorbewegungZAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusWerkzeugAntriebBm(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_5);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_5);

        }
        write();
    }

    @Override
    public void setMotorstatusBandBm(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_15);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_15);

        }
        write();
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtBm() {
        return (read() & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorBm() {
        return (read() & I_25) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
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
    public void setStatusQuerschlittenF(Q_QuerschlittenF neuerStatus) {
        if (neuerStatus == Q_QuerschlittenF.AUS) {
            resetOutputBit(Q_12 | Q_13);

        } else if (neuerStatus == Q_QuerschlittenF.VOR) {
            resetOutputBit(Q_12);
            setOutputBit(Q_13);

        } else if (neuerStatus == Q_QuerschlittenF.RUECK) {
            resetOutputBit(Q_13);
            setOutputBit(Q_12);

        }
        write();
    }

    // todo alle read() in locale variable speichern
    @Override
    public I_QuerschlittenF getStatusQuerschlittenF() {
        if ((read() & (I_11 | I_12)) == (I_11 | I_12))
            return I_QuerschlittenF.DAZWISCHEN;
        else if ((read() & I_11) == 0)
            return I_QuerschlittenF.BANDPOSITION;
        else if ((read() & I_12) == 0)
            return I_QuerschlittenF.STAENDERPOSITION;
        else {
            setStatusQuerschlittenF(Q_QuerschlittenF.AUS);
            throw new RuntimeException("Illegale Bits");
        }
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
