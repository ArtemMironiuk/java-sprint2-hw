package manager;

import tasksOfDifferentTypes.Task;

import java.util.*;

/**
 * История задач в памяти.
 */
public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    protected final Map<Integer, Node<T>> map = new HashMap<>();

    private Node<T> head;
    private Node<T> tail;

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
     * @param task
     */
    private void linkLast(T task) {
        final Node<T> oldTail = tail;  //последний элемент
        final Node<T> newNode = new Node<T>(oldTail, task, null);  //новый элемент
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
    private ArrayList<T> getTasks() {
        ArrayList<T> list = new ArrayList<>();
        Node<T> current = null;
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
     * @param node
     */
    private void removeNode(Node<T> node) {
// //       if(task!=null) {
//            if (task == head) {
//                head = head.next;
//                head.prev = null;
//                return;
//            }
// //       } else {
//            if (task == tail) {
//                tail = tail.prev;
//                tail.next = null;
//                return;
//            }
// //       }
//        task.prev.next = task.next;
//        task.next.prev = task.prev;
        final Node<T> next = node.next;
        final Node<T> prev = node.prev;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node = null;
    }

    @Override
    public List <T> getHistory() {
        return getTasks();
    }

    @Override
    public void add(T task) {
        try {
            if (task != null) {
                Node<T> node = map.get(task.getId());
                if (node != null) {
                    removeNode(node);
                }
                linkLast(task);
            }

        } catch (NullPointerException exp) { // ловим исключение NullPointerException
            System.out.println("Ошибка: передан неинициализированный объект!");
        }
    }

    @Override
    public void remove(int id) {
        removeNode(map.get(id));
    }
}
