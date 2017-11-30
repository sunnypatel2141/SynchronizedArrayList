package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Sunny
 *
 * @param <E>
 */
public class SynchronizedArrayList<E>
{
	private E[] array;
	private int counter = 0;
	private int growMultiplier = 2;
	private final static int DEFAULT_SIZE = 10;
	private final static int NOT_FOUND = -1;
	
	class ListIterSub implements ListIterator<E>
	{
		int index;
		int modificationInd;
		boolean allowed = true;
		
		ListIterSub()
		{
			this(0);
		}
		
		ListIterSub(int index)
		{
			this.index = index;
		}
		
		@Override
		public boolean hasNext()
		{
			if (index < size())
			{
				return true;
			}
			return false;
		}

		@Override
		public synchronized E next() throws NoSuchElementException
		{
			if (index > size())
			{
				String str = "Next index: " + index + ", Size: " + size();
				throw new NoSuchElementException(str);
			}
			if (!allowed)
			{
				allowed = true;
			}
			modificationInd = index;
			index++;
			return array[modificationInd];
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

		@Override
		public synchronized E previous() throws NoSuchElementException
		{
			index--;
			if (index < 0)
			{
				String str = "Previous index: " + index;
				throw new NoSuchElementException(str);
			}
			if (!allowed)
			{
				allowed = true;
			}
			modificationInd = index;
			return array[index];
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
		public synchronized void remove()
		{
			if (!allowed)
			{
				throw new IllegalStateException("Operation not allowed!");
			}
			SynchronizedArrayList.this.remove(modificationInd);
			if (nextIndex() != 0)
			{
				index--;
			}
			allowed = false;
		}

		@Override
		public void set(E eNew)
		{
			if (!allowed)
			{
				throw new IllegalStateException("Operation not allowed!");
			}
			synchronized(SynchronizedArrayList.this)
			{
				SynchronizedArrayList.this.set(modificationInd, eNew);
				allowed = false;
			}
		}

		@Override
		public void add(E e2)
		{
			if (!allowed)
			{
				throw new IllegalStateException("Operation not allowed!");
			}
			synchronized(SynchronizedArrayList.this)
			{
				SynchronizedArrayList.this.add(index, e2);
				allowed = false;
			}
		}
	}

	/**
	 * 
	 */
	public SynchronizedArrayList()
	{
		this(DEFAULT_SIZE);
	}

	/**
	 * SuppressWarning makes sense as only elements of type E will be added
	 * to the array
	 */
	/**
	 * @param num
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public SynchronizedArrayList(int num) throws IllegalArgumentException
	{
		if (num < 0 || num > Integer.MAX_VALUE - 1)
		{
			throw new IllegalArgumentException("Can't instantiate a SynchronizedArrayList with size " + num);
		}
		array = (E[]) new Object[num];
	}

	/**
	 * SuppressWarning makes sense as only elements of type E will be added
	 * to the array
	 */
	/**
	 * @param c
	 * @throws NullPointerException
	 */
	@SuppressWarnings("unchecked")
	public SynchronizedArrayList(Collection<? extends E> c) throws NullPointerException 
	{
		synchronized (array)
		{
			if (c == null)
			{
				String str = "Collection " + c + " is null";
				throw new NullPointerException(str);
			}
			int len = DEFAULT_SIZE; //10
			int cSize = c.size();
			if (cSize > 10)
			{
				len = cSize + (cSize % 10);
			}	
			array = (E[]) new Object[len];
			
			Iterator<? extends E> it = c.iterator();
			int i = 0;
			while (it.hasNext())
			{	
				array[i] = it.next();
				i++;
			}
			counter = cSize;
		}
	}
	
	/**
	 * 
	 */
	public synchronized void trimToSize()
	{
		resizeAndCopyContents(size());
	}
	
	/**
	 * @param minCapacity
	 */
	public synchronized void ensureCapacity(int minCapacity)
	{
		if (capacity() < minCapacity)
		{
			resizeAndCopyContents(minCapacity);
		}
	}
	
	/**
	 * @return
	 */
	public int size()
	{
		return counter;
	}
	
	/**
	 * @return
	 */
	public boolean isEmpty()
	{
		return size() == 0 ? true : false;
	}

	/**
	 * @param o
	 * @return
	 */
	public boolean contains(Object o)
	{
		return indexOf(o) == -1 ? false : true;
	}
	
	/**
	 * @param o
	 * @return
	 */
	public int indexOf(Object o)
	{
		synchronized(array) 
		{
			for (int i = 0; i < size(); i++)
			{
				if (array[i].equals(o))
				{
					return i;
				}
			}
			return NOT_FOUND;
		}
	}
	
	/**
	 * @param o
	 * @return
	 */
	public int lastIndexOf(Object o)
	{
		synchronized(array)
		{
			for (int i = size() - 1; i > -1; i--)
			{
				if (array[i].equals(o))
				{
					return i;
				}
			}
			return NOT_FOUND;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		synchronized(array)
		{
			return array.clone();
		}
	}
	
	/**
	 * @return
	 */
	public Object[] toArray()
	{
		synchronized(array)
		{
			return Arrays.copyOf(array, size());
		}
	}
	
	/**
	 * @param a
	 * @return
	 * @throws ArrayStoreException
	 * @throws NullPointerException
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) throws ArrayStoreException, NullPointerException
	{
		synchronized(array)
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
	}

	/**
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public E get(int index) throws IndexOutOfBoundsException
	{
		if (index < 0 || index >= size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		return array[index];
	}
	
	/**
	 * @param index
	 * @param element
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public E set(int index, E element) throws IndexOutOfBoundsException
	{
		if (index < 0 || index > size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		synchronized(array)
		{
			E obj = array[index];
			array[index] = element;
			return obj;
		}
	}
	
	/**
	 * @param e
	 * @return
	 */
	public boolean add(E e)
	{
		synchronized(array)
		{
			int numOfElements = size();
			int capacity = capacity();
			
			//if unable to increase capacity - return false
			if (!rangeCheckAndIncreaseSize(numOfElements + 1, capacity))
			{
				return false;
			}
			
			array[counter] = e;
			counter++;
			return true;
		}
	}

	/**
	 * @param index
	 * @param element
	 * @throws IndexOutOfBoundsException
	 */
	public void add(int index, E element) throws IndexOutOfBoundsException
	{
		synchronized(array)
		{
			int size = size();
			if (index < 0 || index > size)
			{
				throw new IndexOutOfBoundsException();
			}
			rangeCheckAndIncreaseSize(size + 1, capacity());
			for (int i = size; i > index; i--)
			{
				array[i] = array[i - 1];
			}
			array[index] = element;
			counter++;
		}
	}
	
	/**
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public E remove(int index) throws IndexOutOfBoundsException
	{
		synchronized(array)
		{
			int size = size();
			if (index > size)
			{
				String str = "Index: " + index + ", Size: " + size;
				throw new IndexOutOfBoundsException(str);
			}
			
			E obj = array[index];
			for (int i = index; i < size - 1; i++)
			{
				array[i] = array[i + 1];
			}
			array[size - 1] = null;
			counter--;
	
			size = size();
			rangeCheckAndDecreaseSize(size, capacity());
			return obj;
		}
	}
	
	/**
	 * @param o
	 * @return
	 */
	public boolean remove(Object o)
	{
		synchronized(array)
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
				
				rangeCheckAndDecreaseSize(size(), capacity());
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void clear()
	{
		synchronized(array)
		{
			array = (E[]) new Object[DEFAULT_SIZE];
			counter = 0;
		}
	}
	
	/**
	 * @param c
	 * @return
	 * @throws NullPointerException
	 */
	public boolean addAll(Collection<? extends E> c) throws NullPointerException
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		synchronized(array)
		{
			int size = size();
			int cSize = c.size();
			
			//resize array to accommodate c
			while (true)
			{
				if (!rangeCheckAndIncreaseSize(size + cSize, capacity()))
				{
					return false;
				}
				if (size + cSize <= capacity())
				{
					break;
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
	}
	
	public synchronized boolean addAll(int index, Collection<? extends E> c) throws NullPointerException, IndexOutOfBoundsException 
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		int size = size(), cSize = c.size();
		
		if (index < 0 || index > size)
		{
			String str = "Index: " + index + ", Size: " + size;
			throw new IndexOutOfBoundsException(str);
		}
		
		@SuppressWarnings("unchecked")
		E[] tempArr = (E[]) new Object[size - index];
		for (int i = index, j = 0; i < size; i++, j++)
		{
			tempArr[j] = array[i];
		}
		
		//resize array to accommodate c
		while (true)
		{
			if (!rangeCheckAndIncreaseSize(size + cSize, capacity()))
			{
				return false;
			}
			if (size + cSize <= capacity())
			{
				break;
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
	
	/**
	 * @param fromIndex
	 * @param toIndex
	 * @throws IndexOutOfBoundsException
	 */
	public synchronized void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException
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
		
		@SuppressWarnings("unchecked")
		E[] tempArr = (E[]) new Object[size - toIndex - 1];
		
		for (int i = toIndex + 1, j = 0; i < size; i++, j++)
		{
			tempArr[j] = array[i];
		}
		
		int range = toIndex - fromIndex + 1;
		counter -= range;
		size = size();
		
		while (true)
		{
			if (size * 2 >= capacity() || capacity() <= 10)
			{
				break;
			}

			//divide array in half and copy contents
			resizeAndCopyContents(capacity() / 2);
		}
		
		for (int i = fromIndex, j = 0; j < tempArr.length; j++, i++)
		{
			array[i] = tempArr[j];
		}
	}
	
	/**
	 * @param c
	 * @return
	 * @throws NullPointerException
	 * @throws ClassCastException
	 */
	public boolean removeAll(Collection<?> c) throws NullPointerException, ClassCastException
	{
		if (c == null) 
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}
		
		//not using remove method because of constant resizing
		// this method utilizes assignment instead of allocating smaller array and copying over elements
		synchronized(array)
		{
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
			
			@SuppressWarnings("unchecked")
			E[] tempArr = (E[]) new Object[indices.size()];
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
	}
	
	/**
	 * @param c
	 * @return
	 */
	public boolean retainAll(Collection<?> c)
	{
		if (c == null) 
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}
		
		//not using remove method because of constant resizing
		// this method utilizes assignment instead of allocating smaller array and copying over elements
		synchronized(array)
		{
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
			
			@SuppressWarnings("unchecked")
			E[] tempArr = (E[]) new Object[indices.size()];
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
	}

	/**
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException
	{
		if (index < 0 || index > size())
		{
			String str = "Index: " + index;
			throw new IndexOutOfBoundsException(str);
		}
		synchronized(array)
		{
			return new ListIterSub(index);
		}
	}
	
	/**
	 * @return
	 */
	public ListIterator<E> listIterator()
	{
		synchronized(array)
		{
			return new ListIterSub();
		}
	}
	
	/**
	 * @return
	 */
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

			@Override
			public E next()
			{
				E obj = array[index];
				index++;
				return obj;
			}
		};
		return it;
	}
	
	/**
	 * Returns a subist view of the SynchronizedArrayList object ranging from
	 * @param fromIndex to @param toIndex
	 * 
	 * @param fromIndex starting index of sublist
	 * @param toIndex end index of sublist, inclusive
	 * @return
	 * @throws IndexOutOfBoundsException if ({@code fromIndex < 0 || toIndex > size()})
	 * @throws IllegalArgumentException if ({@code fromIndex > toIndex})
	 */
	public synchronized SynchronizedArrayList<E> subList(int fromIndex, int toIndex)
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
			sal.add(array[i]);
		}
		return sal;
	}
	
	/**
	 * Returns array length
	 * 
	 * @return array length
	 */
	public synchronized int capacity()
	{
		return array.length;
	}

	/** 
	 * Returns a proper representation of SynchronizedArrayList
	 * 
	 * @return String representation of SynchronizedArrayList
	 */
	public synchronized String toString()
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
	
	/**
	 * Increases array size if doubled array length < Integer.MAX_VALUE
	 * 
	 * @param i number of elements
	 * @param size array length
	 * @return true if resized, false otherwise
	 */
	private synchronized boolean rangeCheckAndIncreaseSize(int i, int size)
	{
		if (i > size)
		{
			long validSize = growMultiplier * size;
			if (validSize > Integer.MAX_VALUE)
			{
				return false;
			}
			resizeAndCopyContents((int) validSize);
		}
		return true;
	}
	
	/**
	 * Checks if size of array can be halved and resizes it
	 * 
	 * @param size number of elements in array
	 * @param capacity array length
	 */
	private synchronized void rangeCheckAndDecreaseSize(int size, int capacity)
	{
		if (size * 2 <= capacity && capacity >= 10)
		{
			resizeAndCopyContents(size);
		}
	}
	
	/**
	 * Resized the array to @param size
	 * 
	 * Not synchronized because each caller of this function
	 * is synchronized on array object
	 * 
	 * @param newSize new array size
	 */
	private void resizeAndCopyContents(int newSize)
	{
		/**
		 * Suppress Warning makes sense as 
		 * only elements of type E are added
		 */
		@SuppressWarnings("unchecked")
		E[] tempArr = (E[]) new Object[newSize];
		
		//traverse until end of smaller array
		int len = array.length > newSize ? newSize : array.length;
		for (int i = 0; i < len; i++)
		{
			tempArr[i] = array[i];
		}
		array = tempArr;
	}
}