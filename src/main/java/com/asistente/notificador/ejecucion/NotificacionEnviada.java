package com.asistente.notificador.ejecucion;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("notificaciones_enviadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificacionEnviada {
  @Id private String id;
  private String idAlarma;
  private String titulo;
  private String contenido;
  private String fechaProgramada;
  private Instant entregadaEn;
  private String canal;
  private String estado;
  private String detalle;
}
