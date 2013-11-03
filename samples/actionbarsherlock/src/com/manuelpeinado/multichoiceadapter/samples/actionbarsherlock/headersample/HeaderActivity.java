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
package com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.headersample;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.HomeActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.R;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.arrayadaptersample.MyArrayAdapter;

public class HeaderActivity extends SherlockFragmentActivity
                                  implements OnItemClickListener {
    private MyArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getListView().addHeaderView(createHeaderView(), null, false);
        rebuildList(savedInstanceState);
    }

    private View createHeaderView() {
        return LayoutInflater.from(this).inflate(R.layout.view_header, null);
    }

    private ListView getListView() {
        return (ListView) findViewById(android.R.id.list);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Subtract 1 from position to account for header
        Toast.makeText(this, "Item click: " + adapter.getItem(position - 1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Intent parentActivityIntent = new Intent(this, HomeActivity.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        case R.id.menu_select_all:
            selectAll();
            return true;
        case R.id.menu_reset_list:
            rebuildList(null);
            return true;
        }
        return false;
    }

    private void selectAll() {
        for (int i = 0; i < adapter.getCount(); ++i) {
            adapter.setItemChecked(i, true);
        }
    }

    private void rebuildList(Bundle savedInstanceState) {
        String[] itemArray = getResources().getStringArray(R.array.names);
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(itemArray));
        adapter = new MyArrayAdapter(savedInstanceState, this, items);
        adapter.setOnItemClickListener(this);
        adapter.setAdapterView(getListView());
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        adapter.save(outState);
    }
}
