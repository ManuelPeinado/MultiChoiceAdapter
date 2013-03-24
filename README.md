MultiChoiceAdapter
==================

MultiChoiceAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as in the native Gmail app. 

It provides a functionality similar to that of the [`CHOICE_MODE_MULTIPLE_MODAL`][1] ListView mode, but in a manner that is compatible 
with every version of Android from 2.x. Of course, this requires that your project uses [ActionBarSherlock][2].

![Example Image][3]

Try out the [sample application][4] on Google Play.

Browse the source code of the [sample application][5] for a complete example of use.

Usage
=====

Instead of deriving your adapter from BaseAdapter or one of its subclasses, derive it from MultiChoiceAdapter. You'll have to implement the following methods:

#### ActionMode methods

* <code>onCreateActionMode.</code> Creates the action mode that will be displayed when at least one item is selected
* <code>onActionModeClicked.</code> Responds to a click on any of your action mode's actions

#### ListAdapter methods

* <code>getCount.</code> Returns the number of items to show
* <code>getItem.</code> Returns the item at a given position
* <code>getItemId.</code> Returns the id of the item at a given position
* <code>getViewImpl.</code> Returns the view to show for a given position. **Important:** do not override ListAdapter's getView method, override this method instead

Once you've implemented your class that derives from MultiChoiceAdapter, you attach an instance of it to your ListView like this:

	multiChoiceAdapter.setAdapterView(listView);
	multiChoiceAdapter.setOnItemClickListener(myItemListClickListener);

Do not call setOnItemClickListener on your ListView, call it on the adapter instead.

Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity.

Checkboxes
==========

MultiChoiceAdapter handles list items with check-boxes transparently. Just add a CheckBox to your item's XML layout and give it the id <code>android.R.id.checkbox</code>.

Customization
=============

You can use a custom background (drawable or color) for the selected items of your list. To do so, add an item named <code>multiChoiceAdapterStyle</code> to your theme, and have it reference an additional style which you define like this:

    <style name="MyCustomMultiChoiceAdapter">
        <item name="selectedItemBackground">@color/my_custom_selected_item_background</item>
    </style>

See the sample application for a complete example.

Coming soon
=========

* Support for cursor and array adapters

Developed By
==========

* Manuel Peinado - <manuelpeinado@gmail.com>


License
=======

    Copyright 2013 Manuel Peinado

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





 [1]: http://developer.android.com/reference/android/widget/AbsListView.MultiChoiceModeListener.html
 [2]: http://actionbarsherlock.com
 [3]: https://raw.github.com/ManuelPeinado/MultiChoiceAdapter/master/art/screenshot.png
 [4]: https://play.google.com/store/apps/details?id=com.manuelpeinado.multichoiceadapter.demo
 [5]: https://github.com/ManuelPeinado/MultiChoiceAdapter/tree/master/sample
