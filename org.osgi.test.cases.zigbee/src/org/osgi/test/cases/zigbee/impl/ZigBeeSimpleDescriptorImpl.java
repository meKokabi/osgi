/*
 * Copyright (c) OSGi Alliance (2014, 2015). All Rights Reserved.
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

package org.osgi.test.cases.zigbee.impl;

import org.osgi.service.zigbee.descriptors.ZigBeeSimpleDescriptor;

/**
 * Mocked impl of ZigBeeSimpleDescriptor.
 * 
 * @author $Id$
 */
public class ZigBeeSimpleDescriptorImpl implements ZigBeeSimpleDescriptor {

	private int		deviceId;
	private byte	version;
	private int		profileId;
	private int[]	InputClusters;
	private int[]	outputClusters;

	/**
	 * @param deviceId
	 * @param version
	 * @param profileId
	 */
	public ZigBeeSimpleDescriptorImpl(int deviceId, byte version, int profileId) {
		this.deviceId = deviceId;
		this.version = version;
		this.profileId = profileId;
	}

	public int getApplicationDeviceId() {
		return deviceId;
	}

	public byte getApplicationDeviceVersion() {
		return version;
	}

	public int getApplicationProfileId() {
		return profileId;
	}

	public short getEndpoint() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[] getInputClusters() {
		// TODO Auto-generated method stub
		return InputClusters;
	}

	public int[] getOutputClusters() {
		// TODO Auto-generated method stub
		return outputClusters;
	}

	public void setInputClusters(int[] InputClusters) {
		this.InputClusters = InputClusters;
	}

	public void setOutputClusters(int[] outputClusters) {
		this.outputClusters = outputClusters;
	}

	public boolean providesInputCluster(int clusterId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean providesOutputCluster(int clusterId) {
		// TODO Auto-generated method stub
		return false;
	}

	public String toString() {
		return "" + this.getClass().getName() + "[deviceId: " + deviceId + ", version: " + version + ", profileId: "
				+ profileId + "]";
	}

}