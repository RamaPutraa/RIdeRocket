package com.example.riderocket;

public class Motor {
    private String namaMotor;
    private String deskripsi;
    private int tahunPembuatan;
    private String transmisi;
    private int hargaSewa;
    private int id;

    public Motor(int id, String namaMotor, String deskripsi, int tahunPembuatan, String transmisi, int hargaSewa) {
        this.id = id;
        this.namaMotor = namaMotor;
        this.deskripsi = deskripsi;
        this.tahunPembuatan = tahunPembuatan;
        this.transmisi = transmisi;
        this.hargaSewa = hargaSewa;
    }

    public String getNamaMotor() {
        return namaMotor;
    }
    public int getId() {
        return id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getTahunPembuatan() {
        return tahunPembuatan;
    }

    public String getTransmisi() {
        return transmisi;
    }

    public int getHargaSewa() {
        return hargaSewa;
    }
}


