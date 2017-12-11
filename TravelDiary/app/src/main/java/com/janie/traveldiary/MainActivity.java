package com.janie.traveldiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;
import android.location.Location;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;

import static com.janie.traveldiary.LogIn.userProfilePic;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {
    // PROPERTIES

    // Logcat tag
    private final String TAG = MainActivity.class.getSimpleName();

    // User info
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;

    // Layout elements for user
    private TextView userName;
    private ImageView profilePic;
    private Button btnSignOut, btnCheckIn;

    // Map properties
    private GoogleMap map;
    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private CameraPosition cameraPosition;

    // Default location settings
    private static final int DEFAULT_ZOOM = 15;
    private LatLng defaultLocation = new LatLng(0,0);

    // Save activity location
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // OVERRIDDEN METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        setContentView(R.layout.activity_main);

        Log.i(TAG, "Main created");

        database = FirebaseDatabase.getInstance().getReference();

        // Link to layout
        userName = findViewById(R.id.username);
        profilePic = findViewById(R.id.profilePic);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnCheckIn = findViewById(R.id.btnCheckIn);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Display user info in layout
        try {
            userName.setText(user.getDisplayName());
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
        Picasso.with(this).load(userProfilePic.toString()).into(profilePic);

        // User sign out
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                LogIn.signOut();
                startActivity(new Intent(MainActivity.this, LogIn.class));
            }
        });

        // Build map
        MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check in
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCheckIn = new Intent(MainActivity.this, CheckIn.class);
                toCheckIn.putExtra("Location", currentLocation);
                startActivity(toCheckIn);
            }
        });
    }

    // Build map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocationPermission();

        // Navigate to current position
        updateLocationUI();
        getDeviceLocation();

        // Set custom marker infowindow
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                try {
                    // Get layout from xml file
                    View view = inflater.inflate(R.layout.marker_infowindow, null);

                    TextView tripTitle = view.findViewById(R.id.tripTitle);
                    TextView date = view.findViewById(R.id.date);
                    ImageView locImage = view.findViewById(R.id.locImage);

                    // Display corresponding info to view
                    tripTitle.setText(marker.getTitle());
                    date.setText(marker.getSnippet());

                    Picasso.with(view.getContext()).setLoggingEnabled(true);
                    Picasso.with(view.getContext())
                            .load(marker.getTag().toString())
                            .resize(130,130)
                            .into(locImage);

                    return view;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return null;
            }
        });

        // Add markers to map
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Child added");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    LatLng markLocation = new LatLng(
                            child.child("latitude").getValue(Double.class),
                            child.child("longitude").getValue(Double.class)
                    );


                    map.addMarker(new MarkerOptions()
                            .position(markLocation)
                            .title(child.child("title").getValue(String.class))
                            .snippet(child.child("date").getValue(String.class)))
                            .setTag(child.child("imgUrl").getValue(String.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, empty result arrays
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, currentLocation);
            super.onSaveInstanceState(outState);
        }
    }

    // HELPER METHODS

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private void updateLocationUI() {
        if (map == null)
            return;

        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("updateLocationUI: ", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task locationResult = fusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();

                            // Current location at center of map
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLocation.getLatitude(),
                                            currentLocation.getLongitude()),
                                    DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Null current location");
                            Log.e(TAG, "Exception: " + task.getException());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    // OTHERS

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        Log.i(TAG, "Main started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Main paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = auth.getCurrentUser();
        Log.i(TAG, "Main resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Main stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        user = auth.getCurrentUser();
        Log.i(TAG, "Main restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Main destroyed");
    }
}
