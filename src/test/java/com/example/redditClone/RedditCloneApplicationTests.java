package com.example.redditClone;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RedditCloneApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	// Test class added ONLY to cover main() invocation not covered by application tests.
	public void main() {
		RedditCloneApplication.main(new String[] {});
	}

}
