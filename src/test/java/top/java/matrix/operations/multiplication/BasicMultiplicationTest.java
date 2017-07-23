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

import org.junit.Test;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.internal.StandardMatrix;
import static org.junit.Assert.assertEquals;

public class BasicMultiplicationTest<M extends Dimension, N extends Dimension, K extends Dimension>
{
    @Test
    public void testBasicMultiplicationUsesFactory()
    {
        MatrixFactory factory = StandardMatrix::new;
        BasicMultiplication<M, N, K> multiplication = new BasicMultiplication<>(factory);
        Dimension one = Dimension.FACTORY.create(1);
        Matrix<M, K> left = factory.create(one, one, new float[] {3});
        Matrix<K, N> right = factory.create(one, one, new float[] {5});
        Matrix<M, N> result = multiplication.apply(left, right);
        assertEquals(StandardMatrix.class, result.getClass());
    }
}
