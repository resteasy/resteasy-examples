package org.jboss.resteasy.examples;

import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:l.weinan@gmail.com">Weinan Li</a>
 */
public class OrdersApplication extends Application {
	HashSet<Object> singletons = new HashSet<Object>();

	public OrdersApplication() {
		singletons.add(new Orders());
	}

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
