public class Main {
    private static int t = 2;

    public static void main(String[] args) {
        BTree system = new BTree();
        Pair val = system.search(2);
        if(val == null) {
            System.out.println("There is no value");
        }
        for(int i = 1; i <= 5; ++ i) {
            system.insert(new Pair(i, "data" + i));

            //system.printBTree();
            //System.out.println("~~~~~~~~~~~~~");
        }

        //System.out.println(system.getRoot().getKeys());
        system.printBTree();

        if(system.search(2) != null) {
            System.out.println("Ok, passed");
        } else System.out.println("There is a bug");
    }

    public int getT() {
        return this.t;
    }
}
