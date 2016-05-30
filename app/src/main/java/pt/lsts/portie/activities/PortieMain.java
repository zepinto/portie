package pt.lsts.portie.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import pt.lsts.imc.Announce;
import pt.lsts.imc.Heartbeat;
import pt.lsts.imc.IMCUtil;
import pt.lsts.imc.LogBookEntry;
import pt.lsts.imc.VehicleState;
import pt.lsts.imc.lsf.LsfMessageLogger;
import pt.lsts.imc.net.IMCProtocol;
import pt.lsts.imc.net.IMCVehicle;
import pt.lsts.portie.R;
import pt.lsts.syslist.SystemListAdapter;
import pt.lsts.util.EventSystemBecameVisible;
import pt.lsts.util.EventSystemDisconnected;
import pt.lsts.util.ImcBus;

public class PortieMain extends AppCompatActivity {

    SystemListAdapter sysListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sysListAdapter = new SystemListAdapter(getApplicationContext());
        setContentView(R.layout.activity_portie_main);
        ListView listView = (ListView) findViewById(R.id.systemList);
        listView.setAdapter(sysListAdapter);
        LsfMessageLogger.changeLogBaseDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Portie/");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImcBus.stop();
        LsfMessageLogger.close();
    }

    @Subscribe
    public void on(final Announce announce) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sysListAdapter.setAnnounce(announce);
            }
        });

    }

    @Subscribe
    public void on(final VehicleState vstate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sysListAdapter.setState(vstate);
            }
        });

    }

    @Subscribe
    public void on(EventSystemBecameVisible evt) {
        ImcBus.connect(evt.getSystem());
    }

    @Subscribe
    public void on(final EventSystemDisconnected evt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sysListAdapter.systemDisconnected(evt.getSystem());
            }
        });
    }

    @Subscribe
    public void on(Heartbeat beat) {
        if (beat.getSourceName().equals(ImcBus.getMainSystem())) {
            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("vibrate", false)) {
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImcBus.unregister(this);
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onPause()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LsfMessageLogger.changeLogSingleton();
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImcBus.register(this);
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onResume()");
        ListView systemList = (ListView) findViewById(R.id.systemList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Settings")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
