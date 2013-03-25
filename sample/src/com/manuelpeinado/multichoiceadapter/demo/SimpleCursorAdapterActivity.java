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

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.MultiChoiceSimpleCursorAdapter;

public class SimpleCursorAdapterActivity extends SherlockActivity
                                         implements OnItemClickListener {
    private MultiChoiceSimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initList(savedInstanceState);
    }

    private ListView getListView() {
        return (ListView) findViewById(android.R.id.list);
    }

    public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int position, long id) {
        Uri uri = Uri.withAppendedPath(BuildingsContract.CONTENT_URI, Long.toString(id));
        Cursor cursor = getContentResolver().query(uri, null, "", null, "");
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(BuildingsContract.NAME);
        String name = cursor.getString(index);
        Toast.makeText(this, "Item click: " + name, Toast.LENGTH_SHORT).show();
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
            rebuildList();
            initList(null);
            return true;
        }
        return false;
    }

    private void selectAll() {
        for (int i = 0; i < adapter.getCount(); ++i) {
            adapter.select(adapter.getItemId(i));
        }
    }
    
    @SuppressWarnings("deprecation")
    private void initList(Bundle savedInstanceState) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (!prefs.getBoolean("dbInitialized", false)) {
            prefs.edit().putBoolean("dbInitialized", true).commit();
            rebuildList();
        }
        Cursor cursor = getContentResolver().query(BuildingsContract.CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);
        if (adapter == null) {
            adapter = new MySimpleCursorAdapter(savedInstanceState, this, cursor);
            adapter.setOnItemClickListener(this);
            adapter.setAdapterView(getListView());
        }
        else {
            adapter.changeCursor(cursor);
        }
    }
    
    private void rebuildList() {
        getContentResolver().delete(BuildingsContract.CONTENT_URI, null, null);
        ContentValues values = new ContentValues();
        String[] items = getResources().getStringArray(R.array.items);
        for (String item : items) {
            values.put(BuildingsContract.NAME, item);
            getContentResolver().insert(BuildingsContract.CONTENT_URI, values);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        adapter.save(outState);
    }
}