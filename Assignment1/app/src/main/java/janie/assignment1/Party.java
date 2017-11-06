package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Party extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        setTitle("");

        Intent info = getIntent();
        String time = info.getStringExtra("Time");
        String date = info.getStringExtra("Date");
        String location = info.getStringExtra("Location");
        String partyName = info.getStringExtra("Name");

        TextView timeDisplay = findViewById(R.id.partyTime);
        TextView dateDisplay = findViewById(R.id.partyDate);
        TextView locDisplay = findViewById(R.id.partyLoc);
        TextView nameDisplay = findViewById(R.id.nameDisplay);

        nameDisplay.setText(partyName);
        timeDisplay.setText(time);
        dateDisplay.setText(date);
        locDisplay.setText(location);

        Log.i(".Party", "Party invitation generated successfully.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(".Party", "Party invitation started");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(".Party", "Party invitation restarted");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(".Party", "Party invitation paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(".Party", "Party invitation resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(".Party", "Party invitation stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(".Party", "Party invitation destroyed");
    }
}
