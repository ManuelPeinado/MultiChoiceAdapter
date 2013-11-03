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
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;

/**
 */
public abstract class MultiChoiceSimpleCursorAdapter extends SimpleCursorAdapter implements ActionMode.Callback,
        MultiChoiceAdapter {

    private MultiChoiceAdapterHelper helper = new MultiChoiceAdapterHelper(this) {
        @Override
        protected long positionToSelectionHandle(int position) {
            return getItemId(position);
        }
    };

    public MultiChoiceSimpleCursorAdapter(Bundle savedInstanceState, Context context, int layout, Cursor cursor,
            String[] from, int[] to, int flags) {
        super(context, layout, cursor, from, to, flags);
        helper.restoreSelectionFromSavedInstanceState(savedInstanceState);
    }

    public void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
        helper.setAdapterView(adapterView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        helper.setOnItemClickListener(listener);
    }

    public void save(Bundle outState) {
        helper.save(outState);
    }

    public void setItemChecked(long itemId, boolean checked) {
        helper.setItemChecked(itemId, checked);
    }

    public Set<Long> getCheckedItems() {
        return helper.getCheckedItems();
    }

    public int getCheckedItemCount() {
        return helper.getCheckedItemCount();
    }

    public boolean isChecked(long itemId) {
        return helper.isChecked(itemId);
    }

    public void setItemChecked(int position, boolean checked) {
        helper.setItemChecked(position, checked);
    }

    public void setItemClickInActionModePolicy(ItemClickInActionModePolicy policy) {
        helper.setItemClickInActionModePolicy(policy);
    }

    public ItemClickInActionModePolicy getItemClickInActionModePolicy() {
        return helper.getItemClickInActionModePolicy();
    }

    protected void finishActionMode() {
        helper.finishActionMode();
    }

    protected Context getContext() {
        return helper.getContext();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        helper.onDestroyActionMode();
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View viewWithoutSelection = getViewImpl(position, convertView, parent);
        return helper.getView(position, viewWithoutSelection);
    }

    @Override
    public boolean isItemCheckable(int position) {
        return true;
    }

    /**
     * Override this method if you need to customize the model-to-view mapping performed by SimpleCursorAdapter (for
     * instance, to populate an image view based on a URL stored in a DB column)
     */
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
