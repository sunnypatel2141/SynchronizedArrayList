package JUnit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
	
	@Test
	void testRemoveObj()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		array.add("Int: 99");
		assertEquals(20, array.capacity());
		array.remove("Int: 1");
		array.remove("Int: 6");
		assertEquals(10, array.capacity());
		
		String localStr = "[Int: 0, Int: 2, Int: 3, Int: 4, Int: 5, Int: 7, Int: 8, Int: 9, Int: 99]";
		assertEquals(localStr, array.toString());
	}
	
	@Test
	void testClear()
	{
		testRemoveObj();
		array.clear();
		assertEquals("[]", array.toString());
	}
	
	@Test
	void testAddAll()
	{
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < len; i++)
		{
			arr.add(i, "Int: " + i);
		}
		
		array = new SynchronizedArrayList<>();
		array.addAll(arr);
		
		assertEquals(10, array.capacity());
		assertEquals(str, array.toString());
	}

	
	@Test
	void addAllIndex()
	{
		array = new SynchronizedArrayList<>();
		array.add("Int: 99");
		
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < len; i++)
		{
			arr.add("Int: " + i);
		}
		
		assertEquals(10, array.capacity());
		String strArr = Arrays.toString(arr.toArray());
		strArr = strArr.replaceAll("]", ", ") + "Int: 99]";
		array.addAll(0, arr);
		assertEquals(strArr, array.toString());
	}
	
	@Test
	void testRemoveRange()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < len; i++)
		{
			arr.add("Int: " + i);
		}
		
		array.addAll(arr);
		assertEquals(20, array.capacity());
		
		array.removeRange(10, array.size() - 1);
		assertEquals(str, array.toString());
	}
	
	@Test
	void removeAll()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < len; i+=2)
		{
			arr.add("Int: " + i);
		}
		
		array.removeAll(arr);
		assertEquals("[Int: 1, Int: 3, Int: 5, Int: 7, Int: 9]", array.toString());
		
		List<String> arr2 = null;
		assertThrows(NullPointerException.class, ()->array.retainAll(arr2));
	}
	
	@Test
	void retainAll()
	{
		InstantiateAndPopulate(true);
		assertEquals(str, array.toString());
		List<String> arr = new ArrayList<>();
		for (int i = 0; i < len * 2; i+=2)
		{
			arr.add("Int: " + i);
		}
		
		array.retainAll(arr);
		assertEquals("[Int: 0, Int: 2, Int: 4, Int: 6, Int: 8]", array.toString());
		
		List<String> arr2 = null;
		assertThrows(NullPointerException.class, ()->array.retainAll(arr2));
	}
	
	@Test
	void testListIterator()
	{
		InstantiateAndPopulate(false);
		ListIterator<String> it = array.listIterator();
		int i = 0;
		while (it.hasNext())
		{
			String str = "Int: " + i;
			assertEquals(str, it.next());
			i++;
		}
		while (it.hasPrevious())
		{
			i--;
			String str = "Int: " + i;
			assertEquals(str, it.previous());
		}
		
		it.add("Int: 99");
		assertEquals("Int: 99", it.next());
		while (it.hasNext())
		{
			String str = "Int: " + i++;
			assertEquals(str, it.next());
		}
		it.remove();
		assertThrows(IllegalStateException.class, ()->it.remove());
	}
	
	@Test
	void testSubList()
	{
		InstantiateAndPopulate(true);
		SynchronizedArrayList<String> sal = array.subList(0, array.size());
		assertEquals(sal.toString(), array.toString());
		
		assertThrows(IllegalArgumentException.class, ()->array.subList(0, -1));
		assertThrows(IndexOutOfBoundsException.class, ()->array.subList(-1, 1));
		assertThrows(IndexOutOfBoundsException.class, ()->array.subList(0, array.size()+1));
	}
	
	@Test
	void testIterator()
	{
		InstantiateAndPopulate(true);
		Iterator<String> it = array.iterator();
		int i = 0;
		while (it.hasNext())
		{
			String str = "Int: " + i++;
			assertEquals(str, it.next());
		}
	}
}









