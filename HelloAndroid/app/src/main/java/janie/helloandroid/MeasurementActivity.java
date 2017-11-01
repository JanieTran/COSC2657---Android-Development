package janie.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Float.parseFloat;

public class MeasurementActivity extends AppCompatActivity {
    private Float result = new Float(0);
    private Float operand = new Float(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
    }

    public void KgToLbs(View view) {
        EditText input = (EditText) findViewById(R.id.input);
        operand = parseFloat(input.getText().toString());
        result = operand * 2.20462f;
        TextView mass = (TextView) findViewById(R.id.result);
        mass.setText(result.toString());
    }

    public void FtToM(View view) {
        EditText input = (EditText) findViewById(R.id.input);
        operand = parseFloat(input.getText().toString());
        result = operand * 0.3048f;
        TextView height = (TextView) findViewById(R.id.result);
        height.setText(result.toString());
    }
}
