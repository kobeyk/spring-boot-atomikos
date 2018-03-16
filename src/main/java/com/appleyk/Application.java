package com.appleyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.appleyk.config.MasterConfig;
import com.appleyk.config.SlaveConfig;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration  @ComponentScan
@EnableConfigurationProperties(value = { MasterConfig.class, SlaveConfig.class })
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}