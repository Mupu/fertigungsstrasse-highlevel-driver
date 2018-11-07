package me.mupu.interfaces.bitpos;

public interface IOutput {

    /**
     * (Q_1) Motor-Schieber Einlegestation vor. (RECHTS)
     */
    int Q_1 = 1;

    /**
     * (Q_2) Motor-Schieber Einlegestation rueck. (LINKS)
     */
    int Q_2 = 1<<1;

    /**
     * (Q_3) Motor-Bohrmaschine Hub auf.
     */
    int Q_3 = 1<<2;

    /**
     * (Q_4) Motor-Bohrmaschine Hub ab.
     */
    int Q_4 = 1<<3;

    /**
     * (Q_5) Motor-Bohrmaschine Werkzeug-Antrieb.
     */
    int Q_5 = 1<<4;

    /**
     * (Q_6) Motor-Mehrspindelmaschine Hub auf.
     */
    int Q_6 = 1<<5;

    /**
     * (Q_7) Motor-Mehrspindelmaschine Hub ab.
     */
    int Q_7 = 1<<6;

    /**
     * (Q_8) Motor-Mehrspindelmaschine Revolverdrehung.
     */
    int Q_8 = 1<<7;

    /**
     * (Q_9) Motor-Mehrspindelmaschine Werkzeug-Antrieb.
     */
    int Q_9 = 1<<8;

    /**
     * (Q_10) Motor-Fraesmaschine Hub auf.
     */
    int Q_10 = 1<<9;

    /**
     * (Q_11) Motor-Fraesmaschine Hub ab.
     */
    int Q_11 = 1<<10;

    /**
     * (Q_12) Motor-Fraesmaschine Querschlitten rueck.
     */
    int Q_12 = 1<<11;

    /**
     * (Q_13) Motor-Fraesmaschine Querschlitten vor.
     */
    int Q_13 = 1<<12;

    /**
     * (Q_14) Motor-Fraesmaschine Werkzeug-Antrieb.
     */
    int Q_14 = 1<<13;

    /**
     * (Q_15) Motor-Band Bohrmaschine.
     */
    int Q_15 = 1<<14;

    /**
     * (Q_16) Motor-Band Mehrspindelmaschine.
     */
    int Q_16 = 1<<15;

    /**
     * (Q_17) Motor-Band Fraesmaschine.
     */
    int Q_17 = 1<<16;

    /**
     * (Q_18) Motor X-Achse rechts.
     */
    int Q_18 = 1<<17;

    /**
     * (Q_19) Motor X-Achse links.
     */
    int Q_19 = 1<<18;

    /**
     * (Q_20) Motor Y-Achse vor.
     */
    int Q_20 = 1<<19;

    /**
     * (Q_21) Motor Y-Achse rueck.
     */
    int Q_21 = 1<<20;

    /**
     * (Q_22) Motor Z-Achse hoch.
     */
    int Q_22 = 1<<21;

    /**
     * (Q_23) Motor Z-Achse runter.
     */
    int Q_23 = 1<<22;

    /**
     * (Q_24) Elektromagnet 24V.
     */
    int Q_24 = 1<<23;

}
