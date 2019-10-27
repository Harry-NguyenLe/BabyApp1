package vn.edu.csc.babyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import adapter.ChildAdapterRecycle;
import controller.CommunicateFragment;
import controller.DBHelper;
import controller.OnRecyclerViewClickListener;
import controller.TimerService;
import model.Child;
import model.Route;

public class UpdateAcitivity extends AppCompatActivity implements CommunicateFragment {
    private Button btnCancel, btnSave, btnSetIcon, btnAddRoute, btnAddChild, btnQuickTime, btnManualTime;
    private RelativeLayout content1, layoutUpdateRoute;
    private LinearLayout lineUpdate;
    private ImageView ivIconRoute;
    private ArrayList<Child> listChildren;
    private ChildAdapterRecycle childAdapter;
    private Child child;
    private RecyclerView rvChild;
    private FloatingActionButton btnStart;
    private Fragment fragment;
    private TextInputEditText edtRouteName;
    private TextView tvChildName, tvSetYourAlarmTime, tvRouteTittle;
    private androidx.cardview.widget.CardView cardView;

    private DBHelper dbHelper;

    private int timeInHourManual, timeInMinutesManual;

    private int timeInQuick;

    private int timeTotal;

    private int routeID;

    private byte[] iconID;

    private StringBuffer stringBuffer;

    public int REQ_CODE_CHILD = 1002;
    public int REQ_CODE_SET_ICON = 1003;
    public int REQ_CODE_COUNT_DOWN = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initControls();
        initEvents();

        getIntentFromMainActivity();

        btnQuickTime.setTextColor(getColor(R.color.colorButtonPress));
        btnManualTime.setTextColor(getColor(R.color.colorButtonUnpress));

        //open fragment quick time selection
        fragment = QuickTimeFragment.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content1, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Copy database from asset
        dbHelper = new DBHelper(UpdateAcitivity.this);
        listChildren = dbHelper.getChild();
        childAdapter = new ChildAdapterRecycle(listChildren, UpdateAcitivity.this, R.layout.row_item_child);
        rvChild.setAdapter(childAdapter);
        rvChild.setLayoutManager(new LinearLayoutManager(UpdateAcitivity.this, RecyclerView.VERTICAL, false));

        SharedPreferences sharedPreferences1 = getSharedPreferences("Pref_Transfer_Time", MODE_PRIVATE);
        int routeIDPref = sharedPreferences1.getInt("routeID", 0);

