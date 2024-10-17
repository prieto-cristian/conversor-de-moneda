package com.alurachallenge.conversor.modelos;

import java.util.List;
import java.util.Map;

public class Moneda {
    private final String base_code;
    private Map<String, Double> tasasDeConversion;

    public Moneda(String unaMoneda){
        this.base_code = unaMoneda;
    }
    public Moneda(String unaMoneda, Map<String, Double> tasas){
        this.base_code = unaMoneda;
        this.tasasDeConversion = tasas;
    }

    public void setTasasDeConversion(Map<String, Double> tasasDeConversion) {
        this.tasasDeConversion = tasasDeConversion;
    }

    public Map<String, Double> getTasasDeConversion() {
        return tasasDeConversion;
    }

    public String getBase_code() {
        return base_code;
    }

    // Recibe un valor en su moneda y devuelve el valor en otra moneda. Ej USD -> ARS
    public double convertirA(String unaMoneda, double unValor){
        return unValor * this.tasasDeConversion.get(unaMoneda);
    }

    // Recibe una moneda distinta y devuelve el valor en su moneda. Ej ARS -> USD
    public double convertirAMonedaBase(String unaMoneda, double valorEnOtraMoneda){
        return valorEnOtraMoneda / this.tasasDeConversion.get(unaMoneda);
    }
}
