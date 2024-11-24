package km.model.structures;

import java.util.ArrayList;
import java.util.Comparator;

public class PriorityQueue<T> {
    private final ArrayList<T> heap = new ArrayList<>();
    private final Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Dodaje element do kopca
    public void enqueue(T item) {
        heap.add(item);
        siftUp(heap.size() - 1); // Ustawienie elementu na właściwej pozycji
    }

    // Usuwa i zwraca element o najwyższym priorytecie
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("PriorityQueue is empty");
        }
        T root = heap.get(0);
        T lastItem = heap.remove(heap.size() - 1);
        if (!isEmpty()) {
            heap.set(0, lastItem);
            siftDown(0); // Napraw kopiec
        }
        return root;
    }

    // Sprawdza, czy kopiec jest pusty
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Przesuwa element w górę, aby zachować właściwości kopca
    private void siftUp(int index) {
        int parent = (index - 1) / 2;
        while (index > 0 && comparator.compare(heap.get(index), heap.get(parent)) < 0) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    // Przesuwa element w dół, aby zachować właściwości kopca
    private void siftDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;

        if (leftChild < heap.size() && comparator.compare(heap.get(leftChild), heap.get(smallest)) < 0) {
            smallest = leftChild;
        }
        if (rightChild < heap.size() && comparator.compare(heap.get(rightChild), heap.get(smallest)) < 0) {
            smallest = rightChild;
        }
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    // Zamienia dwa elementy w kopcu
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
