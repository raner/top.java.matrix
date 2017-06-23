package top.java.matrix;

import java.util.function.Function;

import top.java.matrix.util.RawFloatMatrix;

/**
* A {@link MatrixConstructor} provides a mechanism for creating {@link Matrix} objects.
*
* @param <M> the first dimension
* @param <N> the second dimension
*
* @author Mirko Raner
**/
public class MatrixConstructor<M extends Dimension, N extends Dimension>
{
    private final static RawFloatMatrix RAW = RawFloatMatrix.FACTORY.create(0, 0, new float[0]);

    private Function<RawFloatMatrix, Matrix<M, N>> constructor;

    public MatrixConstructor(Function<RawFloatMatrix, Matrix<M, N>> constructor)
    {
        this.constructor = constructor;
    }

    public Matrix<M, N> construct(RawFloatMatrix rawMatrix)
    {
        return constructor.apply(rawMatrix);
    }

    @Override
    public String toString()
    {
        return construct(RAW).getClass().getSimpleName();
    }
}
