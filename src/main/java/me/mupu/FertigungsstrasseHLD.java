package me.mupu;

import me.mupu.interfaces.*;

public class FertigungsstrasseHLD implements IKran, IMBohrmaschine, IMFraesmaschine, IMMehrspindelmaschine, IMSchieber {

    private static FertigungsstrasseHLD instance;

    public FertigungsstrasseHLD getInstance() {
        if (instance == null)
            instance = new FertigungsstrasseHLD();

        return instance;
    }

    private FertigungsstrasseHLD() {

    }

    public IKran getKran() {
        return this;
    }

    public IMBohrmaschine getBohrmaschine() {
        return this;
    }

    public IMFraesmaschine getFraesmaschine() {
        return this;
    }

    public IMMehrspindelmaschine getMehrspindelmaschine() {
        return this;
    }

    public IMSchieber getSchieber() {
        return this;
    }

}
