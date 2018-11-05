package me.mupu.interfaces.bitpos;

public interface IInput {

    /**
     * ET Belegtmeldung Q_Einlegestation
     */
    int I_01 = 1;

    /**
     * ET Schieber Ausgangslage Einlagestation
     */
    int I_02 = 1<<1;

    /**
     * ET Schieber Bandposition Einlegestation
     */
    int I_03 = 1<<2;

    /**
     * ET Bohrmaschine Hubeinheit oben
     */
    int I_04 = 1<<3;

    /**
     * ET Bohrmaschine Hubeinheit unten
     */
    int I_05 = 1<<4;

    /**
     * ET Mehrspindelmaschine Hubeinheit oben
     */
    int I_06 = 1<<5;

    /**
     * ET Mehrspindelmaschine Hubeinheit unten
     */
    int I_07 = 1<<6;


    /**
     * ET Mehrspindelmaschine Revolverpositionsmelder
     */
    int I_08 = 1<<7;

    /**
     * ET Fräsmaschine Hubeinheit oben
     */
    int I_09 = 1<<8;

    /**
     * ET Fräsmaschine Hubeinheit unten
     */
    int I_10 = 1<<9;

    /**
     * ET Fräsmaschine Querschlitten Bandposition
     */
    int I_11 = 1<<10;

    /**
     * ET Fräsmaschine Querschlitten Ständerposition
     */
    int I_12 = 1<<11;

    /**
     * Fotowiderstand Bandstart vor Bohrmaschine
     */
    int I_13 = 1<<12;

    /**
     * Fotowiderstand Ausschleusbahn
     */
    int I_14 = 1<<13;

    // I_15 u. I_16 nicht belegt

    /**
     * ET Xa
     */
    int I_17 = 1<<16;

    /**
     * ET Xe
     */
    int I_18 = 1<<17;

    /**
     * ET Ya
     */
    int I_19 = 1<<18;

    /**
     * ET Ye
     */
    int I_20 = 1<<19;

    /**
     * ET Za
     */
    int I_21 = 1<<20;

    /**
     * ET Ze
     */
    int I_22 = 1<<21;

    // I_23 u. I_24 nicht belegt

    /**
     * Initiator Bohrmaschine Werkstückposition
     */
    int I_25 = 1<<24;

    /**
     * Initiator Mehrspindelmaschine Werkstückposition
     */
    int I_26 = 1<<25;

    /**
     * Initiator Fräsmaschine Werkstückposition
     */
    int I_27 = 1<<26;

    /**
     * Initiator X-Achse
     */
    int I_28 = 1<<27;

    /**
     * Initiator Y-Achse
     */
    int I_29 = 1<<28;

    /**
     * Initiator Z-Achse
     */
    int I_30 = 1<<29;

}
