package eu.darken.ffs.spotify;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by darken on 22.11.2016.
 */

public class FFSSpotify extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
