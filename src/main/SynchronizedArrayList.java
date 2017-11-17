package main;
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
		if (index > counter)
		{
			array[index] = element;
			return;
		}
		for (int i = size - 1; i > index; i--)
		{
			array[i] = array[i-1];
		}
		array[index] = element;
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
	
	private int capacity()
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
