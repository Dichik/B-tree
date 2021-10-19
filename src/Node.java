import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Node {
    private List<Pair> keys;
    private Node parent;
    private List<Node> children;
    private int t = new Main().getT();

    public Node() {
        keys = new ArrayList<>();
        parent = null;
        children = new ArrayList<>();
    }

    public void addKey(Pair key) {
        keys.add(key);
        Collections.sort(keys, (o1, o2) -> {
            if (o1.getKey() > o2.getKey()) return 1;
            if (o1.getKey() < o2.getKey()) return -1;
            return 0;
        });
    }

    public Node getLastChild() {
        return children.get(getCountKeys());
    }

    public Node getChildAt(int index) {
        return children.get(index);
    }

    public Pair getKeyAt(int index) {
        return keys.get(index);
    }

    public int getMiddle() {
        return keys.size() / 2;
    }

    public void addChild(Node node) {
        children.add(node);
        Collections.sort(children, (o1, o2) -> {
            if (o1.getKeyAt(0).getKey() > o2.getKeyAt(0).getKey()) {
                return 1;
            }
            if (o1.getKeyAt(0).getKey() < o2.getKeyAt(0).getKey()) {
                return -1;
            }
            return 0;
        });
    }

    public int getCountKeys() {
        return keys.size();
    }

    public List<Pair> getKeys() {
        return keys;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return (children.size() == 0);
    }

    public void clearAllKeys() {
        keys.clear();
    }

    public void clearAllChildren() {
        children.clear();
    }

    @Override
    public String toString() {
        return "Node{" +
                "keys=" + keys +
                '}';
    }

    public void deleteKeyAt(int index) {
        keys.remove(index);
    }

    public int getIndexOfKey(int key) {
        for(int i = 0; i < keys.size(); ++ i) {
            if(keys.get(i).getKey() == key) {
                return i;
            }
        }
        return -1;
    }

    public Pair getKeyWithDeletion(boolean leftmost) {
        int position = (!leftmost) ? getCountKeys() - 1 : 0;

        Pair result = keys.get(position);
        keys.remove(position);
        return result;
    }

    public Pair replaceAndReturn(Pair fromLeftChild, int key) {
        int position = -1;
        for(int i = 0; i < getCountKeys(); ++ i) {
            if(fromLeftChild.getKey() > keys.get(i).getKey()) {
                position = i;
            }
        }
        Pair result = keys.get(position);
        keys.remove(position);
        addKey(fromLeftChild);
        return result;
    }

    public void replaceKeys(int key, Pair pairFromParent) {
        for(int i = 0; i < getCountKeys(); ++ i) {
            if(keys.get(i).getKey() == key) {
                keys.remove(i);
                addKey(pairFromParent);
            }
        }
    }
}
