package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IMFraesmaschine {

    /**
     * (Q_10) Motor-Fraesmaschine Hub auf.
     * (Q_11) Motor-Fraesmaschine Hub ab.
     */
    void setMotorstatusHubF(EMotorbewegungZAchse neuerStatus);

    /**
     * (I_09) ET Fräsmaschine Hubeinheit oben
     * (I_10) ET Fräsmaschine Hubeinheit unten
     */
    ESensorZAchse getPositionHubF();

    /**
     * (Q_12) Motor-Fraesmaschine Querschlitten rueck.
     * (Q_13) Motor-Fraesmaschine Querschlitten vor.
     */
    void setMotorstatusQuerschlittenF(EMotorbewegungYAchse neuerStatus);

    /**
     * (I_11) ET Fräsmaschine Querschlitten vorne
     * (I_12) ET Fräsmaschine Querschlitten hinten
     */
    ESensorYAchse getPositionQuerschlittenF();

    /**
     * (Q_14) Motor-Fraesmaschine Werkzeug-Antrieb.
     */
    void setMotorstatusWerkzeugAntriebF(EMotorstatus neuerStatus);

    /**
     * (Q_17) Motor-Band Fraesmaschine.
     */
    void setMotorstatusBandF(EMotorstatus neuerStatus);

    /**
     * (I_27) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?)
     */
    ESensorstatus initiatorF();
}
