package test;


import http.KVServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static utils.Status.DONE;
import static utils.Status.NEW;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        TaskManager manager = Managers.getDefaultTaskManager();
        KVServer kvServer = new KVServer();
        kvServer.start();
      //  TaskManager manager1 = FileBackedTasksManager.loadFromFile(new File("test.csv"));

        Task newTask = new Task("Тест", "Описание",NEW,0);
        Task newTask1 = new Task("Тест", "Описание", NEW,0,LocalDateTime.of(2022,5,26,15,50,10), Duration.ofMinutes(30));

        Subtask newSubtask = new Subtask("Тест", "Описание", NEW,0, 0,LocalDateTime.of(2022,5,27,18,00,10), Duration.ofMinutes(15));
        Subtask newSubtask1 = new Subtask("Тест", "Описание", DONE,0,0,LocalDateTime.of(2022,5,27,18,50,10), Duration.ofMinutes(30));
        Subtask newSubtask2 = new Subtask("Тест", "Описание", NEW,0,0,LocalDateTime.of(2022,5,27,16,50,10), Duration.ofMinutes(50));

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
        subtaskList.add(newSubtask);
        subtaskList.add(newSubtask1);
        subtaskList1.add(newSubtask2);

        Epic newEpic = new Epic("Тест", "Описание", NEW, 0,subtaskList);
        Epic newEpic1 = new Epic("Тест", "Описание", NEW,0,subtaskList1);

       int id = manager.creatingTask(newTask);
        manager.creatingTask(newTask1);
        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);
        manager.creatingSubtask(newSubtask2);
        manager.creatingEpic(newEpic);
        manager.creatingEpic(newEpic1);

      //  newTask.setStatus(DONE);
        newTask1.setStatus(Status.IN_PROGRES);

        manager.getTask(0);
        manager.getTask(1);
        manager.getSubtask(2);
        manager.getSubtask(3);
        System.out.println(" ");
        manager.getSubtask(4);
        manager.getSubtask(2);
        manager.getEpic(5);
        manager.getEpic(6);
        manager.getSubtask(3);

        System.out.println(manager.getHistory());
        System.out.println(" ");
        System.out.println(manager.getPrioritizedTasks());
     //   System.out.println(manager1.getHistory());
        //Сравнение сохраненного с восстановленным
     //   System.out.println(manager.getTasks().equals(manager1.getTasks()));
     //   System.out.println(manager.getSubtasks().equals(manager1.getSubtasks()));
     //   System.out.println(manager.getEpics().equals(manager1.getEpics()));
     //   System.out.println(manager.getHistory().equals(manager1.getHistory()));
    }
}
