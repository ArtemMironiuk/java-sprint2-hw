package tasksOfDifferentTypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Status.NEW;

class EpicTest {

    private static Epic epic;
    private static Subtask subtask;
    private static Subtask subtask2;
    private static Subtask subtask3;

    @BeforeEach
    public void beforeEach () {
        subtask = new Subtask("Тест", "Описание", NEW,1, 2, LocalDateTime.of(2022,5,26,16,0,10), Duration.ofMinutes(15));
        subtask2 = new Subtask("Тест", "Описание", NEW,3, 2, LocalDateTime.of(2022,5,26,15,0,10), Duration.ofMinutes(15));
        subtask3 = new Subtask("Тест", "Описание", NEW,4, 2, LocalDateTime.of(2022,5,27,15,0,10), Duration.ofMinutes(15));
        ArrayList<Subtask> list = new ArrayList<>();
        list.add(subtask);
        list.add(subtask2);
        list.add(subtask3);
        epic =new Epic("Тест", "Описание", NEW,2,list);

    }
    @Test
    void getStartTime(){
        final LocalDateTime start = epic.getStartTime();

        assertNotNull(start);
        assertEquals(subtask2.getStartTime(), start);
    }

    @Test
    void getDuration() {
        final Duration duration = epic.getDuration();
        Duration sabDuration = subtask.getDuration().plus(subtask2.getDuration()).plus(subtask3.getDuration());

        assertNotNull(duration);
        assertEquals(sabDuration, duration);
    }

    @Test
    void getEndTime(){
        final LocalDateTime end = epic.getEndTime();

        assertNotNull(end);
        assertEquals(subtask3.getEndTime(), end);
    }

}