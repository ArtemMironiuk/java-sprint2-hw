package test;

import manager.Manager;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("Тест", "Описание", Status.NEW,0);
        Task newTask1 = new Task("Тест", "Описание", Status.NEW,0);

        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW,0, 6);
        Subtask newSubtask1 = new Subtask("Тест", "Описание", Status.DONE,0,6);
        Subtask newSubtask2 = new Subtask("Тест", "Описание", Status.NEW,0,7);

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
        subtaskList.add(newSubtask);
        subtaskList.add(newSubtask1);
        subtaskList1.add(newSubtask2);

        Epic newEpic = new Epic("Тест", "Описание",Status.NEW, 0,subtaskList);
        Epic newEpic1 = new Epic("Тест", "Описание",Status.NEW,0,subtaskList1);

        manager.creatingTask(newTask);
        manager.creatingTask(newTask1);
        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);
        manager.creatingSubtask(newSubtask2);
        manager.creatingEpic(newEpic);
        manager.creatingEpic(newEpic1);

        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        newTask.setStatus(Status.DONE);
        newTask1.setStatus(Status.IN_PROGRES);

        System.out.println(manager.getTasks());
    }
}
