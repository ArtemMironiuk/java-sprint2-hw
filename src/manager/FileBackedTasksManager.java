package manager;

import exception.ManagerSaveException;
import tasksOfDifferentTypes.Epic;
import tasksOfDifferentTypes.Subtask;
import tasksOfDifferentTypes.Task;
import utils.TypeTasks;
import utils.Status;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;
    String file1;

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(String file1) {
        this.file1 = file1;

    }

    @Override
    public List<Task> getTasks() throws IOException, InterruptedException {
        List<Task> task = super.getTasks();
        save();
        return task;
    }

    @Override
    public void deleteTasks() throws IOException, InterruptedException {
        super.deleteTasks();
        save();
    }

    @Override
    public Task getTask(int id) throws IOException, InterruptedException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public int creatingTask(Task task) throws IOException, InterruptedException {
        super.creatingTask(task);
        save();
        return  task.getId();
    }

    @Override
    public void updateTask(Task task) throws IOException, InterruptedException {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskId(int id) throws IOException, InterruptedException {
        super.deleteTaskId(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasks() throws IOException, InterruptedException {
        List<Subtask> subtasks = super.getSubtasks();
        save();
        return subtasks;
    }

    @Override
    public void deleteSubtasks() throws IOException, InterruptedException {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask getSubtask(int id) throws IOException, InterruptedException {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public int creatingSubtask(Subtask subtask) throws IOException, InterruptedException {
        super.creatingSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException, InterruptedException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskId(int id) throws IOException, InterruptedException {
        super.deleteSubtaskId(id);
        save();
    }

    @Override
    public List<Epic> getEpics() throws IOException, InterruptedException {
        List<Epic> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public void deleteEpics() throws IOException, InterruptedException {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpic(int id) throws IOException, InterruptedException {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public int creatingEpic(Epic epic) throws IOException, InterruptedException {
        super.creatingEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException, InterruptedException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicId(int id) throws IOException, InterruptedException {
        super.deleteEpicId(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasksEpic(Epic epic) throws IOException, InterruptedException {
        ArrayList<Subtask> subtasksEpic = super.getSubtasksEpic(epic);
        save();
        return subtasksEpic;
    }

    @Override
    public List<Task> getHistory() throws IOException, InterruptedException {
        List<Task> history = super.getHistory();
        save();
        return history;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
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
                        task.getDescription() + "," + task.getStartTime() + "," + task.getDuration();
            case EPIC:
                Epic epic = (Epic) task;
                return epic.getId() + "," + typeTask + "," + epic.getName() + "," + epic.getStatus() + "," +
                        epic.getDescription();
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                return subtask.getId() + "," + typeTask + "," + subtask.getName() + "," + subtask.getStatus() + "," +
                        subtask.getDescription() + "," + subtask.getEpicId() + "," + subtask.getStartTime() + "," +
                        subtask.getDuration();
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
                    return new Task(pole[2], pole[4], Status.valueOf(pole[3]), Integer.parseInt(pole[0]),
                            LocalDateTime.parse(pole[5]), Duration.parse(pole[6]));
                case SUBTASK:
                    return new Subtask(pole[2], pole[4], Status.valueOf(pole[3]), Integer.parseInt(pole[0]),
                            Integer.parseInt(pole[5]), LocalDateTime.parse(pole[6]), Duration.parse(pole[7]));
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
        List<Task> list = manager.getHistory();
        if (manager.getHistory().size() != 0){
            for(Task task : list){
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
        if(!value.equals("")) {
            String[] id = value.split(",");
            List<Integer> history = new ArrayList<>();
            for (String v : id) {
                history.add(Integer.valueOf(v));
            }
            return history;
        } else {
            return new ArrayList<>();
        }
    }
    /**
     * Сохранение в файл
     */
    protected void save() throws IOException, InterruptedException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file1))) {
            writer.append("id,type,name,status,description,epic,startTime,duration,endTime");
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
    protected void load() throws IOException, InterruptedException {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file1))) {
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
            String line = reader.readLine();//читаем историю
            if (line != null) {
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
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        identifierTask = maxId;
    }
    /**
     * Восстановление из файла
     * @param file1
     * @return
     */
    public static FileBackedTasksManager loadFromFile (File file1) throws IOException, InterruptedException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file1.getName());
        manager.load();
        return manager;
    }
}
