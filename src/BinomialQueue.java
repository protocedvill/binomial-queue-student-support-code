import java.util.function.BiPredicate;

class BinomialHeap<K> {
    K key;
    int height;
    PList<BinomialHeap<K>> children;
    BiPredicate<K, K> lessEq;

    BinomialHeap(K k, int h, PList<BinomialHeap<K>> kids, BiPredicate<K, K> le) {
        this.key = k;
        this.height = h;
        this.children = kids;
        this.lessEq = le;
    }

    /*
     * @precondition this.height == other.height
     */
    BinomialHeap<K> link(BinomialHeap<K> other) {
        if (this.height != other.height)
            throw new UnsupportedOperationException("attempt to link trees of different height");
        if (lessEq.test(other.key, this.key)) {
            PList<BinomialHeap<K>> kids = PList.addFront(other, this.children);
            return new BinomialHeap<>(this.key, this.height + 1, kids, lessEq);
        } else {
            PList<BinomialHeap<K>> kids = PList.addFront(this, other.children);
            return new BinomialHeap<>(other.key, other.height + 1, kids, lessEq);
        }
    }

    /**
     * TODO
     * <p>
     * The isHeap method checks whether or not the subtree of this node
     * satisfies the heap property.
     *
     * Has 2^k nodes.
     * Has a depth of k.
     *
     */
    boolean isHeap() {
        PList<BinomialHeap<K>> workingChildren = children;
        if (workingChildren == null) return true;
        BinomialHeap<K> child = PList.getFirst(workingChildren);
        while(child != null) {
            // If the child is not smaller than the parent, or the child is not heap then the parent isn't heap.
            if (!child.lessEq.test(child.key, key) || !child.isHeap()) return false;

            // Iterate through next item in list
            workingChildren = PList.getNext(workingChildren);
            if (workingChildren == null) return true;
            child = PList.getFirst(workingChildren);
        }
        return true;
    }

    public String toString() {
        String ret = "(" + key.toString();
        if (children != null)
            ret = ret + " " + children.toString();
        return ret + ")";
    }

}

public class BinomialQueue<K> {
    PList<BinomialHeap<K>> forest;
    BiPredicate<K, K> lessEq;

    public BinomialQueue(BiPredicate<K, K> le) {
        forest = null;
        lessEq = le;
    }

    public void push(K key) {
        BinomialHeap<K> heap = new BinomialHeap<>(key, 0, null, lessEq);
        this.forest = insert(heap, this.forest);
    }

    public K pop() {
        BinomialHeap<K> max = PList.find_max(this.forest, (h1,h2) -> this.lessEq.test(h1.key, h2.key));
        this.forest = PList.remove(max, this.forest);
        PList<BinomialHeap<K>> kids = PList.reverse(max.children, null);
        this.forest = merge(this.forest, kids);
        return max.key;
    }

    public boolean isEmpty() {
        return forest == null;
    }

    /**
     * TODO
     * The isHeap method returns whether or not the Binomial Queue (a forest of Binomial Trees)
     * satisfies the heap property.
     */
    public boolean isHeap() {
        PList<BinomialHeap<K>> workingForest = forest;
        if (workingForest == null) return true;
        BinomialHeap<K> workingTree = PList.getFirst(workingForest);
        K max = workingTree.key;
        return isHeapHelper(max, workingForest);
    }

    private boolean isHeapHelper(K max, PList<BinomialHeap<K>> workingForest) {
        if (workingForest == null) return true;
        BinomialHeap<K> workingTree = PList.getFirst(workingForest);
        // Check if current forest is heapy.
        if (!workingTree.isHeap()) return false;
        // Check if each max key in each forest is sequential.
        if (lessEq.test(max, workingTree.key)) {
            max = workingTree.key;
        } //else return false;
        workingForest = PList.getNext(workingForest);
        return isHeapHelper(max, workingForest);
    }

    public String toString() {
        if (this.forest == null)
            return "";
        else
            return this.forest.toString();
    }

    /**********************************
     * Helper functions
     */

    /**
     * TODO
     * The insert method is analogous to binary addition. That is,
     * it inserts the node n into the list ns to produce a new list
     * that is still sorted by height.
     *
     * @param <K> The type of the keys.
     * @param n   The node to insert (must not be null).
     * @param ns  The binomial forest into which to insert, ordered by height. (may be null)
     * @return A new binomial forest that includes the new node.
     */
    static <K> PList<BinomialHeap<K>>
    insert(BinomialHeap<K> n, PList<BinomialHeap<K>> ns) {
        if (n == null) throw new RuntimeException("node to insert must not be null");

        PList<BinomialHeap<K>> workingForest = ns;

        // If given forest is empty, add n to a new list and return.
        if (workingForest == null) return new PList<>(n, null);

        BinomialHeap<K> first = PList.getFirst(workingForest);
        if(n.height < first.height) {
            return PList.addFront(n, workingForest);
        }

        if (first.height == n.height) {
            BinomialHeap<K> linkedNode = first.link(n);
            workingForest = PList.remove(first, workingForest);
            return insert(linkedNode, workingForest);
        }

        return PList.addFront(first, insert(n, PList.getNext(workingForest)));

    }

    /**
     * TODO
     * The merge method is analogous to the merge part of merge sort. That is,
     * it takes two lists that are sorted (by height) and returns a new list that
     * contains the elements of both lists, and the new list is sorted by height.
     *
     * @param ns1
     * @param ns2
     * @return A list that is sorted and contains all and only the elements in ns1 and ns2.
     */
    static <K> PList<BinomialHeap<K>>
    merge(PList<BinomialHeap<K>> ns1, PList<BinomialHeap<K>> ns2) {
        if (ns1 == null) return ns2;
        if (ns2 == null) return ns1;

        BinomialHeap<K> first1 = PList.getFirst(ns1);
        BinomialHeap<K> first2 = PList.getFirst(ns2);

        // test if first1.height is equal to first2.height
        //      if first1 is less than first2
        //      else first2 is less than first 1
        if (first1.height == first2.height) {
            BinomialHeap<K> linkedNode = first1.link(first2);
            return insert(linkedNode, merge(PList.getNext(ns1),PList.getNext(ns2)));
        }

        if (first1.height < first2.height) {
            return PList.addFront(first1,merge(PList.getNext(ns1), ns2));
        }

        return PList.addFront(first2,merge(ns1, PList.getNext(ns2)));
    }
    
}
