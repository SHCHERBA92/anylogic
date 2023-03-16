package parsing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimesOfFly implements Parsing{

    private final ObjectMapper objectMapper;
    private final File file;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

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
                long calcTimeDepart = calcTime(jNode, departureDatePath, departureDateTime);
                long calcTimeArrive = calcTime(jNode, arriveDatePath, arriveDateTime);
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

    private long calcTime(JsonNode jNode, String pathDate, String pathTime){
        String date = jNode.findValue(pathDate).asText();
        String time = jNode.findValue(pathTime).asText();
        if (time.length()<5){
            String[] strings = time.split(":");
            String hour = strings[0];
            String minute = strings[1];
            if (hour.length()<2)hour = "0"+hour;
            if (minute.length()<2) minute = "0"+minute;
            time = hour + ":" + minute;
        }
        return LocalDateTime.parse(date + " " + time, formatter)
                .toEpochSecond(ZoneOffset.UTC);
    }
}
