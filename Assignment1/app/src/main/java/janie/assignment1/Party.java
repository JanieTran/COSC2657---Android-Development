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

        Log.i(".Party", "Party invitation generated successfully.");

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
    }
}
