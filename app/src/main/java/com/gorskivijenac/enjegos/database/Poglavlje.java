package com.gorskivijenac.enjegos.database;

public class Poglavlje {

    int id;
    String nazivPoglavlja;

    public Poglavlje(int id, String nazivPoglavlja) {
        this.id = id;
        this.nazivPoglavlja = nazivPoglavlja;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Poglavlje: " + nazivPoglavlja;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivPoglavlja() {
        return nazivPoglavlja;
    }

    public void setNazivPoglavlja(String nazivPoglavlja) {
        this.nazivPoglavlja = nazivPoglavlja;
    }
}
