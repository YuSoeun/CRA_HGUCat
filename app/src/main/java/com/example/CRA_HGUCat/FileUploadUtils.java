package com.example.CRA_HGUCat;

import android.util.Log;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadUtils {

    public static void goSend(File file) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();
        Request request = new Request.Builder()
                .url("http://49.143.69.123/upload/users/100")
                // Server URL 은 본인 IP를 입력
                .post(requestBody).build();
        OkHttpClient client = new OkHttpClient();<?php

                $host = 'localhost';
        $username = 'webnautes'; # MySQL 계정 아이디
                $password = 'apple9!'; # MySQL 계정 패스워드
                $dbname = 'testdb';  # DATABASE 이름


        $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

        try {

            $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8",$username, $password);
        } catch(PDOException $e) {

            die("Failed to connect to the database: " . $e->getMessage());
        }


        $con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

        if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) {
            function undo_magic_quotes_gpc(&$array) {
                foreach($array as &$value) {
                    if(is_array($value)) {
                        undo_magic_quotes_gpc($value);
                    }
                    else {
                        $value = stripslashes($value);
                    }
                }
            }

            undo_magic_quotes_gpc($_POST);
            undo_magic_quotes_gpc($_GET);
            undo_magic_quotes_gpc($_COOKIE);
        }

        header('Content-Type: text/html; charset=utf-8');
    #session_start();
?>
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TEST : ", response.body().string());
            }
        });
    }
}

//출처: https://derveljunit.tistory.com/302 [Derveljun's Programming Log]