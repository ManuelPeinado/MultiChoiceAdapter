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

public enum ItemClickInActionModePolicy {
    /**
     * Changes the selection state of the clicked item, just as if it had been
     * long clicked. This is what the native MULTICHOICE_MODAL mode of List
     * does, and what almost every app does
     */
    SELECT,
    /**
     * Opens the clicked item, just as if it had been clicked outside of the
     * action mode. This is what the Gmail app does
     */
    OPEN
}
