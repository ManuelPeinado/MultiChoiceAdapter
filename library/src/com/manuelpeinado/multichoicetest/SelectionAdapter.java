package com.manuelpeinado.multichoicetest;

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
 * A ListView adapter with support for multiple choice modal selection. The behavior
 * it provides is similar to that of CHOICE_MODE_MULTIPLE_MODAL ListView mode,
 * but it is compatible with API 8+
 * <br><br>
 * <b>Guidelines:</b> 
 * 	<li>Derive your activity from one of the ActionBarSherlock
 * activities, except SherlockListActivity 
 * 	<li>Call setListView as soon as you
 * can, normally in your activity onCreate 
 * 	<li>Do not call
 * setOnItemClickListener on your ListView, call it on this adapter 
 * 	<li>Do not implement BaseAdapter.getView, implement getViewImpl instead 
 * <br><br>
 */
public abstract class SelectionAdapter extends BaseAdapter 
									   implements OnItemLongClickListener, 
									   			  ActionMode.Callback, 
									   			  OnItemClickListener {
	private Set<Integer> selection = new HashSet<Integer>();
	private AdapterView<? super SelectionAdapter> adapterView;
	private ActionMode actionMode;
	private OnItemClickListener itemClickListener;

	public void setAdapterView(AdapterView<? super SelectionAdapter> adapterView) {
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
