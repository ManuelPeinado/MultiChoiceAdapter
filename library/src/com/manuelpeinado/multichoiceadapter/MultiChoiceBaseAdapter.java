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

import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;


/**
 * MultiChoiceBaseAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as
 * in the native GMail app.
 * <p>
 * It provides a functionality similar to that of the CHOICE_MODE_MULTIPLE_MODAL ListView mode, but in a manner that is
 * compatible with every version of Android from 2.1. Of course, this requires that your project uses ActionBarSherlock
 * <hr>
 * <p>
 * You'll have to implement the following methods:
 * <p>
 * <b>ActionMode methods:</b>
 * <li><b>onCreateActionMode.</b> Create the action mode that will be displayed when at least one item is selected
 * <li><b>onActionModeClicked.</b> Respond to any of your action mode's actions
 * <p>
 * <br>
 * <b>ListAdapter methods:</b><br>
 * <br>
 * <li><b>getCount.</b> Return the number of items to show
 * <li><b>getItem.</b> Return the item at a given position
 * <li><b>getItemId.</b> Return the id of the item at a given position
 * <li><b>getViewImpl.</b> Returns the view to show for a given position. <b>Important:</b> do not override
 * ListAdapter's getView method, override this method instead
 * <hr>
 * <p>
 * Once you've implemented your class that derives from SelectionAdapter, you'll have to attach it to a ListView like
 * this:
 * <p>
 * <br>
 * <code>
 * MultiChoiceBaseAdapter.setListView(listView);
 * MultiChoiceBaseAdapter.setOnItemClickListener(myItemListClickListener);</code>
 * <p>
 * <br>
 * Do not call setOnItemClickListener on your ListView, call it on the adapter instead
 * </p>
 * <p>
 * <br>
 * Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity
 * </p>
 * <p>
 * <br>
 * Do not forget to call save from your activity's onSaveInstanceState method
 * </p>
 * <p>
 * <br>
 * See the accompanying sample project for a full working application that implements this class
 * </p>
 */
public abstract class MultiChoiceBaseAdapter extends BaseAdapter implements ActionMode.Callback, MultiChoiceAdapter {

    private MultiChoiceAdapterHelper helper = new MultiChoiceAdapterHelper(this);

    /**
     * @param savedInstanceState
     *            Pass your activity's saved instance state here. This is necessary for the adapter to retain its
     *            selection in the event of a configuration change
     */
    public MultiChoiceBaseAdapter(Bundle savedInstanceState) {
        helper.restoreSelectionFromSavedInstanceState(savedInstanceState);
    }

    /**
     * Sets the adapter view on which this adapter will operate. You should call this method from the onCreate method of
     * your activity. This method calls setAdapter on the adapter view, so you don't have to do it yourself
     * 
     * @param The
     *            adapter view (typically a ListView) this adapter will operate on
     */
    public void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
        helper.setAdapterView(adapterView);
    }

    /**
     * Register a callback to be invoked when an item in the associated AdapterView has been clicked
     * 
     * @param listener
     *            The callback that will be invoked
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        helper.setOnItemClickListener(listener);
    }

    /**
     * Always call this method from your activity's onSaveInstanceState method. This is necessary for the adapter to
     * retain its selection in the event of a configuration change
     * 
     * @param outState
     *            The same bundle you are passed in onSaveInstanceState
     */
    public void save(Bundle outState) {
        helper.save(outState);
    }

    /**
     * Changes the selection of an item. If the item was already in the specified state, nothing is done. May cause the
     * activation of the action mode if an item is selected an no items were previously selected
     * 
     * @param position
     *            The position of the item to select
     * @param checked
     *            The desired state (selected or not) for the item
     */
    public void setItemChecked(long position, boolean checked) {
        helper.setItemChecked(position, checked);
    }

    /**
     * Returns the indices of the currently selectly items.
     * 
     * @return Indices of the currently selectly items. The empty set if no item is selected
     */
    public Set<Long> getCheckedItems() {
        return helper.getCheckedItems();
    }

    /**
     * Returns the number of selected items
     * 
     * @return Number of selected items
     */
    public int getCheckedItemCount() {
        return helper.getCheckedItemCount();
    }

    /**
     * Returns true if the item at the specified position is selected
     * 
     * @param position
     *            The item position
     * @return Whether the item is selected
     */
    public boolean isChecked(long position) {
        return helper.isChecked(position);
    }

    public void setItemClickInActionModePolicy(ItemClickInActionModePolicy policy) {
        helper.setItemClickInActionModePolicy(policy);
    }

    public ItemClickInActionModePolicy getItemClickInActionModePolicy() {
        return helper.getItemClickInActionModePolicy();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. Subclasses should implement this
     * method instead of the traditional ListAdapter#getView
     * 
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract View getViewImpl(int position, View convertView, ViewGroup parent);

    /**
     * Subclasses can invoke this method in order to finish the action mode. This has the side effect of unselecting all
     * items
     */
    protected void finishActionMode() {
        helper.finishActionMode();
    }

    /**
     * Convenience method for subclasses that need an activity context
     */
    protected Context getContext() {
        return helper.getContext();
    }

    //
    // ActionMode.Callback implementation
    //

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        helper.onDestroyActionMode();
    }

    //
    // MultiChoiceAdapter implementation
    //
    
    @Override
    public boolean isItemCheckable(int position) {
        return true;
    }

    //
    // BaseAdapter implementation
    //

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View viewWithoutSelection = getViewImpl(position, convertView, parent);
        return helper.getView(position, viewWithoutSelection);
    }
}