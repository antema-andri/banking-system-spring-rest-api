package com.backend.bankingsystem.utils;

import java.text.DecimalFormat;

public class Format {
    public static double formatDouble(double value){
        double tmp=value*100;
        int part=(int)tmp;
        return part/100.0;
    }
}
