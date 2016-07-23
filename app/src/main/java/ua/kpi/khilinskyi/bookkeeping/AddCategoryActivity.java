package ua.kpi.khilinskyi.bookkeeping;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private String type = "витрати";
    ListView spinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Button button = (Button) findViewById(R.id.btnAddCatOk);
        button.setOnClickListener(this);
        Spinner spinner = (Spinner) findViewById(R.id.catType);
        spinner.setAdapter(getAdapter());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getSelectedItem().toString();
                if(spinner2!= null)spinner2.setAdapter(getAdapter2());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2 = (ListView) findViewById(R.id.catSpinner);
        spinner2.setAdapter(getAdapter2());
        spinner2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tryDelete(parent.getAdapter().getItem(position).toString());
                spinner2.setAdapter(getAdapter2());
                return true;
            }
        });
    }
    public ArrayAdapter<String> getAdapter2() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getCategoriesName(type));
        return adapter;
    }
    boolean tryDelete(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Підтвердіть дію")
                .setMessage("Ви дійносно бажаєте видалити: "+name+" ?")
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBController.controller.removeCat(name);
                        spinner2.setAdapter(getAdapter2());
                    }
                })
                .setNegativeButton("no",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAddCatOk&&((EditText)findViewById(R.id.editTextCatName)).getText().toString().length()>0){
            DBController.controller.AddCategory(((EditText)findViewById(R.id.editTextCatName)).getText().toString(),type);
            spinner2.setAdapter(getAdapter2());
            ((EditText) findViewById(R.id.editTextCatName)).setText("");
        }else {
            Toast.makeText(this,"please enter name",Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayAdapter<String> getAdapter() {
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.add("витрати");
        adapter.add("доходи");
        return adapter;
    }
}
