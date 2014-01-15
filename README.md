MultiChoiceAdapter
==================

MultiChoiceAdapter is an implementation of ListAdapter which adds support for modal multiple choice selection as in the native Gmail app. 

It provides a functionality similar to that of the [<tt>CHOICE_MODE_MULTIPLE_MODAL</tt>][1] ListView mode, with two additional benefits:

* It's easier to use, as it keeps count of the selected items, updates their background accordingly and handles checkboxes transparently.
* It is compatible with every version of Android from 2.x. Of course, this implies that your project must use either [ActionBarSherlock][2] or the support library's [ActionBarCompat][3]. 

A version of the library that works with the stock action is also provided, for those apps with `minSdkVersion=11` or newer.

![Example Image][4]

Try out the sample application:

<a href="https://play.google.com/store/apps/details?id=com.manuelpeinado.multichoiceadapter.demo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Or browse the [source code of the sample application][5] for a complete example of use.

Including in your project
-------------------------

The library is pushed to Maven Central as a AAR, so you just need to add a dependency for it to your `build.gradle`:
    
    dependencies {
        // Use the following if your project uses ActionBarCompat
        compile 'com.github.manuelpeinado.multichoiceadapter:multichoiceadapter-abc:3.0.0'
        // Or the following if your projet uses ActionBarSherlock
        compile 'com.github.manuelpeinado.multichoiceadapter:multichoiceadapter-abs:3.0.0'
    }
    
Or if your project uses the stock action bar (yes, it makes sense to use MultiChoiceAdapter event in that case, as it will make your life easier):

        dependencies {
        compile 'com.github.manuelpeinado.multichoiceadapter:multichoiceadapter:3.0.0'
    }


Usage
---------

Check any of the provided tutorials:

* [Using MultiChoiceArrayAdapter][8]
* [Using MultiChoiceBaseAdapter][9]
* [Using MultiChoiceSimpleCursorAdapter][10]
* [Usage with GridView][11]

Customization
---------------------
You can customize the way the adapter behaves when an item is clicked and **the action mode was already active**. To do so, add an item named <tt>multiChoiceAdapterStyle</tt> to your theme, and have it reference an additional style which you define like this:

```xml
<style name="MyCustomMultiChoiceAdapter">
    <item name="itemClickInActionMode">selectItem</item>
</style>
```
    
Two values are supported:

* <tt>selectItem</tt>. Changes the selection state of the clicked item, just as if it had been long clicked. This is what the native MULTICHOICE_MODAL mode of ListView does, and what almost every app does, and thus the default value.
* <tt>openItem</tt>. Opens the clicked item, just as if it had been clicked outside of the action mode. This is what the native Gmail app does.


Acknowledgements
--------------------

* The sample app uses the [ProviGen library][6] by Timothee Jeannin

Who's using it
--------------
* [My App List][7]. With this app you can save installed applications in a list to restore them after flash or install a new ROM. 
* [NFC Basic][12]. NFC Basic is an application that allows writing and reading data using wireless technology NFC. It allows you to write content following the NFC-FORUM standards, writing tasks for Android phones, create your custom profiles (so you can do some actions in the same time), delete and clone any tags, 
 
*Does your app use MultiChoiceAdapter? If you want to be featured on this list drop me a line.*


Developed By
--------------------

Manuel Peinado Gallego - <manuel.peinado@gmail.com>

<a href="https://twitter.com/mpg2">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/twitter.png" />
</a>
<a href="https://plus.google.com/106514622630861903655">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/google-plus.png" />
</a>
<a href="http://www.linkedin.com/pub/manuel-peinado-gallego/1b/435/685">
  <img alt="Follow me on Twitter"
       src="https://raw.github.com/ManuelPeinado/NumericPageIndicator/master/art/linkedin.png" />


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
 [3]: http://www.youtube.com/watch?v=6TGgYqfJnyc
 [4]: https://raw.github.com/ManuelPeinado/MultiChoiceAdapter/master/art/readme_pic.png
 [5]: https://github.com/ManuelPeinado/MultiChoiceAdapter/tree/master/samples/stock
 [6]: https://github.com/TimotheeJeannin/ProviGen
 [7]: https://play.google.com/store/apps/details?id=com.projectsexception.myapplist
 [8]: https://github.com/ManuelPeinado/MultiChoiceAdapter/wiki/MultiChoiceArrayAdapter-tutorial
 [9]: https://github.com/ManuelPeinado/MultiChoiceAdapter/wiki/MultiChoiceBaseAdapter-tutorial
 [10]: https://github.com/ManuelPeinado/MultiChoiceAdapter/wiki/MultiChoiceSimpleCursorAdapter-tutorial
 [11]: https://github.com/ManuelPeinado/MultiChoiceAdapter/wiki/Gallery-tutorial
 [12]: https://play.google.com/store/apps/details?id=com.dpizarro.nfc.basic
