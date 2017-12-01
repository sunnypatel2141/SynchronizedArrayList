package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

import main.SynchronizedArrayList;

public class CaliperTestSynchronized extends SimpleBenchmark
{
	private SynchronizedArrayList<Point> array;
	private static int DEFAULT_LEN = 5;
	private static String str = "[[0.0, 0.0, 0.0], [1.0, 1.0, 1.0], [2.0, 2.0, 2.0], [3.0, 3.0, 3.0], [4.0, 4.0, 4.0]]";

	public void timeInstantiateAdd(int reps)
	{
		for (int i = 0; i < reps; i++)
		{
			array = new SynchronizedArrayList<>();

			Thread thread = new Thread()
			{
				public void run()
				{
					array.add(new Point(0, 0, 0));
				}
			};
			Thread thread2 = new Thread()
			{
				public void run()
				{

					array.add(0, new Point(1, 1, 1));
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
					array.addAll(list);
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
	
	public void timeInstantiateSet(int reps)
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
					for (int k = 2 * DEFAULT_LEN; k > DEFAULT_LEN; k--)
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

	public static void main(String[] args)
	{
		Runner.main(CaliperTestSynchronized.class, args);
	}
}