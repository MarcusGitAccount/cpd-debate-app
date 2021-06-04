package com.cpd.topics.config;


import com.cpd.topics.sockets.RightClient;
import com.cpd.topics.sockets.Server;
import com.cpd.topics.sockets.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


/**
 * @author mpop
 */
@Configuration
public class BeanConfiguration {

	@Value("${own}")
	private String own;

	@Value("${right}")
	private String right;

	@Bean("server")
	@DependsOn("tokenService")
	public Server server() {
		return new Server(own, tokenService());
	}

	@Bean
	@DependsOn("server")
	public RightClient rightClient() {
		return new RightClient(right);
	}

	@Bean
	public TokenService tokenService() {
		return new TokenService();
	}

}
