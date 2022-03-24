import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int identifier = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

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
    /**
     * Получение списка всех задач.
     * @return
     */
    public ArrayList<Subtask> getSubtask(){
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Удаление всех задач.
     */
    public void deleteSubtask(){
        subtasks.clear();
    }

    /**
     * Получение по идентификатору.
     * @param id
     */
    public Subtask getSubtasks(int id){
        return subtasks.get(id);
    }

    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param subtask
     * @return
     */
    public Subtask creatingSubtask(Subtask subtask){
        subtask.setEpicId(++identifier);
        subtasks.put(subtask.getEpicId(),subtask);
        return subtask;
    }

    /**
     * Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param subtask
     */
    public void updateSubtask(Subtask subtask){
        if(subtasks.containsKey(subtask.getEpicId())){
            return;
        }
        subtasks.put(subtask.getEpicId(), subtask);
    }

    /**
     * Удаление по идентификатору.
     * @param id
     */
    public void deleteSubtaskId(int id){
        subtasks.remove(id);
    }

}
