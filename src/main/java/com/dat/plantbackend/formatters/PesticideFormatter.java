package com.dat.plantbackend.formatters;

import com.dat.plantbackend.enities.Pesticide;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PesticideFormatter implements Formatter<Pesticide> {
    @Override
    public Pesticide parse(String id, Locale locale) throws ParseException {
        Pesticide p=new Pesticide();
        p.setId(Integer.valueOf(id));
        return p;
    }

    @Override
    public String print(Pesticide object, Locale locale) {
        return String.valueOf(object.getId());
    }
}
