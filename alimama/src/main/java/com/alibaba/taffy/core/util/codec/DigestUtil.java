package com.alibaba.taffy.core.util.codec;

import com.alibaba.taffy.core.util.lang.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
    private static final int STREAM_BUFFER_LENGTH = 1024;

    private static byte[] digest(MessageDigest messageDigest, InputStream inputStream) throws IOException {
        return updateDigest(messageDigest, inputStream).digest();
    }

    public static MessageDigest getMd2Digest() {
        return getDigest(MessageDigestAlgorithms.MD2);
    }

    public static MessageDigest getMd5Digest() {
        return getDigest(MessageDigestAlgorithms.MD5);
    }

    public static MessageDigest getSha1Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_1);
    }

    public static MessageDigest getSha256Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_256);
    }

    public static MessageDigest getSha384Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_384);
    }

    public static MessageDigest getSha512Digest() {
        return getDigest(MessageDigestAlgorithms.SHA_512);
    }

    public static byte[] md2(byte[] bArr) {
        return getMd2Digest().digest(bArr);
    }

    public static byte[] md2(InputStream inputStream) throws IOException {
        return digest(getMd2Digest(), inputStream);
    }

    public static byte[] md2(String str) {
        return md2(StringUtil.getBytesUtf8(str));
    }

    public static String md2Hex(byte[] bArr) {
        return HexUtil.encodeHexString(md2(bArr));
    }

    public static String md2Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(md2(inputStream));
    }

    public static String md2Hex(String str) {
        return HexUtil.encodeHexString(md2(str));
    }

    public static byte[] md5(byte[] bArr) {
        return getMd5Digest().digest(bArr);
    }

    public static byte[] md5(InputStream inputStream) throws IOException {
        return digest(getMd5Digest(), inputStream);
    }

    public static byte[] md5(String str) {
        return md5(StringUtil.getBytesUtf8(str));
    }

    public static String md5Hex(byte[] bArr) {
        return HexUtil.encodeHexString(md5(bArr));
    }

    public static String md5Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(md5(inputStream));
    }

    public static String md5Hex(String str) {
        return HexUtil.encodeHexString(md5(str));
    }

    public static byte[] sha1(byte[] bArr) {
        return getSha1Digest().digest(bArr);
    }

    public static byte[] sha1(InputStream inputStream) throws IOException {
        return digest(getSha1Digest(), inputStream);
    }

    public static byte[] sha1(String str) {
        return sha1(StringUtil.getBytesUtf8(str));
    }

    public static String sha1Hex(byte[] bArr) {
        return HexUtil.encodeHexString(sha1(bArr));
    }

    public static String sha1Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(sha1(inputStream));
    }

    public static String sha1Hex(String str) {
        return HexUtil.encodeHexString(sha1(str));
    }

    public static byte[] sha256(byte[] bArr) {
        return getSha256Digest().digest(bArr);
    }

    public static byte[] sha256(InputStream inputStream) throws IOException {
        return digest(getSha256Digest(), inputStream);
    }

    public static byte[] sha256(String str) {
        return sha256(StringUtil.getBytesUtf8(str));
    }

    public static String sha256Hex(byte[] bArr) {
        return HexUtil.encodeHexString(sha256(bArr));
    }

    public static String sha256Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(sha256(inputStream));
    }

    public static String sha256Hex(String str) {
        return HexUtil.encodeHexString(sha256(str));
    }

    public static byte[] sha384(byte[] bArr) {
        return getSha384Digest().digest(bArr);
    }

    public static byte[] sha384(InputStream inputStream) throws IOException {
        return digest(getSha384Digest(), inputStream);
    }

    public static byte[] sha384(String str) {
        return sha384(StringUtil.getBytesUtf8(str));
    }

    public static String sha384Hex(byte[] bArr) {
        return HexUtil.encodeHexString(sha384(bArr));
    }

    public static String sha384Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(sha384(inputStream));
    }

    public static String sha384Hex(String str) {
        return HexUtil.encodeHexString(sha384(str));
    }

    public static byte[] sha512(byte[] bArr) {
        return getSha512Digest().digest(bArr);
    }

    public static byte[] sha512(InputStream inputStream) throws IOException {
        return digest(getSha512Digest(), inputStream);
    }

    public static byte[] sha512(String str) {
        return sha512(StringUtil.getBytesUtf8(str));
    }

    public static String sha512Hex(byte[] bArr) {
        return HexUtil.encodeHexString(sha512(bArr));
    }

    public static String sha512Hex(InputStream inputStream) throws IOException {
        return HexUtil.encodeHexString(sha512(inputStream));
    }

    public static String sha512Hex(String str) {
        return HexUtil.encodeHexString(sha512(str));
    }

    public static MessageDigest updateDigest(MessageDigest messageDigest, byte[] bArr) {
        messageDigest.update(bArr);
        return messageDigest;
    }

    public static MessageDigest getDigest(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static MessageDigest updateDigest(MessageDigest messageDigest, InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        int read = inputStream.read(bArr, 0, 1024);
        while (read > -1) {
            messageDigest.update(bArr, 0, read);
            read = inputStream.read(bArr, 0, 1024);
        }
        return messageDigest;
    }

    public static MessageDigest updateDigest(MessageDigest messageDigest, String str) {
        messageDigest.update(StringUtil.getBytesUtf8(str));
        return messageDigest;
    }
}
