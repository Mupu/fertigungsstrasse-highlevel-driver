package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorstatus;

/**
 * Dieses interface ist die Schnittstelle fuer die Schieber-Gruppe.
 */
public interface IMSchieber {

    /**
     * Versucht den Motorstatus zu setzen.<br></br>
     * <br></br>
     * 
     * Setzt (Q_1) Motor-Schieber Einlegestation vor (RECHTS),
     * prueft (I_03) ET Schieber Bandposition Einlegestation (RECHTS).<br></br>
     * <br></br>
     *
     * Setzt (Q_2) Motor-Schieber Einlegestation rueck (LINKS),
     * prueft (I_02) ET Schieber Ausgangslage Einlagestation (LINKS).
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusSchieberS(EMotorbewegungXAchse neuerStatus);

    /**
     * Prueft (I_02) ET Schieber Ausgangslage Einlagestation (LINKS).<br></br>
     * <br></br>
     * Prueft (I_03) ET Schieber Bandposition Einlegestation (RECHTS).
     *
     * @return Gibt die Position des Schiebers zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorXAchse getPositionSchieberS();

    /**
     * @return Gibt den Status von (I_01) ET Belegtmeldung Q_Einlegestation zurueck.
     */
    ESensorstatus istEinlegestationBelegtS();

    /**
     * @return Gibt den Status von (I_13) Fotowiderstand Bandstart vor Bohrmaschine zurueck.
     */
    ESensorstatus istUebergabestelleVorBohrmaschineBelegtS();
}
