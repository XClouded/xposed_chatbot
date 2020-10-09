package com.taobao.xcode.szxing.qrcode.encoder;

import com.facebook.imageutils.JfifUtil;
import com.taobao.xcode.szxing.EncodeHintType;
import com.taobao.xcode.szxing.WriterException;
import com.taobao.xcode.szxing.common.BitArray;
import com.taobao.xcode.szxing.common.CharacterSetECI;
import com.taobao.xcode.szxing.common.reedsolomon.GenericGF;
import com.taobao.xcode.szxing.common.reedsolomon.ReedSolomonEncoder;
import com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel;
import com.taobao.xcode.szxing.qrcode.decoder.Mode;
import com.taobao.xcode.szxing.qrcode.decoder.Version;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import kotlin.UByte;

public final class Encoder {
    private static final int[] ALPHANUMERIC_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1};
    static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";

    private Encoder() {
    }

    private static int calculateMaskPenalty(ByteMatrix byteMatrix) {
        return MaskUtil.applyMaskPenaltyRule1(byteMatrix) + MaskUtil.applyMaskPenaltyRule2(byteMatrix) + MaskUtil.applyMaskPenaltyRule3(byteMatrix) + MaskUtil.applyMaskPenaltyRule4(byteMatrix);
    }

    public static QRCode encode(String str, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        return encode(str, errorCorrectionLevel, (Map<EncodeHintType, ?>) null);
    }

    public static QRCode encode(String str, ErrorCorrectionLevel errorCorrectionLevel, Map<EncodeHintType, ?> map) throws WriterException {
        return encode(str, errorCorrectionLevel, map, (Version) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006c, code lost:
        if (r7.getVersionNumber() < r8.getVersionNumber()) goto L_0x007c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x008d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.xcode.szxing.qrcode.encoder.QRCode encode(java.lang.String r5, com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel r6, java.util.Map<com.taobao.xcode.szxing.EncodeHintType, ?> r7, com.taobao.xcode.szxing.qrcode.decoder.Version r8) throws com.taobao.xcode.szxing.WriterException {
        /*
            if (r7 != 0) goto L_0x0004
            r7 = 0
            goto L_0x000c
        L_0x0004:
            com.taobao.xcode.szxing.EncodeHintType r0 = com.taobao.xcode.szxing.EncodeHintType.CHARACTER_SET
            java.lang.Object r7 = r7.get(r0)
            java.lang.String r7 = (java.lang.String) r7
        L_0x000c:
            if (r7 != 0) goto L_0x0010
            java.lang.String r7 = "ISO-8859-1"
        L_0x0010:
            com.taobao.xcode.szxing.qrcode.decoder.Mode r0 = chooseMode(r5, r7)
            com.taobao.xcode.szxing.common.BitArray r1 = new com.taobao.xcode.szxing.common.BitArray
            r1.<init>()
            com.taobao.xcode.szxing.qrcode.decoder.Mode r2 = com.taobao.xcode.szxing.qrcode.decoder.Mode.BYTE
            if (r0 != r2) goto L_0x002e
            java.lang.String r2 = "ISO-8859-1"
            boolean r2 = r2.equals(r7)
            if (r2 != 0) goto L_0x002e
            com.taobao.xcode.szxing.common.CharacterSetECI r2 = com.taobao.xcode.szxing.common.CharacterSetECI.getCharacterSetECIByName(r7)
            if (r2 == 0) goto L_0x002e
            appendECI(r2, r1)
        L_0x002e:
            appendModeInfo(r0, r1)
            com.taobao.xcode.szxing.common.BitArray r2 = new com.taobao.xcode.szxing.common.BitArray
            r2.<init>()
            appendBytes(r5, r0, r2, r7)
            int r7 = r1.getSize()
            r3 = 1
            com.taobao.xcode.szxing.qrcode.decoder.Version r4 = com.taobao.xcode.szxing.qrcode.decoder.Version.getVersionForNumber(r3)
            int r4 = r0.getCharacterCountBits(r4)
            int r7 = r7 + r4
            int r4 = r2.getSize()
            int r7 = r7 + r4
            com.taobao.xcode.szxing.qrcode.decoder.Version r7 = chooseVersion(r7, r6)
            int r4 = r1.getSize()
            int r7 = r0.getCharacterCountBits(r7)
            int r4 = r4 + r7
            int r7 = r2.getSize()
            int r4 = r4 + r7
            com.taobao.xcode.szxing.qrcode.decoder.Version r7 = chooseVersion(r4, r6)
            if (r8 == 0) goto L_0x006f
            int r3 = r7.getVersionNumber()
            int r4 = r8.getVersionNumber()
            if (r3 >= r4) goto L_0x007b
            goto L_0x007c
        L_0x006f:
            int r8 = r7.getVersionNumber()
            if (r8 != r3) goto L_0x007b
            r7 = 2
            com.taobao.xcode.szxing.qrcode.decoder.Version r8 = com.taobao.xcode.szxing.qrcode.decoder.Version.getVersionForNumber(r7)
            goto L_0x007c
        L_0x007b:
            r8 = r7
        L_0x007c:
            com.taobao.xcode.szxing.common.BitArray r7 = new com.taobao.xcode.szxing.common.BitArray
            r7.<init>()
            r7.appendBitArray(r1)
            com.taobao.xcode.szxing.qrcode.decoder.Mode r1 = com.taobao.xcode.szxing.qrcode.decoder.Mode.BYTE
            if (r0 != r1) goto L_0x008d
            int r5 = r2.getSizeInBytes()
            goto L_0x0091
        L_0x008d:
            int r5 = r5.length()
        L_0x0091:
            appendLengthInfo(r5, r8, r0, r7)
            r7.appendBitArray(r2)
            com.taobao.xcode.szxing.qrcode.decoder.Version$ECBlocks r5 = r8.getECBlocksForLevel(r6)
            int r1 = r8.getTotalCodewords()
            int r2 = r5.getTotalECCodewords()
            int r1 = r1 - r2
            terminateBits(r1, r7)
            int r2 = r8.getTotalCodewords()
            int r5 = r5.getNumBlocks()
            com.taobao.xcode.szxing.common.BitArray r5 = interleaveWithECBytes(r7, r2, r1, r5)
            com.taobao.xcode.szxing.qrcode.encoder.QRCode r7 = new com.taobao.xcode.szxing.qrcode.encoder.QRCode
            r7.<init>()
            r7.setECLevel(r6)
            r7.setMode(r0)
            r7.setVersion(r8)
            int r0 = r8.getDimensionForVersion()
            com.taobao.xcode.szxing.qrcode.encoder.ByteMatrix r1 = new com.taobao.xcode.szxing.qrcode.encoder.ByteMatrix
            r1.<init>(r0, r0)
            int r0 = chooseMaskPattern(r5, r6, r8, r1)
            r7.setMaskPattern(r0)
            com.taobao.xcode.szxing.qrcode.encoder.MatrixUtil.buildMatrix(r5, r6, r8, r0, r1)
            r7.setMatrix(r1)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.xcode.szxing.qrcode.encoder.Encoder.encode(java.lang.String, com.taobao.xcode.szxing.qrcode.decoder.ErrorCorrectionLevel, java.util.Map, com.taobao.xcode.szxing.qrcode.decoder.Version):com.taobao.xcode.szxing.qrcode.encoder.QRCode");
    }

    static int getAlphanumericCode(int i) {
        if (i < ALPHANUMERIC_TABLE.length) {
            return ALPHANUMERIC_TABLE[i];
        }
        return -1;
    }

    public static Mode chooseMode(String str) {
        return chooseMode(str, (String) null);
    }

    private static Mode chooseMode(String str, String str2) {
        if ("Shift_JIS".equals(str2)) {
            return isOnlyDoubleByteKanji(str) ? Mode.KANJI : Mode.BYTE;
        }
        boolean z = false;
        boolean z2 = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                z2 = true;
            } else if (getAlphanumericCode(charAt) == -1) {
                return Mode.BYTE;
            } else {
                z = true;
            }
        }
        if (z) {
            return Mode.ALPHANUMERIC;
        }
        if (z2) {
            return Mode.NUMERIC;
        }
        return Mode.BYTE;
    }

    private static boolean isOnlyDoubleByteKanji(String str) {
        try {
            byte[] bytes = str.getBytes("Shift_JIS");
            int length = bytes.length;
            if (length % 2 != 0) {
                return false;
            }
            for (int i = 0; i < length; i += 2) {
                byte b = bytes[i] & UByte.MAX_VALUE;
                if ((b < 129 || b > 159) && (b < 224 || b > 235)) {
                    return false;
                }
            }
            return true;
        } catch (UnsupportedEncodingException unused) {
            return false;
        }
    }

    private static int chooseMaskPattern(BitArray bitArray, ErrorCorrectionLevel errorCorrectionLevel, Version version, ByteMatrix byteMatrix) throws WriterException {
        int i = Integer.MAX_VALUE;
        int i2 = -1;
        for (int i3 = 0; i3 < 8; i3++) {
            MatrixUtil.buildMatrix(bitArray, errorCorrectionLevel, version, i3, byteMatrix);
            int calculateMaskPenalty = calculateMaskPenalty(byteMatrix);
            if (calculateMaskPenalty < i) {
                i2 = i3;
                i = calculateMaskPenalty;
            }
        }
        return i2;
    }

    private static Version chooseVersion(int i, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException {
        for (int i2 = 1; i2 <= 40; i2++) {
            Version versionForNumber = Version.getVersionForNumber(i2);
            if (versionForNumber.getTotalCodewords() - versionForNumber.getECBlocksForLevel(errorCorrectionLevel).getTotalECCodewords() >= (i + 7) / 8) {
                return versionForNumber;
            }
        }
        throw new WriterException("Data too big");
    }

    static void terminateBits(int i, BitArray bitArray) throws WriterException {
        int i2 = i << 3;
        if (bitArray.getSize() <= i2) {
            for (int i3 = 0; i3 < 4 && bitArray.getSize() < i2; i3++) {
                bitArray.appendBit(false);
            }
            int size = bitArray.getSize() & 7;
            if (size > 0) {
                while (size < 8) {
                    bitArray.appendBit(false);
                    size++;
                }
            }
            int sizeInBytes = i - bitArray.getSizeInBytes();
            for (int i4 = 0; i4 < sizeInBytes; i4++) {
                bitArray.appendBits((i4 & 1) == 0 ? 236 : 17, 8);
            }
            if (bitArray.getSize() != i2) {
                throw new WriterException("Bits size does not equal capacity");
            }
            return;
        }
        throw new WriterException("data bits cannot fit in the QR Code" + bitArray.getSize() + " > " + i2);
    }

    static void getNumDataBytesAndNumECBytesForBlockID(int i, int i2, int i3, int i4, int[] iArr, int[] iArr2) throws WriterException {
        if (i4 < i3) {
            int i5 = i % i3;
            int i6 = i3 - i5;
            int i7 = i / i3;
            int i8 = i7 + 1;
            int i9 = i2 / i3;
            int i10 = i9 + 1;
            int i11 = i7 - i9;
            int i12 = i8 - i10;
            if (i11 != i12) {
                throw new WriterException("EC bytes mismatch");
            } else if (i3 != i6 + i5) {
                throw new WriterException("RS blocks mismatch");
            } else if (i != ((i9 + i11) * i6) + ((i10 + i12) * i5)) {
                throw new WriterException("Total bytes mismatch");
            } else if (i4 < i6) {
                iArr[0] = i9;
                iArr2[0] = i11;
            } else {
                iArr[0] = i10;
                iArr2[0] = i12;
            }
        } else {
            throw new WriterException("Block ID too large");
        }
    }

    static BitArray interleaveWithECBytes(BitArray bitArray, int i, int i2, int i3) throws WriterException {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        if (bitArray.getSizeInBytes() == i5) {
            ArrayList<BlockPair> arrayList = new ArrayList<>(i6);
            int i7 = 0;
            int i8 = 0;
            int i9 = 0;
            for (int i10 = 0; i10 < i6; i10++) {
                int[] iArr = new int[1];
                int[] iArr2 = new int[1];
                getNumDataBytesAndNumECBytesForBlockID(i, i2, i3, i10, iArr, iArr2);
                int i11 = iArr[0];
                byte[] bArr = new byte[i11];
                bitArray.toBytes(i7 * 8, bArr, 0, i11);
                byte[] generateECBytes = generateECBytes(bArr, iArr2[0]);
                arrayList.add(new BlockPair(bArr, generateECBytes));
                i8 = Math.max(i8, i11);
                i9 = Math.max(i9, generateECBytes.length);
                i7 += iArr[0];
            }
            if (i5 == i7) {
                BitArray bitArray2 = new BitArray();
                for (int i12 = 0; i12 < i8; i12++) {
                    for (BlockPair dataBytes : arrayList) {
                        byte[] dataBytes2 = dataBytes.getDataBytes();
                        if (i12 < dataBytes2.length) {
                            bitArray2.appendBits(dataBytes2[i12], 8);
                        }
                    }
                }
                for (int i13 = 0; i13 < i9; i13++) {
                    for (BlockPair errorCorrectionBytes : arrayList) {
                        byte[] errorCorrectionBytes2 = errorCorrectionBytes.getErrorCorrectionBytes();
                        if (i13 < errorCorrectionBytes2.length) {
                            bitArray2.appendBits(errorCorrectionBytes2[i13], 8);
                        }
                    }
                }
                if (i4 == bitArray2.getSizeInBytes()) {
                    return bitArray2;
                }
                throw new WriterException("Interleaving error: " + i4 + " and " + bitArray2.getSizeInBytes() + " differ.");
            }
            throw new WriterException("Data bytes does not match offset");
        }
        throw new WriterException("Number of bits and data bytes does not match");
    }

    static byte[] generateECBytes(byte[] bArr, int i) {
        int length = bArr.length;
        int[] iArr = new int[(length + i)];
        for (int i2 = 0; i2 < length; i2++) {
            iArr[i2] = bArr[i2] & UByte.MAX_VALUE;
        }
        new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(iArr, i);
        byte[] bArr2 = new byte[i];
        for (int i3 = 0; i3 < i; i3++) {
            bArr2[i3] = (byte) iArr[length + i3];
        }
        return bArr2;
    }

    static void appendModeInfo(Mode mode, BitArray bitArray) {
        bitArray.appendBits(mode.getBits(), 4);
    }

    static void appendLengthInfo(int i, Version version, Mode mode, BitArray bitArray) throws WriterException {
        int characterCountBits = mode.getCharacterCountBits(version);
        int i2 = 1 << characterCountBits;
        if (i < i2) {
            bitArray.appendBits(i, characterCountBits);
            return;
        }
        throw new WriterException(i + " is bigger than " + (i2 - 1));
    }

    static void appendBytes(String str, Mode mode, BitArray bitArray, String str2) throws WriterException {
        switch (mode) {
            case NUMERIC:
                appendNumericBytes(str, bitArray);
                return;
            case ALPHANUMERIC:
                appendAlphanumericBytes(str, bitArray);
                return;
            case BYTE:
                append8BitBytes(str, bitArray, str2);
                return;
            case KANJI:
                appendKanjiBytes(str, bitArray);
                return;
            default:
                throw new WriterException("Invalid mode: " + mode);
        }
    }

    static void appendNumericBytes(CharSequence charSequence, BitArray bitArray) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int charAt = charSequence.charAt(i) - '0';
            int i2 = i + 2;
            if (i2 < length) {
                bitArray.appendBits((charAt * 100) + ((charSequence.charAt(i + 1) - '0') * 10) + (charSequence.charAt(i2) - '0'), 10);
                i += 3;
            } else {
                i++;
                if (i < length) {
                    bitArray.appendBits((charAt * 10) + (charSequence.charAt(i) - '0'), 7);
                    i = i2;
                } else {
                    bitArray.appendBits(charAt, 4);
                }
            }
        }
    }

    static void appendAlphanumericBytes(CharSequence charSequence, BitArray bitArray) throws WriterException {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int alphanumericCode = getAlphanumericCode(charSequence.charAt(i));
            if (alphanumericCode != -1) {
                int i2 = i + 1;
                if (i2 < length) {
                    int alphanumericCode2 = getAlphanumericCode(charSequence.charAt(i2));
                    if (alphanumericCode2 != -1) {
                        bitArray.appendBits((alphanumericCode * 45) + alphanumericCode2, 11);
                        i += 2;
                    } else {
                        throw new WriterException();
                    }
                } else {
                    bitArray.appendBits(alphanumericCode, 6);
                    i = i2;
                }
            } else {
                throw new WriterException();
            }
        }
    }

    static void append8BitBytes(String str, BitArray bitArray, String str2) throws WriterException {
        try {
            for (byte appendBits : str.getBytes(str2)) {
                bitArray.appendBits(appendBits, 8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new WriterException((Throwable) e);
        }
    }

    static void appendKanjiBytes(String str, BitArray bitArray) throws WriterException {
        try {
            byte[] bytes = str.getBytes("Shift_JIS");
            int length = bytes.length;
            int i = 0;
            while (i < length) {
                byte b = ((bytes[i] & UByte.MAX_VALUE) << 8) | (bytes[i + 1] & UByte.MAX_VALUE);
                int i2 = (b < 33088 || b > 40956) ? (b < 57408 || b > 60351) ? -1 : b - 49472 : b - 33088;
                if (i2 != -1) {
                    bitArray.appendBits(((i2 >> 8) * JfifUtil.MARKER_SOFn) + (i2 & 255), 13);
                    i += 2;
                } else {
                    throw new WriterException("Invalid byte sequence");
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new WriterException((Throwable) e);
        }
    }

    private static void appendECI(CharacterSetECI characterSetECI, BitArray bitArray) {
        bitArray.appendBits(Mode.ECI.getBits(), 4);
        bitArray.appendBits(characterSetECI.getValue(), 8);
    }
}
