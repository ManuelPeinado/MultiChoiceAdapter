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

package com.manuelpeinado.multichoiceadapter.demo.gridsample;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.MultiChoiceArrayAdapter;
import com.manuelpeinado.multichoiceadapter.demo.R;

import java.util.ArrayList;
import java.util.Set;

public class MyGridAdapter  extends MultiChoiceArrayAdapter<String> {

    protected static final String TAG = MyGridAdapter.class.getSimpleName();

    public MyGridAdapter(Bundle savedInstanceState, Context context, ArrayList<String> items) {
        super(savedInstanceState, context, R.layout.grid_item, android.R.id.text1, items);
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
        Set<Long> selection = getSelection();
        String[] items = new String[selection.size()];
        int i = 0;
        for (long position : selection) {
            items[i++] = getItem((int)position);
        }
        for (String item : items) {
            remove(item);
        }
        finishActionMode();
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }
}