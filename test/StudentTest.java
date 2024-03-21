import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.function.BiPredicate;

public class StudentTest {

    BiPredicate<Integer,Integer> lessEq = (Integer x, Integer y) -> x <= y;

    // is heapTest for single forest.
    public void isHeapTest() {
        BinomialHeap<Integer> k0 = new BinomialHeap<>(0,1,null, lessEq);
        PList<BinomialHeap<Integer>> p0 = new PList<>(k0, null);
        BinomialHeap<Integer> k1 = new BinomialHeap<>(1,2,p0, lessEq);
        PList<BinomialHeap<Integer>> p1 = new PList<>(k1, p0);
        BinomialHeap<Integer> k2 = new BinomialHeap<>(2,3,p1, lessEq);
        PList<BinomialHeap<Integer>> p2 = new PList<>(k2, p0);
        BinomialHeap<Integer> k3 = new BinomialHeap<>(3,4,p2, lessEq);
        PList<BinomialHeap<Integer>> p3 = new PList<>(k2, p0);
        BinomialHeap<Integer> k4 = new BinomialHeap<>(4,5,p3, lessEq);


        assertTrue(k0.isHeap());
        assertTrue(k1.isHeap());
        assertTrue(k2.isHeap());
        assertTrue(k3.isHeap());
        assertTrue(k4.isHeap());

        BinomialHeap<Integer> fk0 = new BinomialHeap<>(0,1,null, lessEq);
        PList<BinomialHeap<Integer>> fp0 = new PList<>(fk0, null);
        BinomialHeap<Integer> fk1 = new BinomialHeap<>(1,2,fp0, lessEq);
        PList<BinomialHeap<Integer>> fp1 = new PList<>(fk1, fp0);
        BinomialHeap<Integer> fk2 = new BinomialHeap<>(0,3,fp1, lessEq);
        PList<BinomialHeap<Integer>> fp2 = new PList<>(fk2, fp0);
        BinomialHeap<Integer> fk3 = new BinomialHeap<>(3,4,fp2, lessEq);
        PList<BinomialHeap<Integer>> fp3 = new PList<>(fk2, fp0);
        BinomialHeap<Integer> fk4 = new BinomialHeap<>(4,5,fp3, lessEq);

        assertTrue(fk0.isHeap());
        assertTrue(fk1.isHeap());
        assertFalse(fk2.isHeap());
        assertFalse(fk3.isHeap());
        assertFalse(fk4.isHeap());

    }

    // is heapTest for binomialQueue aka PList forest
    public void isHeapTestPList() {
        BinomialQueue<Integer> list = new BinomialQueue<>(lessEq);

        assertTrue(list.isHeap());
    }

    @Test
    public void insertHeapTest() {
        BinomialQueue<Integer> list = new BinomialQueue<>(lessEq);
        list.push(1);
        assertTrue(list.isHeap());
        assertFalse(list.isEmpty());

        list.push(2);
        list.push(3);
        list.push(4);
        list.push(5);
        list.push(6);
        list.push(67);
        list.push(670);
        list.push(191);
        list.push(1);
        list.push(1);
        list.push(1);
        list.push(1);


        assertTrue(list.isHeap());
        assertEquals(list.pop(), 670);
        System.out.println(list);

    }

    @Test
    public void extractHeapTest() {
        int cap = 10000;

        BinomialQueue<Integer> list = new BinomialQueue<>(lessEq);
        Random rand = new Random();
        int[] arr = new int[cap];

        list.push(1);
        arr[0] = 1;

        for(int i = 1; i < cap; i++) {
            if (!(rand.nextBoolean() && rand.nextBoolean())) {
                int nextInt = rand.nextInt(cap);
                list.push(nextInt);
                arr[i] = nextInt;
                assertTrue(list.isHeap());
            } else {
                assertEquals(list.pop(), arrMax(arr));
                removeArr(arr, arrMax(arr));
            }
        }

    }

    private int arrMax(int[] arr) {
        int max = -1;
        for (int i : arr) {
            if (i > max) max = i;
        }
        return max;
    }

    private void removeArr(int [] arr, int i) {
        for (int j = 0; j < arr.length; j++) {
            if (i == arr[j]) {
                arr[j] = -1;
                break;
            }
        }
    }

    @Test
    public void test() throws Exception {
       isHeapTest();
       isHeapTestPList();
       insertHeapTest();
       extractHeapTest();
    }

}
