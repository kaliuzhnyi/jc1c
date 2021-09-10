package org.jc1c.test;

import org.jc1c.JServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyApp {

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        new JServer.Builder().build().start();

//        JServer jServer = new JServer();
//        jServer.addHandlers(MyAppHandler.class);

//        Class<?> cls = MyAppHandler.class;
//
//        Object obj = cls.getDeclaredConstructor().newInstance();
//
//        Method[] methods = cls.getDeclaredMethods();
//        for (Method method : methods) {
//            System.out.println(method.getName());
//            try {
//                method.invoke(obj);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }

    }

}
