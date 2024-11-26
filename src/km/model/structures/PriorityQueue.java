package km.model.structures;

import java.util.ArrayList;
import java.util.Comparator;

public class PriorityQueue<T> {
    private final ArrayList<T> heap = new ArrayList<>();
    // Komparator do porównywania priorytetów elementów
    private final Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Dodaje element do kolejki priorytetowej
    public void enqueue(T item) {
        // Dodaje element na koniec kopca
        heap.add(item);
        // Przywrócenie właściwości kopca przez przesuwanie elementu w górę
        siftUp(heap.size() - 1);
    }

    // Usuwa i zwraca element o najwyższym priorytecie (pierwszy w kopcu)
    public T dequeue() {
        // Jeśli kopiec jest pusty, zgłasza wyjątek
        if (isEmpty()) {
            throw new IllegalStateException("PriorityQueue is empty");
        }
        // Pobieranie korzeń kopca (najwyższy priorytet)
        T root = heap.get(0);
        // Usuwanie ostatniego elementu z kopca
        T lastItem = heap.remove(heap.size() - 1);
        if (!isEmpty()) {
            // Przeniesienie ostatniego elementu na miejsce korzenia
            heap.set(0, lastItem);
            // Przywrócenie właściwości kopca przez przesuwanie elementu w dół
            siftDown(0);
        }
        // Zwrócenie elementu o najwyższym priorytecie
        return root;
    }

    // Sprawdza, czy kolejka priorytetowa jest pusta
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Przesuwa element w górę, aby zachować właściwości kopca
    private void siftUp(int index) {
        // Znajdź indeks rodzica
        int parent = (index - 1) / 2;
        // Dopóki element nie jest korzeniem i ma wyższy priorytet od rodzica
        while (index > 0 && comparator.compare(heap.get(index), heap.get(parent)) < 0) {
            // Zamień element z rodzicem
            swap(index, parent);
            // Zaktualizuj indeksy
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    // Przesuwa element w dół, aby zachować właściwości kopca
    private void siftDown(int index) {
        // Znajdź indeksy lewego i prawego dziecka
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        // Przyjmij, że bieżący element (index) ma najmniejszy priorytet
        int smallest = index;

        // Sprawdź, czy lewe dziecko istnieje i ma wyższy priorytet
        if (leftChild < heap.size() && comparator.compare(heap.get(leftChild), heap.get(smallest)) < 0) {
            smallest = leftChild;
        }
        // Sprawdź, czy prawe dziecko istnieje i ma wyższy priorytet
        if (rightChild < heap.size() && comparator.compare(heap.get(rightChild), heap.get(smallest)) < 0) {
            smallest = rightChild;
        }
        // Jeśli najmniejszy element to nie bieżący, zamień je miejscami
        if (smallest != index) {
            swap(index, smallest);
            // Rekurencyjnie wywołaj dla zmienionego elementu
            siftDown(smallest);
        }
    }

    // Zamienia dwa elementy w kopcu
    private void swap(int i, int j) {
        // Tymczasowo przechowaj element z pozycji i
        T temp = heap.get(i);
        // Zamień elementy na pozycjach i i j
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
