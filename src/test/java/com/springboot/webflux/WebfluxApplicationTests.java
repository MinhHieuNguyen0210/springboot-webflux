package com.springboot.webflux;

import com.springboot.webflux.dto.SaveOrUpdateUserDto;
import com.springboot.webflux.repository.UserRepository;
import com.springboot.webflux.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class WebfluxApplicationTests {

	@Test
	void contextLoads() {
	}

}
