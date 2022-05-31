package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksOfDifferentTypes.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<FileBackedTasksManager>{

    @BeforeEach
    void initFileBacked() {
        taskManager = new FileBackedTasksManager(new File("test.csv"));
        super.init();
    }

    @Override
    @Test
    void getTasks() {
        super.getTasks();
    }

    @Override
    @Test
    void deleteTasks() {
        super.deleteTasks();
    }

    @Override
    @Test
    void getTask() {
        super.getTask();
    }

    @Override
    @Test
    void creatingTask() {
        super.creatingTask();
    }

    @Override
    @Test
    void updateTask() {
        super.updateTask();
    }

    @Override
    @Test
    void deleteTaskId() {
        super.deleteTaskId();
    }

    @Override
    @Test
    void getSubtasks() {
        super.getSubtasks();
    }

    @Override
    @Test
    void deleteSubtasks() {
        super.deleteSubtasks();
    }

    @Override
    @Test
    void getSubtask() {
        super.getSubtask();
    }

    @Override
    @Test
    void creatingSubtask() {
        super.creatingSubtask();
    }

    @Override
    @Test
    void updateSubtask() {
        super.updateSubtask();
    }

    @Override
    @Test
    void deleteSubtaskId() {
        super.deleteSubtaskId();
    }

    @Override
    @Test
    void getEpics() {
        super.getEpics();
    }

    @Override
    @Test
    void deleteEpics() {
        super.deleteEpics();
    }

    @Override
    @Test
    void getEpic() {
        super.getEpic();
    }

    @Override
    @Test
    void creatingEpic() {
        super.creatingEpic();
    }

    @Override
    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Override
    @Test
    void deleteEpicId() {
        super.deleteEpicId();
    }

    @Override
    @Test
    void getSubtasksEpic() {
        super.getSubtasksEpic();
    }

    @Override
    @Test
    void getHistory() {
        super.getHistory();
    }
}