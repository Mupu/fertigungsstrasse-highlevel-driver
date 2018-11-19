package me.mupu;

import quancom.UsbOptoRel32; // RELEASE
//import me.mupu._testUsbInterface.UsbOptoRel32; // DEBUG

import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;
import me.mupu.interfaces.maschinen.*;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static me.mupu.interfaces.bitpos.IOutput.*;
import static me.mupu.interfaces.bitpos.IInput.*;

/**
 * Die Highlevel-Treiberklasse des USB-Interfaces fuer die Fertigungsstrasse.
 * Bietet die Schnittstellen zum Arbeiten mit der Fertigungsstrasse.
 */
public class FertigungsstrasseHLD implements IKran, IMMehrspindelmaschine, IMBohrmaschine, IMFraesmaschine, IMSchieber {
    // 1 = werkstueck; 0 = kein werkstueck

    //  x ~> y = x braucht initiator-werkstueck-sensor von Y
    //            ~>     ~>    ~>      ~>

    //  x <~ y = y braucht will-abgeben-flag von x
    //            <~     <~    <~

    //  - = braucht ein will abgeben flag
    //       -        -     -
    //      (S)      (B)   (M)     (F)    (Ausgabe)
    //      (1)       0     0       0      (0)
    //      (0)       1     0       0      (0)
    //      (0)       0     1       0      (0)
    //      (0)       0     0       1      (0)
    //      (0)       0     0       0      (1)


    /**
     * Bei Benutzung sollte eine locale kopie erstellt werden,
     * da sich diese Variable veraendern kann.<br>
     * input ist invertiert: Default 111111.... und nicht 000000...
     */
    private int input; // vielleicht volatile ?
    private int output; // vielleicht volatile ?

    private final UsbOptoRel32 usbInterface;

    private static FertigungsstrasseHLD instance = null;
    private static int READ_DELAY = 0;
    private static boolean use32BitInsteadOf64Bit = false;
    private static boolean schnittstelleOffen = false;
    private ReadingThread readingThread = null;


    private boolean flagWillWerkstueckAbgebenS = false;
    private boolean flagWillWerkstueckAbgebenB = false;
    private boolean flagWillWerkstueckAbgebenM = false;


    /**
     * Erstellt die dll und das USB-Interface.
     */
    private FertigungsstrasseHLD() {
        erstelleDLLWennNichtVorhanden();

        usbInterface = new UsbOptoRel32();
        output = 0; // default alles aus
        input = ~0; // default alles aus
    }

