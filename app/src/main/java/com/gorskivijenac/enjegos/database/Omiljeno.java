package com.gorskivijenac.enjegos.database;

public class Omiljeno {

    StihItem stih;
    String komentar;

    public Omiljeno(StihItem stih, String komentar) {
        this.stih = stih;
        this.komentar = komentar;
    }

    public StihItem getStih() {
        return stih;
    }

    public void setStih(StihItem stih) {
        this.stih = stih;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    @Override
    public String toString() {
        return "Stih: " + stih + "- komentar:" + komentar ;
    }
}
