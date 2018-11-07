package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IMBohrmaschine {

    /**
     * (Q_3) Motor-Bohrmaschine Hub auf.
     * (Q_4) Motor-Bohrmaschine Hub ab.
     */
    void setMotorstatusHubBm(EMotorbewegungZAchse neuerStatus);

    /**
     * (Q_5) Motor-Bohrmaschine Werkzeug-Antrieb.
     */
    void setMotorstatusWerkzeugAntriebBm(EMotorstatus neuerStatus);

    /**
     * (Q_15) Motor-Band Bohrmaschine.
     */
    void setMotorstatusBandBm(EMotorstatus neuerStatus);

    /**
     * (I_04) ET Bohrmaschine Hubeinheit oben
     * (I_05) ET Bohrmaschine Hubeinheit unten
     */
    ESensorZAchse getPositionHubBm();

    /**
     * (I_13) Fotowiderstand Bandstart vor Bohrmaschine
     */
    ESensorstatus istUebergabestelleVorBohrmaschineBelegtBm();

    /**
     * (I_25) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?)
     */
    ESensorstatus initiatorBm();

}
