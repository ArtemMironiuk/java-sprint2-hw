import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int identifier = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtask = new HashMap<>();
    private HashMap<Integer, Epic> epic = new HashMap<>();

    /**
     * Получение списка всех задач.
     * @return
     */
    public ArrayList<Task> getTasks(){
        return new ArrayList<>(tasks.values());
    }

    /**
     * Удаление всех задач.
     */
    public void deleteTasks(){
        tasks.clear();
    }

    /**
     * Получение по идентификатору.
     * @param id
     */
    public Task getTasks(int id){
        return tasks.get(id);
    }

    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param task
     * @return
     */
    public Task creatingTask(Task task){
        task.setIdTask(++identifier);
        tasks.put(task.getIdTask(),task);
        return task;
    }

    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param task
     */
    public void updateTask(Task task){
        if(tasks.containsKey(task.getIdTask())){
            return;
        }
        tasks.put(task.getIdTask(), task);
    }

    /**
     * Удаление по идентификатору.
     * @param id
     */
    public void deleteTaskId(int id){
        tasks.remove(id);
    }

}
