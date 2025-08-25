package com.dat.plantbackend.utils;

import java.util.HashMap;

public class DiseaseMap {
    public static HashMap<Integer,String>map = new HashMap<>();
    static {
        map.put(1,"ALgal");
        map.put(2,"Blight");
        map.put(3,"Colletotrichum");
        map.put(4,"Healthy");
        map.put(5,"Phomopsis");
        map.put(6,"Rhizoctonia");
    }
}
