package me.mupu.interfaces;

public interface IMFraesmaschine {

    enum Q_HubF {
        AUF, AB, AUS
    }

    enum QuerschlittenF {
        RUECK, VOR, AUS
    }

    enum WerkzeugAntriebF {
        AN, AUS
    }

    enum BandF {
        AN, AUS
    }

    enum I_HubF {
        OBEN, UNTEN, DAZWISCHEN
    }

    void setStatusHubF(Q_HubF neuerStatus);
    I_HubF getStatusHubF();

    void setStatusQuerschlittenF(QuerschlittenF neuerStatus);

    void setStatusWerkzeugAntriebF(WerkzeugAntriebF neuerStatus);

    void setStatusBandF(BandF neuerStatus);
    boolean istBandpositionF();

    boolean istStaenderpositionF();

    boolean initiatorF();
}
