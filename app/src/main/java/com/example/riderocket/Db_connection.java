package com.example.riderocket;

public class Db_connection {
    public static  String ip = "169.254.36.73";

    public static final String urlLogin = "http://"+ip+"/riderocket/api_login.php";
    public static final String urlRegister = "http://"+ip+"/riderocket/api_register.php";
    public static final String urlGetUser = "http://"+ip+"/riderocket/api_getUser.php?id_user=";
    public static final String urlGetMotor = "http://"+ip+"/riderocket/api_getMotor.php";
    public static final String urlUpdateUser = "http://"+ip+"/riderocket/api_update.php?id_user=";
    public static final String urlDeleteUser = "http://"+ip+"/riderocket/api_delete.php?id_user=";
}
