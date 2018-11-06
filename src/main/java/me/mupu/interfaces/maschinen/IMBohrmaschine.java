package me.mupu.interfaces.maschinen;

public interface IMBohrmaschine {

    enum Q_HubBm {
        AUF, AB, AUS
    }

    enum WerkzeugAntriebBm {
        AN, AUS
    }

    enum BandBm {
        AN, AUS
    }

    enum I_HubBm {
        OBEN, UNTEN, DAZWISCHEN
    }

    void setStatusHubBm(Q_HubBm neuerStatus);
    I_HubBm getStatusHubBm();

    void setStatusWerkzeugAntriebBm(WerkzeugAntriebBm neuerStatus);

    void setStatusBandBm(BandBm neuerStatus);

    boolean istBandstartBm();
    boolean initiatorBm();

}
