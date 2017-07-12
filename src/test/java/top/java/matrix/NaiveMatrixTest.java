package top.java.matrix;

import top.java.matrix.naive.NaiveMatrix;

/**
* {@link NaiveMatrixTest} is a {@link PerformanceTest} for {@link NaiveMatrix} multiplication.
*
* @param <M> the dimensions of the matrix
*
* @author Mirko Raner
**/
public class NaiveMatrixTest<M extends Dimension> extends PerformanceTest<M>
{
    @Override
    protected MatrixConstructor<M, M> constructor()
    {
        return new MatrixConstructor<>(NaiveMatrix::new);
    }
}
