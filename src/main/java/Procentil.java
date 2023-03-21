import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Procentil implements Parsing{

    private final ObjectMapper objectMapper;
    private final File file;
    private final TimeUtils timeUtils = new TimeUtils(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));

    public Procentil(ObjectMapper objectMapper, File file) {
        this.objectMapper = objectMapper;
        this.file = file;
    }

    @Override
    public LocalDateTime average(String departureDatePath, String departureDateTime, String arriveDatePath, String arriveDateTime) {
        JsonNode jsonNode = null;
        LocalDateTime localTime90Procentil = null;
        List<Long> FlyLongList = new ArrayList<>();
        try {
            jsonNode = objectMapper.readTree(file);
            ArrayNode tickets = (ArrayNode)jsonNode.findValue("tickets");
            int size = tickets.size();
            for (JsonNode jNode :
                    tickets) {
                long calcTimeDepart = timeUtils.calcTime(jNode, departureDatePath, departureDateTime);
                long calcTimeArrive = timeUtils.calcTime(jNode, arriveDatePath, arriveDateTime);
                FlyLongList.add(calcTimeArrive - calcTimeDepart);
            }
            Long maxEpohTimeFly = FlyLongList.stream().max(Long::compareTo).orElse(0l);
            Long maxEpohTimeFly90Procentil = maxEpohTimeFly - maxEpohTimeFly/10;
            LocalDateTime localDateTimedddd = LocalDateTime.ofInstant(Instant.ofEpochSecond(maxEpohTimeFly90Procentil, 0), ZoneOffset.UTC);
            localTime90Procentil = localDateTimedddd.minusYears(1970).minusMonths(1).minusDays(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return localTime90Procentil;
    }
}
