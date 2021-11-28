package com.notayessir.serialize.api;

import com.notayessir.serialize.fastjson.FastJSONSerialization;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 序列化抽象类
 */
public abstract class Serialization {


    public abstract ObjectOutput serialize(OutputStream outputStream) throws IOException;

    public abstract ObjectInput deserialize(InputStream inputStream) throws IOException;


    public static Serialization getSerialization(byte serializeId){
        return new FastJSONSerialization();
    }


    /**
     * 序列化框架，未来会引入更多其他框架
     */
    public enum Type {


        /**
         * 阿里 fastjson
         */
        FASTJSON((byte) 0x10);


        private final byte id;

        Type(byte id) {
            this.id = id;
        }

        public byte getId() {
            return id;
        }

        public static Type getById(byte id) {
            Type[] types = Type.values();
            for (Type type : types) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return FASTJSON;
        }

        public static Type getByValue(String value) {
            Type[] types = Type.values();
            for (Type serialization : types) {
                if (StringUtils.equalsIgnoreCase(serialization.name(), value)) {
                    return serialization;
                }
            }
            return FASTJSON;
        }

    }
}
