package com.gorskivijenac.enjegos.database;

import java.io.Serializable;

public class StihItem implements Serializable {
    private String brojStiha;
    private String textStiha;

    public StihItem(String brojStiha, String textStiha) {
        this.brojStiha = brojStiha;
        this.textStiha = textStiha;
    }

    @Override
    public String toString() {
        return brojStiha  + " - " +textStiha;
    }

    public String getBrojStiha() {
        return brojStiha;
    }

    public String getTextStiha() {
        return textStiha;
    }
}
