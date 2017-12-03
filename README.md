# Synchronized ArrayList

SynchronizedArrayList implements a growable array of objects. Like arrays, it contains components that can be accessed using an integer index. The size of of a SynchronizedArrayList can grow or shrink as objects are added or removed after instantiation.  

PerformanceResults.txt file compares runtime of each method as function of time.  
In summary:
- Adding, changing values (using Set method) and searching were faster using SynchronizedArrayList.  
- Remove method of SynchronizedArrayList was the slowest compared to ArrayList and Vector. 
- RemoveAll and RetainAll method of SynchronizedArrayList was slightly slower than ArrayList, but faster than Vector.
- Sublist was tricky because ArrayList and Vector were significantly better than SynchronizedArrayList for small ranges, but for large ranges and higher repetition both ArrayList and Vector threw OutOfMemory exceptions (but SynchronizedArrayList didn't).