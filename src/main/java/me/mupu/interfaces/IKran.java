package me.mupu.interfaces;

public interface IKran {

    enum Q_XAchseK {
        LINKS, RECHTS, AUS
    }
    enum Q_YAchseK {
        VOR, ZURUECK, AUS
    }

    enum Q_ZAchseK {
        HOCH, RUNTER, AUS
    }

    enum Q_MagnetK {
        AN, AUS
    }

    enum I_EinlegestationK {
        BELEGT, NICHT_BELEGT
    }

    enum I_AusschleussbahnK {
        BELEGT, NICHT_BELEGT
    }

    enum I_XAchseK {
        RECHTS, LINKS, DAZWISCHEN
    }

    enum I_YAchseK {
        VORNE, HINTEN, DAZWISCHEN
    }

    enum I_ZAchseK {
        OBEN, UNTEN, DAZWISCHEN
    }

    void setStatusMotorXAchseK(Q_XAchseK neuerStatus);
    I_XAchseK getStatusMotorXAchseK();

    void setStatusMotorYAchseK(Q_YAchseK neuerStatus);
    I_YAchseK getStatusMotorYAchseK();

    void setStatusMotorZAchseK(Q_ZAchseK neuerStatus);
    I_ZAchseK getStatusMotorZAchseK();

    void setStatusMagnetK(Q_MagnetK neuerStatus);

    I_EinlegestationK istEinlegestationBelegtK();
    I_AusschleussbahnK istAusschleussbahnBelegtK();

    boolean InitiatorXAchseK();
    boolean InitiatorYAchseK();
    boolean InitiatorZAchseK();
}
