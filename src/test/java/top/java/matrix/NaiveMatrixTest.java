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

/**
* {@link NaiveMatrixTest} is a {@link PerformanceTest} for {@link NaiveMatrix} multiplication.
*
* @param <M> the dimensions of the matrix
*
* @author Mirko Raner
**/
public class NaiveMatrixTest<M extends Dimension> extends PerformanceTest<M>
{
    @Override
    protected MatrixConstructor<M, M> constructor()
    {
        return new MatrixTest.ModifiedMatrixConstructor<>();
    }
}
