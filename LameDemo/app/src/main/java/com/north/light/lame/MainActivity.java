package com.north.light.lame;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.north.light.liblame.LameUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(LameUtils.getInstance().getVersion() + "版本");
        final String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions,0);
        }
        try {
            String path = Environment.getExternalStorageDirectory().getPath();
            String targetPath = path + "/javaim/target2.mp3";
            Log.d("Main", "targetPath:" + targetPath);
            new File(targetPath).createNewFile();
            LameUtils.getInstance().trainToMp3(path + "/javaim/1623225078991.wav", targetPath);
        } catch (Exception e) {
            Log.d("Main", "error:" + e.getMessage());
        }
    }

}
