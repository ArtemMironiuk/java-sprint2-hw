package manager;

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
    int idSubtask1;
    int idSubtask2;
    int idEpic;

    void init(){
        newTask = new Task("Тест", "Описание",NEW,0, LocalDateTime.now(), Duration.ofMinutes(15));
        idTask = taskManager.creatingTask(newTask);

        newSubtask1 = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,26,16,0,10), Duration.ofMinutes(15));
        newSubtask2 = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,5,27,18,50,10), Duration.ofMinutes(30));
        idSubtask1 = taskManager.creatingSubtask(newSubtask1);
        idSubtask2 = taskManager.creatingSubtask(newSubtask2);

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(newSubtask1);
        subtaskList.add(newSubtask2);

        newEpic = new Epic("Тест", "Описание", NEW, 0,subtaskList);
        idEpic = taskManager.creatingEpic(newEpic);

        taskManager.getTask(idTask);
        taskManager.getSubtask(idSubtask1);
        taskManager.getEpic(idEpic);
    }

    @Test
    void getTasks() {
        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(),"Неверное количество задач");
        assertEquals(1, newTask.getId());
        assertEquals(newTask, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void deleteTasks() {
        final List<Task> tasks = taskManager.getTasks();

        taskManager.deleteTasks();
        assertNull(tasks,"Лист пуст");
        //дописать для истории
    }

    @Test
    void getTask() {
        Task task = taskManager.getTask(idTask);
        assertNotNull(task);
        assertEquals(idTask,task.getId());
        //дописать тест для истории
    }

    @Test
    void creatingTask() {
        assertNotNull(taskManager.getTasks());
        assertEquals(newTask,taskManager.getTask(idTask));
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void updateTask() {
        Task newTask1 = new Task("Тест", "Описание", NEW, 1, LocalDateTime.of(2022, 5,
                29, 16, 0, 10), Duration.ofMinutes(15));
        taskManager.updateTask(newTask1);
        assertEquals(newTask.getId(),newTask1.getId());
        assertEquals(newTask1,taskManager.getTask(1));

    }

    @Test
    void deleteTaskId() {
        taskManager.deleteTaskId(idTask);
        assertNull(taskManager.getTask(idTask));
        //дописать тест для истории
    }

    @Test
    void getSubtasks() {
        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size(),"Две задачи");
        assertEquals(idSubtask1, newSubtask1.getId());
        assertEquals(idSubtask2, newSubtask2.getId());
        assertEquals(newSubtask1, subtasks.get(0));
        assertEquals(newSubtask2, subtasks.get(1));
    }

    @Test
    void deleteSubtasks() {
        final List<Subtask> subtasks = taskManager.getSubtasks();
        taskManager.deleteSubtasks();
        assertNull(subtasks,"Лист пуст");
        //дописать для истории, для рассчета статуса и для удаление из эпика
    }

    @Test
    void getSubtask() {
        Subtask subtask = taskManager.getSubtask(idSubtask1);
        assertNotNull(subtask);
        assertEquals(idSubtask1,subtask.getId());
        assertEquals(subtask, newSubtask1);
        //дописать тест для истории
    }

    @Test
    void creatingSubtask() {
        assertNotNull(taskManager.getSubtasks());
        assertEquals(newSubtask1,taskManager.getSubtask(idSubtask1));
        assertEquals(2, taskManager.getSubtasks().size());
        //дописать для статуса
    }

    @Test
    void updateSubtask() {
        Subtask newSubtask3 = new Subtask("Тест", "Описание", NEW, 2, 0,LocalDateTime.of
                (2022, 5, 29, 16, 0, 10), Duration.ofMinutes(15));
        taskManager.updateSubtask(newSubtask3);
        assertEquals(newSubtask1.getId(),newSubtask3.getId());
        assertEquals(taskManager.getSubtask(2),newSubtask3);
        //написать для статуса
    }

    @Test
    void deleteSubtaskId() {
        taskManager.deleteSubtaskId(idSubtask1);
        assertNull(taskManager.getSubtask(idSubtask1));
        //написать для статуса и для истории
    }

    @Test
    void getEpics() {
        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertEquals(1, epics.size(),"Одна задача");
        assertEquals(4, newEpic.getId());
        assertEquals(newEpic, epics.get(0));
    }

    @Test
    void deleteEpics() {
        final List<Epic> epics = taskManager.getEpics();
        taskManager.deleteEpics();
        assertNull(epics,"Лист пуст");
        assertNull(taskManager.getSubtasks(), "Лист сабтасков пуст");
        //дописать для итории
    }

    @Test
    void getEpic() {
        Epic epic = taskManager.getEpic(idEpic);
        assertNotNull(epic);
        assertEquals(idEpic, epic.getId());
        assertEquals(newEpic, epic);
        //дописать для истории
    }

    @Test
    void creatingEpic() {
        assertNotNull(taskManager.getEpics());
        assertEquals(newEpic,taskManager.getEpic(idEpic));
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(newEpic.getId(),newSubtask1.getEpicId());
        //написать для статуса
    }

    @Test
    void updateEpic() {
        Epic newEpic1 = new Epic("Тест", "Описание", NEW, 4);
        taskManager.updateEpic(newEpic1);
        assertEquals(newEpic.getId(),newEpic1.getId());
        assertEquals(taskManager.getEpic(4),newEpic1);
        assertEquals(newEpic1.getId(),newSubtask1.getEpicId());
        //написать для статуса
    }

    @Test
    void deleteEpicId() {
        taskManager.deleteEpicId(idEpic);
        assertNull(taskManager.getEpic(idEpic));
        assertNull(taskManager.getSubtasks());
        //дописать для истории

    }

    @Test
    void getSubtasksEpic() {
        ArrayList<Subtask> subtasks = taskManager.getSubtasksEpic(newEpic);
        assertNotNull(subtasks);
        assertEquals(2,subtasks.size());
        assertEquals(newSubtask1, subtasks.get(0));
        assertEquals(newSubtask2, subtasks.get(1));
    }

    @Test
    void getHistory() {

    }
}