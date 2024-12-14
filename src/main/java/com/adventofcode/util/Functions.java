package com.adventofcode.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class Functions {
    private Functions() {
        // Utility class
    }

    /**
     * Pairs up each element on the first sequence with the
     * corresponding element of the second sequence, returning
     * the list of pairs.
     * 
     * The list is guaranteed to have the same size as the number
     * of elements on the first sequence.
     * 
     * <pre>
     * var a = List.of(1, 2, 3);
     * var b = List.of(4, 5, 6);
     * 
     * System.out.println(zip(a, b));
     * // Prints:
     * // [Pair[first=1, second=4],
     * // Pair[first=2, second=5],
     * // Pair[first=3, second=6]]
     * </pre>
     * 
     * @param <A>    Type parameter of the first iterable
     * @param <B>    Type parameter of the second iterable
     * @param first  Iterable representing the main sequence of elements
     * @param second Iterable representing the second sequence of elements
     * @return A list mapping the two sequences to a single sequence of pairs
     */
    public static <A, B> List<Pair<A, B>> zip(Iterable<A> first, Iterable<B> second) {
        var result = new ArrayList<Pair<A, B>>();

        var itA = first.iterator();
        var itB = second.iterator();

        while (itA.hasNext()) {
            result.add(new Pair<>(itA.next(),
                    itB.hasNext() ? itB.next() : null));
        }

        return result;
    }

    /**
     * Wait for all futures to complete
     * 
     * @param <T>     Type of the futures.
     * @param futures Collection of futures.
     */
    public static <T> void joinAll(Iterable<CompletableFuture<T>> futures) {
        for (var future : futures) {
            future.join();
        }
    }

    /**
     * Wait for all futures to complete and clear the collection afterwards.
     * 
     * @param <T>     Type of the futures.
     * @param futures Collection of futures.
     */
    public static <T> void joinAllAndClear(Collection<CompletableFuture<T>> futures) {
        joinAll(futures);
        futures.clear();
    }

    /**
     * Prints an object and returns itself. Useful for debugging streams
     * by passing this function to map.
     * 
     * @param <T>    Type of the object.
     * @param object Object to be printed.
     * @return Same object passed as input
     */
    public static <T> T println(T object) {
        System.out.println(object);
        return object;
    }
}
