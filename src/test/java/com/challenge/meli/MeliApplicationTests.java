package com.challenge.meli;

import com.challenge.meli.controller.MutantController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MeliApplicationTests {

	@Autowired
	private MutantController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
