package org.GameEraser.Catalyss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class aa {
    public static  String possibleChar = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9";
    public static String avi1 = "avtr_xxxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxx";
    public static String avi2 = "avtr_xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";


    public static void main(String[] args)  {
        List<String> every = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        //31
        for(int i =0; i < 31;i++) {
            System.out.println(i);
            Writer(every);
            if(i==0){
                for (String str:possibleChar.split(","))
                {
                    every.add(avi1.replaceFirst("x",str));
                    System.out.println(avi1.replaceFirst("x",str));
                }

            }
            else {
                for (Object sstr:every.toArray())
                {
                    for (String str:possibleChar.split(","))
                    {
                        System.out.println(String.valueOf(sstr).replaceFirst("x",str));
                        if(!every.contains(String.valueOf(sstr).replaceFirst("x",str))) every.add(String.valueOf(sstr).replaceFirst("x",str));
                    }
                }
            }
        }
        /*
        for(int i =0; i < 32;i++) {
            if(i==0){
                for (String str:possibleChar.split(","))
                {
                    every.add(avi2.replaceFirst("x",str));
                }

            }
            else {
                for (Object sstr:every.toArray())
                {
                    for (String str:possibleChar.split(","))
                    {
                        if(!every.contains(String.valueOf(sstr).replaceFirst("x",str))) every.add(String.valueOf(sstr).replaceFirst("x",str));
                    }
                }
            }
        }
        for (Object str:every.toArray()) {
            stringBuilder.append(";").append(str).append("\n");
        }

         */


    }
    public static void Writer( List<String> every){
        StringBuilder stringBuilder = new StringBuilder();
        for (Object str:every.toArray()) {
            stringBuilder.append(";").append(str).append("\n");
        }
        try {
            FileWriter fwr = new FileWriter("EVERY AVI ID POSSIBLE");
            fwr.write(stringBuilder.toString());
            fwr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
