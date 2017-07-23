package top.java.matrix.fast;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import com.aparapi.Kernel;
import com.aparapi.Range;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.util.RawFloatMatrix;

/**
* {@link ReversedFastMatrix} implements a GPU-accelerated matrix, but with reversed row and column order for its
* multiplication operation (i.e., target columns first, then target rows). This performs the same function as the
* regular {@link FastMatrix}, but due to the less sequential memory access it is about 2.5x slower (though it is
* still about 3x faster than naive matrix multiplication). Therefore, in practice, there is very little reason to
* use {@link ReversedFastMatrix}, and it mainly serves as a reference implementation for performance comparisons.
*
* @author Mirko Raner
**/
public class ReversedFastMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    private int rows;
    private int columns;
    private float[] matrix;

    public ReversedFastMatrix(RawFloatMatrix matrix)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
    }

    @Override
    public Function<RawFloatMatrix, Matrix<?, ?>> constructor()
    {
        return ReversedFastMatrix::new;
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        int numberOfRows = rows;
        int numberOfColumns = columns;
        int targetNumberOfRows = rightHandSide.getRows();
        int targetNumberOfColumns = rightHandSide.getColumns();
        final float[] A = matrix;
        final float[] B = rightHandSide.getValues();
        final float[] C = new float[rows*targetNumberOfColumns];
        Kernel kernel = new Kernel()
        {
            @Override
            public void run() {
                int row = getGlobalId(1);
                int column = getGlobalId(0);
                int value = 0;
                for (int repeat = 0; repeat < numberOfColumns; repeat++)
                {
                    value += A[row + repeat*numberOfRows] * B[repeat + column*targetNumberOfRows];
                }
                C[row + column*numberOfRows] = value;
            }
         };
         kernel.execute(Range.create2D(targetNumberOfColumns, numberOfRows));
         return new ReversedFastMatrix<>(RawFloatMatrix.FACTORY.create(numberOfRows, targetNumberOfColumns, C));
    }

    @Override
    public Matrix<COLUMNS, ROWS> transpose()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRows()
    {
        return rows;
    }

    @Override
    public int getColumns()
    {
        return columns;
    }

    @Override
    public float[] getValues()
    {
        return matrix;
    }

    @Override
    public float at(int zeroIndexedRow, int zeroIndexedColumn)
    {
        return matrix[zeroIndexedRow + zeroIndexedColumn*rows];
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                string.append(matrix[row + column*rows]).append(' ');
            }
            string.append(System.getProperty("line.separator"));
        }
        return string.toString();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Matrix)
        {
            Matrix<?, ?> otherMatrix = (Matrix<?, ?>)other;
            if (rows == otherMatrix.getRows() && columns == otherMatrix.getColumns())
            {
                return Arrays.equals(otherMatrix.getValues(), matrix);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(rows, columns, Arrays.hashCode(matrix));
    }

    @Override
    public MatrixFactory factory()
    {
        throw new UnsupportedOperationException();
    }
}
