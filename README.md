# Synchronized ArrayList

This is an implementation of ArrayList data structure with synchronized operations.  

PerformanceResults.txt file compares runtime of each method as function of time.  
In summary:
- Adding, changing values (using Set method) and searching were faster using SynchronizedArrayList.  
- Remove method of SynchronizedArrayList was the slowest compared to ArrayList and Vector. 
- RemoveAll and RetainAll method of SynchronizedArrayList was slightly slower than ArrayList, but faster than Vector.
- Sublist was tricky because ArrayList and Vector were significantly better than SynchronizedArrayList for small ranges, but for large ranges and higher repetition both ArrayList and Vector threw OutOfMemory exceptions (but SynchronizedArrayList didn't).