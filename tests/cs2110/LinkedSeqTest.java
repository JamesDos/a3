package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.  By constructing strings with
    // `new`, more likely to catch inadvertent use of `==` instead of `.equals()`.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @Test
    void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @Test
    void testPrependSize() {
        // List creation helper functions use prepend.
        Seq<String> list;

        list = makeList1();
        assertEquals(1, list.size());

        list = makeList2();
        assertEquals(2, list.size());

        list = makeList3();
        assertEquals(3, list.size());
    }

    @Test
    void testToString() {
        Seq<String> list;

        list = makeList0();
        assertEquals("[]", list.toString());

        list = makeList1();
        assertEquals("[A]", list.toString());

        list = makeList2();
        assertEquals("[A, B]", list.toString());

        list = makeList3();
        assertEquals("[A, B, C]", list.toString());
    }

    // TODO: Add new test cases here as you implement each method in `LinkedSeq`.  You may combine
    // multiple tests for the _same_ method in the same @Test procedure, but be sure that each test
    // case is visibly distinct (comments are good for this).  You are welcome to compare against an
    // expected `toString()` output in order to check multiple aspects of the state at once (in
    // general, later tests may make use of methods that have previously been tested).

    @Test
    void testContains() {
        // Basic Testcases
        // Elem in a list that does not contain elem; list is non-empty
        Seq<String> list = makeList3();
        assertFalse(list.contains("D"));
        // Elem in a list that does contain elem; elem in middle of list
        assertTrue(list.contains("B"));
        // Elem in a list that contains multiple instances of elem; both instances in middle
        list.append("B");
        list.append("D");
        assertTrue(list.contains("B"));

        // Edge Cases
        // Elem in a list that does not contain elem; list is empty
        list = makeList0();
        assertFalse(list.contains("A"));
        // Elem in a list that does contain elem; elem is head
        list = makeList3();
        assertTrue(list.contains("A"));
        // Elem in a list that does contain elem; elem is tail
        list = makeList3();
        assertTrue(list.contains("C"));
        // Elem in a list that contains multiple instances of elem; elem is head and tail
        list.append("A");
        assertTrue(list.contains("A"));
    }

    @Test
    void testGet() {
        // List of length > 2
        Seq<String> list = makeList3();
        // Gets elem at index = 0 (head of list)
        assertEquals("A", list.get(0));
        // Gets elem at 0 < index < size (middle of list)
        assertEquals("B", list.get(1));
        // Gets elem at index = size (tail of list)
        assertEquals("C", list.get(2));
        // Gets elem after it has been appended
        list.append("D");
        assertEquals("D", list.get(3));
        // Gets elem after it has been prepended
        list.prepend("Z");
        assertEquals("Z", list.get(0));
        // Checks if other elements are moved
        assertEquals("A", list.get(1));
    }

    @Test
    void testAppend() {
        // Append to empty list
        Seq<String> list = makeList0();
        assertEquals("[]", list.toString());
        assertEquals(0, list.size());
        assertFalse(list.contains("X"));
        list.append("X");
        assertEquals("[X]", list.toString());
        assertEquals(1, list.size());
        assertTrue(list.contains("X"));

        // Append to list of length 1
        list = makeList1();
        assertEquals("[A]", list.toString());
        assertEquals(1, list.size());
        assertFalse(list.contains("X"));
        list.append("X");
        assertEquals("[A, X]", list.toString());
        assertEquals(2, list.size());
        assertTrue(list.contains("X"));

        // Append to list of length 2
        list = makeList2();
        assertEquals("[A, B]", list.toString());
        assertEquals(2, list.size());
        assertFalse(list.contains("X"));
        list.append("X");
        assertEquals("[A, B, X]", list.toString());
        assertEquals(3, list.size());
        assertTrue(list.contains("X"));

        // Append to list of length greater than 2
        list = makeList3();
        assertEquals("[A, B, C]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(3, list.size());
        list.append("X");
        assertEquals("[A, B, C, X]", list.toString());
        assertEquals(4, list.size());
        assertTrue(list.contains("X"));

        // Appending multiple times to the same list
        assertFalse(list.contains("Y"));
        list.append("Y");
        assertEquals("[A, B, C, X, Y]", list.toString());
        assertEquals(5, list.size());
        assertTrue(list.contains("Y"));
    }

    @Test
    void testInsertBefore() {
        // List of length > 2
        Seq<String> list = makeList3();
        // Successor is the head of list
        assertEquals("[A, B, C]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(3, list.size());
        list.insertBefore("X", "A");
        assertEquals("[X, A, B, C]", list.toString());
        assertEquals(4, list.size());
        assertTrue(list.contains("X"));

        // Successor is the tail of the list
        assertFalse(list.contains("Y"));
        list.insertBefore("Y", "C");
        assertEquals("[X, A, B, Y, C]", list.toString());
        assertEquals(5, list.size());
        assertTrue(list.contains("Y"));

        // Successor is in the middle of the list
        assertFalse(list.contains("Z"));
        list.insertBefore("Z", "B");
        assertEquals("[X, A, Z, B, Y, C]", list.toString());
        assertEquals(6, list.size());
        assertTrue(list.contains("Z"));

        // List of length 1
        list = makeList1();
        // Successor is both head and tail
        assertEquals("[A]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(1, list.size());
        list.insertBefore("X", "A");
        assertEquals("[X, A]", list.toString());
        assertEquals(2, list.size());
        assertTrue(list.contains("X"));

        // List of length 2
        list = makeList2();
        // Successor is head
        assertEquals("[A, B]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(2, list.size());
        list.insertBefore("X", "A");
        assertEquals("[X, A, B]", list.toString());
        assertEquals(3, list.size());
        assertTrue(list.contains("X"));

        // Successor is tail
        assertFalse(list.contains("Y"));
        list.insertBefore("Y", "B");
        assertEquals("[X, A, Y, B]", list.toString());
        assertEquals(4, list.size());
        assertTrue(list.contains("Y"));

        // Insert element that has the same data as successor
        list.insertBefore("B", "B");
        assertEquals("[X, A, Y, B, B]", list.toString());
        assertEquals(5, list.size());
        assertTrue(list.contains("B"));

        // Successor is in multiple positions
        list.insertBefore("Z", "B");
        assertEquals("[X, A, Y, Z, B, B]", list.toString());
        assertEquals(6, list.size());
        assertTrue(list.contains("Z"));
    }

    @Test
    void testRemove() {
        // List of length > 2
        Seq<String> list = makeList3();

        // Remove elem from a list that does not contain elem
        assertEquals("[A, B, C]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(3, list.size());
        // list.remove removes elem and returns boolean
        assertFalse(list.remove("X"));
        assertEquals("[A, B, C]", list.toString());
        assertEquals(3, list.size());
        assertFalse(list.contains("X"));

        // Remove elem from a list that contains it once (elem is head)
        assertEquals("[A, B, C]", list.toString());
        assertTrue(list.contains("A"));
        assertEquals(3, list.size());
        assertTrue(list.remove("A"));
        assertEquals("[B, C]", list.toString());
        assertEquals(2, list.size());
        assertFalse(list.contains("A"));

        // Remove elem from a list that contains elem multiple times
        list.append("C");
        list.prepend("C");
        assertEquals("[C, B, C, C]", list.toString());
        assertTrue(list.contains("C"));
        assertEquals(4, list.size());
        assertTrue(list.remove("C"));
        assertEquals("[B, C, C]", list.toString());
        assertEquals(3, list.size());
        assertTrue(list.contains("C"));

        // Empty List
        list = makeList0();
        assertEquals("[]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(0, list.size());
        assertFalse(list.remove("X"));
        assertEquals("[]", list.toString());
        assertEquals(0, list.size());
        assertFalse(list.contains("X"));

        // List of length 1
        // Remove elem from list that does not contain elem
        list = makeList1();
        assertEquals("[A]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(1, list.size());
        assertFalse(list.remove("X"));
        assertEquals("[A]", list.toString());
        assertEquals(1, list.size());
        assertFalse(list.contains("X"));

        // Remove elem from list that does contain elem (elem is head and tail)
        assertTrue(list.remove("A"));
        assertEquals("[]", list.toString());
        assertEquals(0, list.size());
        assertFalse(list.contains("A"));

        // List of length 2
        list = makeList2();

        // Remove elem from list that does not contain elem
        assertEquals("[A, B]", list.toString());
        assertFalse(list.contains("X"));
        assertEquals(2, list.size());
        assertFalse(list.remove("X"));
        assertEquals("[A, B]", list.toString());
        assertEquals(2, list.size());
        assertFalse(list.contains("X"));

        // Remove elem from list that does contain elem (elem is tail)
        assertTrue(list.remove("B"));
        assertEquals("[A]", list.toString());
        assertEquals(1, list.size());
        assertFalse(list.contains("B"));

        // Remove multiple different items from list
        assertTrue(list.remove("A"));
        assertEquals("[]", list.toString());
        assertEquals(0, list.size());
        assertFalse(list.contains("A"));
    }

    @Test
    void testEquals() {
        // Compare empty Lists
        Seq<String> list1 = makeList0();
        Seq<String> list2 = makeList0();
        assertTrue(list1.equals(list2));
        assertTrue(list2.equals(list1));

        // Compare lists of length 1
        list1 = makeList1();
        list2 = makeList1();
        // Lists are the same
        assertTrue(list1.equals(list2));
        assertTrue(list2.equals(list1));
        // Lists are different
        list1 = new LinkedSeq<>();
        list1.append("B");
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // Compare lists of length 2
        list1 = makeList2();
        list2 = makeList2();
        // Lists are the same
        assertTrue(list1.equals(list2));
        assertTrue(list2.equals(list1));
        // Lists are different
        list1.remove("A");
        list1.prepend("X");
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // Compare lists of length > 2
        list1 = makeList3();
        list2 = makeList3();
        // Lists are the same
        assertTrue(list1.equals(list2));
        assertTrue(list2.equals(list1));

        // Lists are different (Don't have the same elements)
        list1.remove("C");
        list1.append("X");
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // Lists are different; lists have the same elements in different order
        list1 = makeList3();
        list2 = makeList3();
        list2.remove("C");
        list2.prepend("C");
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // Compare lists of different length
        list1 = new LinkedSeq<>();
        list1.append("X");
        list2 = new LinkedSeq<>();
        list2.append("Y");
        list2.append("Z");
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // One list contains the other
        list1 = makeList2();
        list2 = makeList3();
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));

        // One list is empty, the other isn't
        list1 = makeList0();
        list2 = makeList2();
        assertFalse(list1.equals(list2));
        assertFalse(list2.equals(list1));
    }


    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered `hashCode()` or `assertThrows()` in class.
     */

    @Test
    void testHashCode() {
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @Test
    void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}
