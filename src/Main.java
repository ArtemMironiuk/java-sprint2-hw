public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("Тест", "Описание", Status.NEW, 0);
//        Task newTask1  = new Task("Тест2","Описание2", Status.IN_PROGRES, 1);
        Subtask newSubtask = new Subtask("Тест", "Описание", Status.NEW, 0,0);
//        Epic newEpic = new Epic();
//        Epic newEpic2 = new Epic();
        final Task task = manager.creatingTask(newTask);
        final Task task1 = manager.getTasks(task.getIdTask());
        final Subtask subtask = manager.creatingSubtask(newSubtask);
        final Subtask subtask1 = manager.getSubtasks(subtask.getEpicId());
        if(!task.equals(task1)) {
            System.out.println("Ошибка задача не находится по id" + task.getIdTask());
        }
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtask());
        manager.deleteTaskId(task.getIdTask());
        System.out.println(manager.getTasks());
    }
}
