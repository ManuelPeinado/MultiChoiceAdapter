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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
 * MultiChoiceAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as in the native GMail app. 
 * <p>It provides a functionality similar to that of the CHOICE_MODE_MULTIPLE_MODAL ListView mode, but in a manner that is  
 * compatible with every version of Android from 2.1. Of course, this requires that your project uses ActionBarSherlock
 * <hr><p>You'll have to implement the following methods:
 * <p><b>ActionMode methods:</b>
 * <li><b>onCreateActionMode.</b> Create the action mode that will be displayed when at least one item is selected
 * <li><b>onActionModeClicked.</b> Respond to any of your action mode's actions
 * <p><br><b>ListAdapter methods:</b><br><br>
 * <li><b>getCount.</b> Return the number of items to show
 * <li><b>getItem.</b> Return the item at a given position
 * <li><b>getItemId.</b> Return the id of the item at a given position
 * <li><b>getViewImpl.</b> Returns the view to show for a given position. <b>Important:</b> do not override ListAdapter's getView method, override this method instead
 * <hr><p>Once you've implemented your class that derives from SelectionAdapter, you'll have
 * to attach it to a ListView like this:<p><br><code>
 * multiChoiceAdapter.setListView(listView);
 * multiChoiceAdapter.setOnItemClickListener(myItemListClickListener);</code>
 * <p><br>Do not call setOnItemClickListener on your ListView, call it on the adapter instead</p> 
 * <p><br>Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity</p> 
 * <p><br>See the accompanying sample project for a full working application that implements this class</p>
 */
public abstract class MultiChoiceAdapter extends BaseAdapter 
                                         implements OnItemLongClickListener, 
                                         ActionMode.Callback, 
                                         OnItemClickListener {
    private Set<Integer> selection = new HashSet<Integer>();
    private AdapterView<? super MultiChoiceAdapter> adapterView;
    private ActionMode actionMode;
    private OnItemClickListener itemClickListener;
    private Drawable selectedItemBackground;

    /**
     * Sets the adapter view on which this adapter will operate. You should call
     * this method from the onCreate method of your activity. This method calls
     * setAdapter on the adapter view, so you don't have to do it yourself
     * 
     * @param The adapter view (typically a ListView) this adapter will operate on
     */
    public void setAdapterView(AdapterView<? super MultiChoiceAdapter> adapterView) {
        this.adapterView = adapterView;
        checkActivity();
        adapterView.setOnItemLongClickListener(this);
        adapterView.setOnItemClickListener(this);
        adapterView.setAdapter(this);
        extractBackgroundColor();
    }

    /**
     * Register a callback to be invoked when an item in the associated
     * AdapterView has been clicked
     * @param listener The callback that will be invoked
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Changes the selection of an item. If the item was already in the
     * specified state, nothing is done. May cause the activation of the action
     * mode if an item is selected an no items were previously selected
     * @param position The position of the item to select
     * @param selected The desired state (selected or not) for the item
     */
    public void select(int position, boolean selected) {
        if (selected) {
            select(position);
        } else {
            unselect(position);
        }
    }

    /**
     * Causes an item to be selected. If the item was already selected, nothing
     * is done May cause the activation of the action mode if no items were
     * previously selected
     * @param position The position of the item to select
     */
    public void select(int position) {
        boolean wasSelected = isSelected(position);
        if (wasSelected) {
            return;
        }
        if (actionMode == null) {
            startActionMode();
        }
        selection.add(position);
        notifyDataSetChanged();
        onItemSelectedStateChanged(actionMode, position, true);
    }

    /**
     * Causes an item to stop being selected. If the item was not selected,
     * nothing is done May cause the deactivation of the action mode if this was
     * the only selected item
     * 
     * @param position The position of the item to select
     */
    public void unselect(int position) {
        boolean wasSelected = isSelected(position);
        if (!wasSelected) {
            return;
        }
        selection.remove(Integer.valueOf(position));
        if (getSelectionCount() == 0) {
            finishActionMode();
            return;
        }
        notifyDataSetChanged();
        onItemSelectedStateChanged(actionMode, position, false);
    }

    /**
     * Returns the indices of the currently selectly items.
     * 
     * @return Indices of the currently selectly items. The empty set if no item
     *         is selected
     */
    public Set<Integer> getSelection() {
        // Return a copy to prevent concurrent modification problems
        return new HashSet<Integer>(selection);
    }

    /**
     * Returns the number of selected items
     * 
     * @return Number of selected items
     */
    public int getSelectionCount() {
        return selection.size();
    }

    /**
     * Returns true if the item at the specified position is selected
     * 
     * @param position The item position
     * @return Whether the item is selected
     */
    public boolean isSelected(int position) {
        return selection.contains(position);
    }

    /**
     * Get a View that displays the data at the specified position in the data
     * set. Subclasses should implement this method instead of the traditional
     * ListAdapter#getView
     * 
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract View getViewImpl(int position, View convertView, ViewGroup parent);

    /**
     * Subclasses can invoke this method in order to finish the action mode.
     * This has the side effect of unselecting all items
     */
    protected void finishActionMode() {
        actionMode.finish();
    }

    /**
     * Convenience method for subclasses that need an activity context
     */
    protected Context getContext() {
        return adapterView.getContext();
    }

    private void clearSelection() {
        selection.clear();
        notifyDataSetChanged();
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

    private void extractBackgroundColor() {
        Context ctx = getContext();
        int styleAttr = R.attr.multiChoiceAdapterStyle;
        int defStyle = R.style.MultiChoiceAdapter_DefaultSelectedItemBackground;
        TypedArray ta = ctx.obtainStyledAttributes(null, R.styleable.MultiChoiceAdapter, styleAttr, defStyle);
        selectedItemBackground = ta.getDrawable(0);
        ta.recycle();
    }

    //
    // OnItemLongClickListener implementation
    //

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        boolean wasChecked = isSelected(position);
        select(position, !wasChecked);
        return true;
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

    @SuppressWarnings("deprecation")
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View v = getViewImpl(position, convertView, parent);
        Resources res = adapterView.getResources();
        if (isSelected(position)) {
            v.setBackgroundDrawable(selectedItemBackground);
        } else {
            v.setBackgroundColor(res.getColor(android.R.color.transparent));
        }
        return v;
    }
}
