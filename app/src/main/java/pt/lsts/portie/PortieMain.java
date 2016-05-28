package pt.lsts.portie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import pt.lsts.imc.Announce;
import pt.lsts.neptus.messages.listener.Periodic;
import pt.lsts.util.EventSystemBecameVisible;
import pt.lsts.util.EventSystemConnected;
import pt.lsts.util.EventSystemDisconnected;
import pt.lsts.util.ImcBus;

public class PortieMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portie_main);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ImcBus.stop();
        System.out.println("Unregistered");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImcBus.register(this);
        ImcBus.connect("lauv-noptilus-1");
    }

    @Subscribe
    public void on(EventSystemBecameVisible evt) {
        System.out.println("System is visible: "+evt.getSystem());
        System.out.println(ImcBus.getActiveSystems());
    }

    @Subscribe
    public void on(EventSystemConnected evt) {
        System.out.println("System connected: "+evt.getSystem());
    }

    @Subscribe
    public void on(EventSystemDisconnected evt) {
        System.out.println("System disconnected: "+evt.getSystem());
    }

    @Periodic(3000)
    public void periodically() {
        System.out.println("Periodically");
    }
}
