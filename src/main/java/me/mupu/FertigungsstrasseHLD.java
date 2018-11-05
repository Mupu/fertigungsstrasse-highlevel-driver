package me.mupu;

import me.mupu.interfaces.*;
import quancom.UsbOptoRel32;

import java.io.IOException;

import static me.mupu.interfaces.bitpos.IOutput.*;
import static me.mupu.interfaces.bitpos.IInput.*;

public class FertigungsstrasseHLD implements IKran, IMMehrspindelmaschine, IMBohrmaschine, IMFraesmaschine, IMSchieber {

    private static FertigungsstrasseHLD instance;
    private final UsbOptoRel32 usbInterface;
    private int output = 0;

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


    /************************
     *  IMSchieber
     ************************/
    @Override
    public synchronized void setStatusEinlegestationS(Q_EinlegestationS neuerStatus) {
        switch (neuerStatus) {
            case AUS:
                resetOutputBit(Q_1 | Q_2);
                break;
            case VOR: // todo bremz zeit einbauen ?
                resetOutputBit(Q_2);
                setOutputBit(Q_1);
                break;
            case RUECK:
                resetOutputBit(Q_1);
                setOutputBit(Q_2);
                break;
        }
        write();
    }

    @Override
    public boolean istBelegtS() {
        return (read() & I_01) == I_01;
    }

    @Override
    public boolean istInAusgangslageS() {
        return (read() & I_02) == I_02;
    }

    @Override
    public boolean istBandpositionS() {
        return (read() & I_03) == I_03;
    }


    /************************
     *  IKran
     ************************/
    @Override
    public void setStatusMotorXAchseK(Q_XAchseK neuerStatus) {
        switch (neuerStatus) {
            case AUS:
                resetOutputBit(Q_18 | Q_19);
                break;
            case LINKS:
                resetOutputBit(Q_18);
                setOutputBit(Q_19);
                break;
            case RECHTS:
                resetOutputBit(Q_19);
                setOutputBit(Q_18);
                break;
        }
        write();
    }

    @Override
    public I_XAchseK getStatusMotorXAchseK() {
        if ((read() & I_17) == I_17)
            return I_XAchseK.RECHTS;
        else if ((read() & I_18) == I_18)
            return I_XAchseK.LINKS;
        else
            return null;
    }

    @Override
    public void setStatusMotorYAchseK(Q_YAchseK neuerStatus) {
        switch (neuerStatus) {
            case AUS:
                resetOutputBit(Q_20 | Q_21);
                break;
            case VOR:
                resetOutputBit(Q_21);
                setOutputBit(Q_20);
                break;
            case ZURUECK:
                resetOutputBit(Q_20);
                setOutputBit(Q_21);
                break;
        }
        write();
    }

    @Override
    public I_YAchseK getStatusMotorYAchseK() {
        if ((read() & I_19) == I_19)
            return I_YAchseK.VORNE;
        else if ((read() & I_20) == I_20)
            return I_YAchseK.HINTEN;
        else
            return null;
    }

    @Override
    public void setStatusMotorZAchseK(Q_ZAchseK neuerStatus) {
        switch (neuerStatus) {
            case AUS:
                resetOutputBit(Q_22 | Q_23);
                break;
            case HOCH:
                resetOutputBit(Q_23);
                setOutputBit(Q_22);
                break;
            case RUNTER:
                resetOutputBit(Q_22);
                setOutputBit(Q_23);
                break;
        }
        write();
    }

    @Override
    public I_ZAchseK getStatusMotorZAchseK() {
        if ((read() & I_21) == I_21)
            return I_ZAchseK.OBEN;
        else if ((read() & I_22) == I_22)
            return I_ZAchseK.UNTEN;
        else
            return null;
    }

    @Override
    public void setStatusMagnet(Q_MagnetK neuerStatus) {
        if (neuerStatus == Q_MagnetK.AUS) {
            resetOutputBit(Q_24);

        } else if (neuerStatus == Q_MagnetK.AN) {
            setOutputBit(Q_24);

        }
        write();
    }

    @Override
    public I_EinlegestationK istEinlegestationBelegtK() {
        return (read() & I_01) == I_01 ? I_EinlegestationK.BELEGT : I_EinlegestationK.NICHT_BELEGT;
    }

    @Override
    public I_AusschleussbahnK istAusschleussbahnBelegtK() {
        return (read() & I_14) == I_14 ? I_AusschleussbahnK.BELEGT : I_AusschleussbahnK.NICHT_BELEGT;
    }

    @Override
    public boolean InitiatorXAchseK() {
        return (read() & I_28) == I_28;
    }

    @Override
    public boolean InitiatorYAchseK() {
        return (read() & I_29) == I_29;
    }

    @Override
    public boolean InitiatorZAchseK() {
        return (read() & I_30) == I_30;
    }



    /************************
     *  IMBohrmaschine
     ************************/
    @Override
    public void setStatusBohrmaschineHub(HubBm neuerStatus) {
        switch (neuerStatus) {
            case AUS:
                resetOutputBit(Q_3 | Q_4);
                break;
            case AUF:
                resetOutputBit(Q_4);
                setOutputBit(Q_3);
                break;
            case AB:
                resetOutputBit(Q_3);
                setOutputBit(Q_4);
                break;
        }
        write();
    }

    @Override
    public HubBm getStatusBohrmaschineHub() {
        if ((read() & I_03) == I_03)
            return HubBm.AUF;
        else if ((read() & I_04) == I_04)
            return HubBm.AB;
        else if ((read() & (I_03 | I_04)) == 0)
            return HubBm.AUS;
        return null;
    }

    @Override
    public void setStatusBohrmaschineWerkzeugAntrieb(WerkzeugAntriebBm neuerStatus) {
        if (neuerStatus == WerkzeugAntriebBm.AUS) {
            resetOutputBit(Q_5);

        } else if (neuerStatus == WerkzeugAntriebBm.AN) {
            setOutputBit(Q_5);

        }
        write();
    }

    @Override
    public void setStatusBohrmaschineBand(BandBm neuerStatus) {
        if (neuerStatus == BandBm.AUS) {
            resetOutputBit(Q_15);

        } else if (neuerStatus == BandBm.AN) {
            setOutputBit(Q_15);

        }
        write();
    }

    @Override
    public boolean istBandstartBm() {
        return (read() & I_13) == I_13;
    }

    @Override
    public boolean InitiatorBm() {
        return (read() & I_25) == I_25;
    }



    /************************
     *  IMFraesmaschine
     ************************/


    /************************
     *  IMMehrspindelmaschine
     ************************/

}
