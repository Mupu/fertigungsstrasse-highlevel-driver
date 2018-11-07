package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

/**
 * Dieses interface ist die Schnittstelle fuer die Mehrspindelmaschinen-Gruppe.
 */
public interface IMMehrspindelmaschine {

    /**
     * Versucht den Motorstatus zu setzen.<br></br>
     * <br></br>
     *
     * Setzt (Q_6) Motor-Mehrspindelmaschine Hub auf,
     * prueft (I_06) ET Mehrspindelmaschine Hubeinheit oben.<br></br>
     * <br></br>
     *
     * Setzt (Q_7) Motor-Mehrspindelmaschine Hub ab.,
     * prueft (I_07) ET Mehrspindelmaschine Hubeinheit unten.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusHubM(EMotorbewegungZAchse neuerStatus);

    /**
     * Prueft (I_06) ET Mehrspindelmaschine Hubeinheit oben.<br></br>
     * <br></br>
     * Prueft (I_07) ET Mehrspindelmaschine Hubeinheit unten.
     *
     * @return Gibt die Position der Hubeinheit zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorZAchse getPositionHubM();

    /**
     * Versucht den Status von (Q_8) Motor-Mehrspindelmaschine Revolverdrehung zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusRevolverdrehungM(EMotorstatus neuerStatus);

    /**
     * Versucht den Status von (Q_9) Motor-Mehrspindelmaschine Werkzeug-Antrieb zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusWerkzeugAntriebM(EMotorstatus neuerStatus);

    /**
     * Versucht den Status von (Q_16) Motor-Band Mehrspindelmaschine zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusBandM(EMotorstatus neuerStatus);

    /**
     * @return Gibt den Status von (I_08) ET Mehrspindelmaschine Revolverpositionsmelder zurueck.
     */
    ESensorstatus revolverPositionMelderM();

    /**
     * @return Gibt den Status von (I_14) Fotowiderstand Ausschleusbahn zurueck.
     */
    ESensorstatus istAusschleussbahnBelegtM();

    /**
     * @return Gibt den Status von (I_26) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?) zurueck.
     */
    ESensorstatus initiatorM();
}
