package com.galihpw.simak;

/**
 * Created by Nada on 1/2/2017.
 */

public class Komentar {

    private String namaMhsKom;
    private String isiKomentar;

    public String getNamaMhsKom() {
        return namaMhsKom;
    }

    public void setNamaMhsKom(String namaMhsKom) {
        this.namaMhsKom = namaMhsKom;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }

    public Komentar(String namaMhsKom, String isiKomentar){
        this.namaMhsKom = namaMhsKom;
        this.isiKomentar = isiKomentar;
    }

}
