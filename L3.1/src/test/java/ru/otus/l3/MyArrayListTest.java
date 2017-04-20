package ru.otus.l3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MyArrayList data type.
 */
class MyArrayListTest {

    // Testing strategy:
    //      list.size(): 0, > 0;
    //      list.toArray(arr): arr.length < list.size(), arr.length == list.size(), arr.length > list.size();
    //      list.remove(obj): list.contains(obj) == true, list.contains(obj) == false;
    //      list.addAll(index, collection): index == list.size(), index == 0, 0 < index < list.size();

    private List<Integer> intList;

    private void fillList(int numOfElems) {
        for(int i = 0; i < numOfElems; i++) {
            intList.add(i+1);
        }
    }

    @BeforeEach
    void init() {
        intList = new MyArrayList<>();
    }

    // covers list.size() == 0
    @Test
    void emptyListTest() {
        assertTrue(intList.size() == 0);
        assertTrue(intList.isEmpty());
    }

    // covers list.size > 0
    // covers list.contains(obj)
    @Test
    void notEmptyListTest() {
        fillList(30);

        assertFalse(intList.isEmpty());
        assertTrue(intList.size() == 30);

        assertTrue(intList.contains(25));
        assertFalse(intList.contains(31));
    }

    // covers list.toArray() returning safe array (method does not
    //      return reference to list internal array)
    @Test
    void toArrayNoArgumentsTest() {
        fillList(50);

        int originalVal = intList.get(25);
        Object[] arrayRep = intList.toArray();
        arrayRep[25] = 1024;

        assertTrue(intList.get(25) != (int)arrayRep[25]);
        assertTrue(intList.get(25) == originalVal);
    }

    // covers list.toArray(arr) where arr.length < list.size()
    @Test
    void toArraySmallerArrayTest() {
        fillList(20);

        Integer[] arr = intList.toArray(new Integer[0]);
        assertTrue(arr.length == intList.size());

        for(int i = 0, length = arr.length; i < length; i++) {
            assertTrue(arr[i].equals(intList.get(i)));
        }
    }

    // covers list.toArray(arr) where arr.length == list.size()
    @Test
    void toArrayEqualArrayTest() {
        fillList(20);

        Integer[] arr = intList.toArray(new Integer[20]);
        assertTrue(arr.length == intList.size());

        for(int i = 0, length = arr.length; i < length; i++) {
            assertTrue(arr[i].equals(intList.get(i)));
        }
    }

    // covers list.toArray(arr) where arr.length > list.size()
    @Test
    void toArrayLargerArrayTest() {
        fillList(20);

        Integer[] arr = intList.toArray(new Integer[30]);
        assertFalse(arr.length == intList.size());

        for(int i = 0, length = arr.length; i < length; i++) {
            if(i >= intList.size()) {
                assertTrue(arr[i] == null);
            } else {
                assertTrue(arr[i].equals(intList.get(i)));
            }
        }
    }

    // covers list.add(elem)
    // covers list.remove(object)
    // covers list.indexOf(object)
    // covers list.lastIndexOf(object)
    @Test
    void addRemoveIndexOfTest() {
        fillList(30);

        int originalSize = intList.size();
        Integer n = intList.get(25);

        intList.add(1024);
        intList.add(n);

        assertTrue(intList.size() > originalSize && intList.size() == originalSize+2);

        intList.remove(n);

        assertTrue(intList.size() == originalSize+1);
        assertTrue(intList.indexOf(n) != -1);
        assertTrue(intList.indexOf(n) == intList.lastIndexOf(n));

        intList.add(intList.get(15));

        assertTrue(intList.lastIndexOf(intList.get(15)) != 15);

        intList.remove(new Integer(1025));

        assertTrue(intList.size() == originalSize+2);
    }

    // covers list.containsAll(someCollection)
    @Test
    void containsAllTest() {
        fillList(30);

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 30; i++) {
            list2.add(i+1);
        }

        assertTrue(intList.containsAll(list2));

        for(int i = 0; i < 30; i++) {
            if(i%2 == 0) list2.add(i+100);
            else list2.add(i+1);
        }

