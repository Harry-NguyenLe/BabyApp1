package vn.edu.csc.babyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import adapter.IconAdapter;
import model.Icon;

public class IconActivity extends AppCompatActivity {
    GridView gvIcon;
    ArrayList<Icon> listIcon;
    IconAdapter adapterIcon;
    ImageButton imbBack;
    Icon icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        initControls();
        initObjects();
        initEvents();

        displayIcon();
        gvIcon.setAdapter(adapterIcon);
    }

    private void initControls() {
        gvIcon = findViewById(R.id.gvIcon);
        imbBack = findViewById(R.id.imbBack);
    }

    private void initObjects() {
        listIcon = new ArrayList<>();
        adapterIcon = new IconAdapter(IconActivity.this, R.layout.column_item_icon, listIcon);
    }

    private void initEvents() {
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                icon = listIcon.get(i);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icon.getIcon());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();

                Intent intentToUpdate = new Intent(IconActivity.this, UpdateAcitivity.class);
                Bundle bundle = new Bundle();
                bundle.putByteArray("IconID", b);

                intentToUpdate.putExtras(bundle);
                setResult(RESULT_OK, intentToUpdate);
                finish();
            }
        });
    }

    private void displayIcon() {
        listIcon.add(new Icon(R.drawable.icon1));
        listIcon.add(new Icon(R.drawable.icon2));
        listIcon.add(new Icon(R.drawable.icon3));
        listIcon.add(new Icon(R.drawable.icon4));
        listIcon.add(new Icon(R.drawable.icon5));
        listIcon.add(new Icon(R.drawable.icon6));
        listIcon.add(new Icon(R.drawable.icon7));
        listIcon.add(new Icon(R.drawable.icon8));
        listIcon.add(new Icon(R.drawable.icon9));
        listIcon.add(new Icon(R.drawable.icon10));
        listIcon.add(new Icon(R.drawable.icon11));
        listIcon.add(new Icon(R.drawable.icon12));
        listIcon.add(new Icon(R.drawable.icon13));
        listIcon.add(new Icon(R.drawable.icon14));
        listIcon.add(new Icon(R.drawable.icon15));
        listIcon.add(new Icon(R.drawable.icon16));
        listIcon.add(new Icon(R.drawable.icon17));


    }
}
