package com.alurachallenge.conversor.modelos;

import java.io.IOException;
import java.util.Map;

public interface ConsultaTasasApi {
    Map<String, Double> consultarTodasLasTasas(String unaMoneda) throws IOException, InterruptedException;
    Map<String, Double> consultarTasasEspecificas(String unaMoneda, String[] listaDeTasasABuscar)throws IOException, InterruptedException;
    Map<String, Double> consultarPorUnaTasa(String monedaBase, String monedaABuscar)throws IOException, InterruptedException;
}
