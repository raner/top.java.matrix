package top.java.matrix.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.file.StandardOpenOption.READ;

/**
* {@link OctaveFloatBinaryReader} is a simple reader for Octave files in {@code -float-binary} format.
*
* @author Mirko Raner
**/
public class OctaveFloatBinaryReader
{
    public RawFloatMatrix readFloatBinaryMatrix(Path path) throws IOException
    {
        try (FileChannel channel = (FileChannel)Files.newByteChannel(path, READ))
        {
            ByteBuffer headers = channel.map(READ_ONLY, 41, 8).order(LITTLE_ENDIAN);
            IntBuffer dimensions = headers.asIntBuffer();
            int rows = dimensions.get(0);
            int columns = dimensions.get(1);
            ByteBuffer matrixMap = channel.map(READ_ONLY, 50, rows*columns*4).order(LITTLE_ENDIAN);
            FloatBuffer floatBuffer = matrixMap.asFloatBuffer();
            float[] array = new float[rows*columns];
            floatBuffer.get(array);
            return RawFloatMatrix.FACTORY.create(rows, columns, array);
        }
    }
}
