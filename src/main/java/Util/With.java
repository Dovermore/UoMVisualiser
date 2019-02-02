package Util;


import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * This Class is adapted from answer by `akuhn`
 * https://stackoverflow.com/questions/477550/is-there-a-way-to-access-an-iteration-counter-in-javas-for-each-loop
 * This class enables the use of index in foreach loop. By invoke `Index index.value` and `Index index.index`
 */
public class With {
    public static class Index<G> {
        private G value;
        private int index;

        public Index(G value, int index) {
            this.value = value;
            this.index = index;
        }

        public G value() {
            return value;
        }

        public int index() {
            return index;
        }
    }

    // Static function to create Iterable<Index<Type>>
    public static <T> Iterable<Index<T>> index(final Iterable<T> array) {
        // Return a new Iterable with such attribute
        return new Iterable<Index<T>>() {
            // Define how such Iterable is implemented (Only has one `iterator` method)
            @NotNull
            public Iterator<Index<T>> iterator() {
                // Return a Iterator
                return new Iterator<Index<T>>() {
                    // Counter used in foreach loop
                    int index = 0;
                    Iterator<T> iterator = array.iterator();
                    // Define how internal of this works
                    public boolean hasNext() { return iterator.hasNext(); }
                    public Index<T> next() { return new Index<>(iterator.next(), index++); }
                };
            }
        };
    }
}
