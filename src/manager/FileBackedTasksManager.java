package manager;

import exception.ManagerSaveException;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.TypeTasks;
import utils.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> task = super.getTasks();
        save();
        return task;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void creatingTask(Task task) {
        super.creatingTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskId(int id) {
        super.deleteTaskId(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasks = super.getSubtasks();
        save();
        return subtasks;
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void creatingSubtask(Subtask subtask) {
        super.creatingSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskId(int id) {
        super.deleteSubtaskId(id);
        save();
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void creatingEpic(Epic epic) {
        super.creatingEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicId(int id) {
        super.deleteEpicId(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasksEpic(Epic epic) {
        ArrayList<Subtask> subtasksEpic = super.getSubtasksEpic(epic);
        save();
        return subtasksEpic;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        save();
        return history;
    }
    /**
     * Из задачи в строку
     * @param task
     * @return
     */
    private String toString(Task task){
        TypeTasks typeTask = task.getType();
        switch (typeTask) {
            case TASK:
                return task.getId() + "," + typeTask + "," + task.getName() + "," + task.getStatus() + "," +
                        task.getDescription();
            case EPIC:
                Epic epic = (Epic) task;
                return epic.getId() + "," + typeTask + "," + epic.getName() + "," + epic.getStatus() + "," +
                        epic.getDescription();
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                return subtask.getId() + "," + typeTask + "," + subtask.getName() + "," + subtask.getStatus() + "," +
                        subtask.getDescription() + "," + subtask.getEpicId();
            default:
                throw new ManagerSaveException("Нет задачи, для передачи в строку");
        }
    }
    /**
     * Из строки в формирование задач
     * @param value
     * @return
     */
    private Task fromString(String value) {
        if (!value.isEmpty()) {
            String[] pole = value.split(",");
            TypeTasks type = TypeTasks.valueOf(pole[1]);
            switch (type) {
                case TASK:
                    return new Task(pole[2], pole[4], Status.valueOf(pole[3]), Integer.parseInt(pole[0]));
                case SUBTASK:
                    return new Subtask(pole[2], pole[4], Status.valueOf(pole[3]), Integer.parseInt(pole[0]),
                            Integer.parseInt(pole[5]));
                case EPIC:
                    return new Epic(pole[2], pole[4], Status.valueOf(pole[3]), Integer.parseInt(pole[0]));
            }
        }
        return null;
    }
    /**
     * Сохранение истории в файл
     * @param manager
     * @return
     */
    static String historyToString(HistoryManager manager){
        StringBuilder sb = new StringBuilder();
        if (manager.getHistory().size() != 0){
            for(Task task : manager.getHistory()){
                sb.append(task.getId()).append(",");
            }
        }
        return sb.toString();
    }
    /**
     * Восстановление менеджера истории
     * @param value
     * @return
     */
    static List<Integer> historyFromString(String value) {
        String[] id = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String v : id) {
            history.add(Integer.valueOf(v));
        }
        return history;
    }
    /**
     * Сохранение в файл
     */
    private void save(){
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,epic");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : mapTasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : mapEpics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : mapSubtasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            writer.append("\n");
            writer.append(historyToString(inMemoryHistoryManager));
            writer.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
    /**
     * Восстановление из файла
     */
    private void load() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();//Пропускаем заголовок

            while(true) {
                String line = reader.readLine();  //Читаем задачи

                final Task task = fromString(line);

                if (task != null) {
                    int id = task.getId();
                    if (task.getType() == TypeTasks.TASK) {
                        mapTasks.put(id, task);
                    } else if (task.getType() == TypeTasks.EPIC) {
                        mapEpics.put(id, (Epic) task);
                    } else if (task.getType() == TypeTasks.SUBTASK) {
                        mapSubtasks.put(id, (Subtask) task);
                        if(mapEpics.containsKey(mapSubtasks.get(id).getEpicId())) {
                            mapEpics.get(mapSubtasks.get(id).getEpicId()).getSubtask().add((Subtask) task);//добавление
                            // сабтаска в лист сабтасков для эпика
                        }
                    }
                    if (maxId < id) {
                        maxId = id;
                    }
                }
                if(line.isEmpty()) {
                    break;
                }
            }
            String line = reader.readLine(); //читаем историю
            List<Integer> id = historyFromString(line);
            for (Integer integer : id) {
                if (mapTasks.containsKey(integer)) {
                    inMemoryHistoryManager.add(mapTasks.get(integer));
                } else if (mapSubtasks.containsKey(integer)) {
                    inMemoryHistoryManager.add(mapSubtasks.get(integer));
                } else if (mapEpics.containsKey(integer)) {
                    inMemoryHistoryManager.add(mapEpics.get(integer));
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        identifierTask = maxId;


    }
    /**
     * Восстановление из файла
     * @param file
     * @return
     */
    public static FileBackedTasksManager loadFromFile (File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
        return manager;

    }
}
