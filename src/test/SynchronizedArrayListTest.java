package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
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
		for (int i = 0; i < count * 2; i++)
		{
			array.add(i);
		}
		for (int i = 0; i < count * 2; i++)
		{
			assertEquals(array.get(i), new Integer(i));
		}
		assertThrows(IndexOutOfBoundsException.class, ()->array.get(count * 2));
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
		
		String str = "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]";
		assertEquals(str, array.toString());
		
		array.add(0, 100);
		
		str = "[100, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]";
		assertEquals(str, array.toString());
//		for (int i = 0; i < count; i++)
//		{
//			assertEquals(array.get(i), new Integer(i));
//		}
	}
	
//	@Test
//	void testSize()
//	{
//		
//	}

	@Test
	void testToString()
	{
		int count = 10;
		array = new SynchronizedArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			array.add(i);
		}
		String str = "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]";
		assertEquals(str, array.toString());
	}

//	@Test
//	void testMain()
//	{
//		fail("Not yet implemented");
//	}

}
