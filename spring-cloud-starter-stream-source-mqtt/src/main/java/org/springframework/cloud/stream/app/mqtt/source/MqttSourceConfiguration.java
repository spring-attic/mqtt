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

package org.springframework.cloud.stream.app.mqtt.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.mqtt.MqttConfiguration;
import org.springframework.cloud.stream.app.mqtt.MqttProperties;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;

/**
 * A source module that receives data from Mqtt.
 *
 * @author Janne Valkealahti
 *
 */
@EnableBinding(Source.class)
@EnableConfigurationProperties({ MqttProperties.class, MqttSourceProperties.class })
@Import(MqttConfiguration.class)
public class MqttSourceConfiguration {

	@Autowired
	private MqttSourceProperties properties;

	@Autowired
	private MqttPahoClientFactory mqttClientFactory;

	@Bean
	public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(properties.getClientId(),
				mqttClientFactory, properties.getTopics());
		adapter.setQos(properties.getQos());
		adapter.setConverter(pahoMessageConverter());
		adapter.setOutputChannelName(Source.OUTPUT);
		return adapter;
	}

	@Bean
	public DefaultPahoMessageConverter pahoMessageConverter() {
		DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter(properties.getCharset());
		converter.setPayloadAsBytes(properties.isBinary());
		return converter;
	}
}
