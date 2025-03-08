package com.examples.streaming_platform;

import org.springframework.boot.SpringApplication;

public class TestStreamingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringbootStreamingPlatform::main).with(TestcontainersConfiguration.class).run(args);
	}

}
