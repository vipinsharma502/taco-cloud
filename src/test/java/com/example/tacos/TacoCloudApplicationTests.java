package com.example.tacos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/*
 @SpringBootTest tells JUnit to bootstrap the test with spring boot capabilities
*/

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TacoCloudApplicationTests {

	@Test
	void contextLoads() {
	}

}
