package pt.lsts.syslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import pt.lsts.imc.Announce;
import pt.lsts.imc.VehicleState;
import pt.lsts.portie.R;
import pt.lsts.util.ImcBus;

/**
 * Created by zp on 29-05-2016.
 */
public class SystemListAdapter extends BaseAdapter {
    Context mContext;

    ArrayList<String> systems = new ArrayList<>();
    LinkedHashMap<String, VehicleState.OP_MODE> states = new LinkedHashMap<>();
    LinkedHashMap<String, Announce.SYS_TYPE> types = new LinkedHashMap<>();

    public void setAnnounce(Announce announce) {
        if (!states.containsKey(announce.getSysName())) {
            synchronized (systems) {
                states.put(announce.getSysName(), VehicleState.OP_MODE.BOOT);
                systems.add(announce.getSysName());
                Collections.sort(systems);
                System.out.println(systems);
                types.put(announce.getSysName(), announce.getSysType());
            }
            notifyDataSetChanged();
        }

    }

    public void systemDisconnected(String system) {
        synchronized (systems) {
            systems.remove(system);
            states.remove(system);
            types.remove(system);
        }
        notifyDataSetChanged();
    }

    public void setState(VehicleState state) {
        VehicleState.OP_MODE previous = states.get(state.getSourceName());

        if (states.containsKey(state.getSourceName())) {
            states.put(state.getSourceName(), state.getOpMode());
        }

        if (previous != state.getOpMode())
            notifyDataSetChanged();
    }

    public SystemListAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        synchronized (systems) {
            return systems.size();
        }
    }

    public Object getItem(int arg0) {

        return states.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View v = mInflater.inflate(R.layout.listview_row, null);
        final String vehicle = systems.get(position);
        TextView tv = (TextView) v.findViewById(R.id.txtName);
        v.setBackgroundColor(0xFF00FF00);
        tv.setText(vehicle);
        ((Button)v.findViewById(R.id.btnSelect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImcBus.setMainSystem(vehicle);
            }
        });
        return v;
    }
}
