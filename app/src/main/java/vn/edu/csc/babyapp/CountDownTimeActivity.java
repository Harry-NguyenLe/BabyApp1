package vn.edu.csc.babyapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import controller.TimerService;

import static controller.ConverterTimer.convertTime;

public class CountDownTimeActivity extends AppCompatActivity {
    TextView tvRouteName, tvCountTime, tvChildName;
    ProgressBar progressBar;
    FloatingActionButton fab_start;
    Button btnCancel, btnEdit;
    RelativeLayout relativeParent;

    String routeTime;

    int routeID;

    long millisUntilFinished;

    private String childName, routeName;

    ArrayList<Integer> manageTimeData;

    long timeData;
    int timeReceiveAdapter;
    String musicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_time);
        addControls();
        addEvents();

        if (!TimerService.isRunning) {

            tvChildName.setText(childName);
            tvRouteName.append(routeName);

            tvCountTime.setText(routeTime);
        } else {
            showSnackbar(relativeParent, "Your alarm are currently activating, please stop to make another", 5000);
        }

    }

    private void addControls() {
        tvRouteName = findViewById(R.id.tvRouteName);
        tvChildName = findViewById(R.id.tvChildName);
        tvCountTime = findViewById(R.id.tvCountTime);
        progressBar = findViewById(R.id.progressBar);
        fab_start = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);
        btnEdit = findViewById(R.id.btnEdit);
        relativeParent = findViewById(R.id.relativeParent);
        manageTimeData = new ArrayList<>();
    }

    private void addEvents() {
        if (getDataFromUpdateActivity() >= 0) {

            int hours = (int) (timeData / 60);
            int minutes = (int) (timeData % 60);
            int seconds = (minutes / 60);

            final String timeDataFormatted = String.format(Locale.getDefault(), "%02d : %02d : %02d", hours, minutes, seconds);

            tvCountTime.setText(timeDataFormatted);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CountDownTimeActivity.this);

                builder.setMessage("Are you sure to cancel this route");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CountDownTimeActivity.this, "Your route is still active", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopService(new Intent(CountDownTimeActivity.this, TimerService.class));
                        Intent intent = new Intent(CountDownTimeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: move to this route's update activity
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public long getDataFromUpdateActivity() {
        Intent intentReceive = getIntent();

        Bundle bundleReceive = intentReceive.getExtras();

        if (bundleReceive != null) {

            if (bundleReceive.containsKey("bundleQuick")) {
                bundleReceive = intentReceive.getBundleExtra("bundleQuick");

                timeData = bundleReceive.getInt("dataQuick");
                childName = bundleReceive.getString("childName");
                routeName = bundleReceive.getString("routeName");
                routeID = bundleReceive.getInt("routeID");

                Log.d("ascefc", musicName + " name");

                if (!TimerService.isRunning) {
                    saveTimeData(timeData);
                    StartService();
                }

            } else if (bundleReceive.containsKey("bundleManual")) {
                bundleReceive = intentReceive.getBundleExtra("bundleManual");

                timeData = bundleReceive.getInt("timeTotal");
                childName = bundleReceive.getString("childName");
                routeName = bundleReceive.getString("routeName");
                routeID = bundleReceive.getInt("routeID");

//                musicName = bundleReceive.getString("songInfo");

                if (!TimerService.isRunning) {
                    saveTimeData(timeData);
                    StartService();
                }

            } else if (bundleReceive.containsKey("bundleAdapter")) {
                bundleReceive = intentReceive.getBundleExtra("bundleAdapter");

                childName = bundleReceive.getString("childName");
                routeName = bundleReceive.getString("routeName");
                routeID = bundleReceive.getInt("routeID");
                routeTime = bundleReceive.getString("routeTime");


                timeReceiveAdapter = bundleReceive.getInt("routeTimeInt");
            }
        }
        return timeData;
    }

    @SuppressLint("SetTextI18n")
    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            millisUntilFinished = intent.getLongExtra("countdown", 0);

            ArrayList<String> listTime = convertTime(millisUntilFinished);
            String hours = listTime.get(0);
            String minutes = listTime.get(1);
            String seconds = listTime.get(2);

            tvCountTime.setText(hours + " : " + minutes + " : " + seconds + " mins");

            long timeInTvCount = timeData * 60000;

            for (int i = 0; i <= millisUntilFinished; i++) {
                float fraction = i / (float) timeInTvCount;
                progressBar.setProgress((int) (fraction * 100));
            }

        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: check what is intent filter
        registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));

        fab_start.setImageResource(R.drawable.ic_pause);
        fab_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TimerService.isRunning) {
                    stopService(new Intent(CountDownTimeActivity.this, TimerService.class));
                    fab_start.setImageResource(R.drawable.ic_play);
                } else {
                    fab_start.setImageResource(R.drawable.ic_pause);
                }
                TimerService.isRunning = false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CountDownTimeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void saveTimeData(long timeData) {
        //save time data to Pref then open it in Service
        SharedPreferences sharedPreferences = getSharedPreferences("Pref_Transfer_Time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("time_transfer", timeData);
        editor.putInt("routeID", routeID);
        editor.apply();
    }

    public void StartService() {
        Intent intent = new Intent(CountDownTimeActivity.this, TimerService.class);
        startService(intent);
    }

    public void showSnackbar(View view, String message, int duration) {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }


}



