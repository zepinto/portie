package pt.lsts.portie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pt.lsts.imc.LogBookEntry;
import pt.lsts.imc.lsf.LsfMessageLogger;
import pt.lsts.portie.R;
import pt.lsts.util.ImcBus;

public class PortieMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portie_main);
        LsfMessageLogger.changeLogBaseDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Portie/");
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onCreate()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImcBus.stop();
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImcBus.unregister(this);
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImcBus.register(this);
        ImcBus.logEntry(LogBookEntry.TYPE.INFO, getClass().getSimpleName(), "onResume()");
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
