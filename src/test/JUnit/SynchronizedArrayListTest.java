package test.JUnit;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;

import main.SynchronizedArrayList;

class SynchronizedArrayListTest
{
	private SynchronizedArrayList<Integer> array;
	private int len = 10;

	@Test
	void testSynchronizedArrayList()
	{
		array = new SynchronizedArrayList<>();
		assertEquals(array.size(), 0);
	}

	@Test
	void testSynchronizedArrayListInt()
	{
		array = new SynchronizedArrayList<>(100);
		assertEquals(array.size(), 0);
	}
	
	@Test
	void testSynchronizedArrayListCollection()
	{
		ArrayList<Integer> arr = new ArrayList<>();
		for (int i = 0; i < 15; i++)
		{
			arr.add(new Integer(i));
		}
		
		array = new SynchronizedArrayList<>(arr);
		assertEquals(15, array.size());
		assertEquals(20, array.capacity());
		
		//test expansion
		for (int i = 15; i < 150; i++)
		{
			arr.add(new Integer(i));
		}
		array = new SynchronizedArrayList<>(arr);
		assertEquals(150, array.size());
		assertEquals(150, array.capacity());
	}
	
	@Test
	void testGet()
	{
		array = InstantiateAndPopulate(len);
		
		for (int i = 0; i < len; i++)
		{
			assertEquals(new Integer(i), array.get(i));
		}
		assertThrows(IndexOutOfBoundsException.class, ()->array.get(10));
	}
	

