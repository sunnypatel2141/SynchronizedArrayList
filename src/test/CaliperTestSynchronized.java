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

				for (int j = 0; j < DEFAULT_LEN; j++)
				{
					assertTrue(array.contains(new Point(j, j, j)));
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	

	// public void timeInstantiateParam(int reps) {
	// for (int i = 0; i < reps; i++) {
	// preSyncArray = new PreSyncArrayList<>(100);
	// for (int j = 0; j < DEFAULT_LEN; j++)
	// {
	// preSyncArray.add(new Point(j, j, j));
	// }
	// }
	// }
	//
	// public void timeInstantiateParamCollection(int reps) {
	// for (int i = 0; i < reps; i++) {
	// ArrayList<Point> list = new ArrayList<>();
	// for (int j = 0; j < DEFAULT_LEN; j++)
	// {
	// list.add(new Point(j, j, j));
	// }
	// preSyncArray = new PreSyncArrayList<>(list);
	// }
	// }
	//

	// public void timeSystemTime(int reps) {
	// for (int i = 0; i < reps; i++) {
	// System.currentTimeMillis();
	// }
	// }

	public static void main(String[] args)
	{
		Runner.main(CaliperTestSynchronized.class, args);
	}
}