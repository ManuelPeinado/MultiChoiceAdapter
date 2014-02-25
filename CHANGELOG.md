Change Log
=======================================

Version 3.1.0 *(2014-02-25)*
----------------------------
* Added support for customized action mode title (to enable things like "2/5")

Version 3.0.1 *(2013-11-10)*
----------------------------
* Fixes bug in actionbarcompat extra

Version 3.0.0 *(2013-11-03)*
----------------------------
* Support for actionbarcompat and for the stock action bar.
* Added Gradle support.
* Removed Maven support.
* New project structure with support for stock action bar, actionbarcompat and actionbarsherlock under the same repo.

Version 2.2.5 *(2013-07-12)*
----------------------------
* Fixed bug in "AlphabetIndexerActivity".

Version 2.2.4 *(2013-07-12)*
----------------------------
* Added support for non-checkable items (useful, for instance,  if you want to have headers between regular, checkable items).
* Added getViewImpl() to MultiChoiceSimpleCursorAdapter so that implementations can customize the model-to-view mapping
* Changed order in which we call setTag and setChecked, previous one caused problems to some people
* Added alphabet indexer sample.

Version 2.2.3
----------------------------
* Skipped

Version 2.1.3 *(2013-05-21)*
----------------------------
* Fixed bug in "header" sample which caused the application to crash when the header was clicked
* Added two more buildings to the various samples

Version 2.1.2 *(2013-05-19)*
----------------------------
* Fixed bug which caused selection to be lost when scrolling on a large list (only when items had checkboxes)

Version 2.1.1 *(2013-05-15)*
----------------------------
* Added support for list views with headers
* Updated versions of ABS (4.3.1) and android maven plugin (3.3.2)

Version 2.1.0 *(2013-05-9)*
----------------------------
* Added methods get/setItemClickInActionModePolicy

Version 2.0.0 *(2013-05-5)*
----------------------------
 * Major rewrite of the adapter classes so that they are more consistent with the way the framework deals with selection of list view items:
   - The library no longer changes the background of checked items. Instead, items must implement the Checkable interface and take care of their background themselves. This is importante because it adds the flexibility that is needed to implement a grid like the one in the "Gallery" sample.
   - Methods like "select", "getSelectedItems" and "getSelectedCount" are now "setCheckedItem", "getCheckedItems" and "getCheckedItemsCount".   
 * Rewritten and reorganized samples, including new GridView ones.
 * Changed maven artifactId to something a bit more descriptive

Version 1.0.7 *(2013-04-18)*
----------------------------
 * Fixed bug which prevented the lib from dealing with orientation changes properly (the action mode was lost, and if a long click was performed afterwards the app crashed)

Version 1.0.6 *(2013-03-29)*
----------------------------

 * Added maven support including deployment to Maven Central
 * Removed ABS from repo
 * Added new attribute "itemClickInActionMode" which enables customization of the adapter's behavior when an item is clicked and the action mode is already active. Two modes are supported: 
 	* "selectItem". Changes the selection state of the clicked item, just as if it had been long clicked. This is what the native MULTICHOICE_MODAL mode of List does, and what almost every app does.
 	* "openItem". Opens the clicked item, just as if it had been clicked outside of the action mode. This is what the Gmail app does.
 


Version 1.0.5 *(2013-03-25)*
----------------------------

 * The 'SimpleCursorAdapter' sample now uses loaders 
 * Added new adapter type 'MultiChoiceArrayAdapter'

Version 1.0.4 *(2013-03-25)*
----------------------------

 * Added MultiChoiceSimpleCursorAdapter
 * Added new activity to the sample app to showcase new MultiChoiceSimpleCursorAdapter, and necessary DB infraestructure
 * Refactored MultiChoiceBaseAdapter to avoid code duplication when implementing MultiChoiceSimpleCursorAdapter


Version 1.0.3 *(2013-03-24)*
----------------------------

 * Fixed a bug which caused item selection to be completely broken in Android 2.X

Version 1.0.2 *(2013-03-24)*
----------------------------

 * Fixed a bug which prevented the selected item background from being shown the second time items were selected
 * Simplified the code that deals with item removal in the sample app
 * Added support for checkboxes
 * Added support for multiple activities to the sample app
 * Added code to persist selection state when a configuration change occurs

Version 1.0.1 *(2013-03-23)*
----------------------------

 * Added new actions (select all, reset list) to demo app; removed useless "settings" action
 * Fixed a bug which caused the app to crash if the initial selection was made programmatically
 * Fixed a bug in demo app which caused the app to crash when several items were deleted at once
 * Added this change log
 * Added one more digit to versioning scheme (1.0 => 1.0.1)
 * Improved support for background customization via styles and attributes
 * Switched to space-based indentation

Version 1.0 *(2013-03-22)*
----------------------------
Initial release.
