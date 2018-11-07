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


    /**
     * Setzt alle outputs auf 0 und beendet das Program.
     */
    public static void notStop() {
        try {
            instance.usbInterface.digitalOut(0);
        } catch (Exception ignored) {}

        System.exit(0);
    }

    // todo bessere interface javadocs
    // todo read thread machen und read() nur variable lesen lassen
    // todo notStop vor error benutzen
    // todo alle read() in locale variable speichern
    // todo add synchronized to every set method
    // todo bei set noch inputs checken (runtimeexceptions werfen und maschine ausschalten)
    // todo fehler bits spezifizieren
    // todo error bei falschen I_.. zb: I_21 | I_22
    // todo flags hinzufügen ÜBERDENKEN
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


    /************************
     *  IMSchieber
     ************************/
    @Override
    public synchronized void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_1 | Q_2);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_2);
            setOutputBit(Q_1);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
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
            throw new RuntimeException("Illegale Bits");
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
    public void setMotorstatusHubF(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_10 | Q_11);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_11);
            setOutputBit(Q_10);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_10);
            setOutputBit(Q_11);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubF() {
        if ((read() & (I_09 | I_10)) == (I_09 | I_10))
            return ESensorZAchse.DAZWISCHEN;
        else if ((read() & I_09) == 0)
            return ESensorZAchse.OBEN;
        else if ((read() & I_10) == 0)
            return ESensorZAchse.UNTEN;
        else {
            setMotorstatusHubF(EMotorbewegungZAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusQuerschlittenF(EMotorbewegungYAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_12 | Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_12);
            setOutputBit(Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_13);
            setOutputBit(Q_12);

        }
        write();
    }


    @Override
    public ESensorYAchse getPositionQuerschlittenF() {
        if ((read() & (I_11 | I_12)) == (I_11 | I_12))
            return ESensorYAchse.DAZWISCHEN;
        else if ((read() & I_11) == 0)
            return ESensorYAchse.VORNE;
        else if ((read() & I_12) == 0)
            return ESensorYAchse.HINTEN;
        else {
            setMotorstatusQuerschlittenF(EMotorbewegungYAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusWerkzeugAntriebF(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_14);

        else if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_14);

        write();
    }

    @Override
    public void setMotorstatusBandF(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_17);
        else if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_17);
        write();
    }

    @Override
    public ESensorstatus initiatorF() {
        return (read() & I_27) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    /************************
     *  IMMehrspindelmaschine
     ************************/

    @Override
    public void setMotorstatusHubM(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_6 | Q_7);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_7);
            setOutputBit(Q_6);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_6);
            setOutputBit(Q_7);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubM() {
        if ((read() & (I_06 | I_07)) == (I_06 | I_07))
            return ESensorZAchse.DAZWISCHEN;
        else if ((read() & I_06) == 0)
            return ESensorZAchse.OBEN;
        else if ((read() & I_07) == 0)
            return ESensorZAchse.UNTEN;
        else {
            setMotorstatusHubM(EMotorbewegungZAchse.AUS);
            throw new RuntimeException("Illegale Bits");
        }
    }

    @Override
    public void setMotorstatusRevolverdrehungM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_8);
        else if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_8);
        write();
    }

    @Override
    public void setMotorstatusWerkzeugAntriebM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_9);
        else if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_9);
        write();
    }

    @Override
    public void setMotorstatusBandM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_16);
        else if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_16);
        write();
    }

    @Override
    public ESensorstatus revolverPositionMelderM() {
        return (read() & I_08) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtM() {
        return (read() & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorM() {
        return (read() & I_26) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }
}
