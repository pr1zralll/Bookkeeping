package ua.kpi.khilinskyi.bookkeeping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class IncomeActivity extends AppCompatActivity implements View.OnClickListener{

    EditText count,info;
    private int selectedCard = 0;
    private String selectedCat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        count = (EditText) findViewById(R.id.editText);
        info = (EditText) findViewById(R.id.editText2);
        String[] cards = DBController.controller.getCardsName();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerIncome);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCard = DBController.controller.getCardID(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item,cards);
        spinner.setAdapter(adapter);
        Spinner spinner1 = (Spinner) findViewById(R.id.incomeSpinner);
        spinner1.setAdapter(getAdapterCat());
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCat = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayAdapter<String> getAdapterCat() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getCategoriesName("доходи"));
        return adapter;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.BtnIncomeOk&&count.getText().length()>0){
            DBController.controller.addIncome(selectedCard,selectedCat,info.getText().toString(),
                    Double.parseDouble(count.getText().toString()),
                    System.currentTimeMillis());
            finish();
        }else{
            Toast.makeText(this,"please enter count",Toast.LENGTH_SHORT).show();
        }
    }
}
