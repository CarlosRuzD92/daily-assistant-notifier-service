package com.asistente.notificador.kafka;

import com.asistente.notificador.ejecucion.NotificacionEnviada;
import com.asistente.notificador.eventos.EventoAlarmaCreada;
import com.asistente.notificador.repositorio.NotificacionEnviadaRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumidorAlarmasProgramadasTest {

    @Mock
    private NotificacionEnviadaRepo repo;

    @InjectMocks
    private ConsumidorAlarmasProgramadas consumidor;

    @Test
    void debeGuardarLaNotificacionCuandoRecibeUnEventoValido() {
        EventoAlarmaCreada evento = EventoAlarmaCreada.builder()
                .id("alarma-1")
                .titulo("Beber agua")
                .contenido("Tomar un vaso de agua")
                .fecha("2026-03-12T16:00:00")
                .build();

        when(repo.save(any(NotificacionEnviada.class))).thenAnswer(invocation -> {
            NotificacionEnviada guardada = invocation.getArgument(0);
            guardada.setId("doc-123");
            return guardada;
        });

        consumidor.onAlarmaProgramada(evento);

        ArgumentCaptor<NotificacionEnviada> captor = ArgumentCaptor.forClass(NotificacionEnviada.class);
        verify(repo).save(captor.capture());

        NotificacionEnviada notificacionGuardada = captor.getValue();
        assertThat(notificacionGuardada.getIdAlarma()).isEqualTo("alarma-1");
        assertThat(notificacionGuardada.getTitulo()).isEqualTo("Beber agua");
        assertThat(notificacionGuardada.getContenido()).isEqualTo("Tomar un vaso de agua");
        assertThat(notificacionGuardada.getFechaProgramada()).isEqualTo("2026-03-12T16:00:00");
        assertThat(notificacionGuardada.getEntregadaEn()).isNotNull();
        assertThat(notificacionGuardada.getCanal()).isEqualTo("LOG");
        assertThat(notificacionGuardada.getEstado()).isEqualTo("OK");
        assertThat(notificacionGuardada.getDetalle()).isEqualTo("Entrega simulada por notificador");
    }

    @Test
    void noDebeGuardarNadaSiElEventoEsNull() {
        consumidor.onAlarmaProgramada(null);

        verify(repo, never()).save(any(NotificacionEnviada.class));
    }
}
