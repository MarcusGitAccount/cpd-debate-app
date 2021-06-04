package com.cpd.topics.queue;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.jms.MapMessage;
import javax.jms.Topic;

import com.cpd.topics.config.JmsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;


/**
 * @author mpop
 */
public class ConsoleMessagesThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ConsoleMessagesThread.class);

	private JmsTemplate jmsTemplate;

	private List<String> topicsOut;

	private String name;

	public ConsoleMessagesThread(JmsTemplate jmsTemplate, String[] topicsOut, String name) {
		this.jmsTemplate = jmsTemplate;
		this.topicsOut = Arrays.asList(topicsOut);
		this.name = name;
	}

	@Override
	public void run() {
		logger.info("Application will start taking input");

		while (true) {
			logger.info("Enter whatever you want to say");
			String stuff = new Scanner(System.in).nextLine();

			String[] tokens = stuff.split(":");

			logger.info("Received {}", stuff);

//			logger.info(tokens[0]);
//			logger.info(tokens[1]);
//			logger.info(topicsOut.toString());

			if (tokens.length == 2 && topicsOut.indexOf(tokens[0]) >= 0) {

				jmsTemplate.setPubSubNoLocal(true);
				jmsTemplate.send(tokens[0], session -> {
					MapMessage message = session.createMapMessage();

					message.setStringProperty(JmsConfiguration.MESSAGE_NAME_PROP, name);
					message.setStringProperty(JmsConfiguration.MESSAGE_BODY_PROP, tokens[1]);
					logger.info("Created message");
					return message;
				});

				logger.info("Message was sent to topic: " + tokens[0]);
			} else {
				logger.info("Message not sent");
			}
		}
	}

	public void stopTakingMessages() {
		logger.info("Application is no longer receiving input");
		this.stop();
	}

}
