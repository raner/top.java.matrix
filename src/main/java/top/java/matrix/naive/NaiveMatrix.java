package top.java.matrix.naive;

import java.util.Arrays;
import java.util.Objects;

import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.util.RawFloatMatrix;

public class NaiveMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    private int rows;
    private int columns;
    private float[] matrix;

    public NaiveMatrix(int rows, int columns, float[] matrix)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
    }

    public NaiveMatrix(RawFloatMatrix matrix)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        // Multiplication example:
        //
        // ROWS X COLUMNS   COLUMNS X DIMENSION     ROWS x DIMENSION
        //      3 x 5         5 x 2                      3 x 2
        //
        //                   ⎡p q⎤
        //  ⎡a b c d e⎤     ⎢r s⎥       ⎡ap+br+ct+dv+ex   aq+bs+cu+dw+ey⎤
        //  ⎢f g h i j⎥  X  ⎢t u⎥   =   ⎢fp+gr+ht+iv+jx   fq+gs+hu+iw+jy⎥
        //  ⎣k l m n o⎦     ⎢v w⎥       ⎣kp+lr+mt+nv+ox   kq+ls+mu+nw+yo⎦
        //                   ⎣x y⎦
        //
        int dimension = rightHandSide.getColumns();
        float[] result = new float[rows*dimension];
        for (int x = 0; x < dimension; x++) // columns
        {
            for (int y = 0; y < rows; y++) // rows
            {
                int sum = 0;
                for (int z = 0; z < columns; z++)
                {
                    sum += matrix[y + z*rows] * rightHandSide.at(z, x);
                }
                result[y + x*rows] = sum;
            }
        }
        return new NaiveMatrix<>(rows, dimension, result);
    }

    @Override
    public Matrix<COLUMNS, ROWS> transpose()
    {
        return null;
    }

    @Override
    public float at(int zeroIndexedRow, int zeroIndexedColumn)
    {
        return matrix[zeroIndexedRow + zeroIndexedColumn*rows];
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
    public boolean equals(Object other)
    {
        if (other instanceof NaiveMatrix)
        {
            NaiveMatrix<?, ?> otherMatrix = (NaiveMatrix<?, ?>)other;
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
