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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for the Mqtt Sink.
 *
 * @author Janne Valkealahti
 *
 */
@ConfigurationProperties("mqtt")
public class MqttSinkProperties {

	/**
	 * identifies the client
	 */
	private String clientId = "scdf.mqtt.client.id.sink";

	/**
	 * the topic to which the sink will publish
	 */
	private String topic = "scdf.mqtt";

	/**
	 * the quality of service to use
	 */
	private int qos = 1;

	/**
	 * whether to set the 'retained' flag
	 */
	private boolean retained = false;

	/**
	 * the charset used to convert a String payload to byte[]
	 */
	private String charset = "UTF-8";

	/**
	 * whether or not to use async sends
	 */
	private boolean async = false;

	public int getQos() {
		return qos;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}
}
