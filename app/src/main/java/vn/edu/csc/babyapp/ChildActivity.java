package vn.edu.csc.babyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.joooonho.SelectableRoundedImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;

import controller.DBHelper;
import model.Child;

public class ChildActivity extends AppCompatActivity implements IPickResult {
    private TextInputEditText edtInputChildName;
    private Button btnAdd, btnDelete, btnCancelChildFragment;
    private ImageButton btnAddPhoto;
    private DBHelper dbHelper;
    private Child child;
    private SelectableRoundedImageView ivChildImage;
    private RelativeLayout layoutAddPhoto, relative2;
    private TextView tvChildTittle;

    private Bitmap selectedImage = null;
    private int childID;
    private String childName;
    private byte[] byteArray;
    private File photoFile;

    private static final int PICK_PHOTO_REQUEST_CODE = 1004;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1099;
    private static final int PICK_IMAGE_ID = 234;
    Intent chooseImageIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        addControls();
        initObjects();
        addEvents();

//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();

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
//                CheckPermission();
                PickImageDialog.build(new PickSetup()).show(ChildActivity.this);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CheckPermission();
                PickImageDialog.build(new PickSetup()).show(ChildActivity.this);
            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });
    }

//    private void CheckPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
//            onPickPhoto();
//
//        }
//
//        else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, 102);
//            onLaunchCamera();
//        }
//
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if ((requestCode == 101) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//            onPickPhoto();
//        }
//        else if ((requestCode == 102) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//            onLaunchCamera();
//        }
//    }

//    private void onPickPhoto() {
//        // Create intent to pick photo from gallery
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            // Perform intent
//            startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_ID && resultCode == RESULT_OK && data != null) {
//            Uri photoUri = data.getData();
//
//            try {
//                // Create bitmap instance with uri
//                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//                byteArray = stream.toByteArray();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // Show image bitmap on imageview
//            if (selectedImage != null) {
//                setPictureFromImageView();
//            }
//        }
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PICK_IMAGE_ID) {
//                if (data.getExtras() == null) {
//                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data, ivChildImage, btnAddPhoto);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byteArray = stream.toByteArray();
//
//                    ivChildImage.setImageBitmap(bitmap);
//                } else {
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byteArray = stream.toByteArray();
//
//                    ivChildImage.setImageBitmap(photo);
//                }
//            }
//        }
//
//
//
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (resultCode == RESULT_OK) {
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
////                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 200);
//
//                MediaStore.Images.Media.insertImage(getContentResolver(),
//                        takenImage,
//                        "demo_image",
//                        "demo_image"
//                );
//                setPictureFromImageView();
//                ivChildImage.setImageBitmap(takenImage);
//
//            } else {
//                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }


//    public Bitmap setPictureFromImageView() {
//        ivChildImage.setImageBitmap(selectedImage);
//        ivChildImage.setCornerRadiiDP(100f, 100f, 100f, 100f);
//        ivChildImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        ivChildImage.isOval();
//        btnAddPhoto.setVisibility(View.GONE);
//        return selectedImage;
//    }

    private void returnDataToUpdateRouteActivity() {
        String nameOfChild = edtInputChildName.getText().toString();

        if (byteArray != null) {
            child = new Child(nameOfChild, byteArray);
            Log.d("csacrv", child + " byteChild");

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

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
            Bitmap bitmap = r.getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();

            ivChildImage.setImageBitmap(bitmap);
            ivChildImage.setCornerRadiiDP(100f, 100f, 100f, 100f);
            ivChildImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivChildImage.isOval();
            btnAddPhoto.setVisibility(View.GONE);


            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


//    private void onLaunchCamera() {
//        //create intent to access camera
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        String photoFileName = "IMG_" + System.currentTimeMillis();
//        photoFile = getPhotoFileUri(photoFileName);
//
//        Uri fileProvider = FileProvider.getUriForFile(this,
//                "vn.edu.csc.babyapp.fileprovider",
//                photoFile);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
//    }
//
//    public File getPhotoFileUri(String fileName) {
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
//            Log.d("TAG", "failed to create directory");
//        }
//
//        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
//        return file;
//    }


}
