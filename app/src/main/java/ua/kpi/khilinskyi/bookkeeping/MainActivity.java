package ua.kpi.khilinskyi.bookkeeping;

import android.app.Application;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import ua.kpi.khilinskyi.bookkeeping.db.DBController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static DBController dbController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        dbController = new DBController(this);

    }

    public void AddIncome() {
        Intent intent = new Intent(this,IncomeActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAdd)
            AddIncome();
        if(v.getId()==R.id.btnRemove)
            AddPurchase();
        if(v.getId()==R.id.btnSet)
            ClearDB();
        if(v.getId()==R.id.btnRef)
            Ref();
    }

    private void Ref() {
        Intent intent = new Intent(this,RefActivity.class);
        startActivityForResult(intent,0);
    }

    private void AddPurchase() {
        Intent intent = new Intent(this,PurchaseActivity.class);
        startActivityForResult(intent,0);
    }

    private void ClearDB() {
        this.deleteDatabase("Bookkeeping.db");
        Toast.makeText(this,"drop db",Toast.LENGTH_SHORT).show();
        finish();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Fragment f1 = new FragmentMain();
        Fragment f2 = new FragmentReports();
        Fragment f3 = new FragmentSettings();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position){
                case 0:
                    return f1;
                case 1:
                    return f2;
                case 2:
                    return f3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Головна";
                case 1:
                    return "Звіти";
                case 2:
                    return "Налаштунки";
            }
            return null;
        }
    }
}
