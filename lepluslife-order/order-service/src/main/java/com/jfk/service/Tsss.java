package com.jfk.service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcg on 2016/12/15.
 */
public class Tsss {

//  public static void main(String[] args) {
//    Method[] declaredMethods = Tsss.class.getDeclaredMethods();
//    for (Method declaredMethod : declaredMethods) {
//      System.out.println(getParameterNames(declaredMethod));
//    }
//  }

  public static List<String> getParameterNames(Method method) {
    Parameter[] parameters = method.getParameters();
    List<String> parameterNames = new ArrayList<>();

    for (Parameter parameter : parameters) {
      if(!parameter.isNamePresent()) {
        throw new IllegalArgumentException("Parameter names are not present!");
      }

      String parameterName = parameter.getName();
      parameterNames.add(parameterName);
    }

    return parameterNames;
  }

  public static String  gets(String s ){
    return  s;
  }

}
