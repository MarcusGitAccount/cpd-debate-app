package com.cpd.topics.sockets;


import java.util.concurrent.Semaphore;


/**
 * @author mpop
 */
public class Locks {

	public static final Semaphore barrier = new Semaphore(0);
}
