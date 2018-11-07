package me.mupu.interfaces.maschinen;

import me.mupu.enums.motorbewegungen.EMotorbewegungYAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungZAchse;
import me.mupu.enums.motorbewegungen.EMotorbewegungXAchse;
import me.mupu.enums.motorbewegungen.EMotorstatus;
import me.mupu.enums.sensoren.ESensorXAchse;
import me.mupu.enums.sensoren.ESensorYAchse;
import me.mupu.enums.sensoren.ESensorZAchse;
import me.mupu.enums.sensoren.ESensorstatus;

public interface IKran {

    /**
     * (Q_18) Motor X-Achse rechts.
     * (Q_19) Motor X-Achse links.
     */
    void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus);

    /**
     * (Q_20) Motor Y-Achse vor.
     * (Q_21) Motor Y-Achse rueck.
     */
    void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus);

    /**
     * (Q_22) Motor Z-Achse hoch.
     * (Q_23) Motor Z-Achse runter.
     */
    void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus);

    /**
     * (Q_24) Elektromagnet 24V.
     */
    void setMotorstatusMagnetK(EMotorstatus neuerStatus);


    /**
     * (I_17) ET Xa (RECHTS).
     * (I_18) ET Xe (LINKS).
     */
    ESensorXAchse getPositionXAchseK();


    /**
     * (I_19) ET Ya (VORNE).
     * (I_20) ET Ye (HINTEN).
     */
    ESensorYAchse getPositionYAchseK();


    /**
     * (I_21) ET Za (OBEN).
     * (I_22) ET Ze (UNTEN).
     */
    ESensorZAchse getPositionZAchseK();

    /**
     * (I_01) ET Belegtmeldung Q_Einlegestation
     */
    ESensorstatus istEinlegestationBelegtK();

    /**
     * (I_14) Fotowiderstand Ausschleusbahn
     */
    ESensorstatus istAusschleussbahnBelegtK();

    /**
     * (I_28) Initiator X-Achse
     */
    ESensorstatus initiatorXAchseK();

    /**
     * (I_29) Initiator Y-Achse
     */
    ESensorstatus initiatorYAchseK();

    /**
     * (I_30) Initiator Z-Achse
     */
    ESensorstatus initiatorZAchseK();
}
