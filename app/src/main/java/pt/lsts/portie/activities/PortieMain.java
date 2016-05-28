package pt.lsts.portie.activities;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.otto.Subscribe;

import pt.lsts.imc.Announce;
import pt.lsts.imc.LoggingControl;
import pt.lsts.imc.lsf.LsfMessageLogger;
import pt.lsts.neptus.messages.listener.Periodic;
import pt.lsts.portie.R;
import pt.lsts.util.EventSystemBecameVisible;
import pt.lsts.util.EventSystemConnected;
import pt.lsts.util.EventSystemDisconnected;
import pt.lsts.util.ImcBus;

public class PortieMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portie_main);
        Log.d("PORTIE-MAIN", "onCreate()");
        LsfMessageLogger.changeLogBaseDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Portie/");
        LoggingControl log = new LoggingControl();
        log.setName("Portie");
        log.setOp(LoggingControl.OP.REQUEST_START);
        LsfMessageLogger.log(log);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ImcBus.stop();
        Log.d("PORTIE-MAIN", "onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImcBus.unregister(this);
        Log.d("PORTIE-MAIN", "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImcBus.register(this);
        Log.d("PORTIE-MAIN", "onResume()");
    }
}
