MultiChoiceAdapter
==================

MultiChoiceAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as in the native GMail app. 

It provides a functionality similar to that of the [`CHOICE_MODE_MULTIPLE_MODAL`][1] ListView mode, but in a manner that is compatible 
with every version of Android from 2.1. Of course, this requires that your project uses [ActionBarSherlock][2].

![Example Image][3]

Try out the [sample application][4] on Google Play.

See the [accompanying sample project][5] for a small application that shows how to use this library.

Usage
=====

Instead of deriving your adapter from BaseAdapter or one of its subclasses, derive it from MultiChoiceAdapter. You'll have to implement the following methods:

#### ActionMode methods

* *onCreateActionMode.* Creates the action mode that will be displayed when at least one item is selected
* *onActionModeClicked.* Responds to a click on any of your action mode's actions

#### ListAdapter methods

* *getCount.* Returns the number of items to show
* *getItem.* Returns the item at a given position
* *getItemId.* Returns the id of the item at a given position
* *getViewImpl.* Returns the view to show for a given position. *Important:* do not override ListAdapter's getView method, override this method instead

Once you've implemented your class that derives from MultiChoiceAdapter, you'll have to attach it to a ListView like this:

	multiChoiceAdapter.setAdapterView(listView);
	multiChoiceAdapter.setOnItemClickListener(myItemListClickListener);

Do not call setOnItemClickListener on your ListView, call it on the adapter instead

Do not forget to derive your activity from one of the ActionBarSherlock activities, except SherlockListActivity

Developed By
============

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
 [3]: http://actionbarsherlock.com/static/feature.png
 [4]: https://play.google.com/store/apps/details?id=com.actionbarsherlock.sample.demos
 [5]: https://play.google.com/store/apps/details?id=com.actionbarsherlock.sample.demos
