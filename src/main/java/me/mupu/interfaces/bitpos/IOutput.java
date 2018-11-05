package me.mupu.interfaces.bitpos;

public interface IOutput {

    /**
     * Motor-Schieber Einlegestation vor.
     */
    int Q_1 = 1;

    /**
     * Motor-Schieber Einlegestation rueck.
     */
    int Q_2 = 1<<1;

    /**
     * Motor-Bohrmaschine Hub auf.
     */
    int Q_3 = 1<<2;

    /**
     * Motor-Bohrmaschine Hub ab.
     */
    int Q_4 = 1<<3;

    /**
     * Motor-Bohrmaschine Werkzeug-Antrieb.
     */
    int Q_5 = 1<<4;

    /**
     * Motor-Mehrspindelmaschine Hub auf.
     */
    int Q_6 = 1<<5;

    /**
     * Motor-Mehrspindelmaschine Hub ab.
     */
    int Q_7 = 1<<6;

    /**
     * Motor-Mehrspindelmaschine Revolverdrehung.
     */
    int Q_8 = 1<<7;

    /**
     * Motor-Mehrspindelmaschine Werkzeug-Antrieb.
     */
    int Q_9 = 1<<8;

    /**
     * Motor-Fraesmaschine Hub auf.
     */
    int Q_10 = 1<<9;

    /**
     * Motor-Fraesmaschine Hub ab.
     */
    int Q_11 = 1<<10;

    /**
     * Motor-Fraesmaschine Querschlitten rueck.
     */
    int Q_12 = 1<<11;

    /**
     * Motor-Fraesmaschine Querschlitten vor.
     */
    int Q_13 = 1<<12;

    /**
     * Motor-Fraesmaschine Werkzeug-Antrieb.
     */
    int Q_14 = 1<<13;

    /**
     * Motor-Band Bohrmaschine.
     */
    int Q_15 = 1<<14;

    /**
     * Motor-Band Mehrspindelmaschine.
     */
    int Q_16 = 1<<15;

    /**
     * Motor-Band Fraesmaschine.
     */
    int Q_17 = 1<<16;

    /**
     * Motor X-Achse rechts.
     */
    int Q_18 = 1<<17;

    /**
     * Motor X-Achse links.
     */
    int Q_19 = 1<<18;

    /**
     * Motor Y-Achse vor.
     */
    int Q_20 = 1<<19;

    /**
     * Motor Y-Achse rueck.
     */
    int Q_21 = 1<<20;

    /**
     * Motor Z-Achse hoch.
     */
    int Q_22 = 1<<21;

    /**
     * Motor Z-Achse runter.
     */
    int Q_23 = 1<<22;

    /**
     * Elektromagnet 24V.
     */
    int Q_24 = 1<<23;

}
