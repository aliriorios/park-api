package br.com.example.park_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Tomcat em ambiente de testes do spring
public class UserIT {
    @Autowired
    WebTestClient testClient;
}
