package top.java.matrix;

import top.java.matrix.fast.ReversedFastMatrix;

/**
* {@link ReversedFastMatrixTest} is a {@link PerformanceTest} for {@link ReversedFastMatrix} multiplication.
*
* @param <M> the dimensions of the matrix
*
* @author Mirko Raner
**/
public class ReversedFastMatrixTest<M extends Dimension> extends PerformanceTest<M>
{
    @Override
    protected MatrixConstructor<M, M> constructor()
    {
        return new MatrixConstructor<>(ReversedFastMatrix::new);
    }
}
