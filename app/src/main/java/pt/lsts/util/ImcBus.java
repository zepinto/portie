package pt.lsts.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import pt.lsts.imc.Announce;
import pt.lsts.imc.IMCMessage;
import pt.lsts.imc.net.IMCProtocol;
import pt.lsts.imc.state.ImcSystemState;
import pt.lsts.neptus.messages.listener.MessageInfo;
import pt.lsts.neptus.messages.listener.MessageListener;
import pt.lsts.neptus.messages.listener.Periodic;
import pt.lsts.neptus.messages.listener.PeriodicCallbacks;

public class ImcBus implements MessageListener<MessageInfo, IMCMessage> {

	private static ImcBus _instance = null;
	private Bus otto;
	private IMCProtocol proto;
	private LinkedHashMap<String, Long> lastAnnounce;
	private LinkedHashMap<String, Long> lastMessage;

	protected ImcBus() {
		otto = new Bus(ThreadEnforcer.ANY);
		proto = new IMCProtocol(6003);
		proto.addMessageListener(this);
		PeriodicCallbacks.register(this);
		lastAnnounce = new LinkedHashMap<>();
		lastMessage = new LinkedHashMap<>();
	}
	
	@Override
	public void onMessage(MessageInfo info, IMCMessage msg) {
		if (instance().proto.getLocalId() == msg.getSrc())
			return;

		otto.post(msg);

		if (msg instanceof Announce) {
			if (!lastAnnounce.containsKey(((Announce) msg).getSysName())) {
				EventSystemBecameVisible evt = new EventSystemBecameVisible(((Announce) msg).getSysName());
				post(evt);
			}
			lastAnnounce.put(((Announce) msg).getSysName(), System.currentTimeMillis());
		}
		else {
			synchronized (lastMessage) {
				if (!lastMessage.containsKey(msg.getSourceName())) {
					EventSystemConnected evt = new EventSystemConnected(msg.getSourceName());
					post(evt);
				}
				lastMessage.put(msg.getSourceName(), System.currentTimeMillis());
			}
		}
	}

	@Periodic(10000)
	public void checkDisconnectedSystems() {
		ArrayList<String> old = new ArrayList<>();
		long tooOld = System.currentTimeMillis() - 60000;

		synchronized (lastMessage) {
			for (Map.Entry<String, Long> entry : lastMessage.entrySet()) {
				if (entry.getValue() < tooOld)
					old.add(entry.getKey());
			}

			for (String s : old)
				lastMessage.remove(s);
		}

		for (String s : old)
			post(new EventSystemDisconnected(s));
	}

	
	private synchronized static ImcBus instance() {
		if (_instance == null)
			_instance = new ImcBus();
		return _instance;
	}	
	
	public static void post(Object event) {
		instance().otto.post(event);
	}
	
	public static boolean send(IMCMessage msg, String destination) {
		return instance().proto.sendMessage(destination, msg);
	}
		
	public static void register(Object pojo) {
		instance().otto.register(pojo);
		PeriodicCallbacks.register(pojo);
	}
	
	public static void unregister(Object pojo) {
		instance().otto.unregister(pojo);
		PeriodicCallbacks.unregister(pojo);
	}

	public static void connect(String system) {
		instance().proto.connect(system);
	}

	public static void disconnect(String system) {
		instance().proto.disconnect(system);
	}

	public static boolean isActive(String system) {
		ImcSystemState state = instance().proto.state(system);
		return state != null && state.isActive();
	}

	public static void stop() {
		instance().proto.removeMessageListener(instance());
		instance().proto.stop();
		_instance = null;
	}
	
	public static List<String> getActiveSystems() {
		ArrayList<String> systems = new ArrayList<>();

		for (String system : instance().proto.systems()) {
			ImcSystemState state = instance().proto.state(system);
			if (state.isActive())
				systems.add(system);
		}

		return systems;
	}
}
