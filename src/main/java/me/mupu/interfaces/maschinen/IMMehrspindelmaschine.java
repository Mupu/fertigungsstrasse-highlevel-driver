package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IMMehrspindelmaschine {

    /**
     * (Q_6) Motor-Mehrspindelmaschine Hub auf.
     * (Q_7) Motor-Mehrspindelmaschine Hub ab.
     */
    void setMotorstatusHubM(EMotorbewegungZAchse neuerStatus);

    /**
     * (I_06) ET Mehrspindelmaschine Hubeinheit oben
     * (I_07) ET Mehrspindelmaschine Hubeinheit unten
     */
    ESensorZAchse getPositionHubM();

    /**
     * (Q_8) Motor-Mehrspindelmaschine Revolverdrehung.
     */
    void setMotorstatusRevolverdrehungM(EMotorstatus neuerStatus);

    /**
     * (Q_9) Motor-Mehrspindelmaschine Werkzeug-Antrieb.
     */
    void setMotorstatusWerkzeugAntriebM(EMotorstatus neuerStatus);

    /**
     * (Q_16) Motor-Band Mehrspindelmaschine.
     */
    void setMotorstatusBandM(EMotorstatus neuerStatus);

    /**
     * (I_08) ET Mehrspindelmaschine Revolverpositionsmelder
     */
    ESensorstatus revolverPositionMelderM();

    /**
     * (I_14) Fotowiderstand Ausschleusbahn
     */
    ESensorstatus istAusschleussbahnBelegtM();

    /**
     * (I_26) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?)
     */
    ESensorstatus initiatorM();
}
