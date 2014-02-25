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
package com.manuelpeinado.multichoiceadapter.samples.actionbarcompat;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.R;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.alphabetindexersample.AlphabetIndexerActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.arrayadaptersample.ArrayAdapterActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.baseadaptersample.BaseAdapterActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.complexitemlayoutsample.ComplexItemLayoutActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.customactionmodetitlesample.CustomActionModeTitleActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.gallerysample.GalleryActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.gridsample.GridActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.headersample.HeaderActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.manyitemssample.ManyItemsActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.noncheckableitemssample.NonCheckableItemsActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.simplecursoradaptersample.SimpleCursorAdapterActivity;
import com.manuelpeinado.multichoiceadapter.samples.actionbarcompat.twolinesarrayadaptersample.TwoLinesArrayAdapterActivity;

public class HomeActivity extends ActionBarActivity implements OnItemClickListener {
    private List<ActivityInfo> activitiesInfo = Arrays.asList(
            new ActivityInfo(ArrayAdapterActivity.class, R.string.activity_title_array_adapter),
            new ActivityInfo(TwoLinesArrayAdapterActivity.class, R.string.activity_title_array_adapter_two_lines),
            new ActivityInfo(BaseAdapterActivity.class, R.string.activity_title_base_adapter),
            new ActivityInfo(SimpleCursorAdapterActivity.class, R.string.activity_title_simple_cursor_adapter),
            new ActivityInfo(GridActivity.class, R.string.activity_title_grid),
            new ActivityInfo(GalleryActivity.class, R.string.activity_title_gallery),
            new ActivityInfo(HeaderActivity.class, R.string.activity_title_header),
            new ActivityInfo(ManyItemsActivity.class, R.string.activity_title_many_items),
            new ActivityInfo(ComplexItemLayoutActivity.class, R.string.activity_title_complex_item_layout),
            new ActivityInfo(AlphabetIndexerActivity.class, R.string.activity_title_alphabet_indexer),
            new ActivityInfo(NonCheckableItemsActivity.class, R.string.activity_title_non_checkable_items),
            new ActivityInfo(CustomActionModeTitleActivity.class, R.string.activity_title_custom_action_mode_title));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] titles = getActivityTitles();
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1, titles));
        listView.setOnItemClickListener(this);
    }

    private String[] getActivityTitles() {
        String[] result = new String[activitiesInfo.size()];
        int i = 0;
        for (ActivityInfo info : activitiesInfo) {
            result[i++] = getString(info.titleResourceId);
        }
        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> l, View arg1, int position, long id) {
        Class<? extends Activity> class_ = activitiesInfo.get(position).activityClass;
        Intent intent = new Intent(this, class_);
        startActivity(intent);
    }
}
