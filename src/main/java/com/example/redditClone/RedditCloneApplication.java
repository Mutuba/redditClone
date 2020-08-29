package com.example.redditClone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication

@EntityScan(basePackageClasses = {
		RedditCloneApplication.class,
		Jsr310JpaConverters.class
})
@EnableAsync
public class RedditCloneApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	public static void main(String[] args) {
		SpringApplication.run(RedditCloneApplication.class, args);
	}

}
