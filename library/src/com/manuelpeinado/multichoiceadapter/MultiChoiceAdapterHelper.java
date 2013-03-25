package com.manuelpeinado.multichoiceadapter;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.ActionMode;
import com.manuelpeinado.multichoicelistadapter.R;

class MultiChoiceAdapterHelper implements OnItemLongClickListener, OnItemClickListener, OnCheckedChangeListener {
    private static final String BUNDLE_KEY = "mca__selection";
    private Set<Long> selection = new HashSet<Long>();
    private AdapterView<? super MultiChoiceBaseAdapter> adapterView;
    private BaseAdapter owner;
    private OnItemClickListener itemClickListener;
    private ActionMode actionMode;
    private Drawable selectedItemBackground;
    private Drawable unselectedItemBackground;
    private Boolean itemIncludesCheckBox;

    
    MultiChoiceAdapterHelper(BaseAdapter owner) {
        this.owner = owner;
    }

    void restoreSelectionFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        long[] array = savedInstanceState.getLongArray(BUNDLE_KEY);
        selection.clear();
        for (long id : array) {
            selection.add(id);
        }
    }

    void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
        this.adapterView = adapterView;
        checkActivity();
        adapterView.setOnItemLongClickListener(this);
        adapterView.setOnItemClickListener(this);
        adapterView.setAdapter(owner);
        extractBackgroundColor();
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
        long[] array = new long[selection.size()];
        int i = 0;
        for (Long id : selection) {
            array[i++] = id;
        }
        outState.putLongArray(BUNDLE_KEY, array);
    }

    void select(long handle, boolean selected) {
        if (selected) {
            select(handle);
        } else {
            unselect(handle);
        }
    }

    void select(long handle) {
        boolean wasSelected = isSelected(handle);
        if (wasSelected) {
            return;
        }
        if (actionMode == null) {
            startActionMode();
        }
        selection.add((long)handle);
        owner.notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    void unselect(long handle) {
        boolean wasSelected = isSelected(handle);
        if (!wasSelected) {
            return;
        }
        selection.remove(handle);
        if (getSelectionCount() == 0) {
            finishActionMode();
            return;
        }
        owner.notifyDataSetChanged();
        onItemSelectedStateChanged();
    }

    Set<Long> getSelection() {
        // Return a copy to prevent concurrent modification problems
        return new HashSet<Long>(selection);
    }

    int getSelectionCount() {
        return selection.size();
    }

    boolean isSelected(long handle) {
        return selection.contains(handle);
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
        int count = getSelectionCount();
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
    
    private void extractBackgroundColor() {
        Context ctx = getContext();
        int styleAttr = R.attr.multiChoiceAdapterStyle;
        int defStyle = R.style.MultiChoiceAdapter_DefaultSelectedItemBackground;
        TypedArray ta = ctx.obtainStyledAttributes(null, R.styleable.MultiChoiceAdapter, styleAttr, defStyle);
        selectedItemBackground = ta.getDrawable(0);
        ta.recycle();
        Resources res = ctx.getResources();
        unselectedItemBackground = new ColorDrawable(res.getColor(android.R.color.transparent));
    }

    //
    // OnItemLongClickListener implementation
    //

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        long handle = positionToSelectionHandle(position);
        boolean wasChecked = isSelected(handle);
        select(handle, !wasChecked);
        return true;
    }
    
    protected long positionToSelectionHandle(int position) {
        return position;
    }

    //
    // ActionMode.Callback related methods
    //

    void onDestroyActionMode(ActionMode mode) {
        selection.clear();
        actionMode = null;
        owner.notifyDataSetChanged();
    }
    
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

    View getView(int position, View viewWithoutSelection) {
        if (itemIncludesCheckBox(viewWithoutSelection)) {
            initItemCheckbox(position, (ViewGroup)viewWithoutSelection);
        }
        updateItemBackground(position, viewWithoutSelection);
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
        boolean selected = isSelected(position);
        checkBox.setTag(position);
        checkBox.setChecked(selected);
        checkBox.setOnCheckedChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    private void updateItemBackground(int position, View v) {
        long handle = positionToSelectionHandle(position);
        boolean selected = isSelected(handle);
        Drawable bg = selected ? selectedItemBackground : unselectedItemBackground;
        v.setBackgroundDrawable(bg);
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (Integer) buttonView.getTag();
        select(position, isChecked);
    }
}
