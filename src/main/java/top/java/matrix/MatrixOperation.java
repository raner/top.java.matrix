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
* {@link MatrixOperation} is the abstract base class for common matrix operations like multiplication or
* transposition. It is also used for more low-level operations like access to the underlying {@code float}
* array or individual elements.
*
* @author Mirko Raner
**/
public abstract class MatrixOperation
{
    protected MatrixFactory factory;

    public MatrixOperation(MatrixFactory factory)
    {
        this.factory = factory;
    }

    /**
    * Determines the basic type of operation implemented by this object.
    *
    * @return the basic operation type (e.g., {@link top.java.matrix.operations.MatrixMultiplication MatrixMultiplication}
    * or {@link top.java.matrix.operations.MatrixElementAccess MatrixElementAccess}
    **/
    public Class<? extends MatrixOperation> getOperationType()
    {
        Class<?> type = getClass();
        while (!MatrixOperation.class.equals(type.getSuperclass()))
        {
            type = type.getSuperclass();
        }
        @SuppressWarnings("unchecked")
        Class<? extends MatrixOperation> result = (Class<? extends MatrixOperation>)type;
        return result;
    }
}
