package org.osgi.impl.service.onem2m;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.impl.service.onem2m.protocol.ServiceLayerUtil;
import org.osgi.impl.service.onem2m.protocol.service.ServiceLayreImplService;
import org.osgi.service.onem2m.ServiceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLayerFactoryImpl implements ServiceFactory<ServiceLayer> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLayerFactoryImpl.class);

	@Override
	public ServiceLayer getService(Bundle bundle, ServiceRegistration<ServiceLayer> registration) {

		LOGGER.info("Start factory");

		String bundleSymbolicName = bundle.getSymbolicName();

		// get Properties
		Map<String, String> property = ServiceLayerUtil.getProperty(bundleSymbolicName, bundle.getBundleContext());


			
		CseService cse = new CseService();
		ServiceLayer sl = new ServiceLayreImplService(property.get(ServiceLayerUtil.ORIGIN), cse);
		LOGGER.info("End factory");
		return sl;

	}

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration<ServiceLayer> registration, ServiceLayer service) {
		// NOP

	}

	

}