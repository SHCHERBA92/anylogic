import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimesOfFly implements Parsing {

    private final ObjectMapper objectMapper;
    private final File file;
    private final TimeUtils timeUtils = new TimeUtils(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));

    public TimesOfFly(ObjectMapper objectMapper, File file) {
        this.objectMapper = objectMapper;
        this.file = file;
    }

    @Override
    public LocalDateTime average(String departureDatePath, String departureDateTime, String arriveDatePath, String arriveDateTime) {
        long toEpochSecondAverage = 0;
        LocalDateTime averageTime = null;
        try {

            JsonNode jsonNode = objectMapper.readTree(file);

            ArrayNode tickets = (ArrayNode)jsonNode.findValue("tickets");
            int size = tickets.size();
            for (JsonNode jNode :
                    tickets) {
                long calcTimeDepart = timeUtils.calcTime(jNode, departureDatePath, departureDateTime);
                long calcTimeArrive = timeUtils.calcTime(jNode, arriveDatePath, arriveDateTime);
                toEpochSecondAverage+=(calcTimeArrive - calcTimeDepart);
            }
            toEpochSecondAverage = toEpochSecondAverage/ size;
            LocalDateTime localDateTimedddd = LocalDateTime.ofInstant(Instant.ofEpochSecond(toEpochSecondAverage, 0), ZoneOffset.UTC);
            averageTime = localDateTimedddd.minusYears(1970).minusMonths(1).minusDays(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return averageTime;
    }
}
