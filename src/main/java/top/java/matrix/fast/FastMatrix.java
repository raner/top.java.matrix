package top.java.matrix.fast;

import java.util.Arrays;
import java.util.Objects;
import com.aparapi.Kernel;
import com.aparapi.Range;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.util.RawFloatMatrix;

/**
* {@link FastMatrix} implements a GPU-accelerated matrix.
*
* @author Mirko Raner
**/
public class FastMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    private int rows;
    private int columns;
    private float[] matrix;

    public FastMatrix(RawFloatMatrix matrix)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
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
                int row = getGlobalId(0);
                int column = getGlobalId(1);
                int value = 0;
                for (int repeat = 0; repeat < numberOfColumns; repeat++)
                {
                    value += A[row + repeat*numberOfRows] * B[repeat + column*targetNumberOfRows];
                }
                C[row + column*numberOfRows] = value;
            }
         };
         kernel.execute(Range.create2D(numberOfRows, targetNumberOfColumns));
         return new FastMatrix<>(RawFloatMatrix.FACTORY.create(numberOfRows, targetNumberOfColumns, C));
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
        if (other instanceof FastMatrix)
        {
            FastMatrix<?, ?> otherMatrix = (FastMatrix<?, ?>)other;
            if (rows == otherMatrix.getRows() && columns == otherMatrix.getColumns())
            {
                return Arrays.equals(otherMatrix.matrix, matrix);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(rows, columns, Arrays.hashCode(matrix));
    }
}
