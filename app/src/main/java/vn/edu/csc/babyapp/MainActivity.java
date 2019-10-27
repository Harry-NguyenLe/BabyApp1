package vn.edu.csc.babyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.RouteAdapterRecycle;
import controller.ConverterTimer;
import controller.DBHelper;
import controller.OnRecyclerViewClickListener;
import controller.TimerService;
import model.Route;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvRoute;
    private Button btnSetting, btnStop;
    private FloatingActionButton btnAddRoute;
    private ArrayList<Route> listRoute;
    private RouteAdapterRecycle routeAdapterRecycle;

    private DBHelper dbHelper;

    private String routeName;

    private Route route;

    private int routeID;

    BroadcastReceiver broadcastReceiver;

    private static int UPDATE_ROUTE_REQ_CODE = 1001;
    private static int SETTING_REQ_CODE = 1008;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("activityLife", "onCreate");

        initControls();
        initObjects();
        initEvents();

    }

    private void initControls() {
        btnSetting = findViewById(R.id.btnSetting);
        rvRoute = findViewById(R.id.rvRoutes);
        btnAddRoute = findViewById(R.id.btnAddRoute);
        btnStop = findViewById(R.id.btnStopService);
    }

    private void initObjects() {
        listRoute = new ArrayList<>();
    }

    private void initEvents() {
        RouteAdapterRecycle.setOnItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void OnItemRecyclerViewClickListener(int position, View view) {
                Intent intentUpdateRoute = new Intent(MainActivity.this, UpdateAcitivity.class);

                Bundle bundle = new Bundle();

                route = listRoute.get(position);
                routeName = route.getRouteName();
                routeID = route.getRouteID();

                bundle.putString("route_name", routeName);
                bundle.putInt("routeID", position);
                bundle.putInt("UpdateRoute", 0);
                bundle.putByteArray("iconID", route.getIcon());
                intentUpdateRoute.putExtras(bundle);

                startActivityForResult(intentUpdateRoute, UPDATE_ROUTE_REQ_CODE);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, SETTING_REQ_CODE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimerService.isRunning) {
                    stopService(new Intent(MainActivity.this, TimerService.class));
//                    countClick = 0;
                }
            }
        });

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("AddingRoute", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, UPDATE_ROUTE_REQ_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Load data from database to display on recycler view
        dbHelper = new DBHelper(this);
        listRoute = dbHelper.getRoute();
        routeAdapterRecycle = new RouteAdapterRecycle(listRoute, this, R.layout.row_item_route);
        rvRoute.setAdapter(routeAdapterRecycle);
        rvRoute.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        //Register broadcast to listen from TimerService
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                RouteAdapterRecycle.mBroadcast.onReceive(context, intent);

                //-------------Save current time to local database, not displaying in the foreground-----------------//
                long timer = intent.getLongExtra("countdown", 0);
                sharedPreferences = getSharedPreferences("Pref_Transfer_Time", MODE_PRIVATE);
                routeID = sharedPreferences.getInt("routeID", 0);

                for (int i = 0; i < listRoute.size(); i++) {
                    route = listRoute.get(i);

                    if (i == routeID) {
                        ArrayList<String> listTime = ConverterTimer.convertTime(timer);
                        String hours = listTime.get(0);
                        String minutes = listTime.get(1);
                        String seconds = listTime.get(2);

                        int hrs = Integer.parseInt(hours);
                        int mins = Integer.parseInt(minutes);

                        int timeTotal = (hrs * 60) + mins;

                        route.setTimeInInteger(timeTotal);
                        route.setRouteTime(hours + " : " + minutes + " : " + seconds + " mins");
                        dbHelper.updateRoute(route);
                        break;
                    }
                }

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ROUTE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                //Receive route's data from route update and route adding activity
                Bundle bundleReceive = data.getExtras();

                if (bundleReceive != null) {

                    //If true, do update route at its position
                    if (bundleReceive.containsKey("DataRouteNoName")) {

                        bundleReceive = data.getBundleExtra("DataRouteNoName");

                        String routeName = bundleReceive.getString("RouteName");
                        String stringProcesed = (String) bundleReceive.getSerializable("listDataChild");
                        byte[] iconID = bundleReceive.getByteArray("icon");
                        int routeID = bundleReceive.getInt("routeID", 0);

                        for (int i = 0; i < listRoute.size(); i++) {
                            route = listRoute.get(i);
                            if (i == routeID) {
                                route.setRouteName(routeName);
                                route.setRouteChildName(stringProcesed);
                                route.setIcon(iconID);

                                if (dbHelper.updateRoute(route)) {
                                    routeAdapterRecycle.notifyDataSetChanged();
                                }
                            }

                            if (i != routeID) {
                                route.setRouteChildName(stringProcesed);
                                dbHelper.updateRouteChildName(route);
                            }
                        }
                    }

                    //If true, do insert new route at following position
                    else if (bundleReceive.containsKey("DataRouteName")) {
                        bundleReceive = data.getBundleExtra("DataRouteName");

                        String routeNameAdding = bundleReceive.getString("RouteName");
                        String stringProcesed = (String) bundleReceive.getSerializable("listDataChild");
                        bundleReceive.getByteArray("icon");
                        byte[] iconID = bundleReceive.getByteArray("icon");

                        for (Route route : listRoute) {
                            if (!route.getRouteName().equalsIgnoreCase(routeNameAdding)) {
                                route = new Route();
                                route.setRouteName(routeNameAdding);
                                route.setRouteChildName(stringProcesed);
                                route.setIcon(iconID);

                                if (dbHelper.insertRoute(route) > 0) {
                                    routeAdapterRecycle.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (requestCode == SETTING_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundleReceive = data.getExtras();

                if (bundleReceive != null) {
                    if (bundleReceive.containsKey("bundle_child_deleted")) {
                        bundleReceive = data.getBundleExtra("bundle_child_deleted");
                        String stringProcesed = (String) bundleReceive.getSerializable("Child_name_process");
                        Log.d("ascwvnv", stringProcesed + " ListPassengerMain");

                        for (int i = 0; i < listRoute.size(); i++) {
                            route = listRoute.get(i);
                            route.setRouteChildName(stringProcesed);
                            dbHelper.updateRouteChildName(route);
                        }
                    }
                }
            }
        }
    }

}


