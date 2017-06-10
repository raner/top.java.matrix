package top.java.matrix.naive;

import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.util.RawFloatMatrix;

public class NaiveMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    private int rows;
    private int columns;
    private float[] matrix;

    public NaiveMatrix(RawFloatMatrix matrix)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        // TODO Auto-generated method stub
        return null;
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
}
