package top.java.matrix;

import top.java.matrix.fast.TiledFastMatrix;

/**
* {@link TiledFastMatrixTest} is a {@link PerformanceTest} for {@link TiledFastMatrix} multiplication.
*
* @param <M> the dimensions of the matrix
*
* @author Mirko Raner
**/
public class TiledFastMatrixTest<M extends Dimension> extends PerformanceTest<M>
{
    @Override
    protected MatrixConstructor<M, M> constructor()
    {
        return new MatrixConstructor<>(TiledFastMatrix::new);
    }
}
