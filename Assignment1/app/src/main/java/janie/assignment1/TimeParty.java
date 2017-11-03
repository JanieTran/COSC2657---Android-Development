package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class TimeParty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_party);
        setTitle("Set time");
    }

    public void setTime(View view) {
        TimePicker timeInput = findViewById(R.id.timePicker);
        String time = timeInput.toString();

        Intent intent = new Intent(this, DateParty.class);
        intent.putExtra("Time", time);
        startActivity(intent);
    }
}
