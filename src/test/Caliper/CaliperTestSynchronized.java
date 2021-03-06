package test.Caliper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import main.SynchronizedArrayList;
import test.Point;

public class CaliperTestSynchronized extends SimpleBenchmark
{
	private static final int TEN_THOUSAND = 10000;
	private SynchronizedArrayList<Point> array = new SynchronizedArrayList<Point>() {{
	    add(new Point(0, 0, 0));
	    add(new Point(1, 1, 1));
	    add(new Point(2, 2, 2));
	    add(new Point(3, 3, 3));
	    add(new Point(4, 4, 4));
	    add(new Point(5, 5, 5));
	    add(new Point(6, 6, 6));
	    add(new Point(7, 7, 7));
	    add(new Point(8, 8, 8));
	    add(new Point(9, 9, 9));
	}};
			
	private static int DEFAULT_LEN = 100;
	
	public void timeOverloadedAddCompare(int reps)
	{
		for (int i = 0; i < reps; i++)
		{
			SynchronizedArrayList<Point> arrayLocal = new SynchronizedArrayList<>();

			Thread thread = new Thread()
			{
				public void run()
				{
					arrayLocal.add(new Point(0, 0, 0));
				}
			};
			Thread thread2 = new Thread()
			{
				public void run()
				{

					arrayLocal.add(0, new Point(1, 1, 1));
				}
			};
			Thread thread3 = new Thread()
			{
				public void run()
				{
					List<Point> list = new ArrayList<>();
					for (int k = 2; k < DEFAULT_LEN; k++)
					{
						list.add(new Point(k, k, k));
					}
					arrayLocal.addAll(list);
				}
			};

			try
			{
				thread.start();
				thread2.start();
				thread3.start();
				
				thread.join();
				thread2.join();
				thread3.join();

//				for (int j = 0; j < DEFAULT_LEN; j++)
//				{
//					assertTrue(array.contains(new Point(j, j, j)));
//				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void timeCompareSetMultiThreaded(int reps)
	{
		for (int i = 0; i < reps; i++)
		{		
			array = new SynchronizedArrayList<>();
			for (int j = 0; j < DEFAULT_LEN; j++)
			{
				array.add(new Point(j, j, j));
			}
			
			Thread thread = new Thread()
			{
				public void run()
				{
					for (int k = 2 * DEFAULT_LEN - 1; k > DEFAULT_LEN; k--)
					{
						array.set(k - DEFAULT_LEN, new Point(k, k, k));
					}
				}
			};
			Thread thread2 = new Thread()
			{
				public void run()
				{
					for (int k = DEFAULT_LEN; k < 2 * DEFAULT_LEN; k++)
					{
						int val = k * 3;
						array.set(k - DEFAULT_LEN, new Point(val, val, val));
					}
				}
			};
			
			try
			{
				thread2.start();
				thread.start();
				
				thread.join();
				thread2.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void timeCompareRemove(int reps)
	{
		for (int i = 0; i < reps; i++)
		{
			Thread thread = new Thread()
			{
				public void run()
				{
					while (!array.isEmpty())
					{
						array.remove(0);
					}
				}
			};
			
			Thread thread2 = new Thread()
			{
				public void run()
				{
					for (int j = 0; j < DEFAULT_LEN; j++)
					{
						array.add(new Point(j, j, j));
					}
				}
			};
			
			Thread thread3 = new Thread()
			{
				public void run()
				{
					for (int j = 0; j < DEFAULT_LEN; j++)
					{
						array.remove(new Point(j, j, j));
					}
				}
			};
			
			try
			{
				thread.start();
				thread.join();
				
				thread2.start();
				thread2.join();
				
				thread3.start();
				thread3.join();
				
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void timeCompareRemoveAllAndRetainAllMultiThreaded(int reps)
	{
		for (int i = 0; i < reps; i++)
		{	
			List<Point> list = new ArrayList<>();
			for (int k = 0; k < DEFAULT_LEN; k++)
			{
				list.add(new Point(k, k, k));
			}
			
			Thread thread = new Thread()
			{
				public void run()
				{
					array.removeAll(list);
				}
			};
			
			Thread thread2 = new Thread()
			{
				public void run()
				{
					array.retainAll(list);
				}
			};
			
			Thread thread3 = new Thread()
			{
				public void run()
				{
					for (int j = 0; j < DEFAULT_LEN; j++)
					{
						array.removeAll(list);
					}
				}
			};
			
			try
			{
				thread.start();
				thread2.start();
				thread3.start();

				thread.join();
				thread2.join();
				thread3.join();
				
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void timeSearchInLargeArray(int reps)
	{
		for (int i = 0; i < reps; i++)
		{
			array = new SynchronizedArrayList<>();
			for (int j = 0; j < TEN_THOUSAND; j++)
			{
				array.add(new Point(j, j, j));
			}
			Random r = new Random();
			for (int j = 0; j < DEFAULT_LEN; j++)
			{
				int val = r.nextInt(TEN_THOUSAND);
				Point p = new Point(val, val, val);
				array.indexOf(p);
				array.lastIndexOf(p);
			}
		}
	}
	
	public void timeSublist(int reps)
	{
		for (int i = 0; i < reps; i++)
		{
			for (int k = 0; k < 1000; k++)
			{
				array.add(new Point(k, k, k));
			}
			
			for (int k = 400; k < 500; k++)
			{
				array.subList(k, k * 2);
			}
		}
	}
	
	public static void main(String[] args)
	{
		Runner.main(CaliperTestSynchronized.class, args);
	}
}