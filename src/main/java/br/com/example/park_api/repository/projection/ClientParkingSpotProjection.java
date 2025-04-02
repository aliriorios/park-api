package br.com.example.park_api.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientParkingSpotProjection {
    String getReceipt();
    String getLicencePlate();
    String getManufacturer();
    String getModel();
    String getColor();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") // Format or LocalDateTime
    LocalDateTime getCheckIn();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getCheckOut();
    BigDecimal getValue();
    BigDecimal getDiscount();

    // Client info ----------------------------------------
    String getClientCpf();

    // Parking Spot info ----------------------------------
    String getParkingSpotCode();
}
