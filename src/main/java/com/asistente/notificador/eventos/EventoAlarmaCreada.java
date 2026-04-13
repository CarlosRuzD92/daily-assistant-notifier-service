package com.asistente.notificador.eventos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoAlarmaCreada {
    String id;
    String titulo;
    String contenido;
    String fecha;
}
