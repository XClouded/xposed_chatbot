package io.flutter.plugin.common;

import io.flutter.plugin.common.StandardMessageCodec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class StandardMethodCodec implements MethodCodec {
    public static final StandardMethodCodec INSTANCE = new StandardMethodCodec(StandardMessageCodec.INSTANCE);
    private final StandardMessageCodec messageCodec;

    public StandardMethodCodec(StandardMessageCodec standardMessageCodec) {
        this.messageCodec = standardMessageCodec;
    }

    public ByteBuffer encodeMethodCall(MethodCall methodCall) {
        StandardMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new StandardMessageCodec.ExposedByteArrayOutputStream();
        this.messageCodec.writeValue(exposedByteArrayOutputStream, methodCall.method);
        this.messageCodec.writeValue(exposedByteArrayOutputStream, methodCall.arguments);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }

    public MethodCall decodeMethodCall(ByteBuffer byteBuffer) {
        byteBuffer.order(ByteOrder.nativeOrder());
        Object readValue = this.messageCodec.readValue(byteBuffer);
        Object readValue2 = this.messageCodec.readValue(byteBuffer);
        if ((readValue instanceof String) && !byteBuffer.hasRemaining()) {
            return new MethodCall((String) readValue, readValue2);
        }
        throw new IllegalArgumentException("Method call corrupted");
    }

    public ByteBuffer encodeSuccessEnvelope(Object obj) {
        StandardMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new StandardMessageCodec.ExposedByteArrayOutputStream();
        exposedByteArrayOutputStream.write(0);
        this.messageCodec.writeValue(exposedByteArrayOutputStream, obj);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }

    public ByteBuffer encodeErrorEnvelope(String str, String str2, Object obj) {
        StandardMessageCodec.ExposedByteArrayOutputStream exposedByteArrayOutputStream = new StandardMessageCodec.ExposedByteArrayOutputStream();
        exposedByteArrayOutputStream.write(1);
        this.messageCodec.writeValue(exposedByteArrayOutputStream, str);
        this.messageCodec.writeValue(exposedByteArrayOutputStream, str2);
        this.messageCodec.writeValue(exposedByteArrayOutputStream, obj);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(exposedByteArrayOutputStream.size());
        allocateDirect.put(exposedByteArrayOutputStream.buffer(), 0, exposedByteArrayOutputStream.size());
        return allocateDirect;
    }

    public Object decodeEnvelope(ByteBuffer byteBuffer) {
        byteBuffer.order(ByteOrder.nativeOrder());
        switch (byteBuffer.get()) {
            case 0:
                Object readValue = this.messageCodec.readValue(byteBuffer);
                if (!byteBuffer.hasRemaining()) {
                    return readValue;
                }
                break;
            case 1:
                break;
        }
        Object readValue2 = this.messageCodec.readValue(byteBuffer);
        Object readValue3 = this.messageCodec.readValue(byteBuffer);
        Object readValue4 = this.messageCodec.readValue(byteBuffer);
        if ((readValue2 instanceof String) && ((readValue3 == null || (readValue3 instanceof String)) && !byteBuffer.hasRemaining())) {
            throw new FlutterException((String) readValue2, (String) readValue3, readValue4);
        }
        throw new IllegalArgumentException("Envelope corrupted");
    }
}
