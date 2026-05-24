package org.example.model;

public class Istrazivac {
    //extenduje User klasu
    private int    istrazivacId;
    private String ime;
    private String prezime;
    private String kontakt;

    public int    getIstrazivacId()           { return istrazivacId; }
    public void   setIstrazivacId(int id)     { this.istrazivacId = id; }
    public String getIme()                    { return ime; }
    public void   setIme(String ime)          { this.ime = ime; }
    public String getPrezime()                { return prezime; }
    public void   setPrezime(String prezime)  { this.prezime = prezime; }
    public String getKontakt()                { return kontakt; }
    public void   setKontakt(String kontakt)  { this.kontakt = kontakt; }
}