package ua.kpi.khilinskyi.bookkeeping;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;
import ua.kpi.khilinskyi.bookkeeping.db.DBHelper;

public class FragmentSettings extends Fragment implements View.OnClickListener {
    View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button button1 = (Button) view.findViewById(R.id.btnAddCard);
        Button button2 = (Button) view.findViewById(R.id.btnAddCat);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        Button buttoni = (Button) view.findViewById(R.id.importDB);
        Button buttone = (Button) view.findViewById(R.id.exportDB);
        buttoni.setOnClickListener(this);
        buttone.setOnClickListener(this);
        Button buttonsms = (Button) view.findViewById(R.id.btnSms);
        buttonsms.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCat:
                addCategory();
                break;
            case R.id.btnAddCard:
                addCard();
                break;
            case R.id.exportDB:
                exportDB();
                break;
            case R.id.importDB:
                importDB();
                break;
            case R.id.btnSms:
                Intent intent = new Intent(getContext(),smsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void importDB() {
       // getContext().deleteDatabase("Bookkeeping.db");
        DBController.controller.db.close();
        DBController.controller.createDB();
        File sdCardDir = Environment.getExternalStorageDirectory();
        File dir = new File(sdCardDir.getAbsolutePath()+"/B.CSV");
        for (File f:dir.listFiles()){
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(f));
            StringBuilder name1 = new StringBuilder();
                String temp = in.readLine();
                String[] ns = temp.split(",");
                if(ns.length<2) continue;
                for (int i = 1; i < ns.length; i++) {
                    name1.append(ns[i]+",");
                }
                name1.deleteCharAt(name1.length()-1);
                String reader;
                while ((reader = in.readLine()) != null){
                    String[] RowData = reader.split(",");
                    StringBuilder values = new StringBuilder();
                    for(int i = 1; i<RowData.length;i++){
                        values.append("'"+RowData[i]+"',");
                    }
                    values.deleteCharAt(values.length()-1);

                    String table_name = f.getName().substring(0,f.getName().length()-4);
                    String sql = "INSERT INTO "+table_name+" ("+name1.toString()+") VALUES ("+values.toString()+")";
                    DBController.controller.db.execSQL(sql);
                }
            } catch (Exception ex) {
                Log.e("import db",ex.getMessage());
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Toast.makeText(getContext(),"import db as .csv from sd card/B.CSV ok",Toast.LENGTH_LONG).show();
        getActivity().moveTaskToBack(true);
        getActivity().finish();
    }

    private void exportDB() {
        String[] tables = DBController.controller.getTables();
        for (int i = 0; i < tables.length; i++) {
            writeTable(tables[i]);
        }
        Toast.makeText(getContext(),"export db as .csv to sd card ok",Toast.LENGTH_LONG).show();
    }

    private void writeTable(String table) {
        Cursor c = null;
        SQLiteDatabase sqldb = DBController.controller.db;
        try {
            c = sqldb.rawQuery("select * from "+table, null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File dir = new File(sdCardDir.getAbsolutePath()+"/B.CSV");
            dir.mkdirs();
            String filename = table+".csv";
            // the name of the file to export with
            File saveFile = new File(dir, filename);
            FileWriter fw = new FileWriter(saveFile,false);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        bw.write(c.getColumnName(i) + ",");

                    } else {

                        bw.write(c.getColumnName(i));

                    }
                }
                bw.write("\n");

                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.write("\n");
                }
                bw.flush();
            }
        } catch (Exception ex) {
            Log.e("export db",ex.getMessage());
        }
    }

    private void addCard() {
        Intent intent = new Intent(this.getContext(),AddCardActivity.class);
        startActivityForResult(intent,0);
    }

    private void addCategory() {
        Intent intent = new Intent(this.getContext(),AddCategoryActivity.class);
        startActivityForResult(intent,0);
    }
}
