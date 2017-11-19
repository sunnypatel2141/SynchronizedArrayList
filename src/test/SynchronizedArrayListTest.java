package test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;

import main.SynchronizedArrayList;

class SynchronizedArrayListTest
{
	private SynchronizedArrayList<Integer> array;

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
	void testAdd()
	{
		int count = 10;
		array = new SynchronizedArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			array.add(i);
		}
		assertEquals(array.size(), 10);

		array.add(count);
		assertEquals(array.capacity(), count * 2);

		for (int i = count + 1; i < count * 2; i++)
		{
			array.add(i);
		}
		for (int i = 0; i < count * 2; i++)
		{
			assertEquals(array.get(i), new Integer(i));
		}
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(count * 2));
	}

	@Test
	void testAddIndexElement()
	{
		int count = 10;
		array = new SynchronizedArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			array.add(i);
		}

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
		array = new SynchronizedArrayList<>(10);
		array.add(0);
		assertEquals(new Integer(0), array.get(0));

		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 10; i++)
		{
			list.add(new Integer(i));
		}
		array.addAll(list);

		for (int i = 0; i < 10; i++)
		{
			assertEquals(new Integer(i), array.get(i));
		}
	}

	@Test
	void subList()
	{
		array = new SynchronizedArrayList<>(10);
		for (int i = 0; i < 10; i++)
		{
			array.add(new Integer(i));
		}
		assertThrows(IndexOutOfBoundsException.class, () -> array.subList(-1, 9));
		assertThrows(IllegalArgumentException.class, () -> array.subList(5, 4));

		array = array.subList(0, 5);
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(6));
	}

	@Test
	void testIterator()
	{
		array = new SynchronizedArrayList<Integer>();
		for (int i = 0; i < 10; i++)
		{
			array.add(new Integer(i));
		}
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
		array = new SynchronizedArrayList<Integer>();
		for (int i = 0; i < 10; i++)
		{
			array.add(new Integer(i));
		}
		ListIterator<Integer> it = array.listIterator();
		int index = 0;
		while (it.hasNext())
		{
			System.out.println(index);
			assertEquals(new Integer(index), it.next());
			index++;
		}

		while (it.hasPrevious())
		{
			index--;
			assertEquals(new Integer(index), it.previous());
		}
	}

	@Test
	void testRemove()
	{
		array = new SynchronizedArrayList<Integer>();
		for (int i = 0; i < 10; i++)
		{
			array.add(new Integer(i));
		}
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
		array = new SynchronizedArrayList<>();
		for (int i = 0; i < 17; i++)
		{
			array.add(new Integer(i));
		}
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
		array = new SynchronizedArrayList<>();
		for (int i = 0; i < 17; i++)
		{
			array.add(new Integer(i));
		}
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]", array.toString());
		assertEquals(20, array.capacity());
		for (int i = 0; i < 10; i++)
		{
			array.remove(new Integer(i));

			// check when reduction happens
			if (i == 6)
			{
				assertEquals(20, array.capacity());
			} else if (i == 7)
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
		array = new SynchronizedArrayList<>();
		for (int i = 0; i < 10; i++)
		{
			array.add(new Integer(i));
		}
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", array.toString());
		for (int i = 0; i < 10; i++)
		{
			array.set(i, new Integer(9 - i));
		}
		assertEquals("[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]", array.toString());
		assertThrows(IndexOutOfBoundsException.class, ()->array.set(-1, new Integer(0)));
		assertThrows(IndexOutOfBoundsException.class, ()->array.set(10, new Integer(0)));
	}
	
	// @Test
	// void testSize()
	// {
	//
	// }

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

	// @Test
	// void testMain()
	// {
	// fail("Not yet implemented");
	// }

}
