/*
 * Copyright (C) 2013 Manuel Peinado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.manuelpeinado.multichoiceadapter.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.manuelpeinado.multichoiceadapter.demo.arraysample.ArrayAdapterActivity;
import com.manuelpeinado.multichoiceadapter.demo.basicsample.BasicUsageActivity;
import com.manuelpeinado.multichoiceadapter.demo.checkboxsample.CheckboxItemsActivity;
import com.manuelpeinado.multichoiceadapter.demo.dbsample.SimpleCursorAdapterActivity;
import com.manuelpeinado.multichoiceadapter.demo.gridsample.GridActivity;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends SherlockListActivity {
    private List<ActivityInfo> activitiesInfo = Arrays.asList(
            new ActivityInfo(BasicUsageActivity.class, R.string.activity_title_basic_usage),
            new ActivityInfo(ArrayAdapterActivity.class, R.string.activity_title_array_adapter),
            new ActivityInfo(CheckboxItemsActivity.class, R.string.activity_title_checkbox_items),
            new ActivityInfo(SimpleCursorAdapterActivity.class, R.string.activity_title_simple_cursor_adapter),
            new ActivityInfo(GridActivity.class, R.string.activity_title_grid_adapter));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] titles = getActivityTitles();
        setListAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1, titles));
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class<? extends Activity> class_ = activitiesInfo.get(position).activityClass;
        Intent intent = new Intent(this, class_);
        startActivity(intent);
    }

    private String[] getActivityTitles() {
        String[] result = new String[activitiesInfo.size()];
        int i = 0;
        for (ActivityInfo info : activitiesInfo) {
            result[i++] = getString(info.titleResourceId);
        }
        return result;
    }
}
