package com.muluofeng.easycode.web;

import com.muluofeng.easycode.core.annotation.EnableGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableGenerator
@SpringBootApplication
public class EasyCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyCodeApplication.class, args);
	}

}
