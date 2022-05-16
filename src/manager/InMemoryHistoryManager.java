package manager;

import tasksOfDifferentTypes.Task;

import java.util.*;

/**
 * История задач в памяти.
 */
public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> map = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;

    private static class Node<T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Добавляет задачу в конец
     *
     * @param task
     */
    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;  //последний элемент
        final Node<Task> newNode = new Node<>(oldTail, task, null);  //новый элемент
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        }
        else {
            oldTail.next = newNode;
        }
        map.put(task.getId(), newNode);

    }

    /**
     * Cобирает все задачи из списка в обычный ArrayList
     */
    private ArrayList<Task> getTasks() {
        //вынес в верх, при заходе оббнулял и заново заполнял
        ArrayList<Task> list = new ArrayList<>();
        Node<Task> current = null;
        if(head != null){
            current = head;
            list.add(current.data);
            while (current.next != null) {
                current = current.next;
                list.add(current.data);
            }
        }

        return list;
    }

    /**
     * Удаление узла связного списка
     *
     * @param task
     */
    private void removeNode(Node<Task> task) {
        if (task == head) {
            head = head.next;
            head.prev = null;
            return;
        }
        if (task == tail) {
            tail = tail.prev;
            tail.next = null;
            return;
        }
        task.prev.next = task.next;
        task.next.prev = task.prev;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Node<Task> node = map.get(task.getId());
            if (node != null) {
                removeNode(node);
            }
            linkLast(task);
        }
        //хз зачем оно тут и так выскочит эксепшен и закончит программу
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void remove(int id) {
        removeNode(map.get(id));
    }
}
