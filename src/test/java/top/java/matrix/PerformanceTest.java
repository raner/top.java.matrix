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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import top.java.matrix.util.OctaveFloatBinaryReader;
import static java.util.stream.Collectors.toList;

/**
* {@link PerformanceTest} is an abstract base class for matrix multiplication performance testing.
*
* @param <M> the dimensions of the test matrix
*
* @author Mirko Raner
**/
@RunWith(Parameterized.class)
public abstract class PerformanceTest<M extends Dimension>
{
    public final static int REPETITIONS = 10;

    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> thousand;

    @Parameter
    public int repetition;

    @Parameters(name="Run #{0}")
    public static List<Object[]> implementations()
    {
        return IntStream.range(1, REPETITIONS+1).mapToObj(index -> new Object[] {index}).collect(toList());
    }

    @Before
    public void initializeMatrices() throws IOException
    {
        // TODO: load only once!
        thousand = constructor().construct(reader.readFloatBinaryMatrix(path("Thousand.float.bin")));
    }

    @Test
    public void testLargeMultiplication1000x1000()
    {
        thousand.times(thousand);
    }

    protected abstract MatrixConstructor<M, M> constructor();

    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
