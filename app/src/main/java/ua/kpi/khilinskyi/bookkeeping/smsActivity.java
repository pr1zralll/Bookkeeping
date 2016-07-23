package ua.kpi.khilinskyi.bookkeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class smsActivity extends Activity implements View.OnClickListener {
    ListView listView;
    private ArrayAdapter<String> adapter;
    EditText phone, format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        listView = (ListView) findViewById(R.id.listViewsms);
        listView.setAdapter(getAdapter());
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String name = parent.getAdapter().getItem(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(smsActivity.this);
                builder.setTitle("Підтвердіть дію")
                        .setMessage("Ви дійносно бажаєте видалити: "+parent.getAdapter().getItem(position).toString()+" ?")
                        .setCancelable(true)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(name);
                                listView.setAdapter(getAdapter());
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
        });
        Button button = (Button) findViewById(R.id.btnAddSms);
        button.setOnClickListener(this);
        phone = (EditText) findViewById(R.id.phone);
        format = (EditText) findViewById(R.id.format);

        Spinner spinner = (Spinner) findViewById(R.id.smsCards);
        spinner.setAdapter(getAdapter2());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_card = parent.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    String selected_card="none";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddSms:
                addSms();
        }
    }

    private void remove(String phone) {
        DBController.controller.removeInfo(phone);
    }

    private void addSms() {
        String p = phone.getText().toString();
        String f = format.getText().toString();
        if(p.equals(""))
        {
            Toast.makeText(this,"enter phone",Toast.LENGTH_SHORT).show();
            return;
        }
        if(f.equals(""))
        {
            Toast.makeText(this,"enter format",Toast.LENGTH_SHORT).show();
            return;
        }
        DBController.controller.addSmsWorker(p,f,selected_card);
        listView.setAdapter(getAdapter());
        Toast.makeText(this,"please restart app to procces new number",Toast.LENGTH_LONG).show();
    }

    public ArrayAdapter<String> getAdapter2() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getCardsName());
        return adapter;
    }
    public ArrayAdapter<String> getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.addAll(DBController.controller.getPhones());
        return adapter;
    }
}
