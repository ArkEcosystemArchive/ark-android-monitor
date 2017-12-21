package com.vrlcrypt.arkmonitor.models;

import java.util.Collections;
import java.util.List;

public class Voters {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
