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
package com.manuelpeinado.multichoiceadapter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.manuelpeinado.multichoicelistadapter.R;

/**
 * MultiChoiceAdapter provides a ListView adapter with support for multiple choice modal selection as in the native GMail app. 
 * <p>
 * It provides a functionality similar to that of the CHOICE_MODE_MULTIPLE_MODAL ListView mode [1], but in a manner that is compatible 
 * with every version of Android from 2.1
 * <p>
 * You'll have to implement the following methods:
 * <br><br>
 * <b>ActionMode methods:</b>
 * <li><b>onCreateActionMode.</b> Create the action mode that will be displayed when at least one item is selected
 * <li><b>onActionModeClicked.</b> Respond to any of your action mode's actions
 * <br><br>
 * <b>ListAdapter methods:</b><br><br>
 * <li><b>getCount.</b> Return the number of items to show
 * <li><b>getItem.</b> Return the item at a given position
 * <li><b>getItemId.</b> Return the id of the item at a given position
 * <li><b>getViewImpl.</b> Returns the view to show for a given position. <b>Important:</b> do not override ListAdapter's getView method, override this method instead
 * <br><br><hr><br>
 * Once you've implemented your class that derives from SelectionAdapter, you'll have
 * to attach it to a ListView like this:
 * <br><br><code>
 * multiChoiceAdapter.setListView(listView);
 * multiChoiceAdapter.setOnItemClickListener(myItemListClickListener);
 * </code><br><br> 
 * Do not call setOnItemClickListener on your ListView, call it on the adapter instead 
 * <br><br>
 * Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity 
 * <br><br>See the accompanying sample project for a full working application that uses this library 
 */
public abstract class MultiChoiceAdapter extends BaseAdapter 
									     implements OnItemLongClickListener, 
									   	   		    ActionMode.Callback, 
									   			    OnItemClickListener {
	private Set<Integer> selection = new HashSet<Integer>();
	private AdapterView<? super MultiChoiceAdapter> adapterView;
	private ActionMode actionMode;
	private OnItemClickListener itemClickListener;

	public void setAdapterView(AdapterView<? super MultiChoiceAdapter> adapterView) {
		this.adapterView = adapterView;
		checkActivity();
		adapterView.setOnItemLongClickListener(this);
		adapterView.setOnItemClickListener(this);
		adapterView.setAdapter(this);
	}
	
	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public void select(int position, boolean selected) {
		if (selected) {
			select(position);
		} else {
			unselect(position);
		}
	}

	protected abstract View getViewImpl(int position, View convertView, ViewGroup parent);

	protected void finishActionMode() {
		actionMode.finish();
	}
	
	protected Context getContext() {
		return adapterView.getContext();
	}

	public void select(int position) {
		boolean wasSelected = isSelected(position);
		if (wasSelected) {
			return;
		}
		selection.add(position);
		notifyDataSetChanged();
		onItemSelectedStateChanged(actionMode, position, true);
	}

	public void unselect(int position) {
		boolean wasSelected = isSelected(position);
		if (!wasSelected) {
			return;
		}
		selection.remove(Integer.valueOf(position));
		notifyDataSetChanged();
		onItemSelectedStateChanged(actionMode, position, false);
	}

	public Set<Integer> getSelection() {
		return selection;
	}

	public void clearSelection() {
		selection.clear();
		notifyDataSetChanged();
	}

	public int getSelectionCount() {
		return selection.size();
	}

	public boolean isSelected(int position) {
		return selection.contains(position);
	}

	private void onItemSelectedStateChanged(ActionMode actionMode, int position, boolean selected) {
		int count = getSelectionCount();
		if (count == 0) {
			finishActionMode();
			return;
		}
		Resources res = adapterView.getResources();
		String title = res.getQuantityString(R.plurals.selected_items, count, count);
		actionMode.setTitle(title);
	}

	private void checkActivity() {
		Context context = adapterView.getContext();
		if (context instanceof ListActivity) {
			throw new RuntimeException("ListView cannot belong to an activity which subclasses ListActivity");
		}
		if (context instanceof SherlockActivity || context instanceof SherlockFragmentActivity || context instanceof SherlockPreferenceActivity) {
			return;
		}
		throw new RuntimeException("ListView must belong to an activity which subclasses SherlockActivity");
	}

	//
	// OnItemLongClickListener implementation
	//

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
		if (actionMode == null) {
			startActionMode();
		}
		boolean wasChecked = isSelected(position);
		select(position, !wasChecked);
		return true;
	}

	private void startActionMode() {
		try {
			Activity activity = (Activity) adapterView.getContext();
			Method method = activity.getClass().getMethod("startActionMode", ActionMode.Callback.class);
			actionMode = (ActionMode) method.invoke(activity, this);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	//
	// ActionMode.Callback implementation
	//

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		clearSelection();
		actionMode = null;
	}

	//
	// OnItemClickListener implementation
	//

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		if (actionMode != null) {
			actionMode.finish();
			return;
		}
		if (itemClickListener != null) {
			itemClickListener.onItemClick(adapterView, view, position, id);
		}
	}

	//
	// BaseAdapter implementation
	//

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = getViewImpl(position, convertView, parent);
		Resources res = adapterView.getResources();
		if (isSelected(position)) {
			v.setBackgroundColor(res.getColor(R.color.selected_list_item_bg));
		} else {
			v.setBackgroundColor(res.getColor(android.R.color.transparent));
		}
		return v;
	}
}
