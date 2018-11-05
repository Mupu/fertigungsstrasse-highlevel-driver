package me.mupu.interfaces;

public interface IMFraesmaschine {

    enum Hub {
        AUF, AB, AUS;
    }

    enum Querschlitten {
        RUECK, VOR, AUS;
    }

    enum WerkzeugAntrieb {
        AN, AUS;
    }

    enum Band {
        AN, AUS;
    }

    void setStatusFraesmaschineHub(Hub neuerStatus);
    Hub getStatusHub();

    void setStatusQuerschlitten(Querschlitten neuerStatus);
    Querschlitten getStatusQuerschlitten();

    void setStatusFraesmaschineWerkzeugAntrieb(WerkzeugAntrieb neuerStatus);
    WerkzeugAntrieb getStatusWerkzeugAntrieb();

    void setStatusFraesmaschineBand(Band neuerStatus);
    Band getStatusBand();

}
