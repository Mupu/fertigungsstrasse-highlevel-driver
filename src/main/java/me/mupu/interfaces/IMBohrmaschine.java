package me.mupu.interfaces;

public interface IMBohrmaschine {

    enum HubBm {
        AUF, AB, AUS
    }

    enum WerkzeugAntriebBm {
        AN, AUS
    }

    enum BandBm {
        AN, AUS
    }

    void setStatusBohrmaschineHub(HubBm neuerStatus);
    HubBm getStatusBohrmaschineHub();

    void setStatusBohrmaschineWerkzeugAntrieb(WerkzeugAntriebBm neuerStatus);

    void setStatusBohrmaschineBand(BandBm neuerStatus);

    boolean istBandstartBm();
    boolean InitiatorBm();

}
