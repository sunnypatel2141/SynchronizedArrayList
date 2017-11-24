package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.SynchronizedArrayList;

class SynchronizedArrayListTestString
{
	private SynchronizedArrayList<String> array;
	private int len = 10;
	private String str = "[Int: 0, Int: 1, Int: 2, Int: 3, Int: 4, Int: 5, Int: 6, Int: 7, Int: 8, Int: 9]";
	
	@Test
	void testSynchronizedArrayList()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		assertEquals(len, array.capacity());
	}
	
	@Test
	void testSynchronizedArrayListInt()
	{
		InstantiateAndPopulate(false);
		assertEquals(str, array.toString());
		assertEquals(len, array.capacity());
	}
	
	void InstantiateAndPopulate(boolean defaultVal)
	{
		array = defaultVal ? new SynchronizedArrayList<>() : new SynchronizedArrayList<>(len);
		for (int i = 0; i < len; i++)
		{
			array.add("Int: " + i);
		}
	}
	
	@Test
	void SynchronizedArrayListCollection()
	{
		List<String> list = new ArrayList<>();
		for (int i = 0; i < len; i++)
		{
			list.add("Int: " + i);
		}
		
		array = new SynchronizedArrayList<>(list);
		assertEquals(str, array.toString());
	}
	
	@Test
	void testIsEmpty()
	{
		InstantiateAndPopulate(false);
		assertEquals(str, array.toString());
		array.clear();
		assertEquals("[]", array.toString());
	}
	
	@Test
	void testContains()
	{
		InstantiateAndPopulate(false);
		assertEquals(str, array.toString());
		assertTrue(array.contains("Int: 0"));
		assertFalse(array.contains("Int: 0 ")); //space after 0
		
		assertFalse(array.contains("Int: 10"));
		
		array.clear();
		assertFalse(array.contains("Int: 0"));
	}
	
	@Test
	void testIndexOf()
	{
		InstantiateAndPopulate(false);
		for (int i = 0; i < len; i++)
		{
			String s = "Int: " + i;
			assertEquals(i, array.indexOf(s));
		}
		
		assertEquals(-1, array.indexOf("Hello"));
	}
	
	@Test
	void testlastIndexOf()
	{
		array = new SynchronizedArrayList<>();
		for (int i = 0; i < len; i++)
		{
			array.add("Int: " + (i%3));
		}
		assertEquals(9, array.lastIndexOf("Int: 0"));
		assertEquals(8, array.lastIndexOf("Int: 2"));
		assertEquals(7, array.lastIndexOf("Int: 1"));
	}
	
	@Test
	void testToArray()
	{
		InstantiateAndPopulate(true);
		String[] sArray = array.toArray(new String[0]);
		assertEquals(str, Arrays.toString(sArray));
	}
	
	@Test
	void testGet()
	{
		InstantiateAndPopulate(false);
		for (int i = 0; i < len; i++)
		{
			String s = "Int: " + i;
			assertEquals(s, array.get(i));
		}
		assertThrows(IndexOutOfBoundsException.class, ()->array.get(1000));
	}
	
	@Test
	void testSet()
	{
		InstantiateAndPopulate(false);
		for (int i = 0; i < len; i++)
		{
			String s = "Int: " + i;
			assertEquals(s, array.get(i));
		}
		for (int i = 0; i < len / 2; i++)
		{
			String temp = array.get(i);
			array.set(i, array.get(len - i - 1));
			array.set(len - i - 1, temp);
		}
		for (int i = 0; i < len; i++)
		{
			String s = "Int: " + i;
			assertEquals(s, array.get(len - i - 1));
		}
	}
	
	@Test
	void testAddIndex()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		assertEquals(10, array.capacity());
		array.add(0, "Int: 100");
		assertEquals(20, array.capacity());
		
		String s = "[Int: 100, " + str.replace("[", "");
		assertEquals(s, array.toString());
	}
	
	@Test
	void testRemove()
	{
		InstantiateAndPopulate(true);
		array.remove(0);
		assertEquals(10, array.capacity());
		assertEquals(str.replace("Int: 0, ", ""), array.toString());
	}
}









