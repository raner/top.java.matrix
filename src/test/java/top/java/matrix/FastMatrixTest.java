package top.java.matrix;

import top.java.matrix.fast.FastMatrix;

/**
* {@link FastMatrixTest} is a {@link PerformanceTest} for {@link FastMatrix} multiplication.
*
* @param <M> the dimensions of the matrix
*
* @author Mirko Raner
**/
public class FastMatrixTest<M extends Dimension> extends PerformanceTest<M>
{
    @Override
    protected MatrixConstructor<M, M> constructor()
    {
        return new MatrixConstructor<>(FastMatrix::new);
    }
}
