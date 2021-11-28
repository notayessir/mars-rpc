package com.notayessir.serialize.fastjson;


import com.alibaba.fastjson.JSON;
import com.notayessir.serialize.api.ObjectInput;

import java.io.*;
import java.lang.reflect.Type;

/**
 * fastJSON 读入流
 */
public class FastJSONObjectInput implements ObjectInput {

    private final BufferedReader reader;

    public FastJSONObjectInput(InputStream inputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public boolean readBoolean() throws IOException {
        return read(boolean.class);
    }

    @Override
    public byte readByte() throws IOException {
        return read(byte.class);
    }

    @Override
    public short readShort() throws IOException {
        return read(short.class);
    }

    @Override
    public int readInt() throws IOException {
        return read(int.class);
    }

    @Override
    public long readLong() throws IOException {
        return read(long.class);
    }

    @Override
    public float readFloat() throws IOException {
        return read(float.class);
    }

    @Override
    public double readDouble() throws IOException {
        return read(double.class);
    }

    @Override
    public byte[] readBytes() throws IOException {
        return readLine().getBytes();
    }

    @Override
    public String readUTF() throws IOException {
        return read(String.class);
    }

    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return read(cls);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        String json = readLine();
        return JSON.parseObject(json, type);
    }

    private String readLine() throws IOException {
        String line = reader.readLine();
        if (line == null || line.trim().length() == 0) {
            throw new EOFException();
        }
        return line;
    }

    private <T> T read(Class<T> cls) throws IOException {
        String json = readLine();
        return JSON.parseObject(json, cls);
    }
}
