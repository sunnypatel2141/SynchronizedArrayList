package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

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

	public SynchronizedArrayList(Collection<? extends E> c) throws NullPointerException 
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		Iterator<? extends E> it = c.iterator();
		array = new Object[10];
		int i = 0;
		
		while (it.hasNext())
		{
			array[i] = it.next();
			i++;
			counter++;
		}
	}
	
	public void trimToSize()
	{
		array = trimContents(false);
	}
	
	public void ensureCapacity(int minCapacity)
	{
		if (capacity() < minCapacity)
		{
			array = copyContents(array, new Object[minCapacity]);
		}
	}
	
	public int size()
	{
		return counter;
	}
	
	public boolean isEmpty()
	{
		if (size() == 0)
		{
			return true;
		}
		return false;
	}

	public boolean contains(Object o)
	{
		int index = indexOf(o);
		if (index != -1)
		{
			return true;
		}
		return false;
	}
	
	public int indexOf(Object o)
	{
		int index = -1;
		for (int i = 0; i < size(); i++)
		{
			if (array[i].equals(o))
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int lastIndexOf(Object o)
	{
		int index = -1;
		for (int i = size() - 1; i > -1; i--)
		{
			if (array[i].equals(o))
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	public Object clone()
	{
		return array.clone();
	}
	
	public Object[] toArray()
	{
		return Arrays.copyOf(array, size());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) throws ArrayStoreException, NullPointerException
	{
		int size = size();
		if (a.length < size)
		{
            return (T[]) Arrays.copyOf(array, size, a.getClass());
		}
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
	}

	@SuppressWarnings("unchecked")
	public E get(int index) throws IndexOutOfBoundsException
	{
		if (index < 0 || index >= size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		return (E) array[index];
	}
	
	@SuppressWarnings("unchecked")
	public E set(int index, E element) throws IndexOutOfBoundsException
	{
		if (index < 0 || index > size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		Object obj = array[index];
		array[index] = element;
		return (E) obj;
	}
	
	public boolean add(E e)
	{
		// counter returned not size
		int numOfElements = size();
		int capacity = capacity();
		if (numOfElements + 1 > capacity)
		{
			long validSize = capacity * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				Object[] temp = new Object[(int) validSize];
				array = copyContents(array, temp);
			} else
			{
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
		if (index < 0 || index > size)
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
			array[i] = array[i - 1];
		}
		array[index] = element;
		counter++;
	}
	
	public E remove(int index) throws IndexOutOfBoundsException
	{
		int size = size();
		if (index > size)
		{
			String str = "Index: " + index + ", Size: " + size;
			throw new IndexOutOfBoundsException(str);
		}
		
		@SuppressWarnings("unchecked")
		E obj = (E) array[index];
		for (int i = index; i < size - 1; i++)
		{
			array[i] = array[i + 1];
		}
		array[size - 1] = null;
		counter--;

		size = size();
		
		if (size * 2 < capacity() && capacity() > 10)
		{
			System.out.println("Here");
			array = trimContents(true);
		}
		return obj;
	}
	
	public boolean remove(Object o)
	{
		int index = -1;
		index = indexOf(o);
		if (index != -1)
		{
			counter--;
			for (int i = index; i < size(); i++)
			{
				array[i] = array[i + 1];
			}
			
			array[size()] = null;
			
			if (size() * 2 < capacity() && capacity() > 10)
			{
				array = trimContents(true);
			}
			return true;
		}
		return false;
	}
	
	public void clear()
	{
		array = new Object[10];
		counter = 0;
	}
	
	public boolean addAll(Collection<? extends E> c) throws NullPointerException
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		
		int size = size();
		int cSize = c.size();
		
		while (size + cSize > capacity())
		{
			long validSize = capacity() * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				array = copyContents(array, new Object[(int) validSize]);
			} else
			{
				return false;
			}
		}
		Iterator<? extends E> iterator = c.iterator();
		while (iterator.hasNext())
		{
			array[size] = iterator.next();
			size++;
		}
		counter = size;
		return true;
	}
	
	public boolean addAll(int index, Collection<? extends E> c) throws NullPointerException, IndexOutOfBoundsException 
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		int size = size();
		
		if (index < 0 || index > size)
		{
			String str = "Index: " + index + ", Size: " + size;
			throw new IndexOutOfBoundsException(str);
		}
		
		Object[] tempArr = new Object[size - index];
		for (int i = index, j = 0; i < size; i++, j++)
		{
			tempArr[j] = array[i];
		}
		
		int cSize = c.size();
		
		while (size() + cSize > capacity())
		{
			long validSize = capacity() * growMultiplier;
			if (validSize < Integer.MAX_VALUE)
			{
				array = copyContents(array, new Object[(int) validSize]);
			} else
			{
				return false;
			}
		}
		
		Iterator<? extends E> it = c.iterator();
		int i = index;
		
		while (it.hasNext())
		{
			array[i] = it.next();
			i++;
			counter++;
		}
		
		for (int j = 0, k = index + cSize; j < tempArr.length; j++, k++)
		{
			array[k] = tempArr[j];
		}
		return true;
	}
	
	public void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException
	{
		int size = size();
		if (fromIndex < 0)
		{
			String str = "Start index: " + fromIndex;
			throw new IndexOutOfBoundsException(str);
		}
		if (toIndex > size)
		{
			String str = "End index: " + toIndex + ", Size: " + size;
			throw new IndexOutOfBoundsException(str);			
		}
		if (fromIndex >= size)
		{
			String str = "From Index: " + fromIndex + ", Size: " + size;
			throw new IndexOutOfBoundsException(str);
		}
		if(toIndex < fromIndex)
		{
			String str = "End index: " + toIndex + ", From index: " + fromIndex;
			throw new IndexOutOfBoundsException(str);
		}
		
		Object[] tempArr = new Object[size - toIndex - 1];
		
		for (int i = toIndex + 1, j = 0; i < size; i++, j++)
		{
			tempArr[j] = array[i];
		}
		
		int range = toIndex - fromIndex + 1;
		counter -= range;
		size = size();
		
		while (size * 2 < capacity())
		{
			int len = array.length / 2;
			array = copyContents(array, new Object[len], true);
		}
		
		for (int i = fromIndex, j = 0; j < tempArr.length; j++, i++)
		{
			array[i] = tempArr[j];
		}
	}
	
	public boolean removeAll(Collection<?> c) throws NullPointerException, ClassCastException
	{
		if (c == null) 
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}
		
		//not using remove method because of constant resizing
		// this method utilizes assignment instead of allocating smaller array and copying over elements
		
		boolean changed = false;
		int size = size();
		
		Set<Integer> indices = new HashSet<>();
		
		for (int index = 0; index < size; index++)
		{
			if (!c.contains(array[index]))
			{
				indices.add(index);
			} else {
				if (!changed)
				{
					changed = true;
				}
				counter-=1;
			}
		}
		
		Object[] tempArr = new Object[indices.size()];
		Iterator<Integer> indIt = indices.iterator();
		int travel = 0;
		
		while (indIt.hasNext())
		{
			tempArr[travel] = array[indIt.next()];
			travel++;
		}
		array = tempArr;
		return changed;
	}
	
	public boolean retainAll(Collection<?> c)
	{
		if (c == null) 
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}
		
		//not using remove method because of constant resizing
		// this method utilizes assignment instead of allocating smaller array and copying over elements
		
		boolean changed = false;
		int size = size();
		counter = 0;
		
		Set<Integer> indices = new HashSet<>();
		
		for (int index = 0; index < size; index++)
		{
			if (c.contains(array[index]))
			{
				indices.add(index);
				if (!changed)
				{
					changed = true;
				}
				counter++;
			}
		}
		
		Object[] tempArr = new Object[indices.size()];
		Iterator<Integer> indIt = indices.iterator();
		int travel = 0;
		
		while (indIt.hasNext())
		{
			tempArr[travel] = array[indIt.next()];
			travel++;
		}
		array = tempArr;
		return changed;
	}

//	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException
//	{
//		if (index < 0 || index > size())
//		{
//			String str = "Index: " + index;
//			throw new IndexOutOfBoundsException(str);
//		}
//		ListIterator<E> it = new ListIterator<E>()
//		{
//			
//			
//			@Override
//			public boolean hasNext()
//			{
//				if (index < size())
//				{
//					return true;
//				}
//				return false;
//			}
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public E next() throws NoSuchElementException
//			{
//				if (index > size())
//				{
//					String str = "Next index: " + index + ", Size: " + size();
//					throw new NoSuchElementException(str);
//				}
//				Object obj = array[index];
//				index++;
//				return (E) obj;
//			}
//
//			@Override
//			public boolean hasPrevious()
//			{
//				if (index - 1 > -1)
//				{
//					return true;
//				}
//				return false;
//			}
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public E previous() throws NoSuchElementException
//			{
//				index--;
//				if (index < 0)
//				{
//					String str = "Previous index: " + index;
//					throw new NoSuchElementException(str);
//				}
//				Object obj = array[index];
//				return (E) obj;
//			}
//
//			@Override
//			public int nextIndex()
//			{
//				return index;
//			}
//
//			@Override
//			public int previousIndex()
//			{
//				return (index - 1);
//			}
//
//			@Override
//			public void remove()
//			{
//
//			}
//
//			@Override
//			public void set(E e)
//			{
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void add(E e)
//			{
//				// TODO Auto-generated method stub
//			}
//		};
//		return it;
//	}
	
	public ListIterator<E> listIterator()
	{
		ListIterator<E> it = new ListIterator<E>()
		{
			int index = 0;
			Object obj;

			@Override
			public boolean hasNext()
			{
				if (index < size())
				{
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next() throws NoSuchElementException
			{
				if (index > size())
				{
					String str = "Next index: " + index + ", Size: " + size();
					throw new NoSuchElementException(str);
				}
				obj = array[index];
				index++;
				return (E) obj;
			}

			@Override
			public boolean hasPrevious()
			{
				if (index - 1 > -1)
				{
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E previous() throws NoSuchElementException
			{
				index--;
				if (index < 0)
				{
					String str = "Previous index: " + index;
					throw new NoSuchElementException(str);
				}
				obj = array[index];
				return (E) obj;
			}

			@Override
			public int nextIndex()
			{
				return index;
			}

			@Override
			public int previousIndex()
			{
				return (index - 1);
			}

			@Override
			public void remove()
			{
				SynchronizedArrayList.this.remove(obj);
				index--;
			}

			@Override
			public void set(E e)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void add(E e)
			{
				// TODO Auto-generated method stub
			}
		};
		return it;
	}
	
	public Iterator<E> iterator()
	{
		Iterator<E> it = new Iterator<E>()
		{
			int index = 0;

			@Override
			public boolean hasNext()
			{
				if (index < size())
				{
					return true;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next()
			{
				Object obj = array[index];
				index++;
				return (E) obj;
			}
		};
		return it;
	}
	
	@SuppressWarnings("unchecked")
	public SynchronizedArrayList<E> subList(int fromIndex, int toIndex)
			throws IndexOutOfBoundsException, IllegalArgumentException
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
		SynchronizedArrayList<E> sal = new SynchronizedArrayList<>(length + 1);
		for (int i = fromIndex; i < toIndex; i++)
		{
			sal.add((E) array[i]);
		}
		return sal;
	}

	private Object[] trimContents(boolean buffer)
	{
		int size = size();
		if (buffer)
		{
			size += 1;
		}
		Object[] newArray = new Object[size];
		for (int i = 0; i < size; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}

	private Object[] copyContents(Object[] from, Object[] to)
	{
		for (int i = 0; i < from.length; i++)
		{
			to[i] = from[i];
		}
		return to;
	}
	
	private Object[] copyContents(Object[] from, Object[] to, boolean smallerToIndex)
	{
		for (int i = 0; i < to.length; i++)
		{
			to[i] = from[i];
		}
		return to;
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
		if (length != 0)
		{
			sb.append(array[length - 1]);
		}
		sb.append("]");
		return sb.toString();
	}
}