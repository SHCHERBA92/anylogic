import com.fasterxml.jackson.databind.ObjectMapper;
import parsing.TimesOfFly;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        TimesOfFly timesOfFly = new TimesOfFly(new ObjectMapper(), new File("src/main/resources/tickets.json"));
        LocalDateTime average = timesOfFly.average("departure_date", "departure_time", "arrival_date", "arrival_time");
        System.out.println("Среднее время полёта составит = " + average.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
