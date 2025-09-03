package co.com.crediya.usecase;

import java.math.BigDecimal;

public class Constantes {

    private Constantes() {
    }

    public static final String REGEX_ALPHA = "^[a-zA-Z]+$";
    public static final String REGEX_TELEFONO = "^3\\d{9}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final BigDecimal SALARIO_MINIMO_PERMITIDO = BigDecimal.valueOf(0);
    public static final BigDecimal SALARIO_MAXIMO_PERMITIDO = BigDecimal.valueOf(15000000);

    public static final int MINIMA_LONGITUD_DOCUMENTO = 5;
    public static final int EDAD_MINIMA_PERMITIDA = 18;


    public static final String ERROR_RANGO_SALARIO = "El salario debe estar entre " + Constantes.SALARIO_MINIMO_PERMITIDO + " y " + Constantes.SALARIO_MAXIMO_PERMITIDO;

    public static final String ERROR_APELLIDO = "El apellido  no tiene un formato válido";
    public static final String ERROR_NOMBRE = "El nombre  no tiene un formato válido";
    public static final String ERROR_DOCUMENTO_IDENTIDAD = "El documento de identidad no tiene el formato esperado";
    public static final String ERROR_FECHA_NACIMIENTO = "Fecha de nacimiento no tiene un formato válido";
    public static final String ERROR_TELEFONO = "El telefono no tiene un formato válido";
    public static final String ERROR_EMAIL = "El email no tiene un formato válido";
    public static final String ERROR_EMAIL_REGISTRADO = "El email ya está registrado";
    public static final String ERROR_DOCUMENTO_REGISTRADO = "El documento ya se encuentra registrado";

    public static final String USUARIO_NO_ENCONTRADO_EMAIL = "No se encontró usuario con el email enviado";
    public static final String USUARIO_NO_ENCONTRADO_NUMERO_DOCUMENTO = "No se encontró usuario con el número de documento enviado";

    public static final String ERROR_INESPERADO = "Error inesperado";
    public static final String MENSAJE_ERROR_INESPERADO = "Intente más tarde";
    public static final int CODIGO_ERROR_INESPERADO = 409;

}
