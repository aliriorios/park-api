package br.com.example.park_api.exception;

public class ParkingSpotCodeUniqueViolationException extends RuntimeException {
    public ParkingSpotCodeUniqueViolationException(String message) {
        super(message);
    }
}