    /**
     * Erstellt die DLL wenn sie noch nicht vorhanden ist und löscht vorherige.
     */
    private void erstelleDLLWennNichtVorhanden() {
        try {
            // delete old files
            Files.deleteIfExists(new File("Qlib32.dll").toPath());
            Files.deleteIfExists(new File("Qlib64.dll").toPath());

            final File tempFile;
            if (use32BitInsteadOf64Bit)
                tempFile = new File("Qlib32.dll");
            else
                tempFile = new File("Qlib64.dll");

            if (tempFile.createNewFile()) {
                final FileOutputStream fos = new FileOutputStream(tempFile);

                final InputStream is;
                if (use32BitInsteadOf64Bit)
                    is = getClass().getClassLoader().getResourceAsStream("Qlib32.dll");
                else
                    is = getClass().getClassLoader().getResourceAsStream("Qlib64.dll");

                int n;
                final byte buffer[] = new byte[4096];
                while (-1 != (n = is.read(buffer))) {
                    fos.write(buffer, 0, n);
                    fos.flush();
                }
                is.close();
                fos.close();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Benutzt die 32-Bit version statt der 64-Bit version.
     * Muss vor der Erstellung dieser Klasse aufgerufen werden.
     */
    public static void use32BitInsteadOf64Bit() {
        use32BitInsteadOf64Bit = true;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Schieber-Gruppe zurueck.
     */
    public static IMSchieber getSchieber() {
        if (instance == null)
            throw new RuntimeException("Quancom Verbindung noch nicht hergestellt. Rufe zuerst 'open()' auf.");
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Kran-Gruppe zurueck.
     */
    public static IKran getKran() {
        if (instance == null)
            throw new RuntimeException("Quancom Verbindung noch nicht hergestellt. Rufe zuerst 'open()' auf.");
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Bohrmaschinen-Gruppe zurueck.
     */
    public static IMBohrmaschine getBohrmaschine() {
        if (instance == null)
            throw new RuntimeException("Quancom Verbindung noch nicht hergestellt. Rufe zuerst 'open()' auf.");
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Fraesmaschinen-Gruppe zurueck.
     */
    public static IMFraesmaschine getFraesmaschine() {
        if (instance == null)
            throw new RuntimeException("Quancom Verbindung noch nicht hergestellt. Rufe zuerst 'open()' auf.");
        return instance;
    }

    /**
     * Eine Klasse, die alle benoetigten Methoden bereitstellt.
     *
     * @return Gibt die Schnittstelle fuer die Mehrspindelmaschinen-Gruppe zurueck.
     */
    public static IMMehrspindelmaschine getMehrspindelmaschine() {
        if (instance == null)
            throw new RuntimeException("Quancom Verbindung noch nicht hergestellt. Rufe zuerst 'open()' auf.");
        return instance;
    }

    /**
     * Versucht USB-Interface zu oeffnen.
     * Erstellt einen ReadingThread der den input des USB-Interfaces zyclisch ausliesst.
     */
    public static void open() {
        if (schnittstelleOffen)
            return;

        instance = new FertigungsstrasseHLD();

        if (!instance.usbInterface.open()) {
            close();
            throw new RuntimeException("Quancom Verbindung konnte nicht hergestellt werden!");
        } else {
            instance.readingThread = new ReadingThread();
            instance.readingThread.start();
            schnittstelleOffen = true;
        }
    }

    /**
     * Thread der den input des USB-Interfaces zyclisch ausliesst.
     */
    private static class ReadingThread extends Thread {
        private boolean terminate = false;

        @Override
        public void run() {
            super.run();
            while (!terminate) {
                try {
                    if (READ_DELAY > 0) {
                        Thread.sleep(READ_DELAY);
                    }
                    instance.input = instance.usbInterface.digitalIn();
                } catch (Exception e) {
                    close();
                    throw new RuntimeException("Interface nicht mehr erreichbar!");
                }
            }
        }

        public void terminate() {
            terminate = true;
        }
    }

    /**
     * Versucht alle output-Bits auf 'AUS' zu setzten.
     * Ausserdem wird versucht das USB-Interface zu schliessen und
     * den ReadingThread zu beenden.
     */
    public static void close() {
        if (!schnittstelleOffen)
            return;

        schnittstelleOffen = false;

        instance.readingThread.terminate();

        synchronized (instance) {
            try {
                instance.usbInterface.digitalOut(0);
            } catch (Exception ignored) {
            }

            instance.usbInterface.close();
        }
    }

    /**
     * Setzt einen delay fuer den Aufruf der USB-Interface-digitalIn Methode fest.<br>
     * 0 = kein Delay.<br>
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
            close();
            throw new RuntimeException("Interface nicht mehr erreichbar!");
        }
    }

    /**
     * @param object Object das getestet werden soll
     * @throws RuntimeException wenn object NULL ist
     */
    private void throwErrorIfNull(Object object) {
        if (object == null) {
            close();
            throw new RuntimeException("Wrong Input: Parameter darf nicht NULL sein!");
        }
    }


    //************************
    //*  IMSchieber
    //************************
    @Override
    public synchronized void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        // todo synchronized ab hier. und davor prüfen ob änderung passiert ist

        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_1 | Q_2);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_2);

            if (getPositionSchieberS() == ESensorXAchse.RECHTS) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_1 wenn I_03 aktiv");
            } else
                setOutputBit(Q_1);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
            resetOutputBit(Q_1);

            if (getPositionSchieberS() == ESensorXAchse.LINKS) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_2 wenn I_02 aktiv");
            } else
                setOutputBit(Q_2);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionSchieberS() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_02 | I_03); // maske

        if (data == (I_02 | I_03)) {
            return ESensorXAchse.DAZWISCHEN;

        } else if (data == I_03) {
            return ESensorXAchse.LINKS;

        } else if (data == I_02) {
            return ESensorXAchse.RECHTS;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_02 | I_03)");
        }
    }

