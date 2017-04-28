/*
 * Copyright 2017 the original author or authors.
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
package org.springframework.cloud.stream.app.test.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;

/**
 * Rule skipping tests if mqtt is not available from localhost with default settings.
 *
 * @author Janne Valkealahti
 *
 */
public class MqttRule extends TestWatcher implements TestRule {

	@Override
	public Statement apply(Statement base, Description description) {
		IMqttClient client = null;
		try {
			DefaultMqttPahoClientFactory connectionFactory = new DefaultMqttPahoClientFactory();
			connectionFactory.setServerURIs("tcp://localhost:1883");
			connectionFactory.setUserName("guest");
			connectionFactory.setPassword("guest");
			connectionFactory.setConnectionTimeout(1);
			client = connectionFactory.getClientInstance("tcp://localhost:1883", "scdf-test-client");
			client.connect(connectionFactory.getConnectionOptions());
		} catch (Exception e) {
			return super.apply(new Statement() {
				@Override
				public void evaluate() throws Throwable {
				}
			}, Description.EMPTY);
		} finally {
			if (client != null) {
				try {
					client.disconnect();
					client.close();
				} catch (MqttException e) {
				}
			}
		}
		return super.apply(base, description);
	}
}
