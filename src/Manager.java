import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    int identifierTask = 0;
    int identifierEpic = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    /**
     * Получение списка всех задач.
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
     */
    public Task getTasks(int id){
        return tasks.get(id);
    }

    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param task
     */
    public Task creatingTask(Task task){
        task.setIdTask(++identifierTask);
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
     */
    public void deleteTaskId(int id){
        tasks.remove(id);
    }
    /**
     * Получение списка всех задач.
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
     */
    public Subtask getSubtasks(int id){
        return subtasks.get(id);
    }

    /**
     * Создание.Сам объект должен передаваться в качестве параметра.
     * @param subtask
     */
    public Subtask creatingSubtask(Subtask subtask){
        subtask.setEpicId(++identifierEpic);
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
     */
    public void deleteSubtaskId(int id){
        subtasks.remove(id);
    }
    /**
     * Получение списка всех Epics.
     */
    public ArrayList<Epic> getEpic(){
        return new ArrayList<>(epics.values());
    }

    /**
     * Удаление всех Epics.
     */
    public void deleteEpic(){
        epics.clear();
    }

    /**
     * Получение по идентификатору.
     */
    public Epic getEpics(int id){
        return epics.get(id);
    }

    /**
     * Создание Epic.Сам объект должен передаваться в качестве параметра.
     * @param epic
     * @return
     */
    public Epic creatingEpic(Epic epic){
        epics.put(epic.getId(),epic);
        return epic;
    }

    /**
     * Обновление Epic. Новая версия объекта с верным идентификатором передаются в виде параметра.
     * @param epic
     */
    public void updateEpic(Epic epic){
        if(epics.containsKey()){
//            return;
//        }
//        subtasks.put(subtask.getEpicId(), subtask);
    }

    /**
     * Удаление по идентификатору.
     */
    public void deleteEpicId(int id){
        epics.remove(id);
    }


}
