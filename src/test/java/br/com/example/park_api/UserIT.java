package br.com.example.park_api;

import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import br.com.example.park_api.web.dto.UserUpdatePasswordDto;
import br.com.example.park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@ActiveProfiles("test")
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
                .contentType(MediaType.APPLICATION_JSON) // request archive type
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
    public void createUser_UsernameDuplicateConflict_ReturnErrorMessageWithStatus409(){
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
    public void createUser_InvalidUsername_ReturnErrorMessageWithStatus422(){
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
    public void createUser_InvalidPassword_ReturnErrorMessageWithStatus422(){
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

    @Test // Successfully test
    public void findUserById_IdValidate_ReturnFindUserWithStatus200() {
        UserResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/users/100") // id to find
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testClient
                .get()
                .uri("/api/v1/users/101") // id to find
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");

        responseBody = testClient
                .get()
                .uri("/api/v1/users/101") // id to find
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test // User not found; user not exist
    public void findUserById_ResourceForbiddenClientSearchOtherClient_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/102") // id to find
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test // User not found; user not exist
    public void findUserById_IdNotExist_ReturnErrorMessageWithStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/200") // id to find
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test // Find all users (list)
    public void findAll_ListWithAllUsers_ReturnAllUsersListWithStatus200() {
        List<UserResponseDto> responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody)
                .isNotNull()
                .isNotEmpty()
                .allSatisfy(user -> org.assertj.core.api.Assertions.assertThat(user.getId()).isNotNull())
                .hasSize(13);
    }

    @Test // Find all users when
    public void findAll_ForbiddenResourceToClient_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test // Successfully test
    public void updatePassword_ValidData_ReturnStatus204(){
        testClient.patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();

        testClient.patch()
                .uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test // Password unauthorized
    public void updatePassword_WrongPassword_ReturnErrorMessageWithStatus400(){
        // Unmatched password confirmation
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456", "123456", "000000"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        // Unmatched current password
        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("000000", "123456", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test // Update password resource forbidden
    public void updatePassword_UsersDifferent_ReturnErrorMessageWithStatus403(){
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = testClient
                .patch()
                .uri("/api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test // Password validation test
    public void updatePassword_InvalidPasswordInputs_ReturnErrorMessageWithStatus422(){
        // Empty password
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Password less then 6
        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        // Password greater 6
        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdatePasswordDto("123456789", "123456789", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
