package janie.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Birthday extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        setTitle("");

        Intent Extra = getIntent();
        String sender = Extra.getStringExtra("Name");
        String message = Extra.getStringExtra("Message");

        TextView messageDisplay = findViewById(R.id.message);
        TextView nameDisplay = findViewById(R.id.sender);

        messageDisplay.setText(message);
        nameDisplay.setText("From " + sender);

        Log.i(".Birthday", "Card generated successfully");
    }
}
