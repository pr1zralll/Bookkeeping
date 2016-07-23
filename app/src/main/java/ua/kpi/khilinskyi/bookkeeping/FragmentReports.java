package ua.kpi.khilinskyi.bookkeeping;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;
import ua.kpi.khilinskyi.bookkeeping.db.entity.Category;

public class FragmentReports extends Fragment{

    private View view;
    PieChart pieChart;
    private Category[] data;
    static boolean type = false;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reports, container, false);
        ((Button)view.findViewById(R.id.btnHistory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),InfoActivity.class);
                intent.putExtra("type","всі");
                getContext().startActivity(intent);
            }
        });
        ((Button)view.findViewById(R.id.btnDohodu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),InfoActivity.class);
                intent.putExtra("type","доходи");
                getContext().startActivity(intent);
            }
        });
        ((Button)view.findViewById(R.id.btnVitratu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),InfoActivity.class);
                intent.putExtra("type","витрати");
                getContext().startActivity(intent);
            }
        });
        ((Button)view.findViewById(R.id.btnReports)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ZvitActivity.class);
                getContext().startActivity(intent);
            }
        });
        pieChart = (PieChart) view.findViewById(R.id.chartReport);
        pieChart.setCenterText("Доходи за поточний місяць");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setDescription("");
        setData();
        final FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.btnPercent);
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

        return view;
    }
    private void setData() {
        data = DBController.controller.getCategoryCosts("Поточний місяць","доходи");
        if(data == null)
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
    @Override
    public void onResume() {
        super.onResume();
        setData();
    }


}
