package eu.darken.ffs.spotify.screens.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.darken.ffs.spotify.R;
import eu.darken.ffs.spotify.backend.BluetoothEventReceiver;
import eu.darken.ffs.spotify.backend.DeviceItem;
import eu.darken.ffs.spotify.backend.DeviceRepo;
import eu.darken.ffs.spotify.tools.Cmd;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.selected_devices) ListView selectedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateSelectedDevices();
    }

    @OnClick(R.id.select_device)
    public void showAddDeviceDialog(View view) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<DeviceItem> devices = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            devices.add(new DeviceItem(device.getName(), device.getAddress()));
        }
        final DeviceAdapter adapter = new DeviceAdapter(devices);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                DeviceItem deviceItem = adapter.getItem(position);
                final DeviceRepo deviceRepo = new DeviceRepo(MainActivity.this);
                deviceRepo.addDevice(deviceItem);
                dialog.dismiss();
                updateSelectedDevices();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.grant_root)
    public void onGrantRootClicked(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cmd cmd = new Cmd();
                cmd.setRuntimeExec("su");
                cmd.execute();
            }
        }).start();
    }

    private void updateSelectedDevices() {
        final DeviceRepo deviceRepo = new DeviceRepo(this);
        Collection<DeviceItem> devices = deviceRepo.getDevices();
        toggleBroadcastReceiver(!devices.isEmpty());
        final DeviceAdapter adapter = new DeviceAdapter(new ArrayList<>(devices));
        selectedDevices.setAdapter(adapter);
        selectedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                deviceRepo.removeDevice(adapter.getItem(position));
                updateSelectedDevices();
            }
        });
    }

    private void toggleBroadcastReceiver(boolean enable) {
        int flag = (enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        ComponentName component = new ComponentName(this, BluetoothEventReceiver.class);
        getPackageManager().setComponentEnabledSetting(component, flag, PackageManager.DONT_KILL_APP);
        Timber.d("BroadcastReceiver enabled=%b", enable);
    }
}
