package vn.edu.csc.babyapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Child;

public class SettingActivity extends AppCompatActivity {
    Switch swVibrate;
    Vibrator vibrator;
    ImageButton imbBack;
    SeekBar seekBarVolume;
    AudioManager audioManager;
    FloatingActionButton btnRecord;
    ImageView btnPassengerManager, btnAlarmSelection;
    RelativeLayout layoutPassengerManager, layoutAlarmSelection;

    final int REQ_CODE_PASSENGER_MANAGER = 1005;
    ArrayList<Child> listChildren;
    Child child;
    StringBuffer sb;

    String pathSave = " ";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSTION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();
        initObjects();
        initEvents();

//        if (checkPermissionFromDevice()) {
//
//            btnRecord.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    pathSave = Environment.getExternalStorageDirectory()
//                            .getAbsolutePath() + "/"
//                            + UUID.randomUUID().toString() + "_audio_record.3gp";
//
//                    setUpMediaRecord();
//                    btnRecord.setImageResource(R.drawable.ic_pause_white);
//                    try {
//                        mediaRecorder.prepare();
//                        mediaRecorder.start();
//                    }
//                    catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//
//                    Toast.makeText(SettingActivity.this, "Recording.....", Toast.LENGTH_LONG).show();
//                }
//
//            });
//        } else {
//            requestPermission();
//        }
    }

//    private void setUpMediaRecord() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setOutputFile(pathSave);
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.RECORD_AUDIO
//        }, REQUEST_PERMISSTION_CODE);
//    }
//
//    private boolean checkPermissionFromDevice() {
//        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
//
//        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_PERMISSTION_CODE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "Pemissition denied", Toast.LENGTH_SHORT).show();
//            }
//
//            break;
//        }
//    }

    private void initControls() {
        swVibrate = findViewById(R.id.swVibrate);
        imbBack = findViewById(R.id.imbBack);
        seekBarVolume = findViewById(R.id.seekbarVolume);
        btnRecord = findViewById(R.id.btnRecord);
        btnPassengerManager = findViewById(R.id.btnPassengerManager);
        layoutPassengerManager = findViewById(R.id.layoutPassengerManager);
        layoutAlarmSelection = findViewById(R.id.layoutAlarmSelection);
        btnAlarmSelection = findViewById(R.id.btnAlarmSelection);
    }

    private void initObjects() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void initEvents() {
        swVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (swVibrate.isChecked()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(500);
                    }
                }
            }
        });

        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sb != null) {
                    moveToMainActivity(sb);
                }
                else {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        seekBarVolume.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }
        });

        btnPassengerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToPassengerManagerActivity();
            }
        });

        layoutPassengerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToPassengerManagerActivity();
            }
        });

        layoutAlarmSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, SongActivity.class);
                startActivity(intent);
            }
        });


    }

    private void moveToPassengerManagerActivity() {
        Intent intent = new Intent(SettingActivity.this, PassengerManagerActivity.class);
        startActivityForResult(intent, REQ_CODE_PASSENGER_MANAGER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PASSENGER_MANAGER && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();

                if (bundle.containsKey("bundleChild")) {
                    bundle = data.getBundleExtra("bundleChild");

                    listChildren = (ArrayList<Child>) bundle.getSerializable("listChild");
//                    Log.d("ascwvnv", listChildren + " ListPassengerSetting");

                    //Use string buffer to connect strings together with the comma between them
                    sb = new StringBuffer();
                    for (Child child : listChildren) {
                        sb.append(", ").append(child.getChildName());
                    }
                    sb.delete(0, 2);
//                    Log.d("ascwvnv", sb + " ListPassengerSetting");
                }
            }
        }
    }

    private void moveToMainActivity(StringBuffer sb) {
        String stringProcess = sb.toString();
        Intent intent = new Intent();
        Bundle bundleSB = new Bundle();
        bundleSB.putSerializable("Child_name_process", stringProcess);
//        Log.d("ascwvnv", stringProcess + " StringPassengerSetting");
        intent.putExtra("bundle_child_deleted", bundleSB);
        setResult(RESULT_OK, intent);
        finish();
    }
}