package vn.edu.csc.babyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.PassengerManagerAdapter;
import controller.DBHelper;
import controller.OnRecyclerViewClickListener;
import model.Child;

public class PassengerManagerActivity extends AppCompatActivity {
    ArrayList<Child> listChildren;
    FloatingActionButton fabAddChild;
    DBHelper dbHelper;
    ImageButton imbBack;
    RecyclerView rvPassengerManager;
    PassengerManagerAdapter passengerManagerAdapter;
    Child child;

    private final int REQ_CODE_PASSENGER = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_manager);
        initControls();
        initObjects();
        initEvents();


    }

    private void initControls() {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        rvPassengerManager = findViewById(R.id.rvPasssenger);
        imbBack = findViewById(R.id.imbBack);
        fabAddChild = findViewById(R.id.fabAddChild);
    }

    private void initObjects() {

    }

    private void initEvents() {
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                if (listChildren != null) {
                    bundle.putSerializable("listChild", listChildren);
                    intent.putExtra("bundleChild", bundle);
                    setResult(RESULT_OK, intent);
                }
                else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        PassengerManagerAdapter.setOnItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void OnItemRecyclerViewClickListener(int position, View view) {
                Intent intent = new Intent(PassengerManagerActivity.this, ChildActivity.class);

                Bundle bundle = new Bundle();

                Child child = listChildren.get(position);

                bundle.putSerializable("childData", child);
                bundle.putInt("CodeToChild", 0);
                bundle.putInt("ChildID", position);

                intent.putExtra("bundleChildID", bundle);
                startActivityForResult(intent, REQ_CODE_PASSENGER);
            }
        });

        fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerManagerActivity.this, ChildActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CodeToChild", 1);
                intent.putExtra("bundleChildID", bundle);
                startActivityForResult(intent, REQ_CODE_PASSENGER);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new DBHelper(this);
        listChildren = dbHelper.getChild();
        passengerManagerAdapter = new PassengerManagerAdapter(PassengerManagerActivity.this, R.layout.row_item_passenger, listChildren);
        rvPassengerManager.setAdapter(passengerManagerAdapter);
        rvPassengerManager.setLayoutManager(new LinearLayoutManager(PassengerManagerActivity.this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PASSENGER && resultCode == RESULT_OK) {
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
                                    passengerManagerAdapter.notifyDataSetChanged();
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
                                    passengerManagerAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
