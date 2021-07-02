# Parallel Sorting Project

This project is a completely bogus scenario because sorting a bunch of **String** data is super easy with the
Streams API or with the **TreeSet** class which implements the **SortedSet** interface. However, the usefulness
of this sample project is to demonstrate how forking and joining of processes to make use of multiple cores for
parallel processing is super easy.

## Concurrency Concepts

The first main concept to understand is the _Executors_ and **ExecutorService** (along with a few other classes we
will soon discuss) in the **java.util.concurrent** package. This package is in Java 8, Java 11, and Java 16, but in
Java 11 or Java 16 the developer has to import the necessary classes from the **java.base** module if the project
is using the Java modules packaging mechanism introduced with Java 9.

It also significantly helps for the developer to understand the difference between a **Runnable** and a **Callable**
interface in normal Java threading because there are analogs to each in the forking and joining world of parallel
processing. It is also helpful to understand the concept of a **Future** and how those are used since forking and
joining process uses **Future** objects under the hood.

### ForkJoinPool

The next major concept to understand is that all threads for the parallel processing will need to participate in a
**ForkJoinPool** instance. There are several nice static methods on this class to get a default thread pool, but as
the developer's needs dictate, the developer has plenty of flexibility to customize the creation/instantiation of a
**ForkJoinPool** including specifying factories for how the developer wishes or needs to create a thread pool when
necessary.

In this project, the *parallelSort* method in the **Sorter** class shows the simplest example of how an instance
of the **ForkJoinPool** gets created and then invoked with a **RecursiveTask** that is described in the following
section. The **RecursiveTask** actually does the job of sorting the data in this project while the thread or
threads in the **ForkJoinPool** will manage the multiple threads doing the actual sorting work.

### RecursiveAction vs. RecursiveTask

The next major concept to consider when splitting up processing across multiple cores is whether the developer is
just doing some work that does not return a value (**RecursiveAction**) or if the work returns a computed value of
some sort (**RecursiveTask**). These two abstract classes are collectively referred to as the **ForkJoinTask**
because that is the parent abstract class from which they both inherit behavior.

The developer necessarily needs to create a class that subclasses from either **RecursiveAction** or
**RecursiveTask** and that describes in detail how to recursively divide the work as needed as well as how to
compute or process the data for each subset of data on each core. Both the **RecursiveAction** and
**RecursiveTask** abstract classes define an abstract *compute* method that the developer needs to implement to
describe how the **ForkJoinTask** needs to compute or process the data for that thread.

In the *compute* method, the developer also needs to write the code that describes how to merge computed results,
particularly when working with a **RecursiveTask**. In this sample project I have created, the
**RecursiveSortingTask** is an example of how this all works.

## Summary

This may seem like an overwhelming amount of information the first time a developer is introduced to all these
concepts, but I encourage you to play around with all these classes and interfaces and write some super simple
demo projects like I did here to familiarize yourself with the concepts. It really does become so much clearer
as you actually use the JDK and develop multithreaded solutions.

## Pro-Tips

1. Use a logging framework to test and debug! I prefer **SLF4J** and the *Logback* logging framework, but use
whatever logging framework you are happy and comfortable with and then put lots of *DEBUG* level stuff in your
log output. It becomes so much easier to see what's going on across multiple threads when you have that available.
The alternative to logging is to use your application in debug mode and stop at various breakpoints to see what
thread is executing what code and what is going on, though I find that approach more tedious in practice and tend
to prefer the logging approach for tracking what my threads are doing.

1. Write unit tests that prove that your **RecursiveAction** and/or **RecursiveTask** implementations properly
compute and merge the results as you expect them to do. Your parallel processing will be completely useless if
your results from the implemented **ForkJoinTask** are wrong!
   
Happy threading!
