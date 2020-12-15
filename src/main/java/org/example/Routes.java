package org.example;

public class Routes {

    @WebRoute(method="GET", path = "/test1")
    public String getTest1() {
        return "test1 with GET method";
    }

    @WebRoute(method="POST", path = "/test1")
    public String postTest1() {
        return "test1 with POST method";
    }

    @WebRoute(method="GET", path = "/test2")
    public String getTest2() {
        return "test2 with GET method";
    }

    @WebRoute(method="GET", path = "/user/<userName>")
    public String getHello(String name) {
        return "hello " + name + "! with GET method";
    }

    @WebRoute(method="GET", path = "/picture/<pictureName>")
    public String getPictureName(String name) {
        return "picture's name: " + name + " with GET method";
    }

}
