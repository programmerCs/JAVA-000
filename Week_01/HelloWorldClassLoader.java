package com.java.geektime.first;

import java.io.*;

public class HelloWorldClassLoader extends ClassLoader {

    private static final String HELLO_CLASS_NAME = "Hello";

    private String helloClassFilePath;

    public HelloWorldClassLoader(String helloClassFilePath) {
        this.helloClassFilePath = helloClassFilePath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (isHelloClass(name)) {
            byte[] bytes = decode(loadClassFile());
            return super.defineClass(name, bytes, 0, bytes.length);
        } else {
            return super.findClass(name);
        }
    }

    private boolean isHelloClass(String className) {
        return HELLO_CLASS_NAME.equals(className);
    }

    private byte[] loadClassFile() throws ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            BufferedInputStream bufferedInputStream = getClassFileInputStream(helloClassFilePath);
            int len = -1;
            byte[] bytes = new byte[4096];
            while (-1 != (len = bufferedInputStream.read(bytes, 0, bytes.length))) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException("load Hello class occur exception", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private BufferedInputStream getClassFileInputStream(String fileClassPath) throws FileNotFoundException {
        InputStream inputStream = this.getResourceAsStream(fileClassPath);
        if (null == inputStream) {
            inputStream = new FileInputStream(fileClassPath);
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return bufferedInputStream;
    }


    private byte[] decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return bytes;
    }

}
