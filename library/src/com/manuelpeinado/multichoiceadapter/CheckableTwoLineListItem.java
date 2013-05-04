/*
 * Copyright (C) 2006 The Android Open Source Project
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



import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <p>A view group with two children, intended for use in ListViews. This item has two 
 * {@link android.widget.TextView TextViews} elements (or subclasses) with the ID values 
 * {@link android.R.id#text1 text1}
 * and {@link android.R.id#text2 text2}. There is an optional third View element with the 
 * ID {@link android.R.id#selectedIcon selectedIcon}, which can be any View subclass 
 * (though it is typically a graphic View, such as {@link android.widget.ImageView ImageView})
 * that can be displayed when a TwoLineListItem has focus. Android supplies a 
 * {@link android.R.layout#two_line_list_item standard layout resource for TwoLineListView} 
 * (which does not include a selected item icon), but you can design your own custom XML
 * layout for this object.
 * 
 * @attr ref android.R.styleable#TwoLineListItem_mode
 */
public class CheckableTwoLineListItem extends CheckableRelativeLayout {

    private TextView mText1;
    private TextView mText2;

    public CheckableTwoLineListItem(Context context) {
        super(context);
    }

    public CheckableTwoLineListItem(Context context, AttributeSet attrs) {
        super(context, attrs); 
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        mText1 = (TextView) findViewById(android.R.id.text1);
        mText2 = (TextView) findViewById(android.R.id.text2);
    }
    
    /**
     * Returns a handle to the item with ID text1.
     * @return A handle to the item with ID text1.
     */
    public TextView getText1() {
        return mText1;
    }
    
    /**
     * Returns a handle to the item with ID text2.
     * @return A handle to the item with ID text2.
     */
    public TextView getText2() {
        return mText2;
    }
}