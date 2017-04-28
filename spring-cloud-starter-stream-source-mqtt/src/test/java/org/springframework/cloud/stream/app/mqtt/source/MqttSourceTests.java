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

package org.springframework.cloud.stream.app.mqtt.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.app.test.mqtt.MqttRule;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for MqttSource.
 *
 * @author Janne Valkealahti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public abstract class MqttSourceTests {

	@ClassRule
	public static MqttRule mqttRule = new MqttRule();

	@Autowired
	protected Source channels;

	@Autowired
	protected MessageCollector messageCollector;

	@Autowired
	protected MqttSourceProperties properties;

	@Autowired
	protected MessageHandler mqttOutbound;

	@SpringBootTest({"mqtt.topics=test,fake", "mqtt.qos=0,0"})
	public static class ReceiveMessageTests extends MqttSourceTests {

		@Test
		public void test() throws Exception {
			mqttOutbound.handleMessage(MessageBuilder.withPayload("hello").build());
			Message<?> out = this.messageCollector.forChannel(this.channels.output()).poll(10, TimeUnit.SECONDS);
			assertNotNull(out);
			assertEquals("hello", out.getPayload());

		}
	}

	@SpringBootApplication
	static class MqttSourceApplication {

		@Autowired
		private MqttPahoClientFactory mqttClientFactory;

		@Bean
		public MessageHandler mqttOutbound() {
			MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("test", mqttClientFactory);
			messageHandler.setAsync(true);
			messageHandler.setDefaultTopic("test");
			messageHandler.setConverter(pahoMessageConverter());
			return messageHandler;
		}

		@Bean
		public DefaultPahoMessageConverter pahoMessageConverter() {
			DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter(1, false, "UTF-8");
			return converter;
		}

	}
}
