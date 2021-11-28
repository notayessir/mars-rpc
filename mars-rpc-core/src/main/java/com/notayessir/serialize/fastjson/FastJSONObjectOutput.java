package com.notayessir.serialize.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.notayessir.serialize.api.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * fastJSON 输出流
 */
public class FastJSONObjectOutput implements ObjectOutput{

    private final PrintWriter writer;

    public FastJSONObjectOutput(OutputStream outputStream) {
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        writer.println(new String(v));
    }

    @Override
    public void writeUTF(String v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.write(obj);
        out.writeTo(writer);
        out.close(); // for reuse SerializeWriter buf
        writer.println();
        writer.flush();
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
    }
}
