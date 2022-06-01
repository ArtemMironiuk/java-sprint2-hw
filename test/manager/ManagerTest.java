package manager;

import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.Status.*;

abstract class ManagerTest <T extends TaskManager>{

    protected T taskManager;
    Task newTask;
    Subtask newSubtask1;
    Subtask newSubtask2;
    Subtask newSubtask3;
    Subtask newSubtask4;
    Epic newEpic;
    Epic newEpic2;
    Epic newEpic3;
    int idTask;
    int idSubtask1;
    int idSubtask2;
    int idSubtask3;
    int idSubtask4;
    int idEpic;

    void init(){
        newTask = new Task("Тест", "Описание",NEW,0, LocalDateTime.now(), Duration.ofMinutes(15));
        idTask = taskManager.creatingTask(newTask);

        newSubtask1 = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,26,16,0,10), Duration.ofMinutes(15));
        newSubtask2 = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,5,27,18,50,10), Duration.ofMinutes(30));
        idSubtask1 = taskManager.creatingSubtask(newSubtask1);
        idSubtask2 = taskManager.creatingSubtask(newSubtask2);

//        newSubtask3 = new Subtask("Тест", "Описание", NEW,0, 6,LocalDateTime.of(2022,5,28,16,0,10), Duration.ofMinutes(15));
//        newSubtask4 = new Subtask("Тест", "Описание", NEW,0,6,LocalDateTime.of(2022,5,29,18,50,10), Duration.ofMinutes(30));
//        idSubtask3 = taskManager.creatingSubtask(newSubtask1);
//        idSubtask4 = taskManager.creatingSubtask(newSubtask2);

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(newSubtask1);
        subtaskList.add(newSubtask2);

