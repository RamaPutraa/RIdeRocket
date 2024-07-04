package com.example.riderocket;

public class Db_connection {
    public static  String ip = "192.168.1.217";

    public static final String urlLogin = "http://"+ip+"/riderocket/api_login.php";
    public static final String urlRegister = "http://"+ip+"/riderocket/api_register.php";
    public static final String urlGetUser = "http://"+ip+"/riderocket/api_getUser.php?id_user=";
    public static final String urlGetAllUser = "http://"+ip+"/riderocket/api_allUser.php";
    public static final String urlGetSpecMotor = "http://"+ip+"/riderocket/api_getSpecMotor.php?id_motor=";
    public static final String urlGetMotor = "http://"+ip+"/riderocket/api_getMotor.php";
    public static final String urlUpdateUser = "http://"+ip+"/riderocket/api_update.php?id_user=";
    public static final String urlAdminUpdateUser = "http://"+ip+"/riderocket/api_adminSetUser.php?id_user=";
    public static final String urlDeleteUser = "http://"+ip+"/riderocket/api_delete.php?id_user=";
    public static final String urlTransaksi = "http://"+ip+"/riderocket/api_transaksi.php";
    public static final String urlTambahMotor = "http://"+ip+"/riderocket/api_tambahMotor.php";
    public static final String urlGetRiwayat = "http://"+ip+"/riderocket/api_getTransaksi.php?id_penyewa=";
    public static final String urlGetAllTransaksi = "http://"+ip+"/riderocket/api_getAllTransaksi.php";
}
