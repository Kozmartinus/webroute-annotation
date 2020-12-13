package org.example;

public class Routes {

    @WebRoute("/test1")
    public String test1() {
        return "test1 value";
    }

    @WebRoute("/test2")
    public String test2() {
        return "test2 value";
    }

}
