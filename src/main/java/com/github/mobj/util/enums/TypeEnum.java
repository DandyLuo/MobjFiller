package com.github.mobj.util.enums;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.mobj.util.exception.RegexpIllegalException;
import com.github.mobj.util.exception.TypeNotMatchException;
import com.github.mobj.util.exception.UninitializedException;
import com.github.mobj.util.regex.OrdinaryNode;
import org.apache.commons.lang3.RandomUtils;

/**
 * 根据Class类型获取对应的枚举，并执行不同的操作
 * @author luoruihua
 * @date 2020/12/12
 */
public enum TypeEnum {
    /**
     * 枚举的class可能有两个，基本类型和包装类型
     */
    SET_BYTE(byte.class, Byte.class) {
        @Override
        public Byte apply() {
            return (byte)(RandomUtils.nextInt() & 0xEF);
        }
    },
    SET_INT(int.class, Integer.class) {
        @Override
        public Integer apply() {
            return RandomUtils.nextInt();
        }
    },
    SET_Long(long.class, Long.class) {
        @Override
        public Long apply() {
            return RandomUtils.nextLong();
        }
    },
    SET_SHORT(short.class, Short.class) {
        @Override
        public Short apply() {
            return (short)(RandomUtils.nextInt() & 0xEFFF);
        }
    },
    SET_FLOAT(float.class, Float.class) {
        @Override
        public Float apply() {
            return RandomUtils.nextFloat();
        }
    },
    SET_DOUBLE(double.class, Double.class) {
        @Override
        public Double apply() {
            return RandomUtils.nextDouble();
        }
    },
    SET_CHARACTER(char.class, Character.class) {
        @Override
        public Character apply() {
            return (char) RandomUtils.nextInt(-128, 128);
        }
    },
    SET_BOOLEAN(boolean.class, Boolean.class) {
        @Override
        public Boolean apply() {
            return RandomUtils.nextBoolean();
        }
    },
    SET_DATE(Date.class, Date.class) {
        @Override
        public Date apply() {
            return new Date();
        }
    },
    SET_STRING(String.class, String.class) {
        @Override
        public String apply() {
            try {
                return new OrdinaryNode("\\w{1,20}").random();
            } catch (final RegexpIllegalException | TypeNotMatchException | UninitializedException e) {
                e.printStackTrace();
                return "default";
            }
        }
    },
    SET_BIG_DECIMAL(BigDecimal.class, BigDecimal.class) {
        @Override
        public Long apply() {
             return RandomUtils.nextLong(1, Long.MAX_VALUE);
        }
    };
    private final Class primitiveClazz;
    private final Class boxedClazz;
    TypeEnum(final Class primitiveClazz, final Class boxedClazz) {
        this.primitiveClazz = primitiveClazz;
        this.boxedClazz = boxedClazz;
    }

    public abstract Object apply();

    public static Optional<TypeEnum> parse(Class targetClazz) {
        return Stream.of(values()).filter(t -> t.primitiveClazz == targetClazz || t.boxedClazz == targetClazz).findAny();
    }

}
