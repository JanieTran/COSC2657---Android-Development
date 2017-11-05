package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setTitle("Set location");

        Log.i(".Location", "Entering Set location");
    }

    public void setLocation(View view) {
        EditText locInput = findViewById(R.id.location);
        String location = locInput.getText().toString();
        EditText nameInput = findViewById(R.id.partyName);
        String partyName = nameInput.getText().toString();

        Intent timeDate = getIntent();
        String time = timeDate.getStringExtra("Time");
        String date = timeDate.getStringExtra("Date");

        Intent intent = new Intent(this, Party.class);
        intent.putExtra("Name", partyName);
        intent.putExtra("Time", time);
        intent.putExtra("Date", date);
        intent.putExtra("Location", location);
        startActivity(intent);
    }
}
