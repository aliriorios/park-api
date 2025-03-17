package br.com.example.park_api;

import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import br.com.example.park_api.web.exception.ErrorMessage;
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

    @Test // Successfully test
    public void createUser_UsernameAndPasswordValidate_ReturnCreatedUserWithStatus201(){
        UserResponseDto responseBody = testClient
                .post() // http method
                .uri("/api/v1/users/save") // method uri
                .contentType(MediaType.APPLICATION_JSON) // response archive type
                .bodyValue(new UserCreateDto("tody@gmail.com", "123456")) // request body
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

    @Test // Username conflict test
    public void createUser_UsernameDuplicateConflict_ReturnErrorMessageStatus409(){
        // The username in this test already exist in test/resources/sql/users/users-insert.sql

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test // Username validation test
    public void createUser_InvalidUsername_ReturnErrorMessageStatus422(){
        // Empty username
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("", "123456"))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Invalid username format
        responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@", "123456"))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Invalid username format
        responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@gmail", "123456"))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test // Password validation test
    public void createUser_InvalidPassword_ReturnErrorMessageStatus422(){
        // Empty password
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@gmail.com", ""))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Password less than 6
        responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@gmail.com", "123"))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Password greater than 6
        responseBody = testClient
                .post()
                .uri("/api/v1/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@gmail.com", "123456789"))
                .exchange() // response
                .expectStatus().isEqualTo(422) // status code 422
                .expectBody(ErrorMessage.class) // tipo do objeto retornado
                .returnResult().getResponseBody(); // retorna um objeto do tipo UserResponseDto

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
