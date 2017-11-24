package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
	
	
}
