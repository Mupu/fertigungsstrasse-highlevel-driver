package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

/**
 * Dieses interface ist die Schnittstelle fuer die Fraesmaschinen-Gruppe.
 */
public interface IMFraesmaschine {

    /**
     * Versucht den Motorstatus zu setzen.<br></br>
     * <br></br>
     *
     * Setzt (Q_10) Motor-Fraesmaschine Hub auf,
     * prueft (I_09) ET Fräsmaschine Hubeinheit oben.<br></br>
     * <br></br>
     *
     * Setzt (Q_11) Motor-Fraesmaschine Hub ab,
     * prueft (I_10) ET Fräsmaschine Hubeinheit unten.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusHubF(EMotorbewegungZAchse neuerStatus);

    /**
     * Prueft (I_09) ET Fräsmaschine Hubeinheit oben.<br></br>
     * <br></br>
     * Prueft (I_10) ET Fräsmaschine Hubeinheit unten.
     *
     * @return Gibt die Position der Hubeinheit zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorZAchse getPositionHubF();

    /**
     * Versucht den Motorstatus zu setzen.<br></br>
     * <br></br>
     *
     * Setzt (Q_12) Motor-Fraesmaschine Querschlitten rueck,
     * prueft (I_12) ET Fräsmaschine Querschlitten Ständerposition (HINTEN).<br></br>
     * <br></br>
     *
     * Setzt (Q_13) Motor-Fraesmaschine Querschlitten vor,
     * prueft (I_11) ET Fräsmaschine Querschlitten Bandposition (VORNE).
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusQuerschlittenF(EMotorbewegungYAchse neuerStatus);

    /**
     * Prueft (I_11) ET Fräsmaschine Querschlitten vorne.<br></br>
     * <br></br>
     * Prueft (I_12) ET Fräsmaschine Querschlitten hinten.
     *
     * @return Gibt die Position des Querschlitten zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorYAchse getPositionQuerschlittenF();

    /**
     * Versucht den Status von (Q_14) Motor-Fraesmaschine Werkzeug-Antrieb zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusWerkzeugAntriebF(EMotorstatus neuerStatus);

    /**
     * Versucht den Status von (Q_17) Motor-Band Fraesmaschine zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusBandF(EMotorstatus neuerStatus);

    /**
     * @return Gibt den Status von (I_27) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?) zurueck.
     */
    ESensorstatus initiatorF();
}
