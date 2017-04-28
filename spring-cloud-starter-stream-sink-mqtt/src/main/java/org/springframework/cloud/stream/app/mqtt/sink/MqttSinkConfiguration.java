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

package org.springframework.cloud.stream.app.mqtt.sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.mqtt.MqttConfiguration;
import org.springframework.cloud.stream.app.mqtt.MqttProperties;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

/**
 * A sink module that sends data to Mqtt.
 *
 * @author Janne Valkealahti
 *
 */
@EnableBinding(Sink.class)
@EnableConfigurationProperties({ MqttProperties.class, MqttSinkProperties.class })
@Import(MqttConfiguration.class)
public class MqttSinkConfiguration {

	@Autowired
	private MqttSinkProperties properties;

	@Autowired
	private MqttPahoClientFactory mqttClientFactory;

	@Bean
	@ServiceActivator(inputChannel = Sink.INPUT)
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(properties.getClientId(), mqttClientFactory);
		messageHandler.setAsync(properties.isAsync());
		messageHandler.setDefaultTopic(properties.getTopic());
		messageHandler.setConverter(pahoMessageConverter());
		return messageHandler;
	}

	@Bean
	public DefaultPahoMessageConverter pahoMessageConverter() {
		DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter(properties.getQos(),
				properties.isRetained(), properties.getCharset());
		return converter;
	}
}