        if (TimerService.isRunning && routeID == routeIDPref) {
            cardView.setVisibility(View.GONE);
            tvSetYourAlarmTime.setVisibility(View.GONE);
            layoutUpdateRoute.setBackground(getDrawable(R.color.colorAccent));
        }
    }

    private void initControls() {
        btnAddChild = findViewById(R.id.btnAddChild);
        btnSetIcon = findViewById(R.id.btnSetIcon);
        content1 = findViewById(R.id.content1);
        rvChild = findViewById(R.id.rvChild);
        btnQuickTime = findViewById(R.id.btnQuickTime);
        btnManualTime = findViewById(R.id.btnManualTime);
        btnAddRoute = findViewById(R.id.btnAdd);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnStart = findViewById(R.id.btnStart);
        edtRouteName = findViewById(R.id.edtRouteName);
        tvChildName = findViewById(R.id.tvChildName);
        tvSetYourAlarmTime = findViewById(R.id.tvSetYourAlarmTime);
        tvRouteTittle = findViewById(R.id.tvRouteTittle);
        cardView = findViewById(R.id.cardView);
        layoutUpdateRoute = findViewById(R.id.layoutUpdateRoute);
        lineUpdate = findViewById(R.id.lineUpdate);
        ivIconRoute = findViewById(R.id.ivIconRoute);
    }

    private void initEvents() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check edittext fill
                if (edtRouteName.getText().toString().equalsIgnoreCase("")) {
                    edtRouteName.setError("Please add a name for this route");
                    edtRouteName.requestFocus();
                } else {
                    checkStatus();
                    if (tvRouteTittle.getText().toString().equalsIgnoreCase("Update Route")) {
                        intentToMainActivityUpdateRoute(stringBuffer);

                    } else if (tvRouteTittle.getText().toString().equalsIgnoreCase("Add Route")) {
                        intentToMainActivityInsertRoute(stringBuffer);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtRouteName.getText().toString().equalsIgnoreCase("")) {
                    edtRouteName.setError("Please add a name for this route");
                    edtRouteName.requestFocus();
                } else if (listChildren.isEmpty()) {
                    Toast.makeText(UpdateAcitivity.this, "Please add a child for this route", Toast.LENGTH_SHORT).show();
                } else if (!listChildren.isEmpty()) {
                    checkRouteActiveAndReceiveData();
                }
            }
        });

        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateAcitivity.this, ChildActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CodeToChild", 1);
                intent.putExtra("bundleChildID", bundle);
                startActivityForResult(intent, REQ_CODE_CHILD);
            }
        });

        childAdapter.setOnItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void OnItemRecyclerViewClickListener(int position, View view) {
                Intent intent = new Intent(UpdateAcitivity.this, ChildActivity.class);

                Bundle bundle = new Bundle();

                Child child = listChildren.get(position);

                bundle.putSerializable("childData", child);
                bundle.putInt("CodeToChild", 0);
                bundle.putInt("ChildID", position);

                intent.putExtra("bundleChildID", bundle);
                startActivityForResult(intent, REQ_CODE_CHILD);
            }
        });

        lineUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });

        layoutUpdateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });

        btnSetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToIconActivity();
            }
        });

        ivIconRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToIconActivity();
            }
        });

    }

    public void checkRouteActiveAndReceiveData() {

        for (Child child : listChildren) {

            if (child.getChecked() != 0) {
                if (timeInQuick > 0) {
                    checkStatus();
                    String childName = stringBuffer.toString();

                    Intent intentCountDownTime = new Intent(UpdateAcitivity.this, CountDownTimeActivity.class);

                    Bundle bundleCountDownTime = new Bundle();
                    bundleCountDownTime.putString("childName", childName);
                    bundleCountDownTime.putInt("dataQuick", timeInQuick);
                    bundleCountDownTime.putString("routeName", edtRouteName.getText().toString());
                    bundleCountDownTime.putInt("routeID", routeID);


                    intentCountDownTime.putExtra("bundleQuick", bundleCountDownTime);

                    startActivityForResult(intentCountDownTime, REQ_CODE_COUNT_DOWN);
                } else if (timeInHourManual >= 0 && timeInMinutesManual >= 0) {
                    checkStatus();
                    String childName = stringBuffer.toString();

                    Intent intentCountDownTime = new Intent(UpdateAcitivity.this, CountDownTimeActivity.class);

                    Bundle bundleCountDownTime = new Bundle();

                    timeTotal = (timeInHourManual * 60) + timeInMinutesManual;

                    bundleCountDownTime.putString("childName", childName);
                    bundleCountDownTime.putInt("timeTotal", timeTotal);
                    bundleCountDownTime.putString("routeName", edtRouteName.getText().toString());
                    bundleCountDownTime.putInt("routeID", routeID);

//                    bundleCountDownTime.putString("songInfo", "soldier_alarm.mp3");
                    intentCountDownTime.putExtra("bundleManual", bundleCountDownTime);

                    startActivityForResult(intentCountDownTime, REQ_CODE_COUNT_DOWN);
                }
            }
        }
    }

    public void openFragmentTime(View v) {

        switch (v.getId()) {
            case R.id.btnQuickTime:
                timeTotal = 0;

                btnQuickTime.setTextColor(getColor(R.color.colorButtonPress));
                btnManualTime.setTextColor(getColor(R.color.colorButtonUnpress));

                fragment = QuickTimeFragment.getInstance();
                break;

            case R.id.btnManualTime:
                timeInQuick = 0;

                btnManualTime.setTextColor(getColor(R.color.colorButtonPress));
                btnQuickTime.setTextColor(getColor(R.color.colorButtonUnpress));

                fragment = ManualTimeFragment.getInstance();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content1, fragment);
        fragmentTransaction.commit();

    }

    public void moveToIconActivity() {
        Intent intentSetIconActivity = new Intent(UpdateAcitivity.this, IconActivity.class);
        startActivityForResult(intentSetIconActivity, REQ_CODE_SET_ICON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CHILD && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();

                if (bundle != null) {
                    //if true, do insert new child
                    if (bundle.containsKey("bundleInsertChild")) {

                        bundle = data.getBundleExtra("bundleInsertChild");

                        child = (Child) bundle.getSerializable("childData");

                        if (bundle.getInt("codeUpdate") == 0) {

                            if (child != null) {
                                if (dbHelper.insertChild(child) > 0) {
                                    childAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    //If true, do update existed child at its position
                    else if (bundle.containsKey("bundleUpdateChild")) {

                        bundle = data.getBundleExtra("bundleUpdateChild");

                        child = (Child) bundle.getSerializable("childData");
                        String childNameReceive = child.getChildName();
                        byte[] childImageReceive = child.getChildImage();

                        int childIDReceive = bundle.getInt("childID");

                        for (int i = 0; i < listChildren.size(); i++) {
                            child = listChildren.get(i);
                            byte[] childImage = child.getChildImage();
                            String childName = child.getChildName();

                            //Check for existed child name, if true, jump out and request users to update again
                            if (childName.trim().equalsIgnoreCase(childNameReceive)) {
                                Toast.makeText(this, "Baby Jaserick: Child exsisted", Toast.LENGTH_LONG).show();
                                break;
                            }

                            if (i == childIDReceive) {
                                child.setChildName(childNameReceive);
                                child.setChildImage(childImageReceive);

                                if (dbHelper.updateChild(child)) {
                                    childAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (requestCode == REQ_CODE_SET_ICON && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                iconID = bundle.getByteArray("IconID");

                Bitmap bmp = BitmapFactory.decodeByteArray(iconID, 0, iconID.length);
                ivIconRoute.setImageBitmap(bmp);
                ivIconRoute.setVisibility(View.VISIBLE);
                btnSetIcon.setVisibility(View.GONE);

                ArrayList<Route> listRoute = dbHelper.getRoute();
                for (int i = 0; i < listRoute.size(); i++){
                    Route route = listRoute.get(i);

                    if (i == routeID){
                        route.setIcon(iconID);
                        dbHelper.updateRoute(route);
                    }
                }
            }
        }

        if (requestCode == REQ_CODE_COUNT_DOWN && resultCode == RESULT_OK) {
            //TODO: receive data back from count down activity
        }
    }

    @Override
    public int sendTimeQuickData(final ArrayList<Integer> listTimeInQuick) {
        timeInQuick = listTimeInQuick.get(0);
        return timeInQuick;
    }

    @Override
    public int sendHoursManualData(ArrayList<Integer> listHoursManual) {
        timeInHourManual = listHoursManual.get(0);
        return timeInHourManual;
    }

    @Override
    public int sendMinutesManualData(ArrayList<Integer> listMinutesManual) {
        timeInMinutesManual = listMinutesManual.get(0);
        return timeInMinutesManual;
    }

    public void checkStatus() {
        ArrayList<Child> listDataChild = new ArrayList<>();

        for (int i = 0; i < listChildren.size(); i++) {
            child = listChildren.get(i);

            dbHelper.updateChildChecked(child);

            if (child.getChecked() != 0) {
                listDataChild.add(child);
            }
        }
        stringBuffer = new StringBuffer();

        for (Child child : listDataChild) {
            stringBuffer.append(", ").append(child.getChildName());
        }
        stringBuffer.delete(0, 2);
    }

    public void intentToMainActivityUpdateRoute(StringBuffer sb) {
        String stringProcessed = sb.toString();

        Intent intentMain = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("listDataChild", stringProcessed);
        bundle.putInt("routeID", routeID);
        bundle.putByteArray("icon", iconID);

        bundle.putString("RouteName", edtRouteName.getText().toString().trim());
        intentMain.putExtra("DataRouteNoName", bundle);

        setResult(RESULT_OK, intentMain);
        finish();
    }

    public void intentToMainActivityInsertRoute(StringBuffer sb) {

        String stringProcessed = sb.toString();
        Intent intentMain = new Intent();

        Bundle bundle = new Bundle();
        bundle.putString("RouteName", edtRouteName.getText().toString().trim());
        bundle.putSerializable("listDataChild", stringProcessed);
        bundle.putByteArray("icon", iconID);

        intentMain.putExtra("DataRouteName", bundle);

        setResult(RESULT_OK, intentMain);
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void getIntentFromMainActivity() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getInt("AddingRoute") == 1) {
                    tvRouteTittle.setText("Add Route");
                    btnSave.setText("Add");
                }
            }

            if (bundle.getInt("UpdateRoute") == 0) {
                edtRouteName.setText(bundle.getString("route_name"));
                routeID = bundle.getInt("routeID");
                if (bundle.getByteArray("iconID") == null) {
                    ivIconRoute.setVisibility(View.GONE);
                } else {
                    iconID = bundle.getByteArray("iconID");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(iconID, 0, iconID.length);
                    ivIconRoute.setImageBitmap(bitmap);
                    btnSetIcon.setVisibility(View.GONE);
                    ivIconRoute.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
