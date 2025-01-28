# CIBELService
Servicio de datos de seguridad y sostenibilidad de dispositivos IoT y aplicaciones Android.

En el archivo Instrucciones.txt se encunetran las instrucciones y la documentación respectiva del servicio.

## Crear servicio Docker en local

Desde el nivel de jerarquía donde está el DockerFile:
- docker build -t cibel-service:latest .
- docker run -d -p 3307:3306 -p 8080:8080 --name cibel_container cibel-service:latest

## Crear y subir a DockerHub

- docker build -t cibel-service .
- docker login
- docker tag cibel-service miusuario/cibel-service:latest
- docker push miusuario/cibel-service:latest

## Descargar de DockerHub

- docker pull miusuario/cibel-service:latest
- docker run -d -p 3307:3306 -p 8080:8080 miusuario/cibel-service:latest

En el navegador, para ver que funciona: http://localhost:8080/apps 
