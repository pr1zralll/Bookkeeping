package ua.kpi.khilinskyi.bookkeeping;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Card;

public class RefActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner1, spinner2;
    private Double how;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ref);

        Button button = (Button) findViewById(R.id.btnRefOk);
        button.setOnClickListener(this);

        spinner1 = (Spinner) findViewById(R.id.spinner2);
        spinner2 = (Spinner) findViewById(R.id.spinner);

        spinner1.setAdapter(getAdapter());
        spinner2.setAdapter(getAdapter());

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        from = spinner1.getAdapter().getItem(0).toString();
        to = spinner2.getAdapter().getItem(0).toString();
        editText = (EditText) findViewById(R.id.editText3);
    }
    EditText editText;
    String from,to;
    @Override
    public void onClick(View v) {
        how = Double.parseDouble(editText.getText().toString());

        DBController.controller.Move(from,to,how);

        finish();
    }
    private String[] getCardsName() {
        Card[] cards = DBController.controller.getBalance();
        if(cards==null) return new String[0];
        String[] strings = new String[cards.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = cards[i].toString();
        }
        return strings;
    }
    public ArrayAdapter<String> getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(getCardsName());
        return adapter;
    }
}
