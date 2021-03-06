package tracker.managers;

import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> taskMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (size >= 10) {
            remove(head.item.getUid());
        }
        try {
            if (taskMap.containsKey(task.getUid())) {
                remove(task.getUid());
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Некорректный uid");
        }
        taskMap.put(task.getUid(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        removeNode(taskMap.get(id));
        taskMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node<Task> linkLast(Task task) {
        final Node<Task> last = tail;
        final Node<Task> newNode = new Node(last, task, null);
        tail = newNode;
        if (last == null)
            head = newNode;
        else
            last.next = newNode;
        size++;
        return newNode;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        final Node next = node.next;
        final Node prev = node.prev;
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
        node.item = null;
        size--;
    }

    private List<Task> getTasks() {
        List<Task> listTask = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            listTask.add(node.item);
            if (node == tail) {
                break;
            }
            node = node.next;
        }
        return listTask;
    }

    private static class Node<T> {
        T item;
        Node<T> next;
        Node<T> prev;

        Node(Node<T> prev, T element, Node<T> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
