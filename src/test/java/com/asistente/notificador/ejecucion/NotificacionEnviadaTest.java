package com.asistente.notificador.ejecucion;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class NotificacionEnviadaTest {

    @Test
    void builderDebeConstruirLaNotificacionCorrectamente() {
        Instant ahora = Instant.parse("2026-03-12T10:15:30Z");

        NotificacionEnviada notificacion = NotificacionEnviada.builder()
                .id("doc-1")
                .idAlarma("alarma-1")
                .titulo("Recordatorio")
                .contenido("Tomar medicación")
                .fechaProgramada("2026-03-12T11:00:00")
                .entregadaEn(ahora)
                .canal("LOG")
                .estado("OK")
                .detalle("Entrega simulada por notificador")
                .build();

        assertThat(notificacion.getId()).isEqualTo("doc-1");
        assertThat(notificacion.getIdAlarma()).isEqualTo("alarma-1");
        assertThat(notificacion.getTitulo()).isEqualTo("Recordatorio");
        assertThat(notificacion.getContenido()).isEqualTo("Tomar medicación");
        assertThat(notificacion.getFechaProgramada()).isEqualTo("2026-03-12T11:00:00");
        assertThat(notificacion.getEntregadaEn()).isEqualTo(ahora);
        assertThat(notificacion.getCanal()).isEqualTo("LOG");
        assertThat(notificacion.getEstado()).isEqualTo("OK");
        assertThat(notificacion.getDetalle()).isEqualTo("Entrega simulada por notificador");
    }

    @Test
    void settersYGettersDebenFuncionarCorrectamente() {
        Instant ahora = Instant.parse("2026-03-12T12:00:00Z");
        NotificacionEnviada notificacion = new NotificacionEnviada();

        notificacion.setId("doc-2");
        notificacion.setIdAlarma("alarma-2");
        notificacion.setTitulo("Cita médica");
        notificacion.setContenido("Consulta a las 18:00");
        notificacion.setFechaProgramada("2026-03-12T18:00:00");
        notificacion.setEntregadaEn(ahora);
        notificacion.setCanal("EMAIL");
        notificacion.setEstado("PENDIENTE");
        notificacion.setDetalle("Pendiente de envío");

        assertThat(notificacion.getId()).isEqualTo("doc-2");
        assertThat(notificacion.getIdAlarma()).isEqualTo("alarma-2");
        assertThat(notificacion.getTitulo()).isEqualTo("Cita médica");
        assertThat(notificacion.getContenido()).isEqualTo("Consulta a las 18:00");
        assertThat(notificacion.getFechaProgramada()).isEqualTo("2026-03-12T18:00:00");
        assertThat(notificacion.getEntregadaEn()).isEqualTo(ahora);
        assertThat(notificacion.getCanal()).isEqualTo("EMAIL");
        assertThat(notificacion.getEstado()).isEqualTo("PENDIENTE");
        assertThat(notificacion.getDetalle()).isEqualTo("Pendiente de envío");
    }
}
