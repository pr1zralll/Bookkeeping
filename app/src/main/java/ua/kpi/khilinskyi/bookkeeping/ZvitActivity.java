package ua.kpi.khilinskyi.bookkeeping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class ZvitActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zvit);
        listView = (ListView) findViewById(R.id.listViewZV);
        listView.setAdapter(getAdapter());
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(getAdapter());
    }

    public ListAdapter getAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        String[] s = DBController.controller.getSMS();
        if(s!=null)
            adapter.addAll(s);
        else
            adapter.add("none");
        return adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter().getItem(position).equals("none")){
            return;
        }
        Intent intent = new Intent(this,SelectSmsActivity.class);
        intent.putExtra("text",parent.getAdapter().getItem(position).toString());
        startActivityForResult(intent,0);
        listView.setAdapter(getAdapter());
    }
}
