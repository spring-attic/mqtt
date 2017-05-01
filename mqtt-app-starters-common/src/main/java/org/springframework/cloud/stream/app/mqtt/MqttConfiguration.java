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
package org.springframework.cloud.stream.app.mqtt;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.util.ObjectUtils;

/**
 * Generic mqtt configuration.
 *
 * @author Janne Valkealahti
 *
 */
public class MqttConfiguration {

	@Autowired
	private MqttProperties mqttProperties;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(mqttProperties.getUrl());
		factory.setUserName(mqttProperties.getUsername());
		factory.setPassword(mqttProperties.getPassword());
		factory.setCleanSession(mqttProperties.isCleanSession());
		factory.setConnectionTimeout(mqttProperties.getConnectionTimeout());
		factory.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
		if (ObjectUtils.nullSafeEquals(mqttProperties.getPersistence(), "file")) {
			factory.setPersistence(new MqttDefaultFilePersistence(mqttProperties.getPersistenceDirectory()));
		}
		else if (ObjectUtils.nullSafeEquals(mqttProperties.getPersistence(), "memory")) {
			factory.setPersistence(new MemoryPersistence());
		}
		return factory;
	}
}
