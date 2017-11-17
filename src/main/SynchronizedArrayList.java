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

	public void add(E e)
	{
		int currSize = size();
		if (counter > currSize)
		{
			long validSize = currSize * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				Object[] temp = new Object[(int) validSize];
				array = copyContents(array, temp);
			}
		}
		array[counter] = e;
		counter++;
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
			throw new IndexOutOfBoundsException("Invalid index.");
		}
		return (E) array[index];
	}
	
	public int size()
	{
		return array.length;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < array.length - 1; i++)
		{
			sb.append(array[i] + ", ");
		}
		sb.append(array[array.length - 1]);
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		SynchronizedArrayList<Integer> sal = new SynchronizedArrayList<>();
		
	}
}
