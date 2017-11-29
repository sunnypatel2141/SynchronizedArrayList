package test;

import java.util.ArrayList;

import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

import main.SynchronizedArrayList;

public class CaliperTest extends SimpleBenchmark {

	private SynchronizedArrayList<Point> array;
	private static int DEFAULT_LEN = 10;
	
    public void timeInstantiateNoParam(int reps) {
        for (int i = 0; i < reps; i++) {
        		array = new SynchronizedArrayList<>();
        		for (int j = 0; j < DEFAULT_LEN; j++)
        		{
        			array.add(new Point(j, j, j));
        		}
        }
    }
    
    public void timeInstantiateParam(int reps) {
        for (int i = 0; i < reps; i++) {
        		array = new SynchronizedArrayList<>(100);
        		for (int j = 0; j < DEFAULT_LEN; j++)
        		{
        			array.add(new Point(j, j, j));
        		}
        }
    }
    
    public void timeInstantiateParamCollection(int reps) {
        for (int i = 0; i < reps; i++) {
        		ArrayList<Point> list = new ArrayList<>();
        		for (int j = 0; j < DEFAULT_LEN; j++)
        		{
        			list.add(new Point(j, j, j));
        		}
        		array = new SynchronizedArrayList<>(list);
        }
    }

//    public void timeSystemTime(int reps) {
//        for (int i = 0; i < reps; i++) {
//            System.currentTimeMillis();
//        }
//    }
    
    public static void main(String[] args) {
        Runner.main(CaliperTest.class, args);
    }
}