import java.util.ArrayList;

public class AVLTree<T extends Comparable<T>> implements AVLTreeInterface<T> {
    
    public Node<T> root;
    
    /**
     * Basic storage units in a tree. Each Node object has a left and right
     * children fields.
     *
     * If a node does not have a left and/or right child, its right and/or left
     * child is null.
     *
     */
    private class Node<E> {
        private E data;
        private Node<E> left, right; // left and right subtrees
        
        public Node(E data) {
            this.data = data;
        }
    }
    
    // CHANGES START BELOW THIS LINE
    
    /**
     * Stores all elements of tree with infix traverse form lower to upper.
     */
    ArrayList<T> inOrder = new ArrayList<T>();
    /**
     * Stores all elements of tree with bfTraverse with using level by level.
     */
    ArrayList<T> bfOrder = new ArrayList<T>();
    
    @Override
    public boolean isEmpty() {
        if (height() == -1) {
            return true;
        }
        return false;
    }
    
    @Override
    public int size() {
        return (sizeOfTree(root));
    }
    
    /**
     * Finds the size of tree whose root is node in the parameter.
     *
     * @param node
     *            is the searching node for height.
     * @return the height of node.
     */
    public int sizeOfTree(Node<T> node) {
        if (node == null) {
            return (0);
        } else {
            return sizeOfTree(node.right) + 1 + sizeOfTree(node.left);
        }
    }
    
    @Override
    public boolean contains(T element) {
        return contains(element, root);
    }
    
    /**
     * Finds whether element is in tree or not with using node starting from
     * root. If element is not in tree, then returns null.
     *
     * @param element
     *            is data which is searched in tree to be sure if it is in tree
     *            or not.
     * @param node
     *            is traversing node until we find node storing element.
     * @return true or false according to element in tree or not.
     */
    private boolean contains(T element, Node<T> node) {
        if (node == null) {
            return false;
        } else if (node.data.compareTo(element) != 0) {
            if (node.data.compareTo(element) < 0) {
                return contains(element, node.right);
            } else {
                return contains(element, node.left);
            }
        }
        return true;
    }
    
    @Override
    public void insert(T element) {
        if (contains(element)) {
            return;
        }
        root = insertNode(root, element);
    }
    
    /**
     * Inserts new element in tree if it is not in tree and adds element by
     * paying attention its T data with using compareTo() method and then
     * controls its balance factor and rotates if tree is not balanced.
     *
     * @param node
     *            is traversing node starting from root until node is null and
     *            adds new node including element.
     * @param element
     *            is data of new node which is added to tree.
     * @return node to create true connections in nodes of tree.
     */
    private Node<T> insertNode(Node<T> node, T element) {
        
        Node<T> current = node;
        if (current == null) {
            current = new Node<T>(element);
            return current;
        }
        int cmp = current.data.compareTo(element);
        if (cmp < 0) {
            if (current.right == null) {
                current.right = new Node<T>(element);
            }
            current.right = insertNode(current.right, element);
        }
        if (cmp > 0) {
            if (current.left == null) {
                current.left = new Node<T>(element);
            }
            current.left = insertNode(current.left, element);
        }
        
        int balance = balanceFactor((T) current.data);
        
        if (balance > 1) {
            if (balanceFactor(current.left.data) > 0) {
                current = rightRotate(current);
                
            } else {
                current.left = leftRotate(current.left);
                current = rightRotate(current);
            }
        } else if (balance < -1) {
            if (balanceFactor(current.right.data) < 0) {
                current = leftRotate(current);
            } else {
                current.right = rightRotate(current.right);
                current = leftRotate(current);
            }
        }
        return current;
    }
    
    /**
     * Rotates the node to the right by storing node.left.right.
     *
     * @param node
     *            is the rotating node.
     * @return newParent which is come instead of old node.
     */
    public Node<T> rightRotate(Node<T> node) {
        Node<T> leftRight = node.left.right;
        Node<T> newParent = node.left;
        newParent.right = node;
        newParent.right.left = leftRight;
        return newParent;
    }
    
    /**
     * Rotates the node to the left by storing node.right.left.
     *
     * @param node
     *            is the rotating node.
     * @return newParent which is come instead of old node.
     */
    public Node<T> leftRotate(Node<T> node) {
        Node<T> rightLeft = node.right.left;
        Node<T> newParent = node.right;
        newParent.left = node;
        newParent.left.right = rightLeft;
        return newParent;
    }
    
    @Override
    public void delete(T element) {
        if (!contains(element)) {
            return;
        }
        root = delete(root, element);
        
    }
    
    /**
     * Deletes node that stores element and balances tree according to balance
     * factor for every node by using recursion and rotates them.
     *
     * @param node
     *            starts from root to node which contains element.
     * @param element
     *            is the deletion data.
     * @return node with recursion.
     */
    public Node<T> delete(Node<T> node, T element) {
        if (root == null) {
            return null;
        }
        int cmp = node.data.compareTo(element);
        if (cmp < 0) {
            node.right = delete(node.right, element);
        } else if (cmp > 0) {
            node.left = delete(node.left, element);
        } else {
            if (node.right == null) {
                return node.left;
            }
            if (node.left == null) {
                return node.right;
            }
            
            Node<T> temp = min(node.right);
            node.data = temp.data;
            node.right = delete(node.right, (T) temp.data);
        }
        
        int balance = balanceFactor(node.data);
        
        if (balance < -1) {
            if (balanceFactor(node.right.data) > 0) {
                node.right = rightRotate(node.right);
                node = leftRotate(node);
            } else {
                node = leftRotate(node);
            }
        }
        if (balance > 1) {
            System.out.println("right delete");
            if (balanceFactor(node.left.data) < 0) {
                node.left = leftRotate(node.left);
                node = rightRotate(node);
            } else {
                node = rightRotate(node);
            }
        }
        return node;
    }
    
