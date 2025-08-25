package com.dat.plantbackend.formatters;

import com.dat.plantbackend.enities.Plant;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PlantFormatter implements Formatter<Plant> {
    @Override
    public Plant parse(String id, Locale locale) throws ParseException {
        Plant p=new Plant();
        p.setId(Integer.valueOf(id));
        return p;
    }

    @Override
    public String print(Plant object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
