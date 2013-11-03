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
package com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.baseadaptersample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.extras.actionbarsherlock.MultiChoiceBaseAdapter;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.Building;
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.R;

public class MyBaseAdapter extends MultiChoiceBaseAdapter {
    protected static final String TAG = MyBaseAdapter.class.getSimpleName();
    private List<Building> items;

    public MyBaseAdapter(Bundle savedInstanceState, List<Building> items) {
        super(savedInstanceState);
        this.items = items;
    }

    @Override
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int layout = R.layout.baseadapter_list_item;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
        }
        ViewGroup group = (ViewGroup)convertView;
        Building building = getItem(position);
        ((TextView)group.findViewById(android.R.id.text1)).setText(building.name);
        ((TextView)group.findViewById(android.R.id.text2)).setText(building.height);
        return group;    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.my_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.menu_discard) {
            discardSelectedItems();
            return true;
        }
        return false;
    }

    private void discardSelectedItems() {
        // http://stackoverflow.com/a/4950905/244576
        List<Long> positions = new ArrayList<Long>(getCheckedItemCount());
        Collections.sort(positions, Collections.reverseOrder());
        for (long position : positions) {
            items.remove((int)position);
        }
        finishActionMode();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Building getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

}


