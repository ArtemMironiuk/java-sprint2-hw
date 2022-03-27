import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("Тест", "Описание", Status.NEW,0);
        Task newTask1 = new Task("Тест", "Описание", Status.NEW,0);
        manager.creatingTask(newTask);
        manager.creatingTask(newTask1);

        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW,0, 1);
        Subtask newSubtask1 = new Subtask("Тест", "Описание", Status.NEW,0,1);
        Subtask newSubtask2 = new Subtask("Тест", "Описание", Status.DONE,0,1);
        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);
        manager.creatingSubtask(newSubtask2);

        ArrayList<Subtask> subtask = new ArrayList<>();
        ArrayList<Subtask> subtask1 = new ArrayList<>();
        subtask.add(newSubtask);
        subtask.add(newSubtask1);
        subtask1.add(newSubtask2);

        Epic newEpic = new Epic("Тест", "Описание",0,subtask);
        Epic newEpic1 = new Epic("Тест", "Описание",0,subtask1);
        manager.creatingEpic(newEpic);
        manager.creatingEpic(newEpic1);

        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

        newTask.setStatus(Status.DONE);
        newTask1.setStatus(Status.IN_PROGRES);
        newSubtask.setStatus(Status.DONE);
//        newSubtask2.setStatus(Status.DONE);
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

//        manager.getSubtasksEpic(newEpic);
//        System.out.println(manager.getSubtasksEpic(newEpic));


//        manager.deleteTaskId(task.getId());
    }
}
