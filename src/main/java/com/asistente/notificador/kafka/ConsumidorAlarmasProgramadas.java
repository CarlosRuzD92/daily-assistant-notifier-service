package com.asistente.notificador.kafka;

import com.asistente.notificador.eventos.EventoAlarmaCreada;
import com.asistente.notificador.ejecucion.NotificacionEnviada;
import com.asistente.notificador.repositorio.NotificacionEnviadaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumidorAlarmasProgramadas {

    private final NotificacionEnviadaRepo repo;

    @KafkaListener(
            topics = "alarmas.programadas",
            groupId = "notificador",
            properties = {
                    "spring.json.trusted.packages=com.asistente.*",
                    "spring.json.use.type.headers=false",
                    "spring.json.value.default.type=com.asistente.notificador.eventos.EventoAlarmaCreada"
            }
    )
    public void onAlarmaProgramada(@Payload EventoAlarmaCreada evt) {
        if (evt == null) return;

        log.info("Recibida alarma programada -> id={} titulo='{}' fecha={}",
                evt.getId(), evt.getTitulo(), evt.getFecha());

        var saved = repo.save(NotificacionEnviada.builder()
                .idAlarma(evt.getId())
                .titulo(evt.getTitulo())
                .contenido(evt.getContenido())
                .fechaProgramada(evt.getFecha())
                .entregadaEn(Instant.now())
                .canal("LOG")
                .estado("OK")
                .detalle("Entrega simulada por notificador")
                .build());

        log.info("Notificación registrada en Mongo idDoc={}", saved.getId());
    }
}
