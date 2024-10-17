package com.alurachallenge.conversor.modelos;

import com.alurachallenge.conversor.calculos.FiltraTasasDeConversion;
import com.alurachallenge.conversor.exceptions.EnlaceIncorrectoException;
import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ConsultaTasasExchangeRateApi implements ConsultaTasasApi{
    public static final HttpClient CLIENT = HttpClient.newHttpClient();
    public static final String LATEST = "latest/";
    public static final String PAIR = "pair/";
    public static final String SLASH = "/";
    public static final String CONVERSION_RATES = "conversion_rates";
    private static final String ENLACE_GENERICO = "https://v6.exchangerate-api.com/v6/YOUR-API-KEY/";
    private static final String CONVERSION_RATE = "conversion_rate";

    public Map<String, Double> consultarTodasLasTasas(String monedaBase) throws IOException, InterruptedException {
        String enlace = ENLACE_GENERICO + LATEST + monedaBase;
        String respuestaJson = enviarSolicitud(enlace);
        return convertirJsonsAMap(respuestaJson, monedaBase);
    }

    @Override
    public Map<String, Double> consultarTasasEspecificas(String unaMoneda, String[] listaDeTasasABuscar) throws IOException, InterruptedException{
        // Este metodo busca todas las monedas y luego las filtra
        String enlace = ENLACE_GENERICO + LATEST + unaMoneda;
        String respuestaJson = enviarSolicitud(enlace);
        FiltraTasasDeConversion filtrador = new FiltraTasasDeConversion();
        return filtrador
                .filtrarPorTasasDeInteres(
                        convertirJsonsAMap(respuestaJson, unaMoneda),
                        listaDeTasasABuscar
                );
    }

    @Override
    public Map<String, Double> consultarPorUnaTasa(String monedaBase, String monedaABuscar) throws IOException, InterruptedException{
        Map<String, Double> tasaDeConversion = new HashMap<>();
        if(monedaBase.length() == 3 && monedaABuscar.length() == 3){
            String enlace = ENLACE_GENERICO + PAIR + monedaBase + SLASH + monedaABuscar;
            String respuestaJson = enviarSolicitud(enlace);
            JsonElement deStringAJsonElement = JsonParser.parseString(respuestaJson);
            JsonObject jsonObject = deStringAJsonElement.getAsJsonObject();
            tasaDeConversion.put(monedaABuscar, jsonObject.getAsJsonPrimitive(CONVERSION_RATE).getAsDouble());
        } else {
            throw new EnlaceIncorrectoException("El codigo ingresado de las monedas es invalido.");
        }
        return tasaDeConversion;
    }

    private Map<String, Double> convertirJsonsAMap(String json, String monedaOrigen){
        JsonElement deStringAJsonElement = JsonParser.parseString(json);
        JsonObject jsonObject = deStringAJsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(CONVERSION_RATES);
        Map<String, Double> tasas = new HashMap<>();
        for (String key : jsonObject1.keySet()) {
            if (jsonObject1.get(key) instanceof JsonPrimitive) {
                if (!key.equals(monedaOrigen)) {
                    tasas.put(key, jsonObject1.getAsJsonPrimitive(key).getAsDouble());
                }
            }
        }
        return tasas;
    }

    private String enviarSolicitud(String enlace) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(enlace))
                .build();
        HttpResponse<String> response = CLIENT
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
