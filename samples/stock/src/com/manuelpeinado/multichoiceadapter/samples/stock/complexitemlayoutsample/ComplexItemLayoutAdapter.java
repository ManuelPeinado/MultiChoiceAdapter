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
package com.manuelpeinado.multichoiceadapter.samples.stock.complexitemlayoutsample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelpeinado.multichoiceadapter.MultiChoiceBaseAdapter;
import com.manuelpeinado.multichoiceadapter.samples.stock.R;

public class ComplexItemLayoutAdapter extends MultiChoiceBaseAdapter {
    protected static final String TAG = ComplexItemLayoutAdapter.class.getSimpleName();
    private List<String> items;
    private Random random = new Random();
    
    private static class ViewHolder {
        TextView nameTextView;
        public ProgressBar progressBar;
    }

    public ComplexItemLayoutAdapter(Bundle savedInstanceState, List<String> items) {
        super(savedInstanceState);
        this.items = items;
    }

    @Override
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            int layout = R.layout.complex_item;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup group = (ViewGroup) inflater.inflate(layout, parent, false);
            convertView = group;
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) group.findViewById(R.id.fName1);
            viewHolder.progressBar = (ProgressBar) group.findViewById(R.id.lp1);
            group.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String country = getItem(position);
        viewHolder.nameTextView.setText(country);
        viewHolder.progressBar.setProgress(random.nextInt(100));
        return convertView;
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
    public String getItem(int position) {
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
