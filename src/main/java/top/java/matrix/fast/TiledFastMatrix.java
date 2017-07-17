package top.java.matrix.fast;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import com.aparapi.Kernel;
import com.aparapi.Range;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.util.RawFloatMatrix;

/**
* {@link TiledFastMatrix} implements a GPU-accelerated matrix that uses tiling to make better use of
* local GPU memory. The tile size needs to be chosen manually and all matrix dimensions need to be a multiple
* of the tile size. Also, the tile size needs to be chosen in such a fashion that it does not exceed the GPUs
* available local memory. If no tile size is specified, the implementation will use {@link #DEFAULT_TILE_SIZE}.
* For matrices with dimensions that are incompatible with the tile size, the implementation will fall back
* to the regular {@link FastMatrix} algorithms.
*
* For multiplying two 1000-by-1000 matrices with a tile size of 10 on a CPU with Intel HD Graphics 6000, this
* implementation is about 35 times faster than the naive, CPU-based implementation. With a tile size of 5 the
* speed increase is about half of that. So far, tile sizes of 5 and 10 are the only tile sizes that have been
* used successfully for 1000-by-1000 matrix multiplication.
*
* <B>NOTE:</B> for a tile size of 1 (i.e., 1-by-1 tiles) this implementation will become extremely slow
* (in fact, slower than the naive implementation on the CPU).
*
* @author Mirko Raner
**/
public class TiledFastMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    public final static int DEFAULT_TILE_SIZE = 10;

    private int rows;
    private int columns;
    private float[] matrix;
    private int tileSize;

    public TiledFastMatrix(RawFloatMatrix matrix)
    {
        this(matrix, DEFAULT_TILE_SIZE);
    }

    public TiledFastMatrix(RawFloatMatrix matrix, int tileSize)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
        this.tileSize = tileSize;
    }

    @Override
    public Function<RawFloatMatrix, Matrix<?, ?>> constructor()
    {
        return TiledFastMatrix::new;
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        int numberOfRows = rows;
        int numberOfColumns = columns;
        int targetNumberOfColumns = rightHandSide.getColumns();
        if (numberOfRows % tileSize + numberOfColumns % tileSize + targetNumberOfColumns % tileSize > 0)
        {
            return new FastMatrix<ROWS, COLUMNS>(RawFloatMatrix.FACTORY.create(rows, columns, matrix)).times(rightHandSide);
        }
        final float[] A = matrix;
        final float[] B = rightHandSide.getValues();
        final float[] C = new float[rows*targetNumberOfColumns];
        final int TILE_SIZE = tileSize;
        Kernel kernel = new Kernel()
        {
            @Local final float[] tileA = new float[TILE_SIZE*TILE_SIZE];
            @Local final float[] tileB = new float[TILE_SIZE*TILE_SIZE];

            @Override
            public void run()
            {
                @Constant int numberOfTiles = numberOfColumns/TILE_SIZE;
                int row = getGlobalId(0);
                int column = getGlobalId(1);
                int localRow = getLocalId(0);
                int localColumn = getLocalId(1);
                float value = 0;

                for (int tile = 0; tile < numberOfTiles; tile++)
                {
                    @Constant int tiledRow = TILE_SIZE*tile + localRow;
                    @Constant int tiledColumn = TILE_SIZE*tile + localColumn;
                    tileA[localColumn*TILE_SIZE + localRow] = A[tiledColumn*numberOfRows + row];
                    tileB[localColumn*TILE_SIZE + localRow] = B[column*numberOfColumns + tiledRow];

                    // Ensure that the entire tile is loaded before starting the computation:
                    //
                    localBarrier();

                    for (int repeat = 0; repeat < TILE_SIZE; repeat++)
                    {
                        value += tileA[repeat*TILE_SIZE + localRow] * tileB[localColumn*TILE_SIZE + repeat];
                    }

                    // Make sure all computations are finished before loading the next tile:
                    //
                    localBarrier();
                }
                C[column*numberOfRows + row] = value;
            }
        };
        kernel.execute(Range.create2D(numberOfRows, targetNumberOfColumns, TILE_SIZE, TILE_SIZE));
        RawFloatMatrix rawMatrix = RawFloatMatrix.FACTORY.create(numberOfRows, targetNumberOfColumns, C);
        @SuppressWarnings("unchecked")
        Matrix<ROWS, DIMENSION> result = (Matrix<ROWS, DIMENSION>)constructor().apply(rawMatrix);
        return result;
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
}
