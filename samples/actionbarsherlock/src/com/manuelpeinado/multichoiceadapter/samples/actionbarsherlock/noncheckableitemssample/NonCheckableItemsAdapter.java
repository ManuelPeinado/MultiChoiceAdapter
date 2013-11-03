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
package com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.noncheckableitemssample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
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
import com.manuelpeinado.multichoiceadapter.samples.actionbarsherlock.R;

public class NonCheckableItemsAdapter extends MultiChoiceBaseAdapter {
    protected static final String TAG = NonCheckableItemsAdapter.class.getSimpleName();

    private static class Item {
        public static final int HEADER = 0;
        public static final int NORMAL = 1;
        boolean isHeader;
        String text;

        public Item(String text, boolean isInitial) {
            this.isHeader = isInitial;
            this.text = text;
        }
    }

    private List<Item> items;

    public NonCheckableItemsAdapter(Bundle savedInstanceState, List<String> items) {
        super(savedInstanceState);
        this.items = addInitials(items);
    }

    private List<Item> addInitials(List<String> items) {
        List<Item> result = new ArrayList<Item>();
        char lastInitial = Character.MIN_VALUE;
        for (String name : items) {
            char initial = Character.toUpperCase(name.charAt(0));
            if (initial != lastInitial) {
                lastInitial = initial;
                result.add(new Item(Character.toString(initial), true));
            }
            result.add(new Item(name, false));
        }
        return result;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader ? Item.HEADER : Item.NORMAL;
    }
    
    @Override
    public boolean isItemCheckable(int position) {
        return !getItem(position).isHeader; 
    }

    @Override
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == Item.HEADER) {
            return getHeaderView(position, convertView, parent);
        } else {
            return getNormalView(position, convertView, parent);
        }
    }

    private View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = (TextView) convertView;
        textView.setBackgroundColor(Color.rgb(230, 230, 230));
        String initial = getItem(position).text;
        textView.setText(initial);
        return textView;
    }

    private View getNormalView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.baseadapter_list_item, parent, false);
        }
        ViewGroup group = (ViewGroup) convertView;
        String country = getItem(position).text;
        ((TextView) group.findViewById(android.R.id.text1)).setText(country);
        ((TextView) group.findViewById(android.R.id.text2)).setVisibility(View.GONE);
        return group;
    }

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
            items.remove((int) position);
        }
        finishActionMode();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
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
