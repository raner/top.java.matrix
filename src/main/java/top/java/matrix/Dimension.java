package top.java.matrix;

import java.util.function.IntSupplier;

public interface Dimension extends IntSupplier
{
    public final class One implements Dimension
    {
        @Override
        public int getAsInt()
        {
            return 1;
        }
    }

    public interface Factory
    {
        Dimension create(int size);
    }

    Factory FACTORY = size -> () -> size;
}
