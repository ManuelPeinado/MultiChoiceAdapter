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

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;

public interface MultiChoiceAdapter {
    void setAdapterView(AdapterView<? super BaseAdapter> adapterView);
    void setOnItemClickListener(OnItemClickListener listener);
    void save(Bundle outState);
    void setItemChecked(long position, boolean checked);
    Set<Long> getCheckedItems();
    int getCheckedItemCount();
    boolean isChecked(long position);
    void setItemClickInActionModePolicy(ItemClickInActionModePolicy policy);
    ItemClickInActionModePolicy getItemClickInActionModePolicy();
    boolean isItemCheckable(int position);
}
