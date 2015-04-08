package com.pstl.gtfo.Algorithmes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kevin Lorant on 08/04/2015.
 */
public class Filtrage {

    public static ArrayList<String> filtre(ArrayList<String> l){
        ArrayList<String> buffer = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
        boolean danslebuffer = false;
        for(String s:l){

            if(!s.contains("-1.0")){
                danslebuffer = true;
                buffer.add(s);
            }
            else {
                if(danslebuffer) {
                    if (buffer.size() > 5) {
                        result.add(getPitch(buffer));
                        buffer = new ArrayList<String>();
                    }
                    danslebuffer = false;
                }
            }
        }
        return result;
    }

    public static String getPitch(ArrayList<String> l){
        ArrayList<Float> list = new ArrayList<Float>();
        ArrayList<Float> listSansHarmo = new ArrayList<Float>();
        for(String s:l){
            list.add(Float.parseFloat(s));
        }
        Collections.sort(list);
        float total = 0;
        listSansHarmo.add(list.get(0));
        total += list.get(0);
        for(int i =1;i<list.size();i++){
            if(list.get(i)> ((list.get(i-1)*0.10) + list.get(i-1))
             ||list.get(i)< (list.get(i-1) - (list.get(i-1)*0.10))
                    ) {
                break;
            }
            else {
                listSansHarmo.add(list.get(i));
                total+= list.get(i);
            }
        }
        return Float.toString(total/listSansHarmo.size());
    }

}
