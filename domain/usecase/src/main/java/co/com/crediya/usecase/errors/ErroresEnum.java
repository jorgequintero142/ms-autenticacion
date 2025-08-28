package co.com.crediya.usecase.errors;

public enum ErroresEnum {
    ERROR_APELLIDO("El apellido  no tiene un formato válido"),
    ERROR_NOMBRE("El nombre  no tiene un formato válido"),
    ERROR_DOCUMENTO_IDENTIDAD("El documento de identidad no tiene el formato esperado"),
    ERROR_FECHA_NACIMIENTO("Fecha de nacimiento no tiene un formato válido"),
    ERROR_TELEFONO("El telefono no tiene un formato válido"),
    ERROR_EMAIL("El email no tiene un formato válido"),
    ERROR_EMAIL_REGISTRADO("El email ya está registrado");
    private final String mensaje;


    ErroresEnum(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}