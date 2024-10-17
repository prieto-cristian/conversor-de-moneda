package com.alurachallenge.conversor.calculos;

import java.util.HashMap;
import java.util.Map;

public class FiltraTasasDeConversion {
    public Map<String, Double> filtrarPorTasasDeInteres(Map<String, Double> tasasDeConversion, String[] listaDeTasasABuscar){
        Map<String, Double> listaFiltrada = new HashMap<>();
        for (String unNombreMoneda : listaDeTasasABuscar){
            listaFiltrada.put(unNombreMoneda, tasasDeConversion.get(unNombreMoneda));
        }
        return listaFiltrada;
    }
}
