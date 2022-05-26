package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Status.NEW;

abstract class ManagerTest <T extends TaskManager>{
    protected T taskManager;
    Task newTask;

    void init(){
        newTask = new Task("Тест", "Описание",NEW,0, LocalDateTime.now(),15);
        taskManager.creatingTask(newTask);

    }

    @Test
    void getTasks() {
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size(),"Одна задача");
        assertEquals(0, newTask.getId());
        assertEquals(newTask, tasks.get(0));
    }

//    @Test
//    void deleteTasks() {
//    }
//
//    @Test
//    void getTask() {
//    }
//
//    @Test
//    void creatingTask() {
//    }
//
//    @Test
//    void updateTask() {
//    }
//
//    @Test
//    void deleteTaskId() {
//    }
//
//    @Test
//    void getSubtasks() {
//    }
//
//    @Test
//    void deleteSubtasks() {
//    }
//
//    @Test
//    void getSubtask() {
//    }
//
//    @Test
//    void creatingSubtask() {
//    }
//
//    @Test
//    void updateSubtask() {
//    }
//
//    @Test
//    void deleteSubtaskId() {
//    }
//
//    @Test
//    void getEpics() {
//    }
//
//    @Test
//    void deleteEpics() {
//    }
//
//    @Test
//    void getEpic() {
//    }
//
//    @Test
//    void creatingEpic() {
//    }
//
//    @Test
//    void updateEpic() {
//    }
//
//    @Test
//    void deleteEpicId() {
//    }
//
//    @Test
//    void getSubtasksEpic() {
//    }
//
//    @Test
//    void getHistory() {
//    }
}