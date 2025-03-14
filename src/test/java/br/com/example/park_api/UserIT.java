package br.com.example.park_api;

import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Tomcat em ambiente de testes do spring
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {
    @Autowired
    WebTestClient testClient;

    // Como colocar nome nos métodos de teste
    //<motivo do teste> _ <o que vai ser testado> _ <o que será retornado>

    @Test
    public void createUser_UsernameAndPasswordValidate_ReturnCreatedUserWithStatus201(){
        UserResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@gmail.com", "123456"))
                .exchange() // response
                .expectStatus().isCreated() // status code 201
                .expectBody(UserResponseDto.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        // Verifica o retorno da requisição
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

}
