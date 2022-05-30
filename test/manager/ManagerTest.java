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
import static utils.Status.DONE;
import static utils.Status.NEW;

abstract class ManagerTest <T extends TaskManager>{
    protected T taskManager;
    Task newTask;
    Subtask newSubtask1;
    Subtask newSubtask2;
    Epic newEpic;
    int idTask;

    void init(){
        newTask = new Task("Тест", "Описание",NEW,0, LocalDateTime.now(), Duration.ofMinutes(15));

        newSubtask1 = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,26,16,0,10), Duration.ofMinutes(15));
        newSubtask2 = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,5,27,18,50,10), Duration.ofMinutes(30));

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(newSubtask1);
        subtaskList.add(newSubtask2);

        newEpic = new Epic("Тест", "Описание", NEW, 0,subtaskList);

        idTask = taskManager.creatingTask(newTask);
        taskManager.creatingTask(newSubtask1);
        taskManager.creatingTask(newSubtask2);
        taskManager.creatingTask(newEpic);

    }

    @Test
    void getTasks() {
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size(),"Одна задача");
        assertEquals(0, newTask.getId());
        assertEquals(newTask, tasks.get(0));
    }

    @Test
    void deleteTasks() {
        final List<Task> tasks = taskManager.getTasks();
        taskManager.deleteTasks();
        assertNull(tasks,"Лист пуст");
    }

    @Test
    void getTask() {
        Task task = taskManager.getTask(idTask);
        assertNotNull(task);


    }

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