package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Task;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager>{

    @BeforeEach
    void initFileBacked() throws IOException, InterruptedException {
        taskManager = (FileBackedTasksManager) Managers.getDefaultTaskManager();
        super.init();
    }

    @Override
    @Test
    void getTasks() throws IOException, InterruptedException {
        super.getTasks();
    }

    @Override
    @Test
    void deleteTasks() throws IOException, InterruptedException {
        super.deleteTasks();
    }

    @Override
    @Test
    void getTask() throws IOException, InterruptedException {
        super.getTask();
    }

    @Override
    @Test
    void creatingTask() throws IOException, InterruptedException {
        super.creatingTask();
    }

    @Override
    @Test
    void updateTask() throws IOException, InterruptedException {
        super.updateTask();
    }

    @Override
    @Test
    void deleteTaskId() throws IOException, InterruptedException {
        super.deleteTaskId();
    }

    @Override
    @Test
    void getSubtasks() throws IOException, InterruptedException {
        super.getSubtasks();
    }

    @Override
    @Test
    void deleteSubtasks() throws IOException, InterruptedException {
        super.deleteSubtasks();
    }

    @Override
    @Test
    void getSubtask() throws IOException, InterruptedException {
        super.getSubtask();
    }

    @Override
    @Test
    void creatingSubtask() throws IOException, InterruptedException {
        super.creatingSubtask();
    }

    @Override
    @Test
    void updateSubtask() throws IOException, InterruptedException {
        super.updateSubtask();
    }

    @Override
    @Test
    void deleteSubtaskId() throws IOException, InterruptedException {
        super.deleteSubtaskId();
    }

    @Override
    @Test
    void getEpics() throws IOException, InterruptedException {
        super.getEpics();
    }

    @Override
    @Test
    void deleteEpics() throws IOException, InterruptedException {
        super.deleteEpics();
    }

    @Override
    @Test
    void getEpic() throws IOException, InterruptedException {
        super.getEpic();
    }

    @Override
    @Test
    void creatingEpic() throws IOException, InterruptedException {
        super.creatingEpic();
    }

    @Override
    @Test
    void updateEpic() throws IOException, InterruptedException {
        super.updateEpic();
    }

    @Override
    @Test
    void deleteEpicId() throws IOException, InterruptedException {
        super.deleteEpicId();
    }

    @Override
    @Test
    void getSubtasksEpic() throws IOException, InterruptedException {
        super.getSubtasksEpic();
    }

    @Override
    @Test
    void getHistory() throws IOException, InterruptedException {
        super.getHistory();
    }

    @Test
    void loadFromFile() {
        TaskManager manager = FileBackedTasksManager.loadFromFile(new File ("test.csv"));
        assertNotNull(manager,"Нет сохранения");
        assertEquals(taskManager.getTasks(),manager.getTasks());
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks());
        assertEquals(taskManager.getEpics(), manager.getEpics());
        assertEquals(taskManager.inMemoryHistoryManager.getHistory(), manager.getHistory());

    }

    @Test
    void loadFromEmptyTasksFile() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        System.out.println(taskManager.getTasks());

        TaskManager manager = FileBackedTasksManager.loadFromFile(new File ("test.csv"));
        assertNotNull(manager,"Нет сохранения");
        assertEquals(taskManager.getTasks(),manager.getTasks());
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks());
        assertEquals(taskManager.getEpics(), manager.getEpics());
        assertEquals(taskManager.inMemoryHistoryManager.getHistory(), manager.getHistory());
    }

    @Test
    void loadFromEmptyHistoryFile() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        idTask = taskManager.creatingTask(newTask);
        idSubtask1 = taskManager.creatingSubtask(newSubtask1);
        idSubtask2 = taskManager.creatingSubtask(newSubtask2);
        idEpic = taskManager.creatingEpic(newEpic);

        TaskManager manager = FileBackedTasksManager.loadFromFile(new File ("test.csv"));
        assertNotNull(manager,"Нет сохранения");
        assertEquals(0, manager.getHistory().size());
        assertEquals(taskManager.getTasks(),manager.getTasks());
        assertEquals(taskManager.getSubtasks(), manager.getSubtasks());
        assertEquals(taskManager.getEpics(), manager.getEpics());
        assertEquals(taskManager.getHistory(), manager.getHistory());
    }
}