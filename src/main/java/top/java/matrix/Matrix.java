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
public interface Matrix<ROWS extends Dimension, COLUMNS extends Dimension>
{
    Function<RawFloatMatrix, Matrix<?, ?>> constructor();

    MatrixFactory factory();

    <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide);

    Matrix<COLUMNS, ROWS> transpose();

    int getRows();

    int getColumns();

    float[] getValues();

    float at(int zeroIndexedRow, int zeroIndexedColumn);
}
