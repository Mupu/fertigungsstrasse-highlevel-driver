package me.mupu.interfaces;

public interface IMMehrspindelmaschine {

    enum Q_HubM {
        AUF, AB, AUS
    }

    enum RevolverdrehungM {
        AN, AUS
    }

    enum WerkzeugAntriebM {
        AN, AUS
    }

    enum BandM {
        AN, AUS
    }

    enum I_HubM {
        OBEN, UNTEN, DAZWISCHEN
    }

    enum I_AusschleussbahnM {
        BELEGT, NICHT_BELEGT
    }

    void setStatusHubM(Q_HubM neuerStatus);
    I_HubM getStatusHubM();

    void setStatusRevolverdrehungM(RevolverdrehungM neuerStatus);

    void setStatusWerkzeugAntriebM(WerkzeugAntriebM neuerStatus);

    void setStatusBandM(BandM neuerStatus);

    boolean revolverPositionMelderM();

    I_AusschleussbahnM istAusschleussbahnBelegtM();

    boolean initiatorM();
}
