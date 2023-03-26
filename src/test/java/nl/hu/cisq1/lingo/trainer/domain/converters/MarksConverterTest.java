package nl.hu.cisq1.lingo.trainer.domain.converters;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.CORRECT;
import static org.junit.jupiter.api.Assertions.*;

class MarksConverterTest {
    @Test
    @DisplayName("test convert marks to string")
    void convertMarksToString() {
        MarksConverter marksConverter = new MarksConverter();

        String marks = marksConverter.convertToDatabaseColumn(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT));

        assertEquals("CORRECT,CORRECT,CORRECT,CORRECT,CORRECT", marks);
    }

    @Test
    @DisplayName("test convert string to marks")
    void convertStringToMarks() {
        MarksConverter marksConverter = new MarksConverter();

        List<Mark> marks = marksConverter.convertToEntityAttribute("CORRECT,CORRECT,CORRECT,CORRECT,CORRECT");

        assertEquals(List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT), marks);
    }

}