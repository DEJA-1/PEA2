package km.model;

import java.util.List;

public class Node {
    public int city;
    public List<Integer> path;
    public int cost;

    public Node(int city, List<Integer> path, int cost) {
        this.city = city;
        this.path = path;
        this.cost = cost;
    }
}
