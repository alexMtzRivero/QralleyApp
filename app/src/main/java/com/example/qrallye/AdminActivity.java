package com.example.qrallye;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private final static String TAG = "AdminActivity";
    private ImageView iv_back;
    private ListView list;
    private ProgressBar progressBar;
    private ArrayList<Administrators> adminList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        iv_back = findViewById(R.id.back_arrow);
        list = findViewById(R.id.adminList);
        progressBar = findViewById(R.id.progressBar);
        adminList = new ArrayList<>();
        SessionMGR.getInstance().requestAdminList(this);
    }

    public void refreshList(ArrayList<Administrators> adminList){
        progressBar.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
        this.adminList = adminList;
        AdminAdapter adapter = new AdminAdapter(getApplicationContext(),adminList);
        list.setAdapter(adapter);
    }
}
