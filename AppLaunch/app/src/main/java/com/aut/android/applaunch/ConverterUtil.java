package com.aut.android.applaunch;

/**
 * Created by Administrator on 30/07/2015.
 */
public class ConverterUtil {
    public static float convertFahrenheitToCelsius(float fahrenheit){
        return ((fahrenheit - 32)*5/9);
    }
    public static float convertCelsiusToFahrenheit(float celsius){
        return ((celsius*9)/5) +32;
    }
}
