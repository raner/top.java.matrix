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
package top.java.matrix.operations;

import org.junit.Test;
import top.java.matrix.MatrixOperation;
import top.java.matrix.internal.StandardMatrix;
import top.java.matrix.operations.access.DirectElementAccess;
import top.java.matrix.operations.multiplication.BasicMultiplication;
import static org.junit.Assert.assertEquals;

public class MatrixOperationTest
{
    @Test
    public void getOperationTypeOfBasicMultiplication()
    {
        Class<? extends MatrixOperation> result = new BasicMultiplication<>(StandardMatrix::new).getOperationType();
        assertEquals(MatrixMultiplication.class, result);
    }

    @Test
    public void getOperationTypeOf()
    {
        Class<? extends MatrixOperation> result = new DirectElementAccess(StandardMatrix::new).getOperationType();
        assertEquals(MatrixElementAccess.class, result);
    }
}
