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
package com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.simplecursoradaptersample;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.extras.actionbarsherlock.MultiChoiceSimpleCursorAdapter;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.HomeActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.R;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.manyitemssample.ManyItemsActivity;

public class SimpleCursorAdapterActivity extends SherlockFragmentActivity
                            implements OnItemClickListener,
                                       LoaderManager.LoaderCallbacks<Cursor>{
    private MultiChoiceSimpleCursorAdapter adapter;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private ListView getListView() {
        return (ListView) findViewById(android.R.id.list);
    }

    public void onItemClick(android.widget.AdapterView<?> adapterView, View view, int position, long id) {
        Cursor cursor = (Cursor) adapter.getItem(position);
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
            adapter.setItemChecked(adapter.getItemId(i), true   );
        }
    }
    
    private void initList(Cursor cursor) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (!prefs.getBoolean("dbInitialized", false)) {
            prefs.edit().putBoolean("dbInitialized", true).commit();
            rebuildList();
        }
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
        ArrayList<String> items = ManyItemsActivity.loadCountries(this);
        for (String item : items) {
            values.put(BuildingsContract.NAME, item);
            getContentResolver().insert(BuildingsContract.CONTENT_URI, values);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        adapter.save(outState);
    }

    //
    // LoaderCallbacks implementation
    //
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = BuildingsContract.NAME +  " ASC";
        return new CursorLoader(this, BuildingsContract.CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        initList(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}