    /**
     * Finds the min value of node when it is deleted and this current becomes
     * main node.
     *
     * @param node
     *            is the node of deletion in algoritm.
     * @return the new node which is become new node.
     */
    private Node<T> min(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        
        return current;
    }
    
    @Override
    public int height() {
        return heightTree(root);
    }
    
    /**
     * Finds the height of node which takes from parameter.
     *
     * @param node
     *            is root or another node .
     * @return the height of node.
     */
    public int heightTree(Node<T> node) {
        if (node == null) {
            return -1;
        } else {
            return 1 + Math.max(heightTree(node.left), heightTree(node.right));
        }
    }
    
    @Override
    public ArrayList<T> inOrderTraversal() {
        return inOrderTraver(root);
    }
    
    /**
     * Traverses all nodes by traversing left to right to use infixTraverse that
     * is in lecture.
     *
     * @param node
     *            is the traversing node.
     * @return inOrder ArrayList.
     */
    private ArrayList<T> inOrderTraver(Node<T> node) {
        if (node != null) {
            inOrderTraver(node.left);
            inOrder.add(node.data);
            inOrderTraver(node.right);
        }
        return inOrder;
    }
    
    @Override
    public ArrayList<T> bfTraverse() {
        Node<T> current = root;
        return bfTraver(current);
    }
    
    /**
     * Traverses all nodes with level by level and adds bfOrder ArrayList.
     *
     * @param node
     *            is the current node.
     * @return bfOrder ArrayList.
     */
    private ArrayList<T> bfTraver(Node<T> node) {
        ArrayList<Node<T>> tempOrder = new ArrayList<Node<T>>();
        if (node == null) {
            return null;
        }
        tempOrder.add(node);
        while (bfOrder.size() != size()) {
            Node<T> current = (Node<T>) tempOrder.get(0);
            tempOrder.remove(0);
            bfOrder.add(current.data);
            
            if (current.left != null) {
                tempOrder.add(current.left);
            }
            if (current.right != null) {
                tempOrder.add(current.right);
            }
        }
        
        return bfOrder;
    }
    
    /**
     * Finds level of node which contains element and uses to determine if nodes
     * are cousin or not.
     *
     * @param element
     *            is data of searching node for level.
     * @return the level of element as a integer.
     */
    public int level(T element) {
        Node<T> current = root;
        int count = 0;
        while (current.data.compareTo(element) != 0) {
            if (current.data.compareTo(element) < 0) {
                count++;
                current = current.right;
            }
            if (current.data.compareTo(element) > 0) {
                count++;
                current = current.left;
            }
        }
        return count;
    }
    
    @Override
    public boolean areCousins(T element1, T element2) {
        Node<T> node1, node2, node3, node4;
        if (!contains(element1) || !contains(element2)) {
            return false;
        }
        node1 = finding(element1);
        node2 = finding(element2);
        
        if (level(node1.data) == level(node2.data)) {
            node3 = findingParent(element1);
            node4 = findingParent(element2);
            if (node3.data != node4.data) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Finds parent of node that contains element by comparing data of
     * traversing node.
     * 
     * @param element
     *            is data of searching node.
     * @return parent of node.
     */
    public Node<T> findingParent(T element) {
        Node<T> node = root;
        Node<T> parent = root;
        if (node == null) {
            return null;
        }
        
        while (node.data.compareTo(element) != 0) {
            parent = node;
            if (node.data.compareTo(element) < 0) {
                if (node.right == null) {
                    break;
                } else {
                    node = node.right;
                    
                }
            } else {
                if (node.left == null) {
                    break;
                } else {
                    node = node.left;
                }
            }
        }
        return parent;
    }
    
    @Override
    public int numElementsInRange(T lower, T upper) {
        inOrderTraversal();
        int count = 0;
        ArrayList<T> ordering = inOrder;
        for (int i = 0; i < ordering.size(); i++) {
            if (ordering.get(i).compareTo(lower) > 0
                && ordering.get(i).compareTo(upper) < 0) {
                count++;
            }
        }
        return count;
        
    }
    
    @Override
    public int balanceFactor(T data) {
        Node<T> current = finding(data);
        if (current.right == null && current.left == null) {
            return 0;
        }
        return heightTree(current.left) - heightTree(current.right);
    }
    
    /**
     * Finds the node that stores data by traversing tree until we find node we
     * want to find and uses this method when determining balance factor.
     * 
     * @param data
     *            is data of searching node.
     * @return node that stores data.
     */
    public Node<T> finding(T data) {
        Node<T> current = root;
        if (current == null) {
            return null;
        }
        while (data.compareTo(current.data) != 0) {
            int comp = current.data.compareTo(data);
            if (comp < 0) {
                if (current.right == null) {
                    break;
                } else {
                    current = current.right;
                }
            }
            if (comp > 0) {
                if (current.left == null) {
                    break;
                } else {
                    current = current.left;
                }
            }
        }
        return current;
    }
    
    // CHANGES END ABOVE THIS LINE
    
    
    
}
