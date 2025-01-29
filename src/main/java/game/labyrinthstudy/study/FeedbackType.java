package game.labyrinthstudy.study;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FeedbackType {

    EVALUATIVE_POSITIVE(true, false),
    COMPARATIVE_POSITIVE(true, false),
    DESCRIPTIVE_POSITIVE(true, false),
    EVALUATIVE_NEGATIVE(false, true),
    COMPARATIVE_NEGATIVE(false, true),
    DESCRIPTIVE_NEGATIVE(false, true);

    private final boolean positive, negative;

    FeedbackType(boolean positive, boolean negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public String fileName() {
        return this.name().toLowerCase()+".txt";
    }
    public static List<FeedbackType> filterFeedbackTypes(boolean positive, boolean negative) {
        return Arrays.stream(values()).filter(t -> t.positive == positive && t.negative == negative).collect(Collectors.toList());
    }

}
