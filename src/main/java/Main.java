import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        TimesOfFly timesOfFly = new TimesOfFly(new ObjectMapper(), new File("src/main/resources/tickets.json"));
        Procentil procentil = new Procentil(new ObjectMapper(),new File("src/main/resources/tickets.json"));

        LocalDateTime average = timesOfFly.average("departure_date", "departure_time", "arrival_date", "arrival_time");
        System.out.println("Среднее время полёта составит = " + average.format(DateTimeFormatter.ofPattern("HH:mm")));
        LocalDateTime localProcentil = procentil.average("departure_date", "departure_time", "arrival_date", "arrival_time");
        System.out.println("90 процентиль время полёта составит = " + localProcentil.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
