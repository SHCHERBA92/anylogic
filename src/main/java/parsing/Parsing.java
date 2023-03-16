package parsing;

import java.time.LocalDateTime;

public interface Parsing {
    LocalDateTime average(String departureDatePath, String departureDateTime, String arriveDatePath, String arriveDateTime);
}
