package com.notayessir.serialize.fastjson;


import com.notayessir.serialize.api.ObjectInput;
import com.notayessir.serialize.api.ObjectOutput;
import com.notayessir.serialize.api.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * fastJSON 序列化实现类
 */
public class FastJSONSerialization extends Serialization {

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        return new FastJSONObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        return new FastJSONObjectInput(inputStream);
    }
}
