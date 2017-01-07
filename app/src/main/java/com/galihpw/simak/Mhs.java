package com.galihpw.simak;



/**
 * Created by ACER on 17/09/2016.
 */
public class Mhs {
    private String nama;
    private String nim;
    private String kelas;
    private String noKontak;
    private String alamat;
    private String bintang;


    public Mhs(){

    }

    Mhs(String nama, String nim, String kelas, String noKontak, String alamat, String bintang){
        this.nama = nama;
        this.nim = nim;
        this.kelas = kelas;
        this.noKontak = noKontak;
        this.alamat = alamat;
        this.bintang = bintang;
    }

    String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    String getNoKontak() {
        return noKontak;
    }

    public void setNoKontak(String noKontak) {
        this.noKontak = noKontak;
    }

    String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public String getBintang(){
        return bintang;
    }

    public void setBintang(String bintang){
        this.bintang = bintang;
    }
}
