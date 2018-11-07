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
    public int output = 0; // default alle aus
    public int input;

    // RELEASE
    //    private static FertigungsstrasseHLD instance;
    //    private final UsbOptoRel32 usbInterface;
    //    private int output = 0; // default alle aus

//    /**
//     * Bei Benutzung sollte eine locale kopie erstellt werden,
//     * da sich diese Variable verändern kann.<br></br>
//     * input ist invertiert: Default 111111.... und nicht 000000...
//     */
//    private int input;
    private static int READ_DELAY = 0;
    private Thread readingThread;

    /**
     * Versucht USB-Interface zu oeffnen.
     */
    // todo maybe open & close methode ?
    private FertigungsstrasseHLD() {

        usbInterface = new UsbOptoRel32();

        if (!usbInterface.open())
            throw new RuntimeException("Quancom Verbindung konnte nicht hergestellt werden!");


        readingThread = new Thread(() -> {
            while (true) {
                try {
                    if (READ_DELAY != 0) {
                        Thread.sleep(READ_DELAY); // todo testen ob funktioniert
                    }
                    input = usbInterface.digitalIn();
                } catch (Exception e) {
                    notfallStop();
                    System.exit(1); // todo testen ?
                    throw new RuntimeException("Interface nicht mehr erreichbar!");
                }
            }
        });
        readingThread.start();

        // todo initial state ?
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Schieber-Gruppe zurueck.
     */
    public static IMSchieber getSchieber() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Kran-Gruppe zurueck.
     */
    public static IKran getKran() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Bohrmaschinen-Gruppe zurueck.
     */
    public static IMBohrmaschine getBohrmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Fraesmaschinen-Gruppe zurueck.
     */
    public static IMFraesmaschine getFraesmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Mehrspindelmaschinen-Gruppe zurueck.
     */
    public static IMMehrspindelmaschine getMehrspindelmaschine() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();
        return instance;
    }


    /**
     * Versucht alle output-bits auf 'aus' zu setzen.
     * Beendet nicht den readingThread.
     */
    public static void notfallStop() { // todo testen
        try {
            instance.usbInterface.digitalOut(0);
        } catch (Exception ignored) {
        }
    }

    /**
     * Setzt einen delay für den Aufruf der USB-Interface-digitalIn Methode fest.<br></br>
     * 0 = kein Delay.<br></br>
     * Default ist 0.
     *
     * @param readDelay Zeit in Millisekunden
     */
    public static void setReadDelay(int readDelay) {
        FertigungsstrasseHLD.READ_DELAY = readDelay;
    }


    /**
     * Setzt die Bits.
     *
     * @param mask Maske
     */
    private void setOutputBit(int mask) {
        this.output |= mask;
    }


    /**
     * Setzt die Bits zurueck.
     *
     * @param mask Maske
     */
    private void resetOutputBit(int mask) {
        this.output &= ~mask;
    }


    /**
     * Gibt den output ueber das USB-Interface an die Maschinen weiter.
     *
     * @throws RuntimeException wenn USB-Interface nicht erreichbar ist
     */
    private synchronized void write() {
        try {
            usbInterface.digitalOut(output);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Interface nicht mehr erreichbar!");
        }
    }


    // todo flags hinzufügen ÜBERDENKEN (band login hinzufügen)
    //  1 = kann annehmen 0 = kann nicht annehmen <- setzen die maschinen selber
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
    // todo add synchronized check testclass
    // todo zimmer fragen wegen vor rueck z.B. Setzt (Q_12) Motor-Fraesmaschine Querschlitten rueck, prueft (I_12) ET Fräsmaschine Querschlitten Ständerposition (HINTEN).

    //************************
    //*  IMSchieber
    //************************
    @Override
    public synchronized void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_1 | Q_2);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_2);

            if (getPositionSchieberS() == ESensorXAchse.RECHTS) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_1 wenn I_03 aktiv");
            } else
                setOutputBit(Q_1);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
            resetOutputBit(Q_1);

            if (getPositionSchieberS() == ESensorXAchse.LINKS) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_2 wenn I_02 aktiv");
            } else
                setOutputBit(Q_2);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionSchieberS() {
        int data = input & (I_02 | I_03); // maske

        if (data == (I_02 | I_03)) {
            return ESensorXAchse.DAZWISCHEN;

        } else if (data == I_03) {
            return ESensorXAchse.LINKS;

        } else if (data == I_02) {
            return ESensorXAchse.RECHTS;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_02 | I_03)");
        }
    }

    @Override
    public ESensorstatus istEinlegestationBelegtS() {
        return (input & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtS() {
        return (input & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IKran
    //************************
    @Override
    public synchronized void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_18 | Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
            resetOutputBit(Q_18);

            if (getPositionXAchseK() == ESensorXAchse.LINKS) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_19 wenn I_18 aktiv");
            } else
                setOutputBit(Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_19);

            if (getPositionXAchseK() == ESensorXAchse.RECHTS) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_18 wenn I_17 aktiv");
            } else
                setOutputBit(Q_18);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionXAchseK() {
        int data = input & (I_17 | I_18); // maske

        if (data == (I_17 | I_18)) {
            return ESensorXAchse.DAZWISCHEN;

        } else if (data == I_18) {
            return ESensorXAchse.RECHTS;

        } else if (data == I_17) {
            return ESensorXAchse.LINKS;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_17 | I_18)");
        }
    }

    @Override
    public synchronized void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_20 | Q_21);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_21);

            if (getPositionYAchseK() == ESensorYAchse.VORNE) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_20 wenn I_19 aktiv");
            } else
                setOutputBit(Q_20);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_20);

            if (getPositionYAchseK() == ESensorYAchse.HINTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_21 wenn I_20 aktiv");
            } else
                setOutputBit(Q_21);

        }
        write();
    }

    @Override
    public ESensorYAchse getPositionYAchseK() {
        int data = input & (I_19 | I_20); // maske

        if (data == (I_19 | I_20)) {
            return ESensorYAchse.DAZWISCHEN;

        } else if (data == I_20) {
            return ESensorYAchse.VORNE;

        } else if (data == I_19) {
            return ESensorYAchse.HINTEN;

        } else {
            throw new RuntimeException("Illegaler Zustand: (I_19 | I_20)");
        }
    }

    @Override
    public synchronized void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_22 | Q_23);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_23);

            if (getPositionZAchseK() == ESensorZAchse.OBEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_22 wenn I_21 aktiv");
            } else
                setOutputBit(Q_22);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_22);

            if (getPositionZAchseK() == ESensorZAchse.UNTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_23 wenn I_22 aktiv");
            } else
                setOutputBit(Q_23);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionZAchseK() {
        int data = input & (I_21 | I_22); // maske


        if (data == (I_21 | I_22)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_22) {
            return ESensorZAchse.OBEN;

        } else if (data == I_21) {
            return ESensorZAchse.UNTEN;

        } else {
            throw new RuntimeException("Illegaler Zustand: (I_21 | I_22)");
        }
    }

    @Override
    public synchronized void setMotorstatusMagnetK(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_24);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_24);

        }
        write();
    }

    @Override
    public ESensorstatus istEinlegestationBelegtK() {
        return (input & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtK() {
        return (input & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorXAchseK() {
        return (input & I_28) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorYAchseK() {
        return (input & I_29) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorZAchseK() {
        return (input & I_30) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMBohrmaschine
    //************************
    @Override
    public synchronized void setMotorstatusHubBm(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_3 | Q_4);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_4);

            if (getPositionHubBm() == ESensorZAchse.OBEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_3 wenn I_04 aktiv");
            } else
                setOutputBit(Q_3);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_3);

            if (getPositionHubBm() == ESensorZAchse.UNTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_4 wenn I_05 aktiv");
            } else
                setOutputBit(Q_4);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubBm() {
        int data = input & (I_04 | I_05); // maske

        if (data == (I_04 | I_05)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_04) {
            return ESensorZAchse.UNTEN;

        } else if (data == I_05) {
            return ESensorZAchse.OBEN;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_04 | I_05)");
        }
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebBm(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_5);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_5);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandBm(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_15);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_15);

        }
        write();
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtBm() {
        return (input & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorBm() {
        return (input & I_25) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMFraesmaschine
    //************************
    @Override
    public synchronized void setMotorstatusHubF(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_10 | Q_11);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_11);

            if (getPositionHubF() == ESensorZAchse.OBEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_10 wenn I_09 aktiv");
            } else
                setOutputBit(Q_10);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_10);

            if (getPositionHubF() == ESensorZAchse.UNTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_11 wenn I_10 aktiv");
            } else
                setOutputBit(Q_11);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubF() {
        int data = input & (I_09 | I_10); // maske

        if (data == (I_09 | I_10)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_10) {
            return ESensorZAchse.OBEN;

        } else if (data == I_09) {
            return ESensorZAchse.UNTEN;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_09 | I_10)");
        }
    }

    @Override
    public synchronized void setMotorstatusQuerschlittenF(EMotorbewegungYAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_12 | Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_12);

            if (getPositionQuerschlittenF() == ESensorYAchse.VORNE) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_13 wenn I_11 aktiv");
            } else
                setOutputBit(Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_13);

            if (getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_12 wenn I_12 aktiv");
            } else
                setOutputBit(Q_12);

        }
        write();
    }


    @Override
    public ESensorYAchse getPositionQuerschlittenF() {
        int data = input & (I_11 | I_12); // maske

        if (data == (I_11 | I_12)) {
            return ESensorYAchse.DAZWISCHEN;

        } else if (data == I_12) {
            return ESensorYAchse.VORNE;

        } else if (data == I_11) {
            return ESensorYAchse.HINTEN;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_11 | I_12)");
        }
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebF(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_14);

        } else if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_14);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandF(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_17);
        else if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_17);
        write();
    }

    @Override
    public ESensorstatus initiatorF() {
        return (input & I_27) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMMehrspindelmaschine
    //************************

    @Override
    public synchronized void setMotorstatusHubM(EMotorbewegungZAchse neuerStatus) {
        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_6 | Q_7);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_7);

            if (getPositionHubM() == ESensorZAchse.OBEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_6 wenn I_06 aktiv");
            } else
                setOutputBit(Q_6);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_6);

            if (getPositionHubM() == ESensorZAchse.UNTEN) {
                notfallStop();
                throw new RuntimeException("Illegale Aktion: set Q_7 wenn I_07 aktiv");
            } else
                setOutputBit(Q_7);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubM() {
        int data = input & (I_06 | I_07); // maske

        if (data == (I_06 | I_07)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_07) {
            return ESensorZAchse.OBEN;

        } else if (data == I_06) {
            return ESensorZAchse.UNTEN;

        } else {
            notfallStop();
            throw new RuntimeException("Illegaler Zustand: (I_06 | I_07)");
        }
    }

    @Override
    public synchronized void setMotorstatusRevolverdrehungM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_8);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_8);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_9);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_9);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandM(EMotorstatus neuerStatus) {
        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_16);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_16);

        }
        write();
    }

    @Override
    public ESensorstatus revolverPositionMelderM() {
        return (input & I_08) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtM() {
        return (input & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorM() {
        return (input & I_26) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }
}
