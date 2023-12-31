package cs2110;

import com.sun.source.tree.WhileLoopTree;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.swing.plaf.IconUIResource;

/*
 * Assignment metadata
 * Name(s) and NetID(s): Lam Le ldl56 James Tu jt737
 * Hours spent on assignment: 12
 */

/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;
        }
        // TODO 0: check that the number of linked nodes is equal to this list's size and that
        // the last linked node is the same object as `tail`.

        // generate a temporary node to reference head node
        Node<T> current = head;
        // counter to track position of node
        int counter = 0;
        // loop that count number of node that has data
        while (current != null) {
            counter++;
            // set the next node in the list
            current = current.next();
        }
        assert counter == this.size;
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by ", ".
     * <p>
     * Example: a list containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a list containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        String str = "[";
        // TODO 1: Complete the implementation of this method according to its specification.
        // Unit tests have already been provided (you do not need to add additional cases).
        Node<T> current = head;
        while (current != null) {
            str += (current == head) ? current.data() : ", " + current.data();
            current = current.next();
        }
        str += "]";
        return str;
    }

    @Override
    public boolean contains(T elem) {
        // TODO 2: Write unit tests for this method, then implement it according to its
        // specification.  Tests must check for `elem` in a list that does not contain `elem`, in a
        // list that contains it once, and in a list that contains it more than once.
        //throw new UnsupportedOperationException();
        assert elem != null;
        Node<T> current = head;
        while (current != null) {
            if (current.data().equals(elem)) {
                return true;
            }
            current = current.next();
        }
        return false;
    }

    @Override
    public T get(int index) {
        // TODO 3: Write unit tests for this method, then implement it according to its
        // specification.  Tests must get elements from at least three different indices.
        // throw new UnsupportedOperationException();
        assert this.size > index;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next();
        }
        return current.data();
    }

    @Override
    public void append(T elem) {
        // TODO 4: Write unit tests for this method, then implement it according to its
        // specification.  Tests must append to lists of at least three different sizes.
        // Implementation constraint: efficiency must not depend on the size of the list.
        // throw new UnsupportedOperationException();
        assert elem != null;
        Node<T> newTail = new Node<>(elem, null);
        if (size == 0) {
            head = newTail;
        } else {
            tail.setNext(newTail);
        }
        tail = newTail;
        size++;
        assertInv();
    }

    @Override
    public void insertBefore(T elem, T successor) {
        // Tip: Since there is a precondition that `successor` is in the list, you don't have to
        // handle the case of the empty list.  Asserting this precondition is optional.
        // TODO 5: Write unit tests for this method, then implement it according to its
        // specification.  Tests must insert into lists where `successor` is in at least three
        // different positions.
        // throw new UnsupportedOperationException();
        assert elem != null;
        assert successor != null;
        assert contains(successor);

        if (successor.equals(head.data())) {
            prepend(elem);
        } else {
            Node<T> current = head;
            while (!current.next().data().equals(successor)) {
                current = current.next();
            }
            Node<T> insertedNode = new Node<>(elem, current.next());
            current.setNext(insertedNode);
            size++;
        }
        assertInv();
    }

    @Override
    public boolean remove(T elem) {
        // TODO 6: Write unit tests for this method, then implement it according to its
        // specification.  Tests must remove `elem` from a list that does not contain `elem`, from a
        // list that contains it once, and from a list that contains it more than once.
        // throw new UnsupportedOperationException();
        assert elem != null;

        if (!contains(elem)) {
            assertInv();
            return false;
        } else {
            // removing head
            if (elem.equals(head.data())) {
                if (size == 1) {
                    head = null;
                    tail = null;
                } else {
                    head = head.next();
                }
            } else {
                Node<T> current = head;
                while (!current.next().data().equals(elem)) {
                    current = current.next();
                }
                // removing tail
                if (current.next().equals(tail)) {
                    current.setNext(null);
                    tail = current;
                } else {
                    current.setNext(current.next().next());
                }
            }
            size -= 1;
            assertInv();
            return true;
        }
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `LinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        // Note: In the `instanceof` check, we write `LinkedSeq` instead of `LinkedSeq<T>` because
        // of a limitation inherent in Java generics: it is not possible to check at run-time
        // what the specific type `T` is.  So instead we check a weaker property, namely,
        // that `other` is some (unknown) instantiation of `LinkedSeq`.  As a result, the static
        // type returned by `currNodeOther.data()` is `Object`.
        if (!(other instanceof LinkedSeq)) {
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node currNodeOther = otherSeq.head;

        // TODO 7: Write unit tests for this method, then finish implementing it according to its
        // specification.  Tests must compare at least three different pairs of lists.
        // throw new UnsupportedOperationException();

        // test for mismatch element size
        if (otherSeq.size() != this.size) {
            return false;
        }
        // cycle through each element in currNodeThis and compare using Object.equals()
        // to currNodeOther
        while (currNodeThis != null) {
            if (!currNodeThis.data().equals(currNodeOther.data())) {
                return false;
            }
            currNodeThis = currNodeThis.next();
            currNodeOther = currNodeOther.next();
        }
        return true;
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered the implementation of these concepts in class.
     */

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.  Requires that the list not be mutated while the iterator is in use.
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();

        // Return an instance of an anonymous inner class implementing the Iterator interface.
        // For convenience, this uses Java features that have not eyt been introduced in the course.
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
