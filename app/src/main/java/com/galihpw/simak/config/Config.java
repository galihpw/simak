package com.galihpw.simak.config;

/**
 * Created by GalihPW on 07/10/2016.
 */

public class Config {
    //URL to our login.php file
    //public static final String URL = "http://192.168.8.105/~classroom/";
    public static final String URL = "http://www.simak.pe.hu/";

    //Keys for nip and password as defined in our $_POST['key'] in dosen.php
    public static final String KEY_NIP = "nip";
    public static final String KEY_PASSWORD = "password";

    //Keys for catch data in getJadwal.php
    public static final String KEY_HARI = "hari";
    public static final String KEY_KODE_MATKUL = "kode_matkul";
    public static final String KEY_NAMA_MATKUL = "nama_matkul";
    public static final String KEY_WAKTU_MULAI = "waktu_mulai";
    public static final String KEY_WAKTU_SELESAI = "waktu_selesai";
    public static final String KEY_PERTEMUAN = "pertemuan";

    //Keys for catch data in getTopik.php
    public static final String KEY_JUDUL = "judul";
    public static final String KEY_ISITOPIK = "isi_topik";

    //Keys for catch dosen data in dosen.php
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_NAMA_DOSEN = "nama_dosen";
    public static final String KEY_KONTAK_DOSEN = "kontak_dosen";
    public static final String KEY_ALAMAT_DOSEN = "alamat_dosen";
    public static final String KEY_EMAIL_DOSEN = "email_dosen";
    public static final String KEY_FB_DOSEN = "facebook_dosen";
    public static final String KEY_TW_DOSEN = "twitter_dosen";

    public static final String KEY_NIM = "nim";
    public static final String KEY_NAMA_MHS = "nama_mhs";
    public static final String KEY_KELAS = "kelas";
    public static final String KEY_BINTANG = "bintang";
    public static final String KEY_KONTAK_MHS = "kontak_mhs";
    public static final String KEY_ALAMAT_MHS = "alamat_mhs";
    public static final String KEY_EMAIL_MHS = "email_mhs";
    public static final String KEY_FB_MHS = "facebook_mhs";
    public static final String KEY_TW_MHS = "twitter_mhs";

    //Materi
    public static final String KEY_ISI_MATERI = "isi_materi";
    public static final String KEY_ISI_UPDATE_MATERI = "isi_update_materi";

    //Forum
    public static final String KEY_ID_TOPIK = "id_topik";

    //Komentar
    public static final String KEY_ISI_KOMENTAR = "isi_komentar";


    //Catch JSON result
    public static final String JSON_ARRAY = "result";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String NIP_SHARED_PREF = "nip";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