	@Test
	void testAdd()
	{
		array = InstantiateAndPopulate(len);
		assertEquals(array.size(), len);

		array.add(len);
		assertEquals(array.capacity(), len * 2);

		for (int i = len + 1; i < len * 2; i++)
		{
			array.add(i);
		}
		for (int i = 0; i < len * 2; i++)
		{
			assertEquals(array.get(i), new Integer(i));
		}
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(len * 2));
	}

	@Test
	void testAddIndexElement()
	{
		array = InstantiateAndPopulate(len);

		array.add(0, 100);

		assertEquals(new Integer(100), array.get(0));
		for (int i = 1; i < array.size(); i++)
		{
			assertEquals(new Integer(i - 1), array.get(i));
		}

		String str = "[100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]";
		assertEquals(str, array.toString());
	}

	@Test
	void testAddAll()
	{
		array = new SynchronizedArrayList<>(len);
		array.add(0);
		assertEquals(new Integer(0), array.get(0));

		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 10; i++)
		{
			list.add(new Integer(i));
		}
		array.addAll(list);

		for (int i = 0; i < len; i++)
		{
			assertEquals(new Integer(i), array.get(i));
		}
	}

	@Test
	void testAddAllIndex()
	{
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 2; i <= 10; i++)
		{
			list.add(new Integer(i));
		}

		array = new SynchronizedArrayList<>();
		array.add(0);
		array.add(1);
		assertEquals("[0, 1]", array.toString());
		
		array.addAll(0,list);
		assertEquals("[2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 1]", array.toString());
		assertEquals(11, array.size());
		
		array.clear();
		
		//add collection - different index
		array.add(0);
		array.add(1);
		array.addAll(1,list);
		assertEquals("[0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1]", array.toString());
		
		//input check
		array.clear();
		assertThrows(IndexOutOfBoundsException.class, ()->array.addAll(array.size()+1, list));
	}
	
	@Test
	void testRemoveRange()
	{
		array = InstantiateAndPopulate(len * 2);
		String str = "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]";
		assertEquals(str, array.toString());
		assertEquals(20, array.capacity());
		
		array.removeRange(3, 15);
		assertEquals("[0, 1, 2, 16, 17, 18, 19]", array.toString());
		assertEquals(10, array.capacity());
		
		array.removeRange(0, 3);
		assertEquals("[17, 18, 19]", array.toString());
		assertEquals(10, array.capacity());
		
		assertThrows(IndexOutOfBoundsException.class, ()->array.removeRange(3, 3));
	}
	
	@Test
	void testremoveAllCollection()
	{
		array = InstantiateAndPopulate(len);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		
		ArrayList<Integer> list = new ArrayList<>();
		list.add(0); list.add(1); list.add(9);
		
		ArrayList<Integer> list2 = null;
		
		assertEquals(true, array.removeAll(list));
		assertEquals("[2, 3, 4, 5, 6, 7, 8]", array.toString());
		assertThrows(NullPointerException.class, ()->array.removeAll(list2));
	}
	
	@Test
	void testretainAllCollection()
	{
		array = InstantiateAndPopulate(len);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		
		ArrayList<Integer> list = new ArrayList<>();
		list.add(0); list.add(1); list.add(9);
		assertEquals(true, array.retainAll(list));
		assertEquals("[0, 1, 9]", array.toString());
		
		ArrayList<Integer> list2 = null;
		assertThrows(NullPointerException.class, ()->array.retainAll(list2));
	}
	
	@Test
	void subList()
	{
		array = InstantiateAndPopulate(len);
		
		assertThrows(IndexOutOfBoundsException.class, () -> array.subList(-1, 9));
		assertThrows(IllegalArgumentException.class, () -> array.subList(5, 4));

		array = array.subList(0, 5);
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(6));
	}

	@Test
	void testIterator()
	{
		array = InstantiateAndPopulate(len);
		
		Iterator<Integer> it = array.iterator();
		int index = 0;
		while (it.hasNext())
		{
			assertEquals(it.next(), new Integer(index));
			index++;
		}
	}

	@Test
	void testListIterator()
	{
		array = InstantiateAndPopulate(5);

		ListIterator<Integer> it = array.listIterator();
		int i = 0;
		
		//0 1 2 3 4
		while (it.hasNext())
		{
			assertEquals(new Integer(i), it.next());
			i++;
		}
		//4 3 2 1 0
		while (it.hasPrevious())
		{
			i--;
			assertEquals(new Integer(i), it.previous());
		}
		
		//remove 0
		it.remove();
		
		assertEquals("[1, 2, 3, 4]", array.toString());
		
		while (it.hasNext())
		{
//			1 -> 4 (0 removed)
			assertEquals(new Integer(i+1), it.next());
			i++;
		}
		
		//4 removed
		it.remove();
		
		assertEquals("[1, 2, 3]", array.toString());
		
		while (it.hasPrevious())
		{
			//1 -> 3
			i--;
			assertEquals(new Integer(i), it.previous());
		}
		
		it.add(new Integer(0));
		assertEquals("[0, 1, 2, 3]", array.toString());
		assertThrows(IllegalStateException.class, ()->it.remove());
	}
	
	@Test
	void testListIteratorIndex()
	{
		InstantiateAndPopulate(len);
		ListIterator<Integer> it = array.listIterator(4);
		int i = 4;
		while (it.hasNext())
		{
			assertEquals(new Integer(i), it.next());
			i++;
		}
		
		while (it.hasPrevious())
		{
			i--;
			assertEquals(new Integer(i), it.previous());
		}
		
		it.remove();
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		
		while (it.hasNext())
		{
			i++;
			assertEquals(new Integer(i), it.next());
		}
		
		it.remove();
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 8]", array.toString());
		while (it.hasPrevious())
		{
			i--;
			assertEquals(new Integer(i), it.previous());
		}
		
		it.set(100);
		assertEquals("[100, 2, 3, 4, 5, 6, 7, 8]", array.toString());
		assertThrows(IllegalStateException.class, ()->it.set(99));
	}

	@Test
	void testRemove()
	{
		array = new SynchronizedArrayList<>();
		assertThrows(IndexOutOfBoundsException.class, ()->array.remove(0));
		array = InstantiateAndPopulate(len);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		assertEquals(10, array.capacity());

		array.remove(new Integer(0));
		array.remove(new Integer(2));
		array.remove(new Integer(4));
		array.remove(new Integer(6));

		assertEquals("[1, 3, 5, 7, 8, 9]", array.toString());
		assertEquals(10, array.capacity());
	}

	@Test
	void testClear()
	{
		array = InstantiateAndPopulate(17);

		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]", array.toString());
		assertEquals(20, array.capacity());
		array.clear();
		assertEquals("[]", array.toString());
		assertEquals(10, array.capacity());
		assertNotEquals(20, array.capacity());
	}

	@Test
	void testRemoveIndex()
	{
		array = InstantiateAndPopulate(17);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]", array.toString());
		assertEquals(20, array.capacity());
		for (int i = 0; i < len; i++)
		{
			array.remove(new Integer(i));

			// check when reduction happens
			if (i == 5)
			{
				assertEquals(20, array.capacity());
			} else if (i == 6)
			{
				assertEquals(10, array.capacity());
			}
		}
		assertEquals("[10, 11, 12, 13, 14, 15, 16]", array.toString());
		assertEquals(10, array.capacity());
	}

	@Test
	void testSet()
	{
		array = InstantiateAndPopulate(len);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		for (int i = 0; i < len; i++)
		{
			array.set(i, new Integer(9 - i));
		}
		assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", array.toString());
		assertThrows(IndexOutOfBoundsException.class, ()->array.set(-1, new Integer(0)));
		assertThrows(IndexOutOfBoundsException.class, ()->array.set(10, new Integer(0)));
	}
	
	@Test
	void testClone()
	{
		array = InstantiateAndPopulate(len);
		assertEquals(false, array == array.clone());
	}
	
	@Test
	void testIndexOf()
	{
		array = InstantiateAndPopulate(len);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		for (int i = 0; i < len; i++)
		{
			assertEquals(i, array.indexOf(new Integer(i)));
		}
	}
	
	@Test
	void testLastIndexOf()
	{
		array = new SynchronizedArrayList<>();
		for (int i = 1; i < len; i++)
		{
			array.add(new Integer(i%3));
		}
		assertEquals(8, array.lastIndexOf(0));
		assertEquals(7, array.lastIndexOf(2));
		assertEquals(6, array.lastIndexOf(1));
	}
	
	@Test
	void testContains()
	{
		array = InstantiateAndPopulate(len);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		for (int i = 0; i < len; i++)
		{
			assertTrue(array.contains(new Integer(i)));
		}
	}
	
	@Test
	void testIsEmpty()
	{
		array = InstantiateAndPopulate(len);
		
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		array.clear();
		assertTrue(array.isEmpty());
	}
	
	@Test
	void testEnsureMinCapAndTrim()
	{
		array = InstantiateAndPopulate(5);
		
		assertEquals(5, array.size());
		
		assertEquals(10, array.capacity());
		
		array.ensureCapacity(20);
		assertEquals(20, array.capacity());
		assertEquals("[0, 1, 2, 3, 4]", array.toString());
		
		assertThrows(IndexOutOfBoundsException.class, ()->array.get(5));
		
		array.trimToSize();
		assertEquals(5, array.capacity());
		assertEquals("[0, 1, 2, 3, 4]", array.toString());
	}

	@Test
	void testToArray()
	{
		array = InstantiateAndPopulate(len);
		
		Object[] arr = array.toArray();
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", Arrays.toString(arr));
	}
	
	SynchronizedArrayList<Integer> InstantiateAndPopulate(int len)
	{
		array = new SynchronizedArrayList<>();
		for (int i = 0; i < len; i++)
		{
			array.add(new Integer(i));
		}
		return array;
	}
	
	@Test
	void testToString()
	{
		int count = 10;
		array = new SynchronizedArrayList<>(count);
		for (int i = 0; i < count * 2; i++)
		{
			array.add(i);
		}
		assertEquals(array.size(), 20);
		String str = "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]";
		assertEquals(str, array.toString());
	}
}