    @Override
    public ESensorstatus istEinlegestationBelegtS() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtS() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istBohrmaschineBelegtS() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_25) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public void setFlagWillWerkstueckAbgebenS(boolean neuerWert) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        flagWillWerkstueckAbgebenS = neuerWert;
    }

    //************************
    //*  IKran
    //************************
    @Override
    public synchronized void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungXAchse.AUS) {
            resetOutputBit(Q_18 | Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.LINKS) {
            resetOutputBit(Q_18);

            if (getPositionXAchseK() == ESensorXAchse.LINKS) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_19 wenn I_18 aktiv");
            } else
                setOutputBit(Q_19);

        } else if (neuerStatus == EMotorbewegungXAchse.RECHTS) {
            resetOutputBit(Q_19);

            if (getPositionXAchseK() == ESensorXAchse.RECHTS) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_18 wenn I_17 aktiv");
            } else
                setOutputBit(Q_18);

        }
        write();
    }

    @Override
    public ESensorXAchse getPositionXAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_17 | I_18); // maske

        if (data == (I_17 | I_18)) {
            return ESensorXAchse.DAZWISCHEN;

        } else if (data == I_18) {
            return ESensorXAchse.RECHTS;

        } else if (data == I_17) {
            return ESensorXAchse.LINKS;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_17 | I_18)");
        }
    }

    @Override
    public synchronized void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_20 | Q_21);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_21);

            if (getPositionYAchseK() == ESensorYAchse.VORNE) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_20 wenn I_19 aktiv");
            } else
                setOutputBit(Q_20);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_20);

            if (getPositionYAchseK() == ESensorYAchse.HINTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_21 wenn I_20 aktiv");
            } else
                setOutputBit(Q_21);

        }
        write();
    }

    @Override
    public ESensorYAchse getPositionYAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_19 | I_20); // maske

        if (data == (I_19 | I_20)) {
            return ESensorYAchse.DAZWISCHEN;

        } else if (data == I_20) {
            return ESensorYAchse.VORNE;

        } else if (data == I_19) {
            return ESensorYAchse.HINTEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_19 | I_20)");
        }
    }

    @Override
    public synchronized void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_22 | Q_23);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_23);

            if (getPositionZAchseK() == ESensorZAchse.OBEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_22 wenn I_21 aktiv");
            } else
                setOutputBit(Q_22);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_22);

            if (getPositionZAchseK() == ESensorZAchse.UNTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_23 wenn I_22 aktiv");
            } else
                setOutputBit(Q_23);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionZAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_21 | I_22); // maske


        if (data == (I_21 | I_22)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_22) {
            return ESensorZAchse.OBEN;

        } else if (data == I_21) {
            return ESensorZAchse.UNTEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_21 | I_22)");
        }
    }

    @Override
    public synchronized void setMotorstatusMagnetK(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_24);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_24);

        }
        write();
    }

    @Override
    public ESensorstatus istSchieberInGrundpositionK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_02) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istEinlegestationBelegtK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_01) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorXAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_28) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorYAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_29) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorZAchseK() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_30) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMBohrmaschine
    //************************
    @Override
    public synchronized void setMotorstatusHubB(EMotorbewegungZAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_3 | Q_4);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_4);

            if (getPositionHubB() == ESensorZAchse.OBEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_3 wenn I_04 aktiv");
            } else
                setOutputBit(Q_3);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_3);

            if (getPositionHubB() == ESensorZAchse.UNTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_4 wenn I_05 aktiv");
            } else
                setOutputBit(Q_4);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubB() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_04 | I_05); // maske

        if (data == (I_04 | I_05)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_04) {
            return ESensorZAchse.UNTEN;

        } else if (data == I_05) {
            return ESensorZAchse.OBEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_04 | I_05)");
        }
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebB(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_5);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_5);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandB(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_15);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_15);

        }
        write();
    }

    @Override
    public ESensorstatus istMehrspindelmaschineBelegtB() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_26) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus sollWerkstueckAnnehmenB() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return flagWillWerkstueckAbgebenS ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public void setFlagWillWerkstueckAbgebenB(boolean neuerWert) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        flagWillWerkstueckAbgebenB = neuerWert;
    }

    @Override
    public ESensorstatus istUebergabestelleVorBohrmaschineBelegtB() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_13) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorB() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_25) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMMehrspindelmaschine
    //************************

    @Override
    public synchronized void setMotorstatusHubM(EMotorbewegungZAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_6 | Q_7);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_7);

            if (getPositionHubM() == ESensorZAchse.OBEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_6 wenn I_06 aktiv");
            } else
                setOutputBit(Q_6);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_6);

            if (getPositionHubM() == ESensorZAchse.UNTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_7 wenn I_07 aktiv");
            } else
                setOutputBit(Q_7);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubM() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_06 | I_07); // maske

        if (data == (I_06 | I_07)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_07) {
            return ESensorZAchse.OBEN;

        } else if (data == I_06) {
            return ESensorZAchse.UNTEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_06 | I_07)");
        }
    }

    @Override
    public synchronized void setMotorstatusRevolverdrehungM(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_8);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_8);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebM(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_9);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_9);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandM(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_16);

        } else if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_16);

        }
        write();
    }

    @Override
    public ESensorstatus istRevolverAufPositionM() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_08) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus istFraesmaschineBelegtM() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_27) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus sollWerkstueckAnnehmenM() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return flagWillWerkstueckAbgebenB ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public void setFlagWillWerkstueckAbgebenM(boolean neuerWert) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        flagWillWerkstueckAbgebenM = neuerWert;
    }

    @Override
    public ESensorstatus initiatorM() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_26) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }


    //************************
    //*  IMFraesmaschine
    //************************
    @Override
    public synchronized void setMotorstatusHubF(EMotorbewegungZAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungZAchse.AUS) {
            resetOutputBit(Q_10 | Q_11);

        } else if (neuerStatus == EMotorbewegungZAchse.AUF) {
            resetOutputBit(Q_11);

            if (getPositionHubF() == ESensorZAchse.OBEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_10 wenn I_09 aktiv");
            } else
                setOutputBit(Q_10);

        } else if (neuerStatus == EMotorbewegungZAchse.AB) {
            resetOutputBit(Q_10);

            if (getPositionHubF() == ESensorZAchse.UNTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_11 wenn I_10 aktiv");
            } else
                setOutputBit(Q_11);

        }
        write();
    }

    @Override
    public ESensorZAchse getPositionHubF() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_09 | I_10); // maske

        if (data == (I_09 | I_10)) {
            return ESensorZAchse.DAZWISCHEN;

        } else if (data == I_10) {
            return ESensorZAchse.OBEN;

        } else if (data == I_09) {
            return ESensorZAchse.UNTEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_09 | I_10)");
        }
    }

    @Override
    public synchronized void setMotorstatusQuerschlittenF(EMotorbewegungYAchse neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorbewegungYAchse.AUS) {
            resetOutputBit(Q_12 | Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.VOR) {
            resetOutputBit(Q_12);

            if (getPositionQuerschlittenF() == ESensorYAchse.VORNE) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_13 wenn I_11 aktiv");
            } else
                setOutputBit(Q_13);

        } else if (neuerStatus == EMotorbewegungYAchse.ZURUECK) {
            resetOutputBit(Q_13);

            if (getPositionQuerschlittenF() == ESensorYAchse.HINTEN) {
                close();
                throw new RuntimeException("Illegale Aktion: set Q_12 wenn I_12 aktiv");
            } else
                setOutputBit(Q_12);

        }
        write();
    }


    @Override
    public ESensorYAchse getPositionQuerschlittenF() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        int data = input & (I_11 | I_12); // maske

        if (data == (I_11 | I_12)) {
            return ESensorYAchse.DAZWISCHEN;

        } else if (data == I_12) {
            return ESensorYAchse.VORNE;

        } else if (data == I_11) {
            return ESensorYAchse.HINTEN;

        } else {
            close();
            throw new RuntimeException("Illegaler Zustand: (I_11 | I_12)");
        }
    }

    @Override
    public synchronized void setMotorstatusWerkzeugAntriebF(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AN) {
            setOutputBit(Q_14);

        } else if (neuerStatus == EMotorstatus.AUS) {
            resetOutputBit(Q_14);

        }
        write();
    }

    @Override
    public synchronized void setMotorstatusBandF(EMotorstatus neuerStatus) {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        throwErrorIfNull(neuerStatus);

        if (neuerStatus == EMotorstatus.AN)
            setOutputBit(Q_17);
        else if (neuerStatus == EMotorstatus.AUS)
            resetOutputBit(Q_17);
        write();
    }

    @Override
    public ESensorstatus istAusschleussbahnBelegtF() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_14) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus sollWerkstueckAnnehmenF() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return flagWillWerkstueckAbgebenM ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }

    @Override
    public ESensorstatus initiatorF() {
        if (!schnittstelleOffen)
            throw new RuntimeException("Interface nicht geoeffnet. Benutze 'open()'.");

        return (input & I_27) == 0 ? ESensorstatus.SIGNAL : ESensorstatus.KEIN_SIGNAL;
    }
}
