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
package top.java.matrix;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import top.java.matrix.internal.StandardMatrix;
import top.java.matrix.operations.multiplication.FastMultiplication;
import top.java.matrix.operations.multiplication.ReversedFastMultiplication;
import top.java.matrix.operations.multiplication.TiledFastMultiplication;

/**
* The {@link AllPerformanceTests} JUnit test suite runs all {@link PerformanceTest}s in a specified order.
*
* @author Mirko Raner
**/
@RunWith(Suite.class)
@SuiteClasses
({
    AllPerformanceTests.SuiteOnly.NaiveMatrixTest.class,
    AllPerformanceTests.SuiteOnly.ReversedFastMatrixTest.class,
    AllPerformanceTests.SuiteOnly.FastMatrixTest.class,
    AllPerformanceTests.SuiteOnly.TiledFastMatrixTest.class
})
public class AllPerformanceTests
{
    static class SuiteOnly
    {
        public static class NaiveMatrixTest<M extends Dimension> extends PerformanceTest<M>
        {
            @Override
            protected MatrixConstructor<M, M> constructor()
            {
                return new MatrixTest.ModifiedMatrixConstructor<>();
            }
        }

        public static class ReversedFastMatrixTest<M extends Dimension> extends PerformanceTest<M>
        {
            @Override
            protected MatrixConstructor<M, M> constructor()
            {
                return new MatrixTest.ModifiedMatrixConstructor<>(new ReversedFastMultiplication<>(StandardMatrix::new));
            }
        }

        public static class FastMatrixTest<M extends Dimension> extends PerformanceTest<M>
        {
            @Override
            protected MatrixConstructor<M, M> constructor()
            {
                return new MatrixTest.ModifiedMatrixConstructor<>(new FastMultiplication<>(StandardMatrix::new));
            }
        }

        public static class TiledFastMatrixTest<M extends Dimension> extends PerformanceTest<M>
        {
            @Override
            protected MatrixConstructor<M, M> constructor()
            {
                return new MatrixTest.ModifiedMatrixConstructor<>(new TiledFastMultiplication<>(StandardMatrix::new));
            }
        }
    }
}
