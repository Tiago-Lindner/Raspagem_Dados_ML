package br.tiagofl.raspagem.principal;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TesteData {
    public static void main(String[] args) throws ParseException {

        Calendar c = Calendar.getInstance();
        Date data = c.getTime();

        Locale brasil = new Locale("pt", "BR"); //Retorna do país e a língua

        DateFormat formato = DateFormat.getDateInstance(DateFormat.FULL, brasil);
        DateFormat hora = DateFormat.getTimeInstance();

        String dataConsulta = "Data e hora da consulta: "+ formato.format(data) + ", " +  hora.format(data);
        System.out.println(dataConsulta);



    }
}
