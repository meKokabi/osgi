/*
 * Copyright (c) OSGi Alliance (2009, 2012). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osgi.test.cases.framework.launch.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.test.support.OSGiTestCase;

public abstract class LaunchTest extends OSGiTestCase {
	private static final String FRAMEWORK_FACTORY = "/META-INF/services/org.osgi.framework.launch.FrameworkFactory";
	private static final String	STORAGEROOT	= "org.osgi.test.cases.framework.launch.storageroot";
	
	private FrameworkFactory frameworkFactory;
	private String frameworkFactoryClassName;
	private List<String> rootBundles = new LinkedList<String>();
	protected String	rootStorageArea;
	
	protected void setUp() throws Exception {
		super.setUp();
		rootStorageArea = getStorageAreaRoot();
		assertNotNull("No storage area root found", rootStorageArea);
		File rootFile = new File(rootStorageArea);
		assertFalse(
				"Root storage area is not a directory: " + rootFile.getPath(),
				rootFile.exists() && !rootFile.isDirectory());
		if (!rootFile.isDirectory())
			assertTrue(
					"Could not create root directory: " + rootFile.getPath(),
					rootFile.mkdirs());
		frameworkFactoryClassName = getFrameworkFactoryClassName();
		assertNotNull("Could not find framework factory class", frameworkFactoryClassName);
		frameworkFactory = getFrameworkFactory();
		StringTokenizer st = new StringTokenizer(System.getProperty(
				"org.osgi.test.cases.framework.launch.bundles", ""), ",");
		rootBundles.clear();
		while (st.hasMoreTokens()) {
			String bundle = st.nextToken();
			assertNotNull(bundle);
			rootBundles.add(bundle);
		}
	}

	protected Framework createFramework(Map<String, String> configuration) {
		Framework framework = null;
		try {
			framework = frameworkFactory.newFramework(configuration);
		}
		catch (Exception e) {
			fail("Failed to construct the framework", e);
		}
		assertEquals("Wrong state for newly constructed framework", Bundle.INSTALLED, framework.getState());
		return framework;
	}
	
	protected URL getBundleInput(String bundle) {
		return getClass().getResource(bundle);
	}
	
	protected void initFramework(Framework framework) {
		boolean unintialized = (framework.getState() & (Framework.INSTALLED | Framework.RESOLVED)) != 0;
		try {
			framework.init();
			assertNotNull("BundleContext is null after init", framework.getBundleContext());
		}
		catch (BundleException e) {
			fail("Unexpected BundleException initializing", e);
		}
		if (unintialized) {
			installRootBundles(framework);
		}
		assertEquals("Wrong framework state after init", Bundle.STARTING, framework.getState());
	}
	
	protected Bundle installBundle(Framework framework, String bundle) throws BundleException, IOException {
		return installBundle(framework, bundle, bundle);
	}
	
	protected Bundle installBundle(Framework framework, String bundle, String location) throws BundleException, IOException {
		BundleContext fwkContext = framework.getBundleContext();
		assertNotNull("Framework context is null", fwkContext);
		URL input = getBundleInput(bundle);
		assertNotNull("Cannot find resource: " + bundle, input);
		return fwkContext.installBundle(location, input.openStream());
	}
	
	protected void startFramework(Framework framework) {
		boolean unintialized = (framework.getState() & (Framework.INSTALLED | Framework.RESOLVED)) != 0;
		try {
			framework.start();
			assertNotNull("BundleContext is null after start", framework.getBundleContext());
		}
		catch (BundleException e) {
			fail("Unexpected BundleException initializing", e);
		}
		if (unintialized) {
			installRootBundles(framework);
		}
		assertEquals("Wrong framework state after init", Bundle.ACTIVE, framework.getState());

	}
	
	protected void stopFramework(Framework framework) {
		int previousState = framework.getState();
		try {
            framework.stop();
			FrameworkEvent event = framework.waitForStop(10000);
			assertNotNull("FrameworkEvent is null", event);
			assertEquals("Wrong event type", FrameworkEvent.STOPPED, event.getType());
			assertNull("BundleContext is not null after stop", framework.getBundleContext());
		}
		catch (BundleException e) {
			fail("Unexpected BundleException stopping", e);
		}
		catch (InterruptedException e) {
			fail("Unexpected InterruptedException waiting for stop", e);
		}
		// if the framework was not STARTING STOPPING or ACTIVE then we assume the waitForStop returned immediately with a FrameworkEvent.STOPPED
		// and does not change the state of the framework
		int expectedState = (previousState & (Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING)) != 0 ? Bundle.RESOLVED : previousState;
		assertEquals("Wrong framework state after init", expectedState, framework.getState());
	}
	
	private FrameworkFactory getFrameworkFactory() {
		try {
			Class<FrameworkFactory> clazz = loadFrameworkFactoryClass(frameworkFactoryClassName);
			return clazz.newInstance();
		} catch (Exception e) {
			fail("Failed to get the framework constructor", e);
		}
		return null;
	}
	
	private String getClassName(URL factoryService) throws IOException {
		InputStream in = factoryService.openStream();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			for (String line = br.readLine(); line != null; line=br.readLine()) {
				int pound = line.indexOf('#');
				if (pound >= 0)
					line = line.substring(0, pound);
				line.trim();
				if (!"".equals(line))
					return line;
			}
		} finally {
			try {
				if (br != null)
					br.close();
			}
			catch (IOException e) {
				// did our best; just ignore
			}
		}
		return null;
	}
	
	private String getFrameworkFactoryClassName() throws IOException {
        URL factoryService = getClass().getResource(FRAMEWORK_FACTORY);
		assertNotNull("Could not locate: " + FRAMEWORK_FACTORY, factoryService);
		return getClassName(factoryService);
	}
	
	private void installRootBundles(Framework framework) {
		List<Bundle> bundles = new LinkedList<Bundle>();

		BundleContext fwkContext = framework.getBundleContext();
		assertNotNull("Framework context is null", fwkContext);
		for (String bundle : rootBundles) {
			try {
				Bundle b = fwkContext.installBundle("file:" + bundle);
				assertNotNull("Cannot install bundle: " + bundle, b);
				System.out.println("installed bundle " + b.getSymbolicName()
						+ " " + b.getVersion());
				bundles.add(b);
			}
			catch (BundleException e) {
				e.printStackTrace();
				fail("Unexpected BundleException installing root ", e);
			}
		}

		for (Bundle b : bundles) {
			if (b.getHeaders().get(Constants.FRAGMENT_HOST) == null) {
				try {
					b.start();
				}
				catch (BundleException e) {
					fail("Unexpected BundleException starting root ", e);
				}
				System.out.println("started bundle " + b.getSymbolicName());
			}
		}
	}
	
	private Class<FrameworkFactory> loadFrameworkFactoryClass(String className)
			throws ClassNotFoundException {
		return (Class<FrameworkFactory>) getClass().getClassLoader().loadClass(
				className);
	}

	private String getStorageAreaRoot() {
			String storageroot = System.getProperty(STORAGEROOT);
			assertNotNull("Must set property: " + STORAGEROOT, storageroot);
			return storageroot;
	}

	protected File getStorageArea(String testName, boolean delete) {
		File storageArea = new File(rootStorageArea, testName);
		if (delete) {
			assertTrue("Could not clean up storage area: " + storageArea.getPath(), delete(storageArea));
			assertTrue("Could not create storage area directory: " + storageArea.getPath(), storageArea.mkdirs());
		}
		return storageArea;
	}

	private boolean delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				String list[] = file.list();
				if (list != null) {
					int len = list.length;
					for (int i = 0; i < len; i++)
						if (!delete(new File(file, list[i])))
							return false;
				}
			}
	
			return file.delete();
		}
		return (true);
	}
}