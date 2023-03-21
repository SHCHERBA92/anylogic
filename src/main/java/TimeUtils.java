import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private DateTimeFormatter formatter;

    public TimeUtils(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public long calcTime(JsonNode jNode, String pathDate, String pathTime){
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
