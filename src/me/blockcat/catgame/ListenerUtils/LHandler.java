package me.blockcat.catgame.ListenerUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import me.blockcat.catgame.CatGame;
import me.blockcat.catgame.handlers.Handler;

import org.bukkit.event.Event;

public class LHandler {
	
	private HashMap<Class<? extends Event>, HashMap<Method, CatEvent>> eventMap = new HashMap<Class<? extends Event>, HashMap<Method, CatEvent>>();
	private HashMap<Method, Handler> classMap = new HashMap<Method, Handler>();
	//private CatGame plugin;
	
	public LHandler(CatGame plugin) {
		//this.plugin = plugin;
	}
	
	public void addClass(Handler c) {
		for (Method method : c.getClass().getDeclaredMethods()) {

			if (method.isAnnotationPresent(CatEvent.class)) {
				CatEvent catevent = method.getAnnotation(CatEvent.class);

				if (eventMap.containsKey(catevent.event())) {
					HashMap<Method, CatEvent> map = eventMap.get(catevent.event());
					map.put(method, catevent);
					eventMap.put(catevent.event(), map);							
				} else {
					HashMap<Method, CatEvent> map = new HashMap<Method, CatEvent>();
					map.put(method, catevent);
					eventMap.put(catevent.event(), map);
				}
				classMap.put(method, c);
			}
		}
	}

	public void triggerEvent(Event event) {
		if (!eventMap.containsKey(event.getClass())){
			return;
		}
		
		HashMap<Method, CatEvent> map = eventMap.get(event.getClass());
		
		for (Entry<Method, CatEvent> m : map.entrySet()) {
			Method method =	m.getKey();
			try {				
				method.invoke(classMap.get(method), event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
