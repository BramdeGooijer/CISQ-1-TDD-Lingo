package nl.hu.cisq1.lingo.trainer.domain.converters;

import jakarta.persistence.AttributeConverter;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MarksConverter implements AttributeConverter<List<Mark>, String> {
    @Override
    public String convertToDatabaseColumn(List<Mark> marks) {
        return marks.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Mark> convertToEntityAttribute(String s) {
        return Arrays.stream(s.split(","))
                .map(Mark::valueOf)
                .collect(Collectors.toList());
    }
}
