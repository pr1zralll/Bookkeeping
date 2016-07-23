package ua.kpi.khilinskyi.bookkeeping;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ua.kpi.khilinskyi.bookkeeping.db.entity.Card;
import ua.kpi.khilinskyi.bookkeeping.db.DBController;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Category;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Entity;

public class FragmentMain extends Fragment{
    View view = null;
    TextView balanceTW = null;
    ListView listView;
    PieChart pieChart = null;
    private Category[] data=null;
    static boolean type = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        balanceTW = (TextView) view.findViewById(R.id.twBalance);
        listView = (ListView) view.findViewById(R.id.cardsList);
        listView.setAdapter(getListAdapter());
        recalcBalance();
        pieChart = (PieChart) view.findViewById(R.id.chartMain);
        pieChart.setCenterText("Витрати за поточний місяць");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setDescription("");
        setData();

        final FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.btnMainPercent);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type){
                    type = false;
                    fab.setImageResource(R.drawable.ic_fab_valuta);
                }
                else {
                    type = true;
                    fab.setImageResource(R.drawable.ic_percent);
                }
                pieChart.setUsePercentValues(type);
                pieChart.invalidate();
            }
        });
        final FloatingActionButton fab2 = (FloatingActionButton)view.findViewById(R.id.btnRef);
        fab2.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

        return view;
    }

    private void setData() {
        data = DBController.controller.getCategoryCosts("Поточний місяць","витрати");
        if(data==null)
            return;
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < data.length; i++) {
            yVals.add(new Entry(data[i].count,i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < data.length; i++) {
            xVals.add(data[i].name);
        }
        PieDataSet dataSet = new PieDataSet(yVals, "");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private ListAdapter getListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,getCardsName());
        return adapter;
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

    private void recalcBalance() {
        double bal = 0;
        Card[] cards = DBController.controller.getBalance();
        if(cards==null) return;
        for (int i = 0;i < cards.length; i++)
            bal+=cards[i].balance;
        balanceTW.setText("Баланс: "+bal);
    }

    @Override
    public void onResume() {
        super.onResume();
        recalcBalance();
        listView.setAdapter(getListAdapter());
        setData();
    }
}
