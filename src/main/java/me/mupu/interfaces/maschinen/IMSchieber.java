package me.mupu.interfaces.maschinen;

public interface IMSchieber {

    enum Q_EinlegestationS {
        VOR, RUECK, AUS
    }

    void setStatusEinlegestationS(Q_EinlegestationS neuerStatus);

    boolean istBelegtS();
    boolean istInAusgangslageS();
    boolean istBandpositionS();

}
