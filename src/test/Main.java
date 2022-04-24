package test;

import manager.Managers;
import manager.TaskManager;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        Task newTask = new Task("Тест", "Описание",Status.NEW,0);
        Task newTask1 = new Task("Тест", "Описание", Status.NEW,0);

        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW,0, 0);
        Subtask newSubtask1 = new Subtask("Тест", "Описание", Status.DONE,0,0);
        Subtask newSubtask2 = new Subtask("Тест", "Описание", Status.NEW,0,0);

        ArrayList<Subtask> subtaskList = new ArrayList<>();
        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
        ArrayList<Subtask> subtaskList2 = new ArrayList<>();
        subtaskList.add(newSubtask);
        subtaskList.add(newSubtask1);
        subtaskList1.add(newSubtask2);

        Epic newEpic = new Epic("Тест", "Описание",Status.NEW, 0,subtaskList);
        Epic newEpic1 = new Epic("Тест", "Описание",Status.NEW,0,subtaskList1);
        Epic newEpic2 = new Epic("Тест", "Описание",Status.NEW,0,subtaskList2);

        manager.creatingTask(newTask);
        manager.creatingTask(newTask1);
        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);
        manager.creatingSubtask(newSubtask2);
        manager.creatingEpic(newEpic);
        manager.creatingEpic(newEpic1);
        manager.creatingEpic(newEpic2);

        newTask.setStatus(Status.DONE);
        newTask1.setStatus(Status.IN_PROGRES);

        manager.getTask(0);
        System.out.println("\n" + manager.getHistory());
        manager.getTask(1);
        System.out.println(manager.getHistory());
        System.out.println(" ");
        manager.getSubtask(2);
        manager.getSubtask(3);
        System.out.println(manager.getHistory());
        System.out.println(" ");
        manager.getSubtask(4);
        manager.getSubtask(2);
        System.out.println(manager.getHistory());
        System.out.println(" ");
        manager.getEpic(5);
        manager.getEpic(6);
        manager.getEpic(7);
        System.out.println(manager.getHistory());
        System.out.println(" ");
        manager.getSubtask(3);
        System.out.println(manager.getHistory());
        System.out.println(" ");

        manager.deleteEpicId(5);
        System.out.println(manager.getHistory());
        System.out.println(" ");

    }
}
