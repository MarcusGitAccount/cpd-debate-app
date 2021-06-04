package com.cpd.topics;

import java.util.Arrays;

import com.cpd.topics.sockets.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TopicsApplication implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(TopicsApplication.class);

	@Value("${topics.in}")
	private String[] topicsIn;

	@Value("${topics.out}")
	private String[] topicsOut;

	@Value("${name}")
	private String name;

	@Value("${left}")
	private String left;

	@Value("${right}")
	private String right;

	@Value("${own}")
	private String own;

	/**
	 * First application receives the token instantly
	 */
	@Value("${is-first}")
	private boolean isFist;

	@Autowired
	private TokenService tokenService;

	public static void main(String[] args) {
		SpringApplication.run(TopicsApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		logger.info("Name: " + name);
		logger.info("Own: " + own);
		logger.info("Left: " + left);
		logger.info("Right: " + right);
		logger.info("Topics subscribed to " + Arrays.stream(topicsIn).reduce("", (a, b) -> b + ' ' + a));
		logger.info("Topics publishing to: " + Arrays.stream(topicsOut).reduce("", (a, b) -> b + ' ' + a));
		logger.info("Is first? " + isFist);

//		logger.info(String.valueOf(tokenService));
		if (isFist) {
			tokenService.receiveToken();
		}
	}
}
