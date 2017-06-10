package top.java.matrix.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.charset.StandardCharsets.UTF_8;
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
        int rows, columns, offset;
        try (DataInputStream data = new DataInputStream(new FileInputStream(path.toFile())))
        {
            offset = 38;
            byte[] identifier = new byte[9];
            data.readFully(identifier);
            expect(identifier).toBe("Octave-1-");
            byte endianness = data.readByte();
            expect(endianness).toBe('L');
            expect(data.readByte()).toBe(0);
            int length = readLittleEndianInteger(data);
            data.skip(length);
            data.skip(5);
            offset += length;
            expect(data.readByte()).toBe(0xFF);
            length = readLittleEndianInteger(data);
            offset += length;
            byte[] typeName = new byte[length];
            data.readFully(typeName);
            expect(typeName).toBe("matrix");
            byte dimensions = (byte)-readLittleEndianInteger(data);
            expect(dimensions).toBe(2);
            rows = readLittleEndianInteger(data);
            columns = readLittleEndianInteger(data);
        }
        try (FileChannel channel = (FileChannel)Files.newByteChannel(path, READ))
        {
            ByteBuffer matrixMap = channel.map(READ_ONLY, offset, rows*columns*4).order(LITTLE_ENDIAN);
            FloatBuffer floatBuffer = matrixMap.asFloatBuffer();
            float[] array = new float[rows*columns];
            floatBuffer.get(array);
            return RawFloatMatrix.FACTORY.create(rows, columns, array);
        }
    }

    private int readLittleEndianInteger(DataInput input) throws IOException
    {
        byte[] bytes = new byte[4];
        input.readFully(bytes);
        return ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN).getInt(); 
    }

    private Expectation expect(byte... actual)
    {
        return new Expectation(actual);
    }

    private static class Expectation
    {
        byte[] actual;

        Expectation(byte... actual)
        {
            this.actual = actual;
        }

        void toBe(byte... expected) throws IOException
        {
            if (!Arrays.equals(expected, actual))
            {
                throw new IOException("Unexpected file contents");
            }
        }

        void toBe(int value) throws IOException
        {
            toBe(new byte[] {(byte)value});
        }

        void toBe(String value) throws IOException
        {
            toBe(value.getBytes(UTF_8));
        }
    }
}
