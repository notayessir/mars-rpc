package com.notayessir.serialize.api;

import java.io.IOException;

/**
 * 对象写出接口
 */
public interface ObjectOutput {

    void writeBoolean(boolean v) throws IOException;

    void writeByte(byte v) throws IOException;

    void writeShort(short v) throws IOException;

    void writeInt(int v) throws IOException;

    void writeLong(long v) throws IOException;

    void writeFloat(float v) throws IOException;

    void writeDouble(double v) throws IOException;

    void writeBytes(byte[] v) throws IOException;

    void writeUTF(String v) throws IOException;

    void writeObject(Object obj) throws IOException;

    void flushBuffer() throws IOException;
}
