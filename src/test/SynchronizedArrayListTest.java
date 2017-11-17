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
		assertEquals(array.size(), 10);
	}

	@Test
	void testSynchronizedArrayListInt()
	{
		int count = 100;;
		array = new SynchronizedArrayList<>(count);
		assertEquals(array.size(), count);
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
		for (int i = 0; i < count; i++)
		{
			assertEquals(array.get(i), new Integer(i));
		}
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
