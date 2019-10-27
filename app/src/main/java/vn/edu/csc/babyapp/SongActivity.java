package vn.edu.csc.babyapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import adapter.SongAdapter;
import controller.SimpleDividerItemDecoration;
import model.SongInfo;

public class SongActivity extends AppCompatActivity {
    RecyclerView rvSong;
    SongAdapter songAdapter;
    ArrayList<SongInfo> listSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        rvSong = findViewById(R.id.rvSong);
        listSong = new ArrayList<>();


//        listSong.add(new SongInfo("Class of dream", 0, R.raw.classofdream));
//        listSong.add(new SongInfo("Soldier Alarm", 0, R.raw.soldier_alarm));
//
        songAdapter = new SongAdapter(SongActivity.this, R.layout.row_item_song, listSong);
        rvSong.setAdapter(songAdapter);
        rvSong.addItemDecoration(new SimpleDividerItemDecoration(SongActivity.this));
        rvSong.setLayoutManager(new LinearLayoutManager(SongActivity.this, RecyclerView.VERTICAL, false));

        AssetManager assetManager = getAssets();

        try {
            String[] inputStream = assetManager.list("sound");

            for(String songInfo : inputStream){
                listSong.add(new SongInfo(songInfo));

            }
            songAdapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
