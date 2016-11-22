# ffs-spotify
FFS Spotify is an Android app that I wrote for myself, which kills and restarts the Spotify app when specific bluetooth devices connect.

## Why
For some time now Spotify no longer starts playing when getting connecting to bluetooth headsets or car stereos requiring me to take the phone out of my pocket, force-stop the Spotify app and reopen it.
This seems to be caused by some kind of lifecycle issue causing the Spotify app to become disconnected from it's own notification and or background service.

The symptons usually include:
* No reaction to bluetooth media controller events
* No reaction to notification actions
* Notification not being shown when it should be

Also see:
* https://community.spotify.com/t5/Android/Bug-no-notification-icon-when-spotify-is-playing-on-android/td-p/1469333
* https://community.spotify.com/t5/Android/Spotify-icon-gone-from-notification-bar/td-p/641462
* https://community.spotify.com/t5/Android/Notification-and-lock-screen-controls-not-available-on-Android-7/td-p/1475686
* https://community.spotify.com/t5/Android/Car-Bluetooth-controls-stopped-working/td-p/1489254
* https://community.spotify.com/t5/Android/Can-t-use-Bluetooth-controls-after-last-update/td-p/1476088
* https://community.spotify.com/t5/Android/Widget-Buttons-Not-working/td-p/1477285
* https://community.spotify.com/t5/Android/Notification-and-lock-screen-controls-not-available-on-Android-7/td-p/1475686

Note: **Some of these issues have since been fixed in Spotify v6.9+**

## Requirements
* **Requires Root to kill the Spotify app** 
* Android 4.4+

## Downloads
[Here](https://github.com/d4rken/ffs-spotify/releases)
