public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task newTask = new Task("Тест", "Описание", Status.NEW, 0);
        final Task task = manager.creatingTask(newTask);
        final Task task1 = manager.getTasks(task.getIdTask());
        if(!task.equals(task1)) {
            System.out.println("Ошибка задача не находится по id" + task.getIdTask());
        }
        System.out.println(manager.getTasks());
        manager.deleteTaskId(task.getIdTask());
    }
}
