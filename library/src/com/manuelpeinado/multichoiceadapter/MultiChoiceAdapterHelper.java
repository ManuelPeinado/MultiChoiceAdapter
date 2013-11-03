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

import android.app.Activity;
import android.view.ActionMode;
import android.widget.BaseAdapter;

public class MultiChoiceAdapterHelper extends MultiChoiceAdapterHelperBase {
    private ActionMode actionMode;

    protected MultiChoiceAdapterHelper(BaseAdapter owner) {
        super(owner);
    }

    @Override
    protected void startActionMode() {
        try {
            Activity activity = (Activity) adapterView.getContext();
            Method method = activity.getClass().getMethod("startActionMode", ActionMode.Callback.class);
            actionMode = (ActionMode) method.invoke(activity, owner);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void finishActionMode() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    @Override
    protected void setActionModeTitle(String title) {
        actionMode.setTitle(title);
    }

    @Override
    protected boolean isActionModeStarted() {
        return actionMode != null;
    }

    @Override
    protected void clearActionMode() {
        actionMode = null;
    }
}
