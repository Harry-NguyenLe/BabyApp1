package vn.edu.csc.babyapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import controller.DBHelper;
import model.Child;

public class ChildActivity extends AppCompatActivity {
    TextInputEditText edtInputChildName;
    Button btnAdd, btnDelete, btnCancelChildFragment;
    ImageButton btnAddPhoto;
    DBHelper dbHelper;
    Child child;
    SelectableRoundedImageView ivChildImage;
    RelativeLayout layoutAddPhoto, relative2;
    TextView tvChildTittle;

    Bitmap selectedImage = null;
    int childID;
    String childName;
    byte[] byteArray;

    private static final int PICK_PHOTO_REQUEST_CODE = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        addControls();
        initObjects();
        addEvents();

        getChildInstanceFromUpdateAndPassenger();
    }

    private void initObjects() {
        dbHelper = new DBHelper(ChildActivity.this);
    }

    private void addControls() {
        edtInputChildName = findViewById(R.id.edtInputChildName);
        btnDelete = findViewById(R.id.btnDelete);
        btnAdd = findViewById(R.id.btnAdd);
        layoutAddPhoto = findViewById(R.id.layoutAddPhoto);
        tvChildTittle = findViewById(R.id.tvChildTitle);
        btnCancelChildFragment = findViewById(R.id.btnCancelChildFragment);
        ivChildImage = findViewById(R.id.ivChildImage);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        relative2 = findViewById(R.id.relative2);
    }

    private void addEvents() {

        btnCancelChildFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String childName = edtInputChildName.getText().toString();
                if (childName.equals("")) {
                    edtInputChildName.setError("Please fill in the blank");
                    edtInputChildName.requestFocus();
                } else {
                    if (tvChildTittle.getText().toString().equalsIgnoreCase("Add Child")) {
                        returnDataToUpdateRouteActivity();
                    } else if (tvChildTittle.getText().toString().equalsIgnoreCase("Update Child")) {
                        returnDataToUpdateActivityNoName(childID);
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildInstanceFromUpdateAndPassenger();
                childID = child.getId();
                dbHelper.deleteChild(childID);
                finish();
            }
        });

        layoutAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            onPickPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 101) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            CheckPermission();
        }
    }

    private void onPickPhoto() {
        // Create intent to pick photo from gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Perform intent
            startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri photoUri = data.getData();

            try {
                // Create bitmap instance with uri
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byteArray = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Show image bitmap on imageview
            if (selectedImage != null) {
                setPictureFromImageView();
            }
        }
    }

    public Bitmap setPictureFromImageView() {
        ivChildImage.setImageBitmap(selectedImage);
        ivChildImage.setCornerRadiiDP(100f, 100f, 100f, 100f);
        ivChildImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivChildImage.isOval();
        ivChildImage.setRotation(-90);
        btnAddPhoto.setVisibility(View.GONE);
        return selectedImage;
    }

    private void returnDataToUpdateRouteActivity() {
        String nameOfChild = edtInputChildName.getText().toString();

        if (byteArray != null) {
            child = new Child(nameOfChild, byteArray);
        } else {
            child = new Child(nameOfChild, null);
        }
        Intent intentToUpdate = new Intent();
        Bundle bundle = new Bundle();

        bundle.putSerializable("childData", child);
        bundle.putInt("codeUpdate", 0);

        intentToUpdate.putExtra("bundleInsertChild", bundle);

        setResult(RESULT_OK, intentToUpdate);

        finish();
    }

    private void returnDataToUpdateActivityNoName(int childID) {
        String nameOfChild = edtInputChildName.getText().toString();

        if (byteArray != null) {
            child = new Child(nameOfChild, byteArray);
        } else {
            child = new Child(nameOfChild, null);
        }
        Intent intentToUpdate = new Intent();
        Bundle bundle = new Bundle();

        bundle.putSerializable("childData", child);
        bundle.putInt("codeUpdate", 1);
        bundle.putInt("childID", childID);
        intentToUpdate.putExtra("bundleUpdateChild", bundle);

        setResult(RESULT_OK, intentToUpdate);

        finish();
    }

    @SuppressLint("SetTextI18n")
    private void getChildInstanceFromUpdateAndPassenger() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleChildID");

        if (bundle != null) {
            if (bundle.getInt("CodeToChild") == 0) {
                child = (Child) bundle.getSerializable("childData");
                childName = child.getChildName();
                btnAdd.setText("Save");
                childID = bundle.getInt("ChildID");
                tvChildTittle.setText("Update Child");
                edtInputChildName.setText(child.getChildName());
            }

            if (bundle.getInt("CodeToChild") == 1) {
                tvChildTittle.setText("Add Child");
                btnDelete.setVisibility(View.GONE);
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
