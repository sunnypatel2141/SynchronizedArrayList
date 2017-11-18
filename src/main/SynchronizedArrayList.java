package main;

import java.util.Collection;
import java.util.Iterator;

public class SynchronizedArrayList<E>
{
	private Object[] array;
	private int counter = 0;
	private int growMultiplier = 2;
	
	public SynchronizedArrayList()
	{
		this(10);
	}
	
	public SynchronizedArrayList(int num) throws IllegalArgumentException
	{
		if (num < 0 || num > Integer.MAX_VALUE - 1)
		{
			throw new IllegalArgumentException("Can't instantiate a SynchronizedArrayList with size " + num);
		}
		array = new Object[num];
	}

	public boolean add(E e)
	{
		//counter returned not size
		int numOfElements = size();
		int capacity = capacity();
		if (numOfElements + 1 > capacity)
		{
			long validSize = capacity * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				Object[] temp = new Object[(int) validSize];
				array = copyContents(array, temp);
			} else {
				return false;
			}
		}
		array[counter] = e;
		counter++;
		return true;
	}
	
	public void add(int index, E element) throws IndexOutOfBoundsException
	{
		int size = size();
		if(index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException();
		}
		if (size + 1 > capacity())
		{
			long validSize = capacity() * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				Object[] temp = new Object[(int) validSize];
				array = copyContents(array, temp);
			}
		}
		for (int i = size; i > index; i--)
		{
			array[i] = array[i-1];
		}
		array[index] = element;
		counter++;
	}
	
	public boolean addAll(Collection<? extends E> c)
	{
		int size = size();
		int cSize = c.size();
		while (size + cSize > capacity())
		{
			long validSize = capacity() * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				array = copyContents(array, new Object[(int) validSize]);
			} else {
				return false;
			}
		}
		Iterator<? extends E> iterator = c.iterator();
		while (iterator.hasNext())
		{
			array[size] = iterator.next();
			size++;
		}
		counter = size + cSize;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public SynchronizedArrayList<E> subList(int fromIndex, int toIndex) throws IndexOutOfBoundsException, IllegalArgumentException 
	{
		if (fromIndex > toIndex)
		{
			String str = "Start index: " + fromIndex + ", End index: " + toIndex;
			throw new IllegalArgumentException(str);
		}
		if (fromIndex < 0)
		{
			String str = "Start index: " + fromIndex;
			throw new IndexOutOfBoundsException(str);
		}
		if (toIndex > size())
		{
			String str = "End index: " + toIndex;
			throw new IndexOutOfBoundsException(str);
		}
		int length = toIndex - fromIndex;
		SynchronizedArrayList<E> sal = new SynchronizedArrayList<>(length+1);
		for (int i = fromIndex; i < toIndex; i++)
		{
			sal.add((E) array[i]);
		}
		return sal;
	}
	
	private Object[] copyContents(Object[] from, Object[] to)
	{
		for (int i = 0; i < from.length; i++)
		{
			to[i] = from[i];
		}
		return to;
	}
	
	@SuppressWarnings("unchecked")
	public E get(int index) throws IndexOutOfBoundsException 
	{
		if (index < 0 || index > size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		return (E) array[index];
	}
	
	public int size()
	{
		return counter;
	}
	
	public int capacity()
	{
		return array.length;
	}
	
	public String toString()
	{
		int length = size();
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < length - 1; i++)
		{
			sb.append(array[i] + ", ");
		}
		sb.append(array[length - 1]);
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
//		SynchronizedArrayList<Integer> sal = new SynchronizedArrayList<>();
		
	}
}
