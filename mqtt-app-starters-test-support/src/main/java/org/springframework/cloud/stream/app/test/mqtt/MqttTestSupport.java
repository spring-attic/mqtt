/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.test.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;

import org.springframework.cloud.stream.test.junit.AbstractExternalResourceTestSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

/**
 * Test support checking if MQTT is available from default settings.
 *
 * @author Janne Valkealahti
 *
 */
public class MqttTestSupport extends AbstractExternalResourceTestSupport<IMqttClient> {

	public MqttTestSupport() {
		super("MQTT");
	}

	@Override
	protected void cleanupResource() throws Exception {
		resource.disconnect();
		resource.close();
	}

	@Override
	protected void obtainResource() throws Exception {
		DefaultMqttPahoClientFactory connectionFactory = new DefaultMqttPahoClientFactory();
		connectionFactory.setServerURIs("tcp://localhost:1883");
		connectionFactory.setUserName("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setConnectionTimeout(1);
		resource = connectionFactory.getClientInstance("tcp://localhost:1883", "scdf-test-client");
		resource.connect(connectionFactory.getConnectionOptions());
	}
}
