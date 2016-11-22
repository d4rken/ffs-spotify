package eu.darken.ffs.spotify.backend;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Collection;

import eu.darken.ffs.spotify.tools.Cmd;
import timber.log.Timber;

/**
 * Created by darken on 22.11.2016.
 */

public class DealWithSpotifyService extends IntentService {
    public DealWithSpotifyService() {
        super("FFS Spotify Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("onHandleIntent(%s)", intent);
        String action = intent.getStringExtra(BluetoothEventReceiver.EXTRA_ACTION);
        BluetoothDevice currentDevice = intent.getParcelableExtra(BluetoothEventReceiver.EXTRA_DEVICE);
        DeviceRepo deviceRepo = new DeviceRepo(this);
        Collection<DeviceItem> wantedDevices = deviceRepo.getDevices();

        if (wantedDevices.isEmpty()) {
            Timber.w("Receiver was active but target devices are empty!");
            return;
        }

        if (!wantedDevices.contains(new DeviceItem(currentDevice))) {
            Timber.d("Device %s is not among our targets.", currentDevice);
            return;
        } else Timber.d("Device %s is our target!", currentDevice.getAddress());

        if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
            Timber.d("Handling device connected.");
            if (killSpotify()) {
                sleep(500);
                startSpotify();
                sleep(2000);
                sendPlay();
            }
        } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
            Timber.d("Handling device disconnected.");
        }
        BluetoothEventReceiver.completeWakefulIntent(intent);
    }

    private void startSpotify() {
        Timber.d("Launching Spotify...");
        PackageManager manager = getPackageManager();
        Intent i = manager.getLaunchIntentForPackage("com.spotify.music");
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);

        Timber.d("... Spotify launched.");
    }

    private boolean killSpotify() {
        Timber.d("Killing Spotify...");
        Cmd cmd = new Cmd();
        cmd.setRuntimeExec("su");
        cmd.addCommand("am force-stop com.spotify.music");
        cmd.execute();
        if (cmd.getExitCode() == Cmd.OK) Timber.d("... Spotify killed.");
        else Timber.e("... error killing Spotify (%s)", cmd.getErrors());
        return cmd.getExitCode() == Cmd.OK;
    }

    private void sendPlay() {
        Timber.d("Telling Spotify to get that shit going...");
        Intent play = new Intent("com.spotify.mobile.android.ui.widget.NEXT");
        play.setPackage("com.spotify.music");
        sendBroadcast(play);
        Timber.d("... Spotify got told.");
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}