package com.kiba.bottomnavigation;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by KiBa-PC on 2016/6/22.
 */
public interface OnNavigationItemSelectListener {

    void onSelected(BottomNavigationView bottomNavigationView, View itemView, int position);

}
