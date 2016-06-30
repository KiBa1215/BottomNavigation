# BottomNavigation
An bottom navigation for android, label, icon and badge supported.
## ScreenShot
![image](/demo.gif)

## Gradle
    compile 'com.kiba:bottomnavigation:0.2.0'
    
## Usage
```xml
<com.kiba.bottomnavigation.BottomNavigationView
            android:id="@+id/bottomNavigationView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            app:enable_label="true"
            app:enable_icon="true"
            app:enable_badge="true"
            android:background="#567890"
            android:minHeight="?attr/actionBarSize"/>
```

# License
    Copyright 2016 - 2017 Yang Jiahui

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.