//        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
//        subtaskList1.add(newSubtask3);
//        subtaskList1.add(newSubtask4);

        newEpic = new Epic("Тест", "Описание", NEW, 0,subtaskList);
        newEpic2 = new Epic("Тест", "Описание", NEW, 0);
       // newEpic3 = new Epic("Тест", "Описание", NEW, 0,subtaskList1);

        idEpic = taskManager.creatingEpic(newEpic);
     //   taskManager.creatingEpic(newEpic3);

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
        taskManager.deleteTasks();
        final List<Task> tasks = taskManager.getTasks();
        final List<Task> tasksHistory = taskManager.getHistory();
        for (Task task : tasks) {
            assertTrue(tasksHistory.contains(task));
        }
        assertEquals(0,tasks.size(),"Лист пуст");
    }

    @Test
    void getTask() {
        Task task = taskManager.getTask(idTask);
        List<Task> tasksHistory = taskManager.getHistory();
        assertNotNull(task);
        assertNotNull(tasksHistory);
        assertTrue(tasksHistory.contains(task));
        assertEquals(idTask,task.getId());

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
        final Task task = taskManager.getTask(idTask);
        final List<Task> tasksHistory = taskManager.getHistory();
        assertFalse(tasksHistory.contains(task));
        assertEquals(taskManager.getHistory().size(), tasksHistory.size());
        assertNull(taskManager.getTask(idTask));
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
        taskManager.deleteSubtasks();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        final List<Task> subtasksHistory = taskManager.getHistory();
        for (Subtask subtask : subtasks) {
            assertFalse(subtasksHistory.contains(subtask));
        }
        assertEquals(0,subtasks.size(),"Лист пуст");

        final List<Epic> epics = taskManager.getEpics();
        for (Epic epic : epics) {
            assertEquals(0,epic.getSubtask().size());
            assertEquals(NEW,epic.getStatus());
        }
    }

    @Test
    void getSubtask() {
        Subtask subtask = taskManager.getSubtask(idSubtask1);
        List<Task> subtaskHistory = taskManager.getHistory();
        assertNotNull(subtask);
        assertNotNull(subtaskHistory);
        assertTrue(subtaskHistory.contains(subtask));
        assertEquals(idSubtask1,subtask.getId());
        assertEquals(subtask, newSubtask1);
    }

    @Test
    void creatingSubtask() {
        Epic epic = taskManager.getEpic(newSubtask1.idEpic);
        assertNotNull(taskManager.getSubtasks());
        assertEquals(newSubtask1,taskManager.getSubtask(idSubtask1));
        assertEquals(2, taskManager.getSubtasks().size());
        assertEquals(IN_PROGRES,epic.getStatus());
    }

    @Test
    void updateSubtask() {
        Subtask newSubtask3 = new Subtask("Тест", "Описание", NEW, 2, 4,LocalDateTime.of
                (2022, 5, 23, 16, 0, 10), Duration.ofMinutes(15));
        taskManager.updateSubtask(newSubtask3);
        assertEquals(newSubtask1.getId(),newSubtask3.getId());
        assertEquals(taskManager.getSubtask(2),newSubtask3);
        Epic epic = taskManager.getEpic(newSubtask3.idEpic);
        assertEquals(IN_PROGRES, epic.getStatus());
    }

    @Test
    void deleteSubtaskId() {
        taskManager.deleteSubtaskId(idSubtask1);
        final Subtask subtask = taskManager.getSubtask(idSubtask1);
        final List<Task> subtasksHistory = taskManager.getHistory();
        assertFalse(subtasksHistory.contains(subtask));
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
        taskManager.deleteEpics();
        final List<Epic> epics = taskManager.getEpics();
        final List<Task> epicsHistory = taskManager.getHistory();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        for (Epic epic: epics) {
            assertFalse(epicsHistory.contains(epic));
        }

        assertNull(epics,"Лист пуст");
        assertNull(subtasks);

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
        int idEpic2 = taskManager.creatingEpic(newEpic2);
        assertEquals(NEW, newEpic2.getStatus());
        assertEquals(5,idEpic2);
        assertNotNull(taskManager.getEpics());
        assertEquals(newEpic,taskManager.getEpic(idEpic));
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(newEpic.getId(),newSubtask1.getEpicId());

        //написать для статуса
    }

    @Test
    void updateEpic() {
        Subtask subtask = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,5,31,18,50,10), Duration.ofMinutes(30));
        ArrayList<Subtask> listSub = new ArrayList<>();
        listSub.add(subtask);
        Epic newEpic1 = new Epic("Тест", "Описание", NEW, 4,listSub);
        taskManager.updateEpic(newEpic1);

        assertEquals(newEpic.getId(),newEpic1.getId());
        assertEquals(newEpic1.getId(),newSubtask1.getEpicId());
        assertEquals(DONE, taskManager.getEpic(4).getStatus());
    }

    @Test
    void deleteEpicId() {
        taskManager.deleteEpicId(idEpic);
        final Epic epic = taskManager.getEpic(idEpic);
        final List<Task> epicsHistory = taskManager.getHistory();

        for (Subtask subtask : epic.getSubtask()) {
            assertNull(subtask);
        }
        assertNull(taskManager.getEpic(idEpic));
        assertFalse(epicsHistory.contains(epic));
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
        final List<Task> tasksHistory = taskManager.getHistory();
        assertNotNull(tasksHistory);
        assertEquals(3,tasksHistory.size());
        assertTrue(tasksHistory.contains(newTask));

    }

    @Test
    void epicWithNewSubtask(){
        Subtask subtask = new Subtask("Тест", "Описание", NEW,0,7,LocalDateTime.of(2022,6,1,18,50,10), Duration.ofMinutes(30));
        ArrayList<Subtask> listSub = new ArrayList<>();
        listSub.add(subtask);
        Epic newEpic1 = new Epic("Тест", "Описание", IN_PROGRES, 7,listSub);
        taskManager.creatingEpic(newEpic1);
        assertEquals(NEW, newEpic1.getStatus());

    }
    @Test
    void getPrioritizedTasks() {
        Task newTask1 = new Task("Тест", "Описание",NEW,0);
        Task newTask2 = new Task("Тест", "Описание",NEW,0);
        Task newTask3 = new Task("Тест", "Описание",NEW,0, LocalDateTime.now(), Duration.ofMinutes(15));

        taskManager.creatingTask(newTask1);
        taskManager.creatingTask(newTask2);
        taskManager.creatingTask(newTask3);


        assertNotNull(taskManager.getTasks());
        assertEquals(newTask1, taskManager.getTasks().get(1));
        assertEquals(newTask2, taskManager.getTasks().get(2));

    }
}