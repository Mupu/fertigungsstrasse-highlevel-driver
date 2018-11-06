package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IMSchieber {

    /**
     * (Q_1) Motor-Schieber Einlegestation vor (RECHTS).
     * (Q_2) Motor-Schieber Einlegestation rueck (LINKS).
     */
    void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus);

    /**
     * (I_02) ET Schieber Ausgangslage Einlagestation (LINKS).
     * (I_03) ET Schieber Bandposition Einlegestation (RECHTS).
     */
    ESensorXAchse getPositionSchieberS();

    ESensorstatus istEinlegestationBelegtS();

    /**
     * (I_13) Fotowiderstand Bandstart vor Bohrmaschine
     */
    ESensorstatus istUebergabestelleVorBohrmaschineBelegtS();
}
