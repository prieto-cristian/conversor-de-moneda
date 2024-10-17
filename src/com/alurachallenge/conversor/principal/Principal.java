package com.alurachallenge.conversor.principal;

import com.alurachallenge.conversor.exceptions.EnlaceIncorrectoException;
import com.alurachallenge.conversor.modelos.ConsultaTasasApi;
import com.alurachallenge.conversor.modelos.ConsultaTasasExchangeRateApi;
import com.alurachallenge.conversor.modelos.Moneda;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    // Todos las cadenas utilizadas
    public static final String MENSAJE_INGRESAR_VALOR = "Ingrese el monto que deseas convertir: ";
    public static final String MONEDA_COP = "COP";
    public static final String MONEDA_BRL = "BRL";
    public static final String MONEDA_BOB = "BOB";
    public static final String MONEDA_CLP = "CLP";
    public static final String MONEDA_USD = "USD";
    public static final String MONEDA_ARS = "ARS";
    public static final String MENSAJE_INGRESE_UNA_OPCION_VALIDA = "Por favor ingrese una opcion valida!";
    public static final String MENU = "*************************\n\n" +
            "Sea bienvenido/a al Conversor de Moneda =]\n" +
            "1) Dólar =>> Peso argentino\n" +
            "2) Peso argentino =>> Dólar\n" +
            "3) Dólar =>> Real brasileño\n" +
            "4) Real brasileño =>> Dólar\n" +
            "5) Dólar =>> Peso colombiano\n" +
            "6) Peso colombiano =>> Dólar\n" +
            "7) Eliga las monedas de su preferencia\n" +
            "8) Salir\n" +
            "Elija una opcion válida:\n\n" +
            "*************************";
    public static final String MENSAJE_FINALIZO_EL_PROGRAMA = "Finalizo el programa!";
    public static final String MENSAJE_INGRESE_UNA_MONEDA_BASE = "Escriba el código de su moneda base (Ejemplo para argentina el código es ARS): ";
    public static final String MENSAJE_INGRESE_UNA_MONEDA_DESTINO = "Ahora escriba el codigo de la moneda a la que desea convertir su valor (Ejemplo para argentina el código es ARS): ";

    public static void main(String[] args){
        String[] monedasEstandar = {MONEDA_ARS, MONEDA_BOB, MONEDA_BRL, MONEDA_CLP, MONEDA_COP, MONEDA_USD};
        ConsultaTasasApi consultaTasasApi = new ConsultaTasasExchangeRateApi();

        try {
            Moneda dolar = new Moneda(MONEDA_USD, consultaTasasApi.consultarTasasEspecificas(MONEDA_USD, monedasEstandar));
            int opcion = 1;
            Scanner input = new Scanner(System.in);
            double valorIngresado = 0;
            double valorDestino = 0;
            while (esOpcionCorrecta(opcion)) {
                System.out.println(MENU);

                opcion = input.nextInt();

                if(!esOpcionCorrecta(opcion)){
                    System.out.println(MENSAJE_INGRESE_UNA_OPCION_VALIDA);
                    opcion = 1;
                } else if (opcion == 8) {
                    opcion = -1;
                } else if (opcion == 7) {
                    System.out.println(MENSAJE_INGRESE_UNA_MONEDA_BASE);
                    String codigoMonedaBase = input.next().toUpperCase();
                    System.out.println(MENSAJE_INGRESAR_VALOR);
                    valorIngresado = input.nextDouble();
                    System.out.println(MENSAJE_INGRESE_UNA_MONEDA_DESTINO);
                    String codigoMonedaDestino = input.next().toUpperCase();
                    Moneda monedaBaseElegidaPorElUsuario = new Moneda(codigoMonedaBase, consultaTasasApi.consultarPorUnaTasa(codigoMonedaBase, codigoMonedaDestino));
                    mostrarConversion(codigoMonedaBase, valorIngresado,codigoMonedaDestino, monedaBaseElegidaPorElUsuario.convertirA(codigoMonedaDestino, valorIngresado));
                } else {
                    System.out.println(MENSAJE_INGRESAR_VALOR);
                    valorIngresado = input.nextDouble();
                    if(opcion == 1){
                        valorDestino = dolar.convertirA(MONEDA_ARS, valorIngresado);
                        mostrarConversion(dolar.getBase_code(), valorIngresado, MONEDA_ARS, valorDestino);
                    } else if (opcion == 2) {
                        valorDestino = dolar.convertirAMonedaBase(MONEDA_ARS, valorIngresado);
                        mostrarConversion(MONEDA_ARS, valorIngresado, dolar.getBase_code(), valorDestino);
                    } else if (opcion == 3) {
                        valorDestino = dolar.convertirA(MONEDA_BRL, valorIngresado);
                        mostrarConversion(dolar.getBase_code(), valorIngresado, MONEDA_BRL, valorDestino);
                    } else if (opcion == 4) {
                        valorDestino = dolar.convertirAMonedaBase(MONEDA_BRL, valorIngresado);
                        mostrarConversion(MONEDA_BRL, valorIngresado, dolar.getBase_code(), valorDestino);
                    } else if (opcion == 5) {
                        valorDestino = dolar.convertirA(MONEDA_COP, valorIngresado);
                        mostrarConversion(dolar.getBase_code(), valorIngresado, MONEDA_COP, valorDestino);
                    } else if (opcion == 6) {
                        valorDestino = dolar.convertirAMonedaBase(MONEDA_COP, valorIngresado);
                        mostrarConversion(MONEDA_COP, valorIngresado, dolar.getBase_code(), valorDestino);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InputMismatchException e){
            System.out.println("El numero ingresado no es valido: " + e.getMessage());
        } catch (EnlaceIncorrectoException e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println(MENSAJE_FINALIZO_EL_PROGRAMA);
        }
    }

    private static void mostrarConversion(String monedaBase, double valorBase, String monedaDestino, double valorDestino) {
        System.out.println("El valor " + valorBase + " [" + monedaBase+ "]"
                + " corresponde al valor final de =>>> " 
                + valorDestino + " [" + monedaDestino + "]");
    }

    private static boolean esOpcionCorrecta(int opcion) {
        return opcion > 0 && opcion < 9;
    }
}