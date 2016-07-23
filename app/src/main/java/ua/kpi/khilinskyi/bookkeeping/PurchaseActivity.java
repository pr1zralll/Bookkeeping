package ua.kpi.khilinskyi.bookkeeping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinner = null,spinnerCard;
    Button button = null;
    EditText price=null,info=null;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        spinner = (Spinner) findViewById(R.id.PurchaseSpinner);
        spinnerCard = (Spinner) findViewById(R.id.SelectCard);
        spinnerCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCard = DBController.controller.getCardID(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = DBController.controller.getCategoryID(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button = (Button) findViewById(R.id.BtnPurchaseOk);
        button.setOnClickListener(this);
        price = (EditText) findViewById(R.id.purchaseTjext);
        info = (EditText) findViewById(R.id.editTextPurchase);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item,
                DBController.controller.getCategoriesName("витрати"));
        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item,
                DBController.controller.getCardsName());
        spinnerCard.setAdapter(adapter2);
    }
    private int selectedCategory=0,selectedCard =0;
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.BtnPurchaseOk&&price.getText().length()>0){
            DBController.controller.addPurchase(selectedCategory,selectedCard,info.getText().toString(),
                    Double.parseDouble( price.getText().toString()),
                    System.currentTimeMillis());
            finish();
        }else{
            Toast.makeText(this,"please enter price",Toast.LENGTH_SHORT).show();
        }
    }

}
