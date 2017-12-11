package com.janie.traveldiary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckIn extends AppCompatActivity {
    // PROPERTIES

    // Logcat tag
    private final String TAG = CheckIn.class.getSimpleName();

    // Firebase realtime database
    private FirebaseAuth auth;
    private DatabaseReference database;

    // Firebase storage
    private FirebaseStorage imgStorage;
    private StorageReference imgStorageRef;
    private Uri imgUri;

    // Layout elements
    private EditText tripTitleIn;
    private Button btnTakePhoto, btnCancel, btnConfirm;
    private ImageView imgPreview;
    private ProgressBar progressBar;

    // Current location info
    private Location currentLocation;

    // Photo capture properties
    private static final int CAMERA_CAPTURE_IMAGE = 100;
    private static final String IMAGE_DIRECTORY = "TravelDiary";
    private Uri fileUri;
    private String fileName;
    private File storageDir = new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY);

    // OVERRRIDDEN METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Log.i("Checkin", "Check in launched");

        // Get Firebase auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // Get Firebase storage
        imgStorage = FirebaseStorage.getInstance();
        imgStorageRef = imgStorage.getReference().child(auth.getUid());

        // Link to layout
        tripTitleIn = findViewById(R.id.tripTitle);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        imgPreview = findViewById(R.id.imgPreview);
        progressBar = findViewById(R.id.progressBar);

        // Get current location info from MainActivity
        currentLocation = (Location) getIntent().getExtras().get("Location");

        // Take photo
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        // Cancel check in
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckIn.this, MainActivity.class));
                finish();
            }
        });

        // Confirm check-in
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Upload image to storage
                if (fileUri != null) {
                    imgStorageRef.child(fileName).child(fileName).putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Done uploading image");

                                // Write location info to realtime database
                                writeToDatabase(currentLocation);

                            } else {
                                System.out.println("Fail to upload image");
                            }
                        }
                    });
                }
            }
        });
    }

    // Check result after closing camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Successfully captured image -> display in view
        if (resultCode == RESULT_OK) {
            previewImage();
        }
        // User cancelled capture
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Image Capture cancelled", Toast.LENGTH_SHORT).show();
        }
        // Image capture failed
        else {
            Toast.makeText(getApplicationContext(), "Image Capture failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Store url because it will be null after returning from camera to app

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        // Save uri in bundle
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved uri
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    // HELPER METHODS

    // Write info of location to database
    private void writeToDatabase(Location location) {
        String dateStamp = new SimpleDateFormat().format(new Date());
        final DatabaseReference push = database.child(auth.getUid()).push();
        if (auth.getUid() != null) {
            // Push location info
            push.setValue(location);

            // Push trip title
            database.child(auth.getUid()).child(push.getKey())
                    .child("title").setValue(tripTitleIn.getText().toString());

            // Push date
            database.child(auth.getUid()).child(push.getKey())
                    .child("date").setValue(dateStamp);

            // Push url to image on cloud storage
            imgStorageRef.child(fileName).child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imgUri = uri;
                    System.out.println("Get photo uri: " + imgUri);
                    database.child(auth.getUid()).child(push.getKey())
                            .child("imgUrl").setValue(imgUri.toString());

                    startActivity(new Intent(CheckIn.this, MainActivity.class));
                    finish();
                }
            });
        }
    }

    // Open camera app of device
    private void captureImage() {
        // Create uri to store image
        fileUri = Uri.fromFile(getImage());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Start camera
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE);
    }

    // Get image
    private File getImage() {
        // Create storage directory if not exist
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY, "Fail to create " + IMAGE_DIRECTORY);
                return null;
            }
        }

        // Create file name
        String timeStamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        // Create file
        fileName = "TD_" + timeStamp + ".jpg";
        File image = new File(storageDir.getPath() + File.separator +
                fileName);

        return image;
    }

    // Preview image
    private void previewImage() {
        try {
            // Show image preview
            imgPreview.setVisibility(View.VISIBLE);

            // Bitmap factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // Downsizing image
            options.inSampleSize = 8;

            // Create bitmap and show in view
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // OTHERS


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Check-in started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Check-in paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Check-in resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Check-in stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Check-in restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Check-in destroyed");
    }
}
