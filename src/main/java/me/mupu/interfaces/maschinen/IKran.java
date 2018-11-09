package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

/**
 * Dieses interface ist die Schnittstelle fuer die Kran-Gruppe.
 */
public interface IKran {

    /**
     * Versucht den Motorstatus zu setzen.<br>
     * <br>
     *
     * Setzt (Q_18) Motor X-Achse rechts,
     * prueft (I_17) ET Xa (RECHTS).<br>
     * <br>
     *
     * Setzt (Q_19) Motor X-Achse links,
     * prueft (I_18) ET Xe (LINKS).
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus);

    /**
     * Versucht den Motorstatus zu setzen.<br>
     * <br>
     *
     * Setzt (Q_20) Motor Y-Achse vor,
     * prueft (I_19) ET Ya (VORNE).<br>
     * <br>
     *
     * Setzt (Q_21) Motor Y-Achse rueck,
     * prueft (I_20) ET Ye (HINTEN).
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus);

    /**
     * Versucht den Motorstatus zu setzen.<br>
     * <br>
     *
     * Setzt (Q_22) Motor Z-Achse hoch,
     * prueft (I_21) ET Za (OBEN).<br>
     * <br>
     *
     * Setzt (Q_23) Motor Z-Achse runter,
     * prueft (I_22) ET Ze (UNTEN).
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     * @throws RuntimeException wenn Pruefung fehlschlaegt.
     */
    void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus);

    /**
     * Versucht den Status von (Q_24) Elektromagnet 24V zu setzen.
     *
     * @param neuerStatus Status den die Maschine einnehmen soll.
     */
    void setMotorstatusMagnetK(EMotorstatus neuerStatus);


    /**
     * Prueft (I_17) ET Xa (RECHTS).<br>
     * <br>
     * Prueft (I_18) ET Xe (LINKS).
     *
     * @return Gibt die Position der XAchse zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorXAchse getPositionXAchseK();


    /**
     * Prueft (I_19) ET Ya (VORNE).<br>
     * <br>
     * Prueft (I_20) ET Ye (HINTEN).
     *
     * @return Gibt die Position der YAchse zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorYAchse getPositionYAchseK();


    /**
     * Prueft (I_21) ET Za (OBEN).<br>
     * <br>
     * Prueft (I_22) ET Ze (UNTEN).
     *
     * @return Gibt die Position der ZAchse zurueck.
     * @throws RuntimeException wenn beide aktiv.
     */
    ESensorZAchse getPositionZAchseK();

    /**
     * @return Gibt den Status von (I_01) ET Belegtmeldung Q_Einlegestation zurueck.
     */
    ESensorstatus istEinlegestationBelegtK();

    /**
     * @return Gibt den Status von (I_14) Fotowiderstand Ausschleusbahn zurueck.
     */
    ESensorstatus istAusschleussbahnBelegtK();

    /**
     * @return Gibt den Status von (I_28) Initiator X-Achse zurueck.
     */
    ESensorstatus initiatorXAchseK();

    /**
     * @return Gibt den Status von (I_29) Initiator Y-Achse zurueck.
     */
    ESensorstatus initiatorYAchseK();

    /**
     * @return Gibt den Status von (I_30) Initiator Z-Achse zurueck.
     */
    ESensorstatus initiatorZAchseK();
}
