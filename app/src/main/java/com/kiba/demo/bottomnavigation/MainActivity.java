package com.kiba.demo.bottomnavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.kiba.bottomnavigation.BottomNavigationItem;
import com.kiba.bottomnavigation.BottomNavigationView;
import com.kiba.bottomnavigation.OnNavigationItemSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnShowBadge;
    private Button btnSwitchIcon;
    private BottomNavigationView bottomNavigationView;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnShowBadge = (Button) findViewById(R.id.btn_show_badge);
        btnSwitchIcon = (Button) findViewById(R.id.btn_switch_icon);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        BottomNavigationItem item1 = new BottomNavigationItem("label1", R.mipmap.ic_launcher, 0);
        BottomNavigationItem item2 = new BottomNavigationItem("label2", R.mipmap.ic_launcher, 0);
        BottomNavigationItem item3 = new BottomNavigationItem("label3", R.mipmap.ic_launcher, 0);

        final List<BottomNavigationItem> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);

        bottomNavigationView.addItems(list);

        btnShowBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = 1;
                for (int i = 0; i < list.size(); i++) {
                    bottomNavigationView.setBadgeViewNumberByPosition(i, number);
                }
            }
        });

        btnSwitchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setIconEnable(flag);
                flag = !flag;
            }
        });

        bottomNavigationView.setNavigationItemSelectListener(new OnNavigationItemSelectListener() {
            @Override
            public void onSelected(BottomNavigationView bottomNavigationView, View itemView, int position) {
                bottomNavigationView.setBadgeViewNumberByPosition(position, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
