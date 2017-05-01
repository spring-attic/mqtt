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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.app.test.mqtt.MqttTestSupport;
//import org.springframework.cloud.stream.binder.test.junit.rabbit.RabbitTestSupport;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for MqttSink.
 *
 * @author Janne Valkealahti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public abstract class MqttSinkTests {

	@ClassRule
	public static MqttTestSupport mqttTestSupport = new MqttTestSupport();

	@Autowired
	protected Sink channels;

	@Autowired
	protected MqttSinkProperties properties;

	@Autowired
	protected MqttPahoMessageDrivenChannelAdapter mqttInbound;

	@Autowired
	protected QueueChannel queue;

	@SpringBootTest({"mqtt.topic=test"})
	public static class ReceiveMessageTests extends MqttSinkTests {

		@Test
		public void test() throws Exception {
			this.channels.input().send(MessageBuilder.withPayload("hello").build());
			Message<?> in = this.queue.receive(10000);
			assertNotNull(in);
			assertEquals("hello", in.getPayload());
		}
	}

	@SpringBootApplication
	static class MqttSinkApplication {

		@Autowired
		private MqttPahoClientFactory mqttClientFactory;

		@Bean
		public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
			MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("test",
					mqttClientFactory, "test");
			adapter.setQos(0);
			adapter.setConverter(pahoMessageConverter());
			adapter.setOutputChannelName("queue");
			return adapter;
		}

		@Bean
		public DefaultPahoMessageConverter pahoMessageConverter() {
			DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter("UTF-8");
			converter.setPayloadAsBytes(false);
			return converter;
		}

		@Bean
		public QueueChannel queue() {
			return new QueueChannel();
		}
	}
}
