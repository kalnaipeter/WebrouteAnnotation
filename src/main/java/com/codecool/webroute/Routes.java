package com.codecool.webroute;

public class Routes {

    @WebRoute("/test1")
    public String test1(){
        return "asd1";
    }

    @WebRoute("/test2")
    public String test2(){
        return "asd2";
    }
}
