package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Status.NEW;

class InMemoryHistoryManagerTest{

    private  static HistoryManager historyTasks;
    private static Task newTask;
    private static Subtask newSubtask1;
    private static Epic newEpic;

    @BeforeEach
    void initInMemory() {
        historyTasks = new InMemoryHistoryManager();
        newTask = new Task("Тест", "Описание",NEW,1, LocalDateTime.now(), Duration.ofMinutes(15));
        newSubtask1 = new Subtask("Тест", "Описание", NEW,1, 3,LocalDateTime.of(2022,5,26,16,0,10), Duration.ofMinutes(15));
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(newSubtask1);
        newEpic = new Epic("Тест", "Описание", NEW, 3,subtaskList);


    }

    @Test
    void getHistory() {
        historyTasks.add(newTask);
        historyTasks.add(newEpic);
        final List<Task> history = historyTasks.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
    }

    @Test
    void add() {

        historyTasks.add(newTask);
        final List<Task> tasks = historyTasks.getHistory();
        assertNotNull(historyTasks);
        assertEquals(1,tasks.size());
        assertEquals(newTask,tasks.get(0));
        historyTasks.add(newSubtask1);
        assertFalse(tasks.contains(newSubtask1));

    }

    @Test
    void remove() {
        historyTasks.add(newTask);
        historyTasks.add(newEpic);
        historyTasks.remove(1);
        List<Task> list =historyTasks.getHistory();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(newEpic,list.get(0));
    }
}