-- Creación de la base de datos (ya se crea automáticamente por docker-compose)
USE solicitudes_db;

-- Tabla principal de solicitudes
CREATE TABLE IF NOT EXISTS solicitudes (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(255) NOT NULL,
    token_solicitud INT UNIQUE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre_usuario (nombre_usuario),
    INDEX idx_token_solicitud (token_solicitud)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de entidades asociadas a cada solicitud
CREATE TABLE IF NOT EXISTS entidades_solicitud (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud INT NOT NULL,
    nombre_entidad VARCHAR(255) NOT NULL,
    cantidad_inicial INT NOT NULL,
    FOREIGN KEY (id_solicitud) REFERENCES solicitudes(id_solicitud) ON DELETE CASCADE,
    INDEX idx_id_solicitud (id_solicitud)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de resultados (relación 1:1 con solicitudes)
CREATE TABLE IF NOT EXISTS resultados (
    id_resultado INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud INT UNIQUE NOT NULL,
    datos_resultado TEXT,
    fecha_procesamiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_solicitud) REFERENCES solicitudes(id_solicitud) ON DELETE CASCADE,
    INDEX idx_id_solicitud (id_solicitud)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Comentarios de las tablas
ALTER TABLE solicitudes COMMENT = 'Almacena las solicitudes de los usuarios';
ALTER TABLE entidades_solicitud COMMENT = 'Almacena las entidades y cantidades de cada solicitud';
ALTER TABLE resultados COMMENT = 'Almacena el resultado procesado de cada solicitud (1:1)';
