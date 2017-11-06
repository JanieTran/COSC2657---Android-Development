package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class TimeParty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_party);
        setTitle("Set time");

        Log.i(".TimeParty", "Entering party time customisation");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(".TimeParty", "Party time customisation started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(".TimeParty", "Party time customisation paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(".TimeParty", "Party time customisation resumed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(".TimeParty", "Party time customisation stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(".TimeParty", "Party time customisation restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(".TimeParty", "Party time customisation destroyed");
    }

    public void setTime(View view) {
        TimePicker timeInput = findViewById(R.id.timePicker);

        String hour = String.valueOf(timeInput.getCurrentHour());
        String min = String.valueOf(timeInput.getCurrentMinute());
        String time = hour + ":" + min;

        Intent intent = new Intent(this, DateParty.class);
        intent.putExtra("Time", time);
        startActivity(intent);
    }
}
