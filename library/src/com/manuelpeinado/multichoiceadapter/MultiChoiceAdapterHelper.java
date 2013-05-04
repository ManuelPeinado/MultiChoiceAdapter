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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.ActionMode;
import com.manuelpeinado.multichoicelistadapter.R;

class MultiChoiceAdapterHelper implements OnItemLongClickListener, OnItemClickListener, OnCheckedChangeListener {
    private static final String BUNDLE_KEY = "mca__selection";
    
    /**
     *  Changes the selection state of the clicked item, just as if it had been 
     *  long clicked. This is what the native MULTICHOICE_MODAL mode of List does,
     *  and what almost every app does
     */
    public static final int SELECT = 0;
    /**
     * Opens the clicked item, just as if it had been clicked outside of the action 
     * mode. This is what the Gmail app does
     */
    public static final int OPEN = 1;
    private Set<Long> checkedItems = new HashSet<Long>();
    private AdapterView<? super MultiChoiceBaseAdapter> adapterView;
    private BaseAdapter owner;
    private OnItemClickListener itemClickListener;
    private ActionMode actionMode;
    private Boolean itemIncludesCheckBox;
    /*
     * Defines what happens when an item is clicked and the action mode was already active
     */
    private int itemClickInActionModePolicy;

    
    MultiChoiceAdapterHelper(BaseAdapter owner) {
        this.owner = owner;
    }

    void restoreSelectionFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        long[] array = savedInstanceState.getLongArray(BUNDLE_KEY);
        checkedItems.clear();
        for (long id : array) {
            checkedItems.add(id);
        }
    }

    void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
        this.adapterView = adapterView;
        checkActivity();
        adapterView.setOnItemLongClickListener(this);
        adapterView.setOnItemClickListener(this);
        adapterView.setAdapter(owner);
        parseAttrs();

        if (!checkedItems.isEmpty()) {
            startActionMode();
            onItemSelectedStateChanged();
        }
    }
    
    void checkActivity() {
        Context context = adapterView.getContext();
        if (context instanceof ListActivity) {
            throw new RuntimeException("ListView cannot belong to an activity which subclasses ListActivity");
        }
        if (context instanceof SherlockActivity || context instanceof SherlockFragmentActivity || context instanceof SherlockPreferenceActivity) {
            return;
        }
        throw new RuntimeException("ListView must belong to an activity which subclasses SherlockActivity");
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    void save(Bundle outState) {
        long[] array = new long[checkedItems.size()];
        int i = 0;
        for (Long id : checkedItems) {
            array[i++] = id;
        }
        outState.putLongArray(BUNDLE_KEY, array);
    }

    void setItemChecked(long handle, boolean checked) {
        if (checked) {
            checkItem(handle);
        } else {
            uncheckItem(handle);
        }
    }

    void checkItem(long handle) {
        boolean wasSelected = isChecked(handle);
        if (wasSelected) {
            return;
        }
        if (actionMode == null) {
            startActionMode();
        }
        checkedItems.add((long)handle);
        owner.notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    void uncheckItem(long handle) {
        boolean wasSelected = isChecked(handle);
        if (!wasSelected) {
            return;
        }
        checkedItems.remove(handle);
        if (getCheckedItemCount() == 0) {
            finishActionMode();
            return;
        }
        owner.notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    Set<Long> getCheckedItems() {
        // Return a copy to prevent concurrent modification problems
        return new HashSet<Long>(checkedItems);
    }

    int getCheckedItemCount() {
        return checkedItems.size();
    }

    boolean isChecked(long handle) {
        return checkedItems.contains(handle);
    }

    void finishActionMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    Context getContext() {
        return adapterView.getContext();
    }
    
    private void onItemSelectedStateChanged() {
        int count = getCheckedItemCount();
        if (count == 0) {
            finishActionMode();
            return;
        }
        Resources res = adapterView.getResources();
        String title = res.getQuantityString(R.plurals.selected_items, count, count);
        actionMode.setTitle(title);
    }
    
    private void startActionMode() {
        try {
            Activity activity = (Activity) adapterView.getContext();
            Method method = activity.getClass().getMethod("startActionMode", ActionMode.Callback.class);
            actionMode = (ActionMode) method.invoke(activity, owner);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private void parseAttrs() {
        Context ctx = getContext();
        int styleAttr = R.attr.multiChoiceAdapterStyle;
        int defStyle = R.style.MultiChoiceAdapter_DefaultSelectedItemStyle;
        TypedArray ta = ctx.obtainStyledAttributes(null, R.styleable.MultiChoiceAdapter, styleAttr, defStyle);
        itemClickInActionModePolicy = ta.getInt(R.styleable.MultiChoiceAdapter_itemClickInActionMode, SELECT);
        ta.recycle();
    }

    //
    // OnItemLongClickListener implementation
    //

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        long handle = positionToSelectionHandle(position);
        boolean wasChecked = isChecked(handle);
        setItemChecked(handle, !wasChecked);
        return true;
    }
    
    protected long positionToSelectionHandle(int position) {
        return position;
    }

    //
    // ActionMode.Callback related methods
    //

    void onDestroyActionMode(ActionMode mode) {
        checkedItems.clear();
        actionMode = null;
        owner.notifyDataSetChanged();
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (actionMode != null) {
            switch (itemClickInActionModePolicy) {
            case SELECT:
                onItemLongClick(adapterView, view, position, id);
                return;
            case OPEN:
                finishActionMode();
                break;
            default:
                throw new RuntimeException("Invalid \"itemClickInActionMode\" value: " + itemClickInActionModePolicy);
            }
        }
        if (itemClickListener != null) {
            itemClickListener.onItemClick(adapterView, view, position, id);
        }
    }

    View getView(int position, View viewWithoutSelection) {
        if (viewWithoutSelection instanceof Checkable) {
            long handle = positionToSelectionHandle(position);
            boolean selected = isChecked(handle);
            ((Checkable)viewWithoutSelection).setChecked(selected);
        }
        if (itemIncludesCheckBox(viewWithoutSelection)) {
            initItemCheckbox(position, (ViewGroup)viewWithoutSelection);
        }
        return viewWithoutSelection;
    }
    
    private boolean itemIncludesCheckBox(View v) {
        if (itemIncludesCheckBox == null) {
            if (!(v instanceof ViewGroup)) {
                itemIncludesCheckBox = false;
            }
            else {
                ViewGroup root = (ViewGroup)v;
                itemIncludesCheckBox = root.findViewById(android.R.id.checkbox) != null;
            }
        }
        return itemIncludesCheckBox;
    }

    private void initItemCheckbox(int position, ViewGroup view) {
        CheckBox checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
        boolean selected = isChecked(position);
        checkBox.setTag(position);
        checkBox.setChecked(selected);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (Integer) buttonView.getTag();
        setItemChecked(position, isChecked);
    }
}
