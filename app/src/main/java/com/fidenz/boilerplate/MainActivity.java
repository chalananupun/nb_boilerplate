package com.fidenz.boilerplate;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;


import com.fidenz.android_boilerplate.utility.EncryptUtility;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("onCreate");
        try {
            String encryptedText = EncryptUtility.encrypt("Password123","your text here");
            String decryptedText = EncryptUtility.encrypt("Password123","encrypted text here");
            System.out.println(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}