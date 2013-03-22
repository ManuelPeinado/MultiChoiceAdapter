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
package com.manuelpeinado.multichoicetest;

import java.util.List;
import java.util.Set;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.multichoiceadapter.MultiChoiceAdapter;

public class MySelectionAdapter extends MultiChoiceAdapter {

	protected static final String TAG = MySelectionAdapter.class.getSimpleName();
	private final List<String> items;

	public MySelectionAdapter(List<String> items) {
		this.items = items;
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
		Set<Integer> positions = getSelection();
		for (Integer position : positions) {
			items.remove((int)position);
			unselect(position);
		}
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
	protected View getViewImpl(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView != null) {
			textView = (TextView) convertView; 
		}
		else {
			int layout = android.R.layout.simple_list_item_1;
			LayoutInflater inflater = LayoutInflater.from(getContext());
			textView = (TextView)inflater.inflate(layout, parent, false);
		}
		String item = getItem(position);
		textView.setText(item);
		return textView;
	}
}
