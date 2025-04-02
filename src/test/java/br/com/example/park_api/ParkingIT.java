package br.com.example.park_api;

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
}
