package ua.kpi.khilinskyi.bookkeeping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;
import ua.kpi.khilinskyi.bookkeeping.db.tools.Parcer;

public class SelectSmsActivity extends AppCompatActivity implements View.OnClickListener {
    String sCar="всі",sCat="всі";
    EditText sumText;
    Spinner cat,car;
    private String mes;
    private EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sms);

        mes = getIntent().getStringExtra("text");
        final double sum = Parcer.getCount(mes);
        sumText = (EditText) findViewById(R.id.sms_sum);
        sumText.setText(String.valueOf(sum));
        cat = (Spinner) findViewById(R.id.spinner4);
        car = (Spinner) findViewById(R.id.spinner3);
        cat.setAdapter(getCatAdapter());
        car.setAdapter(getCarAdapter());
        car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sCar = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sCat = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button button = (Button) findViewById(R.id.btn_select_sms_ok);
        info = (EditText) findViewById(R.id.sms_info);
        button.setOnClickListener(this);
    }

    public SpinnerAdapter getCatAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getCategoriesName("всі"));
        return adapter;
    }

    public SpinnerAdapter getCarAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getCardsName());
        return adapter;
    }

    @Override
    public void onClick(View v) {
        DBController.controller.insert(sumText.getText().toString(),info.getText().toString(),sCar,sCat,System.currentTimeMillis(),mes);
        finish();
    }
}
