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

        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW,0, 1);
        Subtask newSubtask1 = new Subtask("Тест", "Описание", Status.NEW,0,1);

        manager.creatingSubtask(newSubtask);
        manager.creatingSubtask(newSubtask1);

        ArrayList<Subtask> subtask = new ArrayList<>();
        ArrayList<Subtask> subtask1 = new ArrayList<>();
        subtask1.add(newSubtask);
        subtask1.add(newSubtask1);


        System.out.println(manager.getSubtasks());

        Epic newEpic = new Epic("Тест", "Описание",0,subtask);
        Epic newEpic1 = new Epic("Тест", "Описание",0,subtask1);

        manager.creatingEpic(newEpic);
        manager.creatingEpic(newEpic1);
        System.out.println(manager.getEpics());

//        manager.getSubtasksEpic(newEpic);
//        System.out.println(manager.getSubtasksEpic(newEpic));


//        manager.deleteTaskId(task.getId());
    }
}
