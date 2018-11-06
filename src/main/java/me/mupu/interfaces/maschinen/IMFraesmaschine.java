package me.mupu.interfaces.maschinen;

public interface IMFraesmaschine {

    enum Q_HubF {
        AUF, AB, AUS
    }

    enum Q_QuerschlittenF {
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

    enum I_QuerschlittenF {
        BANDPOSITION, STAENDERPOSITION, DAZWISCHEN
    }

    void setStatusHubF(Q_HubF neuerStatus);
    I_HubF getStatusHubF();

    void setStatusQuerschlittenF(Q_QuerschlittenF neuerStatus);
    I_QuerschlittenF getStatusQuerschlittenF();

    void setStatusWerkzeugAntriebF(WerkzeugAntriebF neuerStatus);

    void setStatusBandF(BandF neuerStatus);

    boolean initiatorF();
}
