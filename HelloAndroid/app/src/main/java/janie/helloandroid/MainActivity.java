package janie.helloandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {
    private Float currentResult = new Float(0);
    private Float operand = new Float(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void plus(View view) {
        EditText editText = (EditText) findViewById(R.id.number);
        operand = parseFloat(editText.getText().toString());
        currentResult += operand;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(currentResult.toString());
        Toast.makeText(this, "Result is " + currentResult.toString(), Toast.LENGTH_LONG).show();
    }

    public void minus(View view){
        EditText editText = (EditText) findViewById(R.id.number);
        operand = parseFloat(editText.getText().toString());
        currentResult -= operand;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(currentResult.toString());
        Toast.makeText(this, "Result is " + currentResult.toString(), Toast.LENGTH_LONG).show();
    }

    public void multiply(View view) {
        EditText editText = (EditText) findViewById(R.id.number);
        operand = parseFloat(editText.getText().toString());
        currentResult *= operand;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(currentResult.toString());
        Toast.makeText(this, "Result is " + currentResult.toString(), Toast.LENGTH_LONG).show();
    }

    public void divide(View view) {
        EditText editText = (EditText) findViewById(R.id.number);
        operand = parseFloat(editText.getText().toString());
        currentResult /= operand;
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(currentResult.toString());
        Toast.makeText(this, "Result is " + currentResult.toString(), Toast.LENGTH_LONG).show();
    }

    public void ac(View view) {
        currentResult = new Float(0);
        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(currentResult.toString());
        Toast.makeText(this, "Result reset", Toast.LENGTH_LONG).show();
    }

    // Executed when "Money Converter" button clicked, starting MoneyActivity.java
    public void moneyConverter(View view) {
        Intent intent = new Intent(this, MoneyActivity.class);
        startActivity(intent);
    }

    public void measurementConverter (View view){
        Intent intent = new Intent(this, MeasurementActivity.class);
        startActivity(intent);
    }
}
