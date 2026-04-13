package com.asistente.notificador.eventos;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventoAlarmaCreadaTest {

    @Test
    void builderDebeCrearElEventoCorrectamente() {
        EventoAlarmaCreada evento = EventoAlarmaCreada.builder()
                .id("alarma-100")
                .titulo("Estudiar")
                .contenido("Repasar Spring Boot")
                .fecha("2026-03-12T20:00:00")
                .build();

        assertThat(evento.getId()).isEqualTo("alarma-100");
        assertThat(evento.getTitulo()).isEqualTo("Estudiar");
        assertThat(evento.getContenido()).isEqualTo("Repasar Spring Boot");
        assertThat(evento.getFecha()).isEqualTo("2026-03-12T20:00:00");
    }

    @Test
    void dosEventosConLosMismosDatosDebenSerIguales() {
        EventoAlarmaCreada primero = EventoAlarmaCreada.builder()
                .id("alarma-101")
                .titulo("Entrenar")
                .contenido("Sesión de fuerza")
                .fecha("2026-03-13T09:00:00")
                .build();

        EventoAlarmaCreada segundo = EventoAlarmaCreada.builder()
                .id("alarma-101")
                .titulo("Entrenar")
                .contenido("Sesión de fuerza")
                .fecha("2026-03-13T09:00:00")
                .build();

        assertThat(primero).isEqualTo(segundo);
        assertThat(primero.hashCode()).isEqualTo(segundo.hashCode());
    }
}