        assertFalse(intList.containsAll(list2));
    }

    // covers list.addAll(someCollection) and list.addAll(indx, someCollection) where
    //      indx == list.size()
    // covers implicit call toe list.iterator() in for loop
    @Test
    void addAllToEndTest() {
        fillList(20);
        int originalSize = intList.size();

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 15; i++) {
            list2.add(i+100);
        }

        intList.addAll(list2);

        assertTrue(intList.size() == list2.size()+originalSize);

        for(Integer n: list2) {
            assertTrue(intList.contains(n));
        }

        init();
        fillList(20);
        originalSize = intList.size();

        intList.addAll(intList.size(), list2);

        assertTrue(intList.size() == list2.size()+originalSize);
    }

    // covers list.addAll(someCollection) and list.addAll(indx, someCollection) where
    //      0 < indx < list.size()
    @Test
    void addAllToMiddleTest() {
        fillList(20);
        int originalSize = intList.size();

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 15; i++) {
            list2.add(i+100);
        }

        intList.addAll(12, list2);

        assertTrue(intList.size() == list2.size()+originalSize);

        for(Integer n: list2) {
            assertTrue(intList.contains(n));
        }
    }

    // covers list.addAll(someCollection) and list.addAll(indx, someCollection) where
    //      indx == 0
    @Test
    void addAllToBeginningTest() {
        fillList(20);
        int originalSize = intList.size();

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 15; i++) {
            list2.add(i+100);
        }

        intList.addAll(0, list2);

        assertTrue(intList.size() == list2.size()+originalSize);

        for(Integer n: list2) {
            assertTrue(intList.contains(n));
        }
    }

    // covers list.removeAll(someCollection)
    @Test
    void removeAllTest() {
        fillList(30);

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 15; i++) {
            list2.add(i);
        }
        intList.removeAll(list2);

        assertTrue(intList.size() == 16);
        for(Integer n: list2) {
            assertFalse(intList.contains(n));
        }
    }

    // covers list.retainAll(someCollection)
    @Test
    void retainAllTest() {
        fillList(30);

        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 15; i++) {
            list2.add(i+1);
        }
        intList.retainAll(list2);

        assertTrue(intList.size() == 15);
        for(Integer n: list2) {
            assertTrue(intList.contains(n));
        }
    }

    // covers list.clear()
    @Test
    void clearTest() {
        fillList(20);
        intList.clear();

        assertTrue(intList.isEmpty());
    }

    // covers list.set(indx, elem)
    @Test
    void setTest() {
        fillList(20);
        Integer originalElem = intList.get(10);

        assertEquals(originalElem, intList.set(10, 1024));
        assertTrue(intList.get(10) == 1024);
    }

    // covers list.add(indx, elem)
    @Test
    void addToPositionTest() {
        fillList(20);
        int originalSize = intList.size();
        Object[] originalList = intList.toArray();

        int indx = 12;
        intList.add(indx, 288);

        assertTrue(intList.get(12) == 288);
        assertTrue(intList.size() == originalSize+1);

        for(int i = indx+1; i < intList.size(); i++) {
            assertTrue(intList.get(i).equals(originalList[i-1]));
        }
    }

    @Test
    void addAllCollectionsTest() {
        fillList(20);
        Collections.addAll(intList, 999, 998, 997, 996);

        for(int i = 996; i < 1000; i++) {
            assertTrue(intList.contains(i));
        }
    }

    @Test
    void copyCollectionsTest() {
        fillList(20);
        List<Integer> listCopy = new MyArrayList<>();
        for(int i = 0; i < 20; i++) {
            listCopy.add(i+30);
        }
        Collections.copy(listCopy, intList);

        assertTrue(listCopy.size() == intList.size());

        for(int i = 0; i < intList.size(); i++) {
            assertTrue(intList.get(i).equals(listCopy.get(i)));
        }
    }

    @Test
    void sortCollectionsTest() {
        fillList(20);
        List<Integer> list2 = new MyArrayList<>();
        for(int i = 0; i < 20; i++) {
            list2.add(i+11);
        }
        intList.addAll(3, list2);

        Collections.sort(intList, Comparator.comparingInt(o -> o));

        for(int i = 1; i < intList.size(); i++) {
            assertTrue(intList.get(i) >= intList.get(i-1));
        }
    }
}