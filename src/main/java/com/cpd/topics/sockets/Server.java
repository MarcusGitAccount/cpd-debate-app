package com.cpd.topics.sockets;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author mpop
 */
public class Server {

	private static Logger logger = LoggerFactory.getLogger(TokenService.class);

	private ServerSocket serverSocket;

	private String own;

	private TokenService tokenService;

	public Server(String own, TokenService tokenService) {
		this.own = own;
		this.tokenService = tokenService;

		try {
			this.serverSocket = new ServerSocket(Integer.parseInt(own));
		} catch (IOException e) {
			logger.error(e.toString());
		}

		logger.info("Created socket server {}", tokenService);
		new ServerThread(serverSocket, tokenService).start();
//		Locks.barrier.release();
	}
}
