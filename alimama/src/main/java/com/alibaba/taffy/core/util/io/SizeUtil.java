package com.alibaba.taffy.core.util.io;

import com.taobao.weex.el.parse.Operators;

public class SizeUtil {
    private static final long UNIT = 1024;

    public static String fitString(long j) {
        return fit(j).toString();
    }

    public static long fitTo(long j, SizeType sizeType, SizeType sizeType2) {
        return sizeType2.floor(sizeType.toBytes(j));
    }

    public static SizeInfo fit(long j) {
        SizeType sizeType;
        SizeType sizeType2 = SizeType.BYTES;
        SizeType next = sizeType2.next();
        while (true) {
            SizeType sizeType3 = next;
            sizeType = sizeType2;
            sizeType2 = sizeType3;
            if (j < 1024 && sizeType2 != null) {
                j /= 1024;
                next = sizeType2.next();
            }
        }
        return new SizeInfo(j, sizeType);
    }

    public enum SizeType {
        BYTES {
            public long floor(long j) {
                return j;
            }

            public SizeType prev() {
                return null;
            }

            public long toBytes(long j) {
                return j;
            }

            public long toGB(long j) {
                return j * 1024 * 1024 * 1024;
            }

            public long toKB(long j) {
                return j * 1024;
            }

            public long toMB(long j) {
                return j * 1024 * 1024;
            }

            public long toPB(long j) {
                return j * 1024 * 1024 * 1024 * 1024 * 1024;
            }

            public long toTB(long j) {
                return j * 1024 * 1024 * 1024 * 1024;
            }

            public SizeType next() {
                return KB;
            }
        },
        KB {
            public long toGB(long j) {
                return j * 1024 * 1024;
            }

            public long toKB(long j) {
                return j;
            }

            public long toMB(long j) {
                return j * 1024;
            }

            public long toPB(long j) {
                return j * 1024 * 1024 * 1024 * 1024;
            }

            public long toTB(long j) {
                return j * 1024 * 1024 * 1024;
            }

            public long floor(long j) {
                return j / 1024;
            }

            public SizeType next() {
                return MB;
            }

            public SizeType prev() {
                return BYTES;
            }

            public long toBytes(long j) {
                return j / 1024;
            }
        },
        MB {
            public long toGB(long j) {
                return j * 1024;
            }

            public long toMB(long j) {
                return j;
            }

            public long toPB(long j) {
                return j * 1024 * 1024 * 1024;
            }

            public long toTB(long j) {
                return j * 1024 * 1024;
            }

            public long floor(long j) {
                return (j / 1024) / 1024;
            }

            public SizeType next() {
                return GB;
            }

            public SizeType prev() {
                return KB;
            }

            public long toBytes(long j) {
                return (j / 1024) / 1024;
            }

            public long toKB(long j) {
                return j / 1024;
            }
        },
        GB {
            public long toGB(long j) {
                return j;
            }

            public long toPB(long j) {
                return j * 1024 * 1024;
            }

            public long toTB(long j) {
                return j * 1024;
            }

            public long floor(long j) {
                return ((j / 1024) / 1024) / 1024;
            }

            public SizeType next() {
                return TB;
            }

            public SizeType prev() {
                return MB;
            }

            public long toBytes(long j) {
                return ((j / 1024) / 1024) / 1024;
            }

            public long toKB(long j) {
                return (j / 1024) / 1024;
            }

            public long toMB(long j) {
                return j / 1024;
            }
        },
        TB {
            public long toPB(long j) {
                return j * 1024;
            }

            public long toTB(long j) {
                return j;
            }

            public long floor(long j) {
                return (((j / 1024) / 1024) / 1024) / 1024;
            }

            public SizeType next() {
                return PB;
            }

            public SizeType prev() {
                return GB;
            }

            public long toBytes(long j) {
                return (((j / 1024) / 1024) / 1024) / 1024;
            }

            public long toKB(long j) {
                return ((j / 1024) / 1024) / 1024;
            }

            public long toMB(long j) {
                return (j / 1024) / 1024;
            }

            public long toGB(long j) {
                return j / 1024;
            }
        },
        PB {
            public SizeType next() {
                return null;
            }

            public long toPB(long j) {
                return j;
            }

            public long floor(long j) {
                return ((((j / 1024) / 1024) / 1024) / 1024) / 1024;
            }

            public SizeType prev() {
                return TB;
            }

            public long toBytes(long j) {
                return ((((j / 1024) / 1024) / 1024) / 1024) / 1024;
            }

            public long toKB(long j) {
                return (((j / 1024) / 1024) / 1024) / 1024;
            }

            public long toMB(long j) {
                return ((j / 1024) / 1024) / 1024;
            }

            public long toGB(long j) {
                return (j / 1024) / 1024;
            }

            public long toTB(long j) {
                return j / 1024;
            }
        };

        public SizeType next() {
            throw new AbstractMethodError();
        }

        public SizeType prev() {
            throw new AbstractMethodError();
        }

        public long floor(long j) {
            throw new AbstractMethodError();
        }

        public long toBytes(long j) {
            throw new AbstractMethodError();
        }

        public long toKB(long j) {
            throw new AbstractMethodError();
        }

        public long toMB(long j) {
            throw new AbstractMethodError();
        }

        public long toGB(long j) {
            throw new AbstractMethodError();
        }

        public long toTB(long j) {
            throw new AbstractMethodError();
        }

        public long toPB(long j) {
            throw new AbstractMethodError();
        }
    }

    public static class SizeInfo {
        public long size = 0;
        public SizeType unit;

        public SizeInfo(long j, SizeType sizeType) {
            this.size = j;
            this.unit = sizeType;
        }

        public String toString() {
            return this.size + Operators.SPACE_STR + this.unit.name();
        }
    }
}
