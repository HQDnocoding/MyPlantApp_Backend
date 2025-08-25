package com.dat.plantbackend.formatters;

import com.dat.plantbackend.enities.Disease;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class DiseaseFormatter implements Formatter<Disease> {

    @Override
    public Disease parse(String id, Locale locale) throws ParseException {
        Disease d=new Disease();
        d.setId(Integer.valueOf(id));
        return d;
    }

    @Override
    public String print(Disease object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
