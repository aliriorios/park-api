package br.com.example.park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {
    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15MINUTES = 1.75;
    private static final double PERCENTAGE_DISCOUNT = 0.30;

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

    public static BigDecimal calculateCost(LocalDateTime checkIn, LocalDateTime checkOut) {
        long minutes = checkIn.until(checkOut, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = FIRST_15_MINUTES;

        } else if (minutes <= 60) {
            total = FIRST_60_MINUTES;

        } else {
            long extrasMinutes = minutes - 60;
            int extraTracks = (int) Math.ceil(extrasMinutes / 15.0);
            total = FIRST_60_MINUTES + (ADDITIONAL_15MINUTES * extraTracks);

        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal cost, long count) {
        if ((count > 0) && (count % 10 == 0)) {
            return cost.multiply(new BigDecimal(PERCENTAGE_DISCOUNT)).setScale(2, RoundingMode.HALF_EVEN);
        }

        return BigDecimal.ZERO;
    }
}
