package br.com.example.park_api;

import br.com.example.park_api.web.dto.PageableDto;
import br.com.example.park_api.web.dto.ParkingCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkings/parkings-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkings/parkings-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {
    @Autowired
    WebTestClient testClient;

    // checkIn --------------------------------------------
    @Test
    public void CreateCheckIn_ValidateData_ReturnCreatedAndLocation() {
        ParkingCreateDto createDto = ParkingCreateDto.builder()
                .licencePlate("WER-1111").manufacturer("FIAT").model("PALIO 1.0")
                .color("BLUE").clientCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("licencePlate").isEqualTo("WER-1111")
                .jsonPath("manufacturer").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO 1.0")
                .jsonPath("color").isEqualTo("BLUE")
                .jsonPath("clientCpf").isEqualTo("09191773016")
                .jsonPath("receipt").exists()
                .jsonPath("checkIn").exists()
                .jsonPath("parkingSpotCode").exists();
    }

    @Test
    public void CreateCheckIn_LikeClient_ReturnErrorWithStatus403() {
        ParkingCreateDto createDto = ParkingCreateDto.builder()
                .licencePlate("WER-1111").manufacturer("FIAT").model("PALIO 1.0")
                .color("BLUE").clientCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void CreateCheckIn_CpfNotExist_ReturnErrorWithStatus404() {
        ParkingCreateDto createDto = ParkingCreateDto.builder()
                .licencePlate("WER-1111").manufacturer("FIAT").model("PALIO 1.0")
                .color("BLUE").clientCpf("92844345050")
                .build();

        testClient
                .post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    @Sql(scripts = "/sql/parkings/parkings-insert-spots-occupied.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parkings/parkings-delete-spots-occupied.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void CreateCheckIn_FreeParkingSpotNotFound_ReturnErrorWithStatus404() {
        ParkingCreateDto createDto = ParkingCreateDto.builder()
                .licencePlate("WER-1111").manufacturer("FIAT").model("PALIO 1.0")
                .color("BLUE").clientCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void CreateCheckIn_InvalidData_ReturnErrorWithStatus422() {
        ParkingCreateDto createDto = ParkingCreateDto.builder()
                .licencePlate("").manufacturer("").model("")
                .color("").clientCpf("")
                .build();

        testClient
                .post()
                .uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    // find -----------------------------------------------
    @Test
    public void FindByReceipt_RoleAdmin_ReturnStatus200() {
        testClient
                .get()
                .uri("/api/v1/parking/check-in/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("licencePlate").isEqualTo("FIT-1020")
                .jsonPath("manufacturer").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("checkIn").isEqualTo("2025-03-13 10:15:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-01");

    }

    @Test
    public void FindByReceipt_RoleClient_ReturnStatus200() {
        testClient
                .get()
                .uri("/api/v1/parking/check-in/20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("licencePlate").isEqualTo("FIT-1020")
                .jsonPath("manufacturer").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("checkIn").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-01");

    }

    @Test
    public void FindByReceipt_ReceiptNotExist_ReturnErrorStatus404() {
        testClient
                .get()
                .uri("/api/v1/parking/check-in/20230313-999999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in/20230313-999999")
                .jsonPath("method").isEqualTo("GET");

    }

    // checkOut -------------------------------------------
    @Test
    public void CheckOut_SuccessfullyUpdate_ReturnStatus200() {
        testClient
                .put()
                .uri("/api/v1/parking/check-out/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("licencePlate").isEqualTo("FIT-1020")
                .jsonPath("manufacturer").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("98401203015")
                .jsonPath("receipt").isEqualTo("20230313-101300")
                .jsonPath("checkIn").isEqualTo("2025-03-13 10:15:00")
                .jsonPath("parkingSpotCode").isEqualTo("A-01")
                .jsonPath("checkOut").exists()
                .jsonPath("value").exists()
                .jsonPath("discount").exists();
    }

    @Test
    public void CheckOut_LikeClient_ReturnErrorStatus403() {
        testClient
                .put()
                .uri("/api/v1/parking/check-out/{receipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-out/20230313-101300")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void CheckOut_ReceiptNotExist_ReturnErrorStatus404() {
        testClient
                .put()
                .uri("/api/v1/parking/check-out/{receipt}", "20230313-000000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-out/20230313-000000")
                .jsonPath("method").isEqualTo("PUT");
    }

    // findAllParkingByCpf --------------------------------
    @Test
    public void FindAllParkingByCpf_SuccessfullyFounded_ReturnStatus200() {
        PageableDto responseBody = testClient
                .get()
                .uri("/api/v1/parking/cpf/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1); // Current Page Elements
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2); // 2 elements, 1 for page = 2 pages
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0); // Number of pages
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1); // Number of elements per page

        responseBody = testClient
                .get()
                .uri("/api/v1/parking/cpf/{cpf}?size=1&page=1", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1); // Current Page Elements
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2); // 2 elements, 1 for page = 2 pages
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1); // Number of pages
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1); // Number of elements per page
    }

    @Test
    public void FindAllParkingByCpf_RoleClient_ReturnErrorStatus403() {
        testClient
                .get()
                .uri("/api/v1/parking/cpf/{cpf}", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/cpf/98401203015")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void FindAllParkingByCpf_CpfNotExist_ReturnErrorStatus404() {
        testClient
                .get()
                .uri("/api/v1/parking/cpf/{cpf}", "95235416090")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/cpf/95235416090")
                .jsonPath("method").isEqualTo("GET");
    }
}
