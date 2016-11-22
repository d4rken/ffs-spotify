package eu.darken.ffs.spotify.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;

import timber.log.Timber;

/**
 * Created by darken on 22.11.2016.
 */

public class DeviceRepo {
    private static final String PREFS_FILE = "device_settings";
    private static final String TARGET_DEVICES_KEY = "target.devices";
    private final SharedPreferences preferences;

    public DeviceRepo(Context context) {
        preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public void addDevice(@NonNull DeviceItem device) {
        Timber.d("Adding device %s", device);
        Collection<DeviceItem> currentDevices = getDevices();
        currentDevices.remove(device); // If name was updated?
        currentDevices.add(device);
        saveDevices(currentDevices);
    }

    private void saveDevices(Collection<DeviceItem> deviceItems) {
        JSONArray jsonArray = new JSONArray();
        for (DeviceItem item : deviceItems) {
            try {
                jsonArray.put(item.toJSON());
            } catch (JSONException e) {Timber.e(e, null); }
        }
        preferences.edit().putString(TARGET_DEVICES_KEY, jsonArray.toString()).apply();
    }

    public void removeDevice(@NonNull DeviceItem deviceItem) {
        Collection<DeviceItem> devices = getDevices();
        devices.remove(deviceItem);
        saveDevices(devices);
    }

    @NonNull
    public Collection<DeviceItem> getDevices() {
        String json = preferences.getString(TARGET_DEVICES_KEY, null);
        if (json == null) return new ArrayList<>();

        try {
            Collection<DeviceItem> devices = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                devices.add(new DeviceItem(jsonArray.getJSONObject(i)));
            }
            return devices;
        } catch (JSONException e) {
            Timber.e(e, null);
            return new ArrayList<>();
        }
    }
}
