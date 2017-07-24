//                                                                          //
// Copyright 2017 Mirko Raner                                               //
//                                                                          //
// Licensed under the Apache License, Version 2.0 (the "License");          //
// you may not use this file except in compliance with the License.         //
// You may obtain a copy of the License at                                  //
//                                                                          //
//     http://www.apache.org/licenses/LICENSE-2.0                           //
//                                                                          //
// Unless required by applicable law or agreed to in writing, software      //
// distributed under the License is distributed on an "AS IS" BASIS,        //
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. //
// See the License for the specific language governing permissions and      //
// limitations under the License.                                           //
//                                                                          //
package top.java.matrix.operations.multiplication;

import com.aparapi.Kernel;
import com.aparapi.Range;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.operations.MatrixMultiplication;

/**
* {@link TiledFastMultiplication} implements GPU-accelerated matrix multiplication that uses tiling to make better
* use of local GPU memory. The tile size needs to be chosen manually and all matrix dimensions need to be a multiple
* of the tile size. Also, the tile size needs to be chosen in such a fashion that it does not exceed the GPUs
* available local memory. If no tile size is specified, the implementation will use {@link #DEFAULT_TILE_SIZE}.
* For matrices with dimensions that are incompatible with the tile size, the implementation will fall back
* to the regular {@link FastMultiplication} algorithm.
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
public class TiledFastMultiplication<M extends Dimension, N extends Dimension, K extends Dimension>
extends MatrixMultiplication<M, N, K>
{
    public final static int DEFAULT_TILE_SIZE = 10;

    private int tileSize;

    public TiledFastMultiplication(MatrixFactory factory)
    {
        this(factory, DEFAULT_TILE_SIZE);
    }

    public TiledFastMultiplication(MatrixFactory factory, int tileSize)
    {
        super(factory);
        this.tileSize = tileSize;
    }

    @Override
    public Matrix<M, N> apply(Matrix<M, K> left, Matrix<K, N> right)
    {
        int numberOfRows = left.getRows();
        int numberOfColumns = left.getColumns();
        int targetNumberOfColumns = right.getColumns();
        if (numberOfRows % tileSize + numberOfColumns % tileSize + targetNumberOfColumns % tileSize > 0)
        {
            return new FastMultiplication<M, N, K>(factory).apply(left, right);
        }
        final float[] A = left.getValues();
        final float[] B = right.getValues();
        final float[] C = new float[numberOfRows*targetNumberOfColumns];
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
        return factory.create(Dimension.FACTORY.create(numberOfRows), Dimension.FACTORY.create(targetNumberOfColumns), C);
    }
}
