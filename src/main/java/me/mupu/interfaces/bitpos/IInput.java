package me.mupu.interfaces.bitpos;

public interface IInput {

    /**
     * (I_01) ET Belegtmeldung Q_Einlegestation
     */
    int I_01 = 1;

    /**
     * (I_02) ET Schieber Ausgangslage Einlagestation (LINKS)
     */
    int I_02 = 1<<1;

    /**
     * (I_03) ET Schieber Bandposition Einlegestation (RECHTS)
     */
    int I_03 = 1<<2;

    /**
     * (I_04) ET Bohrmaschine Hubeinheit oben
     */
    int I_04 = 1<<3;

    /**
     * (I_05) ET Bohrmaschine Hubeinheit unten
     */
    int I_05 = 1<<4;

    /**
     * (I_06) ET Mehrspindelmaschine Hubeinheit oben
     */
    int I_06 = 1<<5;

    /**
     * (I_07) ET Mehrspindelmaschine Hubeinheit unten
     */
    int I_07 = 1<<6;


    /**
     * (I_08) ET Mehrspindelmaschine Revolverpositionsmelder
     */
    int I_08 = 1<<7;

    /**
     * (I_09) ET Fraesmaschine Hubeinheit oben
     */
    int I_09 = 1<<8;

    /**
     * (I_10) ET Fraesmaschine Hubeinheit unten
     */
    int I_10 = 1<<9;

    /**
     * (I_11) ET Fraesmaschine Querschlitten Bandposition (VORNE)
     */
    int I_11 = 1<<10;

    /**
     * (I_12) ET Fraesmaschine Querschlitten Staenderposition (HINTEN)
     */
    int I_12 = 1<<11;

    /**
     * (I_13) Fotowiderstand Bandstart vor Bohrmaschine
     */
    int I_13 = 1<<12;

    /**
     * (I_14) Fotowiderstand Ausschleusbahn
     */
    int I_14 = 1<<13;

    // I_15 u. I_16 nicht belegt

    /**
     * (I_17) ET Xa (RECHTS)
     */
    int I_17 = 1<<16;

    /**
     * (I_18) ET Xe (LINKS)
     */
    int I_18 = 1<<17;

    /**
     * (I_19) ET Ya (VORNE)
     */
    int I_19 = 1<<18;

    /**
     * (I_20) ET Ye (HINTEN)
     */
    int I_20 = 1<<19;

    /**
     * (I_21) ET Za (OBEN)
     */
    int I_21 = 1<<20;

    /**
     * (I_22) ET Ze (UNTEN)
     */
    int I_22 = 1<<21;

    // I_23 u. I_24 nicht belegt

    /**
     * (I_25) Initiator Bohrmaschine Werkstueckposition (ist Werkstueck in Position?)
     */
    int I_25 = 1<<24;

    /**
     * (I_26) Initiator Mehrspindelmaschine Werkstueckposition (ist Werkstueck in Position?)
     */
    int I_26 = 1<<25;

    /**
     * (I_27) Initiator Fraesmaschine Werkstueckposition (ist Werkstueck in Position?)
     */
    int I_27 = 1<<26;

    /**
     * (I_28) Initiator X-Achse
     */
    int I_28 = 1<<27;

    /**
     * I_29 Initiator Y-Achse
     */
    int I_29 = 1<<28;

    /**
     * I_30 Initiator Z-Achse
     */
    int I_30 = 1<<29;

}
