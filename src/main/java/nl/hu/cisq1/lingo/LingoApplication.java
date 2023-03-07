package nl.hu.cisq1.lingo;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class LingoApplication {
//    Test de cache
    public static void main(String[] args) {
//        SpringApplication.run(LingoApplication.class, args);
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        System.out.println(feedback.hashCode());
    }
}
