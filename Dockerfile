# Imagen base de Ubuntu (o tu distro favorita)
FROM ubuntu:22.04

# Evitamos prompts en la instalación de paquetes
ENV DEBIAN_FRONTEND=noninteractive

# 1. Actualizar repos e instalar:
#    - MySQL Server
#    - OpenJDK (para correr tu app Java)
#    - netcat (nc) para chequear si MySQL está activo (opcional, pero útil)
RUN apt-get update && apt-get install -y \
    mysql-server \
    openjdk-17-jdk \
    netcat \
 && apt-get clean

# 2. Copiamos la aplicación (CIBELService.jar) y el backup de la BD al contenedor
WORKDIR /app
COPY CIBELServiceSpring/target/CIBELService.jar /app/CIBELService.jar
COPY Datos/backup_cibel_db.sql /app/backup_cibel_db.sql

# 3. Copiamos el script de entrada (entrypoint.sh) y le damos permisos de ejecución
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# 4. Exponemos los puertos:
#    - 3306 para MySQL
#    - 8080 para la aplicación Java (ajustar si tu app usa otro puerto)
EXPOSE 3306 8080

# 5. Indicamos el ENTRYPOINT
#    Va a ejecutar el script que arranca MySQL, espera a que esté operativo
#    y luego lanza tu aplicación Java
ENTRYPOINT ["/entrypoint.sh"]
