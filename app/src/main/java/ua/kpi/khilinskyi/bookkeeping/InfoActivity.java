package ua.kpi.khilinskyi.bookkeeping;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class InfoActivity extends AppCompatActivity {

    Spinner spinner=null,spinnerCat=null;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        listView = (ListView) findViewById(R.id.infoListView);
        spinner = (Spinner) findViewById(R.id.infoSpin);
        spinnerCat = (Spinner) findViewById(R.id.infoCategory);

        spinner.setAdapter(getSpinerAdapter());
        spinnerCat.setAdapter(getCategoryAdapter());

        setupListners();

        listView.setAdapter(getListAdapter());

    }


    boolean tryDelete(final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Підтвердіть дію")
                .setMessage("Ви дійносно бажаєте видалити: "+name+" ?")
                .setCancelable(true)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBController.controller.removeItem(name,getIntent().getStringExtra("type"),selectedCategory);
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
    @NonNull
    private ArrayAdapter<String> getCategoryAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);
        adapter.add("всі");
        adapter.addAll(DBController.controller.getCategoriesName(getIntent().getStringExtra("type")));
        return adapter;
    }

    String selectedTime = "За рік";
    String selectedCategory="всі";
    private void setupListners() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tryDelete((String) parent.getAdapter().getItem(position));
                listView.setAdapter(getListAdapter());
                return true;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime=parent.getSelectedItem().toString();
                listView.setAdapter(getListAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory=parent.getSelectedItem().toString();
                listView.setAdapter(getListAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   String type = getIntent().getStringExtra("type");
        if(type=="dohodu")
            spinnerCat.setVisibility(View.GONE);
        else
            spinnerCat.setVisibility(View.VISIBLE);
        spinnerCat.invalidate();*/
    }

    private ArrayAdapter<String> getListAdapter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        String type = getIntent().getStringExtra("type");
        String[] data=null;
        switch(type){
            case "всі":
                data = DBController.controller.getHistory(selectedTime,selectedCategory);
                break;
            case "витрати":
                data = DBController.controller.getVitratu(selectedTime,selectedCategory);
                break;
            case "доходи":
                data = DBController.controller.getDohodu(selectedTime,selectedCategory);
                break;
        }
        if(data==null)
            data = new String[]{"порожньо"};
        adapter.addAll(data);
        return adapter;
    }

    private ArrayAdapter<String> getSpinerAdapter() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.add("За місяць");
        if(getIntent().getStringExtra("type").equals("витрати")) adapter.add("З моменту останнього доходу");
        adapter.add("За тиждень");
        adapter.add("За день");
        adapter.add("За рік");
        return adapter;
    }
}
