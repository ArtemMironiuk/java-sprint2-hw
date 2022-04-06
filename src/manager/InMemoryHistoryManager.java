package manager;

import tasksOfDifferentTypes.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public void add(Task task) {
        //добавить ограничение на длину
        if(history.size()==10){
            history.remove(0);
        }
        history.add(task);

    }
}
