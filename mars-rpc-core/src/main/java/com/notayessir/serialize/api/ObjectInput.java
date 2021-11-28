package com.notayessir.serialize.api;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 对象读入接口
 */
public interface ObjectInput{

    boolean readBoolean() throws IOException;

    byte readByte() throws IOException;

    short readShort() throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    float readFloat() throws IOException;

    double readDouble() throws IOException;

    byte[] readBytes() throws IOException;

    String readUTF() throws IOException;

    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;

    <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;
}
