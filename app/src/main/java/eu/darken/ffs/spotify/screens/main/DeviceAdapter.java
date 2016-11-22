package eu.darken.ffs.spotify.screens.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.darken.ffs.spotify.R;
import eu.darken.ffs.spotify.backend.DeviceItem;

/**
 * Created by darken on 22.11.2016.
 */
class DeviceAdapter extends BaseAdapter {

    private final List<DeviceItem> data;

    public DeviceAdapter(List<DeviceItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DeviceItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_device_line, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        final DeviceItem item = getItem(position);
        if (item != null) holder.bind(item);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.address) TextView address;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bind(DeviceItem deviceItem) {
            if (deviceItem == null) return;
            name.setText(deviceItem.getName());
            address.setText(deviceItem.getAddress());
        }
    }
}
