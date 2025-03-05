//package com.revature.aspects;
//
//import jakarta.servlet.http.HttpSession;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Aspect //This Class is an ASPECT - a class that can trigger functionality at any point in our app runtime
////When a certain method is called, this class can listen for that invocation and trigger some functionality
//@Component
//public class AuthAspect {
//
//    //2 use cases we'll see
//        //1) When any method in a controller OTHER than AuthController is called, make sure the user is logged in
//        //2) When any method that's annotated with our custom @AdminOnly annotation is called,
//            //...make sure the user has a role = admin
//
//
//    //@Before allows us to invoke this method BEFORE any method we specify
//    //"Invoke the login check before any method in the controllers package BESIDES AuthController""
//    @Order(1) //This advice will always run first
//    @Before("within(com.revature.controllers.*)" +
//            "&& !within(com.revature.controllers.AuthController)")
//    public void checkLoggedIn(){
//
//        //get access to the session (or lack thereof)
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attributes.getRequest().getSession(false);
//
//        //We're getting an object that has the session, and then trying to extract that session
//        //getSession(false)?? Don't make a new session if one doesn't exist
//
//        //If the session is null, the user isn't logged in, throw an exception!!!
//        if(session == null || session.getAttribute("userId") == null){
//            throw new IllegalArgumentException("User must be logged in to do this!");
//        }
//    }
//
//    //"Before any method annotation with @AdminOnly, check is the user is an admin"
//    @Order(2)
//    @Before("@annotation(com.revature.aspects.AdminOnly)")
//    public void checkAdmin(){
//
//        //get access to the session so we can extract the role attribute
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attributes.getRequest().getSession(false);
//
//        //First, we'll check if the session exists
//        if(session == null){
//            throw new IllegalArgumentException("user must be logged in to do this!");
//        }
//
//        String role = session.getAttribute("role").toString();
//
//        //just to see:
//        System.out.println(session.getAttribute("role").toString());
//
//        //If the User's role != "admin", throw an exception
//        if(!role.equals("admin")){
//            throw new IllegalArgumentException("User must be an admin to do this!");
//        }
//
//    }
//
//}