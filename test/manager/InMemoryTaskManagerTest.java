package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends ManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void initInMemory() {
        taskManager = new InMemoryTaskManager();
        super.init();
    }

    @Test
    void testInMemoryTaskManager(){
        taskManager = new InMemoryTaskManager(); //после создания менеджера он пустой
        assertEquals(0, taskManager.getTasks().size(),"Задач нет");
        assertEquals(0, taskManager.getSubtasks().size(),"Подзадач нет");
        assertEquals(0, taskManager.getEpics().size(),"Эпиков нет");
        assertEquals(0, taskManager.getHistory().size(),"Истории нет");
    }
}