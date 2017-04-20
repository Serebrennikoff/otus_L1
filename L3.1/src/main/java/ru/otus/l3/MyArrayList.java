package ru.otus.l3;

import java.util.*;

/**
 * Mutable implementation of List ADT.
 */
public class MyArrayList<E> implements List<E> {

    /** Array default starting capacity */
    private static final int INITIAL_CAPACITY = 10;

    /** Buffer for storing data put in the List */
    private Object[] dataCollection;

    /** Index of the last element in the array */
    private int dataTail = -1;

    // Rep invariant:
    //      dataCollection != null;
    //      dataTail >= -1.
    // Abstraction function:
    //      Represents a list which can be mutated by adding and removing single elements
    //          and group of elements.
    // Safety from rep exposure:
    //      dataTail is immutable primitive type;
    //      methods does not return direct reference to dataCollection.

    /**
     * Constructs an empty list with default initial capacity
     * of 10 elements.
     */
    public MyArrayList() {
        dataCollection = new Object[INITIAL_CAPACITY];
    }

    /**
     * Constructs an empty list with predefined initial capacity.
     * @param initialCapacity - initial size of the list
     */
    public MyArrayList(int initialCapacity) {
        dataCollection = new Object[initialCapacity];
    }

    /**
     * Increases capacity if buffer is loaded on 80% or more.
     */
    private void checkCapacity() {
        if((double)(dataTail+1)/dataCollection.length*100 >= 80) {
            int newCapacity = dataCollection.length * (dataCollection.length >> 1);
            dataCollection = Arrays.copyOf(dataCollection, newCapacity);
        }
    }

    /**
     * Increases capacity of the buffer on a specific value if it is required.
     * @param val - the number of new elements which are to be added to buffer
     */
    private void checkCapacity(int val) {
        if(val+dataTail+1 > dataCollection.length)
            dataCollection = Arrays.copyOf(dataCollection, dataCollection.length+val);
    }

    public int size() {
        return dataTail+1;
    }

    public boolean isEmpty() {
        return dataTail < 0;
    }

    public boolean contains(Object o) {
        for(int i = 0; i <= dataTail; i++) {
            if(dataCollection[i].equals(o)) return true;
        }
        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(dataCollection, dataTail+1);
    }

    public <T> T[] toArray(T[] a) {
        if(a.length <= dataTail) {
            return (T[]) Arrays.copyOf(dataCollection, dataTail+1, a.getClass());
        } else {
            return (T[]) Arrays.copyOf(dataCollection, a.length, a.getClass());
        }
    }

    /**
     * Appends the specified element to the end of the list.
     * @param e element to be appended to this list. Should not be equal to null.
     * @return true if operation was successful
     * @throws NullPointerException if the specified element is null
     */
    public boolean add(E e) {
        if(e == null) throw new NullPointerException();

        checkCapacity();

        dataCollection[++dataTail] = e;

        return true;
    }

    public boolean remove(Object o) {
        if(o == null) throw new NullPointerException();

        int index;
        if((index = indexOf(o)) >= 0) {
            for(;index < dataTail; dataCollection[index] = dataCollection[++index]);
            dataTail--;
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        Iterator<?> itr = c.iterator();
        while(itr.hasNext()) {
            if(!contains(itr.next())) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        checkCapacity(c.size());

        if(c.size() == 0) return false;

        Iterator<? extends E> itr = c.iterator();
        while(itr.hasNext()) {
            add(itr.next());
        }
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        checkCapacity(c.size());

        if(c.size() == 0) return false;

        Object[] tempArr = Arrays.copyOfRange(dataCollection, index, dataTail+1);

        Iterator<? extends E> itr = c.iterator();
        int counter = index;
        while(itr.hasNext()) {
            set(counter++,itr.next());
        }

        dataTail = counter-1;

        for(Object elem : tempArr) {
            add((E)elem);
        }

        return true;
    }

    public boolean removeAll(Collection<?> c) {
        Iterator<?> itr = c.iterator();
        boolean res = false;

        while(itr.hasNext()) {
            if(remove(itr.next())) res = true;
        }

        return res;
    }

    public boolean retainAll(Collection<?> c) {
        boolean res = false;

        for(int i = 0; i <= dataTail;) {
            if(!c.contains(dataCollection[i])) {
                remove(i);
                res = true;
            } else {
                i++;
            }
        }

        return res;
    }

    public void clear() {
        dataTail = -1;
    }

    public E get(int index) {
        return (E)dataCollection[index];
    }

    public E set(int index, E element) {
        if(element == null) throw new NullPointerException();

        E originalElem = (E)dataCollection[index];
        dataCollection[index] = element;

        return originalElem;
    }

    public void add(int index, E element) {
        if(element == null) throw new NullPointerException();

        checkCapacity();

        for(int i = dataTail; i >= index; i--) {
            dataCollection[i+1] = dataCollection[i];
        }
        dataTail++;
        dataCollection[index] = element;
    }

    public E remove(int index) {
        E deletedElem = (E)dataCollection[index];

        for(; index <= dataTail; dataCollection[index] = dataCollection[++index]);
        dataTail--;

        return deletedElem;
    }

    public int indexOf(Object o) {
        for(int i = 0; i <= dataTail; i++) {
            if(o.equals(dataCollection[i])) return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        for(int i = dataTail; i >= 0; i++) {
            if (o.equals(dataCollection[i])) return i;
        }
        return -1;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        int pointer = 0;
        int upperBound = MyArrayList.this.dataTail+1;
        int lastReturned = -1;

        @Override
        public boolean hasNext() {
            return pointer != (upperBound);
        }

        @Override
        public E next() {
            if(pointer >= upperBound) {
                throw new NoSuchElementException();
            }
            lastReturned = pointer;
            return (E) MyArrayList.this.dataCollection[pointer++];
        }
    }

    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public ListIterator<E> listIterator(int index) {
        if(index < 0 || index > dataTail) throw new IndexOutOfBoundsException();

        return new ListItr(index);
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            pointer = index;
        }

        @Override
        public boolean hasPrevious() {
            return pointer != 0;
        }

        @Override
        public E previous() {
            int i = pointer - 1;
            if(i < 0) throw new NoSuchElementException();
            pointer = lastReturned = i;
            return (E) MyArrayList.this.dataCollection[i];
        }

        @Override
        public int nextIndex() {
            return pointer;
        }

        @Override
        public int previousIndex() {
            return pointer-1;
        }

        @Override
        public void remove() {
            if(lastReturned < 0) throw new IllegalStateException();

            MyArrayList.this.remove(lastReturned);
            pointer = lastReturned;
            lastReturned = -1;
        }

        @Override
        public void set(E e) {
            if(lastReturned < 0) throw new IllegalStateException();

            MyArrayList.this.set(lastReturned, e);
        }

        @Override
        public void add(E e) {
            MyArrayList.this.add(pointer, e);
            pointer++;
            lastReturned--;
        }
    }

    public List<E> subList(int fromIndex, int toIndex) {
        if(fromIndex < 0 || toIndex > dataTail+1 || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();

        return new SubList(this, 0, fromIndex, toIndex-1);
    }

    private class SubList<E> implements List<E> {
        private final List<E> parent;
        private final int parentOffset;
        private final int dataHead; // index of the first element in the range
        private int dataTail; // index of the last elements in the range

        SubList(List<E> parent, int offset, int dataHead, int dataTail) {
            this.parent = parent;
            this.parentOffset = offset;
            this.dataHead = dataHead + offset;
            this.dataTail = dataTail;
        }

        private void rangeCheck(int index) {
            if(index < 0 || index > this.dataTail)
                throw new IndexOutOfBoundsException();
        }

        public int size() {
            return this.dataTail+1 - this.dataHead;
        }

        public boolean isEmpty() {
            return this.dataTail < this.dataHead;
        }

        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }

        public boolean add(E e) {
            parent.add(++this.dataTail, e);
            return true;
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.dataTail+1, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheck(index);
            if(c.size() == 0) return false;
            parent.addAll(parentOffset+index, c);
            this.dataTail += c.size();
            return true;
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            for(; this.dataTail >= this.dataHead; parent.remove(this.dataTail--));
        }

        public E get(int index) {
            rangeCheck(index);
            return (E)MyArrayList.this.dataCollection[dataHead+index];
        }

        public E set(int index, E element) {
            rangeCheck(index);
            E originalVal = (E)MyArrayList.this.dataCollection[dataHead+index];
            MyArrayList.this.dataCollection[dataHead+index] = element;

            return originalVal;
        }

        public void add(int index, E element) {
            rangeCheck(index);
            parent.add(parentOffset+index, element);
            this.dataTail++;
        }

        public E remove(int index) {
            rangeCheck(index);
            E result = parent.remove(parentOffset+index);
            this.dataTail--;
            return result;
        }

        public int indexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        public int lastIndexOf(Object o) {
            throw new UnsupportedOperationException();
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public ListIterator<E> listIterator(int index) {
            rangeCheck(index);

            return new ListIterator<E>() {
                int pointer = index;
                int lastReturned = -1;

                @Override
                public boolean hasNext() {
                    return pointer != SubList.this.dataTail+1;
                }

                @Override
                public E next() {
                    int i = pointer;
                    if(i >= SubList.this.dataTail+1)
                        throw new NoSuchElementException();
                    pointer++;
                    return (E) MyArrayList.this.dataCollection[dataHead+(lastReturned = i)];
                }

                @Override
                public boolean hasPrevious() {
                    return pointer != 0;
                }

                @Override
                public E previous() {
                    int i = pointer - 1;
                    if(i < 0) throw new NoSuchElementException();
                    pointer = i;
                    return (E) MyArrayList.this.dataCollection[dataHead+(lastReturned = i)];
                }

                @Override
                public int nextIndex() {
                    return pointer;
                }

                @Override
                public int previousIndex() {
                    return pointer-1;
                }

                @Override
                public void remove() {
                    if(lastReturned < 0) throw new IllegalStateException();

                    SubList.this.remove(lastReturned);
                    pointer = lastReturned;
                    lastReturned = -1;
                }

                @Override
                public void set(E e) {
                    if(lastReturned < 0) throw new IllegalStateException();

                    SubList.this.set(lastReturned, e);
                }

                @Override
                public void add(E e) {
                    int i = pointer;
                    SubList.this.add(i, e);
                    pointer++;
                    lastReturned = -1;
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            if(fromIndex < 0 || toIndex > this.dataTail+1 || fromIndex > toIndex)
                throw new IndexOutOfBoundsException();

            return new SubList(this, dataHead, fromIndex, toIndex-1);
        }
    }
}
