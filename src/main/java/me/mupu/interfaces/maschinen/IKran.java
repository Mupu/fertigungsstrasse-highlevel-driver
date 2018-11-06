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

    void setMotorstatusXAchseK(EMotorbewegungXAchse neuerStatus);

    void setMotorstatusYAchseK(EMotorbewegungYAchse neuerStatus);

    void setMotorstatusZAchseK(EMotorbewegungZAchse neuerStatus);

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


    ESensorstatus istEinlegestationBelegtK();
    ESensorstatus istAusschleussbahnBelegtK();

    ESensorstatus initiatorXAchseK();
    ESensorstatus initiatorYAchseK();
    ESensorstatus initiatorZAchseK();
}
