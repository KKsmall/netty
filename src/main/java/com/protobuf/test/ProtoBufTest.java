package com.protobuf.test;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        //构建对象
        dataInfo.Student student = dataInfo.Student.newBuilder()
                .setName("张三")
                .setAddress("北京")
                .setAge(20)
                .build();

        //转为字节数组（序列化）
        byte[] student2ByteArray = student.toByteArray();
        //反序列化
        dataInfo.Student student1 = dataInfo.Student.parseFrom(student2ByteArray);

        System.out.println(student1.getAddress());
        System.out.println(student1.getName());
        System.out.println(student1.getAge());
    }
}
