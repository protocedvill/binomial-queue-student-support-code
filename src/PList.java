import java.util.function.BiPredicate;

/* Persistent Linke List */
public class PList<T> {
    private final T data;
    private final PList<T> next;
    PList(T d, PList<T> nxt) { data = d; next = nxt; }

    public String toString() {
        if (next == null)
            return data.toString();
        else
            return data.toString() + ", " + next.toString();
    }

    public static <T> PList<T>
    addFront(T first, PList<T> rest) {
        if (first == null)
            return rest;
        else
            return new PList<>(first, rest);
    }

    public static <T> T
    getFirst(PList<T> n) {
        return n.data;
    }

    public static <T> PList<T>
    getNext(PList<T> n) {
        return n.next;
    }

    public static <E> PList<E>
    remove(E n, PList<E> ns) {
        if (ns == null)
            return null;
        else {
            if (getFirst(ns) == n)
                return getNext(ns);
            else
                return addFront(getFirst(ns), remove(n, getNext(ns)));
        }
    }

    public static <E> PList<E>
    reverse(PList<E> ns, PList<E> out) {
        if (ns == null)
            return out;
        else {
            return reverse(getNext(ns), addFront(getFirst(ns), out));
        }
    }

    public static <E> E find_max(PList<E> ns, BiPredicate<E,E> lessEq) {
        if (ns == null)
            return null;
        else
            return find_max_helper(getNext(ns), getFirst(ns), lessEq);
    }

    public static <E> E
    find_max_helper(PList<E> ns, E best, BiPredicate<E,E> lessEq) {
        if (ns == null)
            return best;
        else if (lessEq.test(getFirst(ns), best))
            return find_max_helper(getNext(ns), best, lessEq);
        else
            return find_max_helper(getNext(ns), getFirst(ns), lessEq);
    }
}