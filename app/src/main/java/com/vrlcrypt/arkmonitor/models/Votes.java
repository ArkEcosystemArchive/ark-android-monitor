package com.vrlcrypt.arkmonitor.models;

import java.util.Collections;
import java.util.List;

public class Votes {
    private List<Delegate> delegates;

    public List<Delegate> getDelegates() {
        return Collections.unmodifiableList(delegates);
    }

    public void setDelegates(List<Delegate> delegates) {
        this.delegates = delegates;
    }
}
