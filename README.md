MultiChoiceAdapter
==================

MultiChoiceAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as in the native Gmail app. 

It provides a functionality similar to that of the [`CHOICE_MODE_MULTIPLE_MODAL`][1] ListView mode, but in a manner that is compatible 
with every version of Android from 2.x. Of course, this requires that your project uses [ActionBarSherlock][2].

![Example Image][3]

Try out the sample application:

<a href="https://play.google.com/store/apps/details?id=com.manuelpeinado.multichoiceadapter.demo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Or browse the [source code of the sample application][5] for a complete example of use.

Including in your project
-------------------------

If you’re using Eclipse with the ADT plugin you can include MultiChoiceAdaptar as a library project. Create a new Android project in Eclipse using the library/ folder as the existing source. Then, open the properties of this new project and, in the 'Android' category, add a reference to the ActionBarSherlock library project. Finally, in your application project properties, add a reference to the created library project.

If you use maven to build your Android project you can simply add a dependency for this library.

    <dependency>
        <groupId>com.github.manuelpeinado.multichoiceadapter</groupId>
        <artifactId>library</artifactId>
        <version>1.0.5</version>
        <type>apklib</type>
    </dependency>

Usage
---------

### If you need <code>BaseAdapter</code>-like functionality

Instead of deriving your adapter from BaseAdapter derive it from MultiChoiceBaseAdapter. You'll have to implement the usual BaseAdapter methods (getCount(), getItem()...), but instead of implementing BaseAdapter.getView(), implement getViewImpl(). You'll also have to implement the different ActionMode.Callback methods (onCreateActionMode, onActionModeClicked()...)

Once you've implemented your class that derives from MultiChoiceBaseAdapter, you attach an instance of it to your ListView like this:

	multiChoiceAdapter.setAdapterView(listView);
	multiChoiceAdapter.setOnItemClickListener(myItemListClickListener);

Do not call <code>setOnItemClickListener()</code> on your ListView, call it on the adapter instead.

Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity which is not supported.

Finally, do not forget to call <code>save(outState)</code> from your activity's <code>onSaveInstanceState()</code> method. This is necessary for the selection state to be persisted across configuration changes.

### If you need <code>SimpleCursorAdapter</code>-like functionality

Derive your adapter from MultiChoiceSimpleCursorAdapter, implement the ActionMode.Callback methods and configure it at construction time using the different parameters of the MultiChoiceSimpleCursorAdapter constructor (cursor, from, to...)

### If you need <code>ArrayAdapter</code>-like functionality

Derive your adapter from MultiChoiceArrayAdapter, implement the ActionMode.Callback methods and configure it at construction time using the different parameters of the MultiChoiceArrayAdapter constructor (layoutResourceId, textViewResourceId)

Checkboxes
------------------

MultiChoiceAdapter handles list items with check-boxes transparently. Just add a CheckBox to your item's XML layout and give it the id <code>android.R.id.checkbox</code>.

Customization
---------------------

You can use a custom background (drawable or color) for the selected items of your list. To do so, add an item named <code>multiChoiceAdapterStyle</code> to your theme, and have it reference an additional style which you define like this:

    <style name="MyCustomMultiChoiceAdapter">
        <item name="selectedItemBackground">@color/my_custom_selected_item_background</item>
    </style>

See the sample application for a complete example.


Libraries used
--------------------

* [ActionBarSherlock][2] by Jake Wharton
* The sample app uses the [ProviGen library][6] by Timothee Jeannin

Developed By
--------------------

* Manuel Peinado - <manuelpeinado@gmail.com>


License
-----------

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
 [3]: https://raw.github.com/ManuelPeinado/MultiChoiceAdapter/master/art/readme_pic.png
 [4]: https://play.google.com/store/apps/details?id=com.manuelpeinado.multichoiceadapter.demo
 [5]: https://github.com/ManuelPeinado/MultiChoiceAdapter/tree/master/sample
 [6]: https://github.com/TimotheeJeannin/ProviGen