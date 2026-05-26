package org.example.model;

public class Istrazivac extends User {

    private int    istrazivacId;
    private String kontakt;

    public Istrazivac() {
        super();
    }

    public int    getIstrazivacId()           { return istrazivacId; }
    public void   setIstrazivacId(int id)     { this.istrazivacId = id; }
    public String getKontakt()                { return kontakt; }
    public void   setKontakt(String kontakt)  { this.kontakt = kontakt; }
}