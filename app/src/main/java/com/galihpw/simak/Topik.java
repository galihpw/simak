package com.galihpw.simak;

/**
 * Created by GalihPW on 07/11/2016.
 */

public class Topik {
    private String judul;
    private String deskripsi;
    private String penanya;


    public Topik(){

    }

    public Topik(String judul, String deskripsi, String penanya){
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.penanya = penanya;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPenanya() {
        return penanya;
    }

    public void setPenanya(String penanya) {
        this.penanya = penanya;
    }
}
