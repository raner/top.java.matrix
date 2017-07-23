package top.java.matrix.operations.access;

import top.java.matrix.Dimension;
import top.java.matrix.MatrixFactory;
import top.java.matrix.operations.MatrixElementAccess;

public class DirectElementAccess extends MatrixElementAccess
{
    public DirectElementAccess(MatrixFactory factory)
    {
        super(factory);
    }

    @Override
    public float elementAt(float[] matrix, Dimension rows, Dimension columns, int row, int column)
    {
        return matrix[row + column*rows.getAsInt()];
    }
}
