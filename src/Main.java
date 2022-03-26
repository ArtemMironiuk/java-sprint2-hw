import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("Тест", "Описание", Status.NEW,0);
        Task newTask1 = new Task("Тест", "Описание", Status.NEW,0);
        manager.creatingTask(newTask);
        manager.creatingTask(newTask1);

        System.out.println(manager.getTasks());
        System.out.println(manager.getTask(0));

        manager.deleteTaskId(0);
        System.out.println(manager.getTasks());

        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW,0);
        Subtask newSubtask1 = new Subtask("Тест", "Описание", Status.NEW,0);

        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);

        ArrayList<Subtask> subtask = new ArrayList<>();
        subtask.add(newSubtask);
        subtask.add(newSubtask1);

        System.out.println(manager.getSubtasks());

        Epic newEpic = new Epic("Тест", "Описание",0,subtask);

        manager.creatingEpic(newEpic);
        System.out.println(manager.getEpic());

//        manager.getSubtasksEpic(newEpic);
        System.out.println(manager.getSubtasksEpic(newEpic));


//        manager.deleteTaskId(task.getId());
    }
}
