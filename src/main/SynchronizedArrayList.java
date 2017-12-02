package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This is a synchronized version of ArrayList. Similar to ArrayList,
 * {@link SynchronizedArrayList} grants clients the ability to store, retrieve,
 * and traverse over elements amongst other features.
 * {@link SynchronizedArrayList} is backed up by an array of E type, with
 * ability to grow and shrink automatically based on the array length.
 * 
 * @author Sunny Patel
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
			synchronized (SynchronizedArrayList.this)
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
			synchronized (SynchronizedArrayList.this)
			{
				SynchronizedArrayList.this.add(index, e2);
				allowed = false;
			}
		}
	}

	/**
	 * Instantiate {@link SynchronizedArrayList} with DEFAULT_SIZE
	 */
	public SynchronizedArrayList()
	{
		this(DEFAULT_SIZE);
	}

	/**
	 * Instantiate {@link SynchronizedArrayList} with @param num
	 * 
	 * @param num size of {@link SynchronizedArrayList}
	 * @throws IllegalArgumentException if ({@code num < 0 || num > Integer.MAX_VALUE - 1})
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
	 * Instantiate {@link SynchronizedArrayList} with elements from Collection c
	 * 
	 * @param c Collection
	 * @throws NullPointerException if ({@code c == null})
	 */
	@SuppressWarnings("unchecked")
	public SynchronizedArrayList(Collection<? extends E> c) throws NullPointerException
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		int len = DEFAULT_SIZE; // 10
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

	/**
	 * Resizes array length to number of elements in {@link SynchronizedArrayList}
	 */
	public synchronized void trimToSize()
	{
		resizeAndCopyContents(size());
	}

	/**
	 * Resizes array to @param minCapacity, if it's greater than capacity()
	 * 
	 * @param minCapacity
	 *            new array length, if greater than capacity()
	 */
	public synchronized void ensureCapacity(int minCapacity)
	{
		if (capacity() < minCapacity)
		{
			resizeAndCopyContents(minCapacity);
		}
	}

	/**
	 * Returns number of elements in the {@link SynchronizedArrayList}
	 * 
	 * @return number of elements
	 */
	public synchronized int size()
	{
		return counter;
	}

	/**
	 * Returns true if {@link SynchronizedArrayList} is empty
	 * 
	 * @return true if empty, false otherwise
	 */
	public synchronized boolean isEmpty()
	{
		return size() == 0 ? true : false;
	}

	/**
	 * Returns true of {@link SynchronizedArrayList} contains Object o
	 * 
	 * @param o
	 *            Object to search for
	 * @return true if {@link SynchronizedArrayList} contains o, false otherwise
	 */
	public synchronized boolean contains(Object o)
	{
		return indexOf(o) == -1 ? false : true;
	}

	/**
	 * Returns index of first occurrence of Object o
	 * 
	 * @param o Object to search index of
	 * @return position of the Object o in {@link SynchronizedArrayList}
	 */
	public synchronized int indexOf(Object o)
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

	/**
	 * Returns the index of last occurrence of passed-in object
	 * 
	 * @param o Object to determine index of
	 * @return index of specified object
	 */
	public int lastIndexOf(Object o)
	{
		synchronized (array)
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

	/**
	 * Returns a clone of this vector. The copy will contain a reference to a clone
	 * of the internal data array, not a reference to the original internal data
	 * array of this {@code Vector} object.
	 *
	 * @return a clone of this vector
	 */
	public Object clone()
	{
		synchronized (array)
		{
			return array.clone();
		}
	}

	/**
	 * Returns an array containing all of the elements in this Vector in the correct
	 * order.
	 */
	public Object[] toArray()
	{
		synchronized (array)
		{
			return Arrays.copyOf(array, size());
		}
	}

	/**
	 * Returns an array containing all of the elements in this Vector in the correct
	 * order; the runtime type of the returned array is that of the specified array.
	 * If the {@link SynchronizedArrayList} fits in the specified array, it is
	 * returned therein. Otherwise, a new array is allocated with the runtime type
	 * of the specified array and the size of this {@link SynchronizedArrayList}.
	 *
	 * <p>
	 * If the {@link SynchronizedArrayList} fits in the specified array with room to
	 * spare (i.e., the array has more elements than the
	 * {@link SynchronizedArrayList}), the element in the array immediately
	 * following the end of the {@link SynchronizedArrayList} is set to null. (This
	 * is useful in determining the length of the {@link SynchronizedArrayList}
	 * <em>only</em> if the caller knows that the {@link SynchronizedArrayList} does
	 * not contain any null elements.)
	 *
	 * @param a
	 *            the array into which the elements of the
	 *            {@link SynchronizedArrayList} are to be stored, if it is big
	 *            enough; otherwise, a new array of the same runtime type is
	 *            allocated for this purpose.
	 * @return an array containing the elements of the {@link SynchronizedArrayList}
	 * @throws ArrayStoreException
	 *             if the runtime type of a is not a supertype of the runtime type
	 *             of every element in this {@link SynchronizedArrayList}
	 * @throws NullPointerException
	 *             if the given array is null
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) throws ArrayStoreException, NullPointerException
	{
		synchronized (array)
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
	 * Returns object at the specified at index
	 * 
	 * @param index index of the element to return
	 * @return object at the specified index
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range
	 *             ({@code index < 0 || index >= size()})
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
	 * Replaces the element at the specified position with the specified element.
	 *
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range
	 *             ({@code index < 0 || index >= size()})
	 */
	public synchronized E set(int index, E element) throws IndexOutOfBoundsException
	{
		if (index < 0 || index >= size())
		{
			String str = "Index: " + index + ", Size: " + size();
			throw new IndexOutOfBoundsException(str);
		}
		E obj = array[index];
		array[index] = element;
		return obj;
	}

	/**
	 * Adds to {@link SynchronizedArrayList} element passed-in
	 * 
	 * @param e element to be added at end of array
	 * @return true if added successfully, false otherwise
	 */
	public synchronized boolean add(E e)
	{
		int numOfElements = size();
		int capacity = capacity();

		// if unable to increase capacity - return false
		if (!rangeCheckAndIncreaseSize(numOfElements + 1, capacity))
		{
			return false;
		}

		array[counter] = e;
		counter++;
		return true;
	}

	/**
	 * Adds to the {@link SynchronizedArrayList} element at specified index
	 * 
	 * @param index position at which element will be added
	 * @param element object to be added
	 * @throws IndexOutOfBoundsException
	 *             if ({@code index < 0 || index > size()})
	 */
	public synchronized void add(int index, E element) throws IndexOutOfBoundsException
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

	/**
	 * Removes from {@link SynchronizedArrayList} the element at passed-in index
	 * 
	 * @param index position of element to be removed
	 * @return true if removed successfully, false otherwise
	 * @throws IndexOutOfBoundsException
	 *             if ({@code index < 0 || index > size()})
	 */
	public synchronized E remove(int index) throws IndexOutOfBoundsException
	{
		int size = size();
		if (index < 0 || index >= size)
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

	/**
	 * Removes from {@link SynchronizedArrayList} the passed-in object
	 * 
	 * @param o element to be removed
	 * @return true if removed successfully, false otherwise
	 */
	public synchronized boolean remove(Object o)
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

	/**
	 * Removes all of the elements from this SynchronizedArrayList.
	 */
	@SuppressWarnings("unchecked")
	public void clear()
	{
		synchronized (array)
		{
			array = (E[]) new Object[DEFAULT_SIZE];
			counter = 0;
		}
	}

	/**
	 * Adds elements from Collection c to SynchronizedArrayList
	 * 
	 * @param c Collection
	 * @return true if added successfully, false otherwise
	 * @throws NullPointerException
	 *             if ({@code c == null})
	 */
	public synchronized boolean addAll(Collection<? extends E> c) throws NullPointerException
	{
		if (c == null)
		{
			String str = "Collection " + c + " is null";
			throw new NullPointerException(str);
		}
		int size = size();
		int cSize = c.size();

		// resize array to accommodate c
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

	/**
	 * Adds elements from Collection c to SynchronizedArrayList starting at index
	 * 
	 * @param index position to insert elements from Collection c
	 * @param c Collection
	 * @return true if added successfully, false otherwise
	 * @throws NullPointerException
	 *             if ({@code c == null})
	 * @throws IndexOutOfBoundsException
	 *             if ({@code index < 0 || index > size()})
	 */
	public synchronized boolean addAll(int index, Collection<? extends E> c)
			throws NullPointerException, IndexOutOfBoundsException
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

		// resize array to accommodate c
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
	 * Removes from {@link SynchronizedArrayList} elements starting at @param
	 * fromIndex to @param toIndex, inclusive
	 * 
	 * @param fromIndex starting point of remove range
	 * @param toIndex end point of removal
	 * @throws IndexOutOfBoundsException
	 *             if ({@code fromIndex < 0 || toIndex > size || fromIndex >= size || toIndex < fromIndex})
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
		if (toIndex < fromIndex)
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

			// divide array in half and copy contents
			resizeAndCopyContents(capacity() / 2);
		}

		for (int i = fromIndex, j = 0; j < tempArr.length; j++, i++)
		{
			array[i] = tempArr[j];
		}
	}

	/**
	 * Removes from {@link SynchronizedArrayList} elements in Collection c
	 * 
	 * @param c Collection
	 * @return true if element(s) changed in {@link SynchronizedArrayList}
	 * @throws NullPointerException if ({@code c==null})
	 * @throws ClassCastException
	 */
	public synchronized boolean removeAll(Collection<?> c) throws NullPointerException, ClassCastException
	{
		if (c == null)
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}

		boolean changed = false;
		int size = size();

		Set<Integer> indices = new HashSet<>();

		for (int index = 0; index < size; index++)
		{
			if (!c.contains(array[index]))
			{
				indices.add(index);
			} else
			{
				if (!changed)
				{
					changed = true;
				}
				counter -= 1;
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

	/**
	 * Retains only the elements common to Collection c and SynchronizedArrayList
	 * object
	 * 
	 * @param c Collection
	 * @return return true if common element(s) found
	 */
	public synchronized boolean retainAll(Collection<?> c)
	{
		if (c == null)
		{
			String str = "Collection " + c + " cannot be null.";
			throw new NullPointerException(str);
		}
		
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

	/**
	 * Returns a ListIterator over {@link SynchronizedArrayList} starting at
	 * index @param index
	 *
	 * @param index starting point of traversal
	 * @return list iterator
	 * @throws IndexOutOfBoundsException
	 *             if ({@code index < 0 || index > size()})
	 */
	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException
	{
		if (index < 0 || index > size())
		{
			String str = "Index: " + index;
			throw new IndexOutOfBoundsException(str);
		}
		synchronized (array)
		{
			return new ListIterSub(index);
		}
	}

	/**
	 * Returns a ListIterator over {@link SynchronizedArrayList} starting at index 0
	 * 
	 * @return list iterator
	 */
	public ListIterator<E> listIterator()
	{
		synchronized (array)
		{
			return new ListIterSub();
		}
	}

	/**
	 * Returns an iterator over {@link SynchronizedArrayList}
	 * 
	 * @return iterator over {@link SynchronizedArrayList}
	 */
	public synchronized Iterator<E> iterator()
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
	 * Returns a subist view of the {@link SynchronizedArrayList} object ranging
	 * from @param fromIndex to @param toIndex
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
	 * Returns a proper representation of {@link SynchronizedArrayList}
	 * 
	 * @return String representation of {@link SynchronizedArrayList}
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
	private void rangeCheckAndDecreaseSize(int size, int capacity)
	{
		if (size * 2 <= capacity && capacity >= 10)
		{
			resizeAndCopyContents(size);
		}
	}

	/**
	 * Resized the array to @param size
	 * 
	 * Not synchronized because each caller of this function is synchronized on
	 * array object
	 * 
	 * @param newSize new array size
	 */
	private void resizeAndCopyContents(int newSize)
	{
		/**
		 * Removed code to improve performance (Arrays.copyOf superior)
		 */
//		@SuppressWarnings("unchecked")
//		E[] tempArr = (E[]) new Object[newSize];
//
//		// traverse until end of smaller array
//		int len = array.length > newSize ? newSize : array.length;
//		for (int i = 0; i < len; i++)
//		{
//			tempArr[i] = array[i];
//		}
//		array = tempArr;
		array = Arrays.copyOf(array, newSize);
	}
}