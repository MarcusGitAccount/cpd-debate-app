package com.cpd.topics.config;


import java.util.Arrays;

import javax.jms.JMSException;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;


/**
 * @author mpop
 */
@Configuration
@EnableJms
public class JmsConfiguration implements JmsListenerConfigurer {

	private static Logger logger = LoggerFactory.getLogger(JmsConfiguration.class);

	public static final String MESSAGE_NAME_PROP = "name";

	public static final String MESSAGE_BODY_PROP = "body";

	@Value("${topics.in}")
	private String[] topicsIn;

	@Value("${name}")
	private String name;

	@Bean
	public DefaultJmsListenerContainerFactory jmsContainerFactory() {
		DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();

		containerFactory.setPubSubDomain(true);
		containerFactory.setConnectionFactory(connectionFactory());
		return containerFactory;
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

		connectionFactory.setBrokerURL("tcp://localhost:61616");
		cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);
		return cachingConnectionFactory;
	}

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		Arrays.stream(topicsIn).forEach(topic -> {
			SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();

			logger.info("Configuring listener for topic " + topic);

			endpoint.setId(topic);
			endpoint.setDestination(topic);
			endpoint.setMessageListener(message -> {
//				logger.info(String.valueOf(message));
				try {
					String from = message.getStringProperty(MESSAGE_NAME_PROP);
					String body = message.getStringProperty(MESSAGE_BODY_PROP);

					if (!name.equals(from)) {
						String log = String.format("Received message from [%s] on topic [%s]: %s", from, topic, body);

						logger.info(log);
					}
				} catch (JMSException e) {
					logger.error(e.toString());
				}
			});

			registrar.registerEndpoint(endpoint);
		});
	}
}
