import java.util.List;

public class BTree {
    private Node root;
    private int T;

    public BTree() {
        root = new Node();
        T = new Main().getT();
    }

    public void insert(Pair key) {
        insert(root, key);
    }

    private void insert(Node currentNode, Pair key) {
        if (currentNode.isLeaf()) {
            currentNode.addKey(key);
            rebuild(currentNode);
        } else {
            Node subtree = findSubtree(currentNode, key);
            insert(subtree, key);
        }
    }

    private Node findSubtree(Node currentNode, Pair key) {
        // here should be a binary search
        for (int i = 0; i < currentNode.getCountKeys(); i++) {
            if (key.getKey() <= currentNode.getKeyAt(i).getKey()) {
                return currentNode.getChildAt(i);
            }
        }
        return currentNode.getLastChild();
    }

    public Pair search(int key) {
        return search(root, key);
    }

    private Pair search(Node currentNode, int key) {
        if (currentNode == null) return null;

        if (currentNode.isLeaf()) {
            for (Pair pr : currentNode.getKeys()) {
                if (key == pr.getKey())
                    return pr;
            }
            return null;
        }

        for (int i = 0; i < currentNode.getKeys().size(); ++i) {
            Pair value = currentNode.getKeys().get(i);
            if (key <= value.getKey()) {
                if (key == value.getKey()) return value;
                return search(currentNode.getChildren().get(i), key);
            }
        }
        return search(currentNode.getLastChild(), key);
    }

    public void remove(int key) {
        // check if we have this key
        Node currentNode = findNodeWithKey(key);
        if(currentNode == null) {
            System.out.println("There is no [" + key + "] key");
            return;
        }

        if(currentNode.isLeaf()) {
            if(currentNode.getCountKeys() > T - 1) {
                for(int i = 0; i < currentNode.getCountKeys(); ++ i) {
                    if(currentNode.getKeyAt(i).getKey() == key) {
                        currentNode.deleteKeyAt(i);
                    }
                 }
            } else {
                int position = -1;
                for(int i = 0; i < currentNode.getParent().getChildren().size(); ++ i) {
                    if(currentNode.getParent().getChildAt(i).getIndexOfKey(key) != -1) {
                        position = i;
                    }
                }
                //System.out.println("Position: " + position);
                if(position > 0 && currentNode.getParent()
                        .getChildAt(position - 1)
                        .getCountKeys() > T - 1) {
                    /*OK*/
                    Pair fromLeftChild = currentNode.getParent()
                            .getChildAt(position - 1)
                            .getKeyWithDeletion(false);
                    /*OK*/
                    Pair pairFromParent = currentNode.getParent().replaceAndReturn(fromLeftChild, key);

                    /*OK*/
                    currentNode.replaceKeys(key, pairFromParent);
                } else if(position + 1 < currentNode.getParent().getChildren().size()
                        && currentNode.getParent()
                        .getChildAt(position + 1)
                        .getCountKeys() > T - 1) {
                    /*OK*/
                    Pair fromRightChild = currentNode.getParent()
                            .getChildAt(position + 1)
                            .getKeyWithDeletion(true);
                    /*OK*/
                    Pair pairFromParent = currentNode.getParent().replaceAndReturn(fromRightChild, key);

                    /*OK*/
                    currentNode.replaceKeys(key, pairFromParent);
                } else {
                    // ... merging

                }
            }
            return;
        }

        // check case for leaf

        // if internal node and can swap with child

        // the other case with merge and rebuilding
    }

    private Node findNodeWithKey(int key) {
        return findNodeWithKey(root, key);
    }

    private Node findNodeWithKey(Node currentNode, int key) {
        if (currentNode == null) return null;

        if (currentNode.isLeaf()) {
            for (Pair pr : currentNode.getKeys()) {
                if (key == pr.getKey())
                    return currentNode;
            }
            return null;
        }

        for (int i = 0; i < currentNode.getKeys().size(); ++i) {
            Pair value = currentNode.getKeys().get(i);
            if (key <= value.getKey()) {
                if (key == value.getKey()) return currentNode;
                return findNodeWithKey(currentNode.getChildren().get(i), key);
            }
        }
        return findNodeWithKey(currentNode.getLastChild(), key);
    }

    private void rebuild(Node currentNode) {
        if (currentNode.getCountKeys() < 2 * T - 1) return;

        int middle = T - 1;

        Node leftSubtree = getLeftSubtree(currentNode);
        Node rightSubtree = getRightSubtree(currentNode);

        if (currentNode.getParent() != null) {
            currentNode.getParent().addKey(currentNode.getKeyAt(middle));

            for (int i = 0; i < currentNode.getParent().getChildren().size(); i++) {
                if (currentNode.getParent().getChildAt(i).getKeys().size() >= 2 * T - 1) {
                    currentNode.getParent().getChildren().remove(i);
                    break;
                }
            }

            currentNode.getParent().addChild(leftSubtree);
            currentNode.getParent().addChild(rightSubtree);

            leftSubtree.setParent(currentNode.getParent());
            rightSubtree.setParent(currentNode.getParent());

            currentNode = null;

            rebuild(leftSubtree.getParent());
        } else {
            Pair key = currentNode.getKeyAt(middle);
            currentNode.clearAllKeys();
            currentNode.addKey(key);
            currentNode.clearAllChildren();
            currentNode.addChild(leftSubtree);
            currentNode.addChild(rightSubtree);

            leftSubtree.setParent(currentNode);
            rightSubtree.setParent(currentNode);
        }
    }

    private Node getLeftSubtree(Node currentNode) {
        Node leftSubtree = new Node();

        for (int i = 0; i < T - 1; ++i) {
            leftSubtree.addKey(currentNode.getKeyAt(i));
        }

        if (!currentNode.isLeaf()) {
            for (int i = 0; i <= T - 1; ++i) {
                leftSubtree.addChild(currentNode.getChildAt(i));
                leftSubtree.getChildAt(i).setParent(leftSubtree);
            }
        }

        return leftSubtree;
    }

    private Node getRightSubtree(Node currentNode) {
        Node rightSubtree = new Node();

        for (int i = T; i < currentNode.getCountKeys(); ++i) {
            rightSubtree.addKey(currentNode.getKeyAt(i));
        }

        if (!currentNode.isLeaf()) {
            for (int i = T; i < currentNode.getChildren().size(); ++i) {
                rightSubtree.addChild(currentNode.getChildAt(i));
                rightSubtree.getChildAt(i - T).setParent(rightSubtree);
            }
        }

        return rightSubtree;
    }

    public void printBTree() {
        dive(1, root);
    }

    private void dive(int depth, Node root) {
        if (root == null) {
            return;
        }
        System.out.println(root.getKeys());
        System.out.println("Depth: " + depth);

        for (int i = 0; i < root.getChildren().size(); ++i) {
            dive(depth + 1, root.getChildAt(i));
        }
    }
}
