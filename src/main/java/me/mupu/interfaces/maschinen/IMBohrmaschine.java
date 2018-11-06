package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IMBohrmaschine {


    void setMotorstatusHubBm(EMotorbewegungZAchse neuerStatus);

    void setMotorstatusWerkzeugAntriebBm(EMotorstatus neuerStatus);

    void setMotorstatusBandBm(EMotorstatus neuerStatus);

    ESensorZAchse getPositionHubBm();

    /**
     * (I_13) Fotowiderstand Bandstart vor Bohrmaschine
     */
    ESensorstatus istUebergabestelleVorBohrmaschineBelegtBm();

    /**
     * (I_25) Initiator Bohrmaschine Werkstückposition
     */
    // todo ist das hubinitiator ?
    ESensorstatus initiatorBm();

}
