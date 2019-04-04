package com.okay.demo;

public class A {
    public A(){
        B b = new B();
        b.haha = "xxx";
        System.out.println(b.haha);
    }
    class B {
        private String haha = "";
    }
    public static void main(String[] args){
        A b = new A();
    }
}
