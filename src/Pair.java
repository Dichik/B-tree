import java.util.Comparator;

public class Pair {
    private int key;
    private String data;

    public Pair(int key, String data) {
        this.key = key;
        this.data = data;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", data='" + data + '\'' +
                '}';
    }
}
