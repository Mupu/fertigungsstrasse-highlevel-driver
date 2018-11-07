package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

/**
 * Dieses interface ist die Schnittstelle fuer die Bohrmaschinen-Gruppe.
 */
public interface IMBohrmaschine {

    /**
     * Versucht den Motorstatus zu setzen.<br></br>
     * <br></br>
     *
     * Setzt (Q_3) Motor-Bohrmaschine Hub auf,
     * prueft (I_04) ET Bohrmaschine Hubeinheit oben.<br></br>
     * <br></br>
     *
     * Setzt (Q_4) Motor-Bohrmaschine Hub ab,
     * prueft (I_05) ET Bohrmaschine Hubeinheit unten.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusHubBm(EMotorbewegungZAchse neuerStatus);

    /**
     * Versucht den Status von (Q_5) Motor-Bohrmaschine Werkzeug-Antrieb zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusWerkzeugAntriebBm(EMotorstatus neuerStatus);

    /**
     * Versucht den Status von (Q_15) Motor-Band Bohrmaschine zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusBandBm(EMotorstatus neuerStatus);

    /**
     * Prueft (I_04) ET Bohrmaschine Hubeinheit oben.<br></br>
     * <br></br>
     * Prueft (I_05) ET Bohrmaschine Hubeinheit unten.
     *
     * @return Gibt die Position der Hubeinheit zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorZAchse getPositionHubBm();

    /**
     * @return Gibt den Status von (I_26) Initiator Mehrspindelmaschine Werkstückposition (ist Werkstueck in Position?) zurueck.
     */
    ESensorstatus istMehrspindelmaschineBelegtB();

    /**
     * Zeigt an, ob die vorherige Maschine ihr Werkstueck abgeben will.
     *
     * @return Gibt den Flag des Schiebers zurueck.
     */
    ESensorstatus sollWerkstueckAnnehmenB();

    /**
     * Zeigt der Mehrspindelmaschine an, dass diese das Werkstueck weitergeben will.
     *
     * @param neuerWert Wert den der Flag haben soll.
     */
    void setFlagWillWerkstueckAbgebenB(boolean neuerWert);

    /**
     * @return Gibt den Status von (I_13) Fotowiderstand Bandstart vor Bohrmaschine.
     */
    ESensorstatus istUebergabestelleVorBohrmaschineBelegtBm();

    /**
     * @return Gibt den Status von (I_25) Initiator Bohrmaschine Werkstückposition (ist Werkstueck in Position?) zurueck.
     */
    ESensorstatus initiatorBm();

}
