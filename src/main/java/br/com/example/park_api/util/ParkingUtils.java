package br.com.example.park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {
    // 2025-03-31T15:23:48.616463500    -> LocalDateTime pattern
    // 20250331-152348                  -> Receipt pattern
    public static String generateReceipt() {
        LocalDateTime date = LocalDateTime.now();

        String receipt = date.toString().substring(0, 19);
        return receipt
                .replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }
}
