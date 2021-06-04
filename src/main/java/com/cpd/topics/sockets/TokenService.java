package com.cpd.topics.sockets;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cpd.topics.queue.ConsoleMessagesThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;


/**
 * @author mpop
 */
public class TokenService {

	private static Logger logger = LoggerFactory.getLogger(TokenService.class);

	private final Object lock = new Object();

	private boolean token;

	public TokenService() {
		this.token = false;
	}

	private ConsoleMessagesThread consoleMessagesThread;

	@Autowired
	private RightClient rightClient;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${topics.out}")
	private String[] topicsOut;

	@Value("${name}")
	private String name;

	public void receiveToken() {
		synchronized (lock) {
			logger.info("Received token at {}", new Date());

			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					giveItAwayLoser();
				}
			}, 15 * 1000L);

			this.token = true;
			this.consoleMessagesThread = new ConsoleMessagesThread(jmsTemplate, topicsOut, name);
			this.consoleMessagesThread.start();
		}
	}

	public void giveItAwayLoser() {
		synchronized (lock) {
			logger.info("Parted ways with our token :( at {}", new Date());
			this.token = false;
			this.consoleMessagesThread.stopTakingMessages();
			this.rightClient.sendToken();
			this.consoleMessagesThread = null;
		}
	}

	public boolean doWeHaveIt() {
		synchronized (lock) {
			return this.token;
		}
	}

}
