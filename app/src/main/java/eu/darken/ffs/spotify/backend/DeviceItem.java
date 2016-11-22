package eu.darken.ffs.spotify.backend;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by darken on 22.11.2016.
 */
public class DeviceItem {
    private final String name;
    private final String address;

    public DeviceItem(@NonNull String name, @Nullable String address) {
        this.name = name;
        this.address = address;
    }

    public DeviceItem(BluetoothDevice bluetoothDevice) {
        this.name = bluetoothDevice.getName();
        this.address = bluetoothDevice.getAddress();
    }

    public DeviceItem(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        address = jsonObject.getString("address");
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("address", address);
        return jsonObject;
    }


    @Override
    public String toString() {
        return String.format(Locale.US, "DeviceItem(name=%s, address=%s)", name, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceItem that = (DeviceItem) o;

        return address.equals(that.address);

    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
