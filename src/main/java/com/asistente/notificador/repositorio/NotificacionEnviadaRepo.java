package com.asistente.notificador.repositorio;

import com.asistente.notificador.ejecucion.NotificacionEnviada;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificacionEnviadaRepo extends MongoRepository<NotificacionEnviada, String> {}
