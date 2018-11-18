package pl.edu.wat.wcy.pz.project.server.hello.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class HelloWorldController {

    @GetMapping("hello")
    public HelloWorldBean getHelloWorld(HttpServletRequest httpServletRequest) {
        String requestURI = httpServletRequest.getRemoteAddr();
        int port = httpServletRequest.getRemotePort();
        System.out.println("-->" + requestURI + ", port" + port);

        return new HelloWorldBean("Hello World");
    }
}
