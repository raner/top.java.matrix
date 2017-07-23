package top.java.matrix;

import java.util.function.Function;
import top.java.matrix.util.RawFloatMatrix;

/**
* The {@link Matrix} interface describes a single-precision matrix of arbitrary (but type-safe) dimensions.
*
* @param <ROWS> the rows dimension of the matrix
* @param <COLUMNS> the columns dimension of the matrix
*
* @author Mirko Raner
**/
public abstract class Matrix<ROWS extends Dimension, COLUMNS extends Dimension>
{
    public abstract Function<RawFloatMatrix, Matrix<?, ?>> constructor();

    public abstract MatrixFactory factory();

    public abstract <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide);

    public abstract Matrix<COLUMNS, ROWS> transpose();

    public abstract int getRows();

    public abstract int getColumns();

    public abstract float[] getValues();

    public abstract float at(int zeroIndexedRow, int zeroIndexedColumn);
}
