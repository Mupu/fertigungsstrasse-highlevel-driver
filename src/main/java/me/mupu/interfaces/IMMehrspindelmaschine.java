package me.mupu.interfaces;

public interface IMMehrspindelmaschine {

    enum Hub {
        AUF, AB, AUS;
    }

    enum Revolverdrehung {
        AN, AUS;
    }

    enum WerkzeugAntrieb {
        AN, AUS;
    }

    enum Band {
        AN, AUS;
    }

    boolean setStatusMehrspindelmaschineHub(Hub neuerStatus);
    Hub getStatusHub();

    boolean setStatusRevolverdrehung(Revolverdrehung neuerStatus);
    Revolverdrehung getStatusRevolverdrehung();

    boolean setStatusMehrspindelmaschineWerkzeugAntrieb(WerkzeugAntrieb neuerStatus);
    WerkzeugAntrieb getStatusWerkzeugAntrieb();

    boolean setStatusMehrspindelmaschineBand(Band neuerStatus);
    Band getStatusBand();

}
