### Escuela Colombiana de Ingeniería

### Arquitectura Empresarial - AREP

#  TALLER SECURE APPLICATION DESIGN

El taller se centra en diseñar y desplegar una aplicación segura y escalable utilizando la infraestructura de AWS. Cuenta con dos servidores principales: un servidor Apache para servir un cliente HTML+JavaScript seguro a través de TLS, y un servidor Spring para los servicios backend con APIs RESTful, también aseguradas por TLS. Las principales características de seguridad incluyen cifrado TLS, optimización del cliente asincrónico, seguridad en el inicio de sesión con contraseñas almacenadas como hashes, y el despliegue en AWS para garantizar la fiabilidad.


## Despliegue


### Prerequisitos

- Java 17 preferiblemente.
- Maven 3.x
- Acceso a una terminal.
- Cuenta con creditos en AWS.
- Tener una conexion a una DB MySql ya configurada (preferiblemente en una instancia EC2)
- Abrir puertos necesarios para las instancias EC2 (443, 22, y demás)

### Obtención certificado con Duck DNS
Primero se crean las instancias EC2 para el back y el front, con las ips publicas creamos dominios con el uso de duck dns.
![image](https://github.com/user-attachments/assets/5043b7dc-da92-4ec1-a428-e8a553e9f93b)

### Certificado para el servicio back-end (Spring)

1. Accede a la instancia (ya sea desde la consola o con la llave privada).

2. Intala las dependencias necesarias:
   ```bash
   sudo yum install epel-release -y
   ```
   ```bash
   sudo yum install certbot python3-certbot-apache -y
   ```
   ```bash
   sudo certbot certonly --standalone -d tudominio.duckdns.org
   ```
3. Una vez generado el certificado para el dominio con los archivos generados `fullchain.pem` y `privkey.pem` se creara el ecikeystore.p12 con el siguiente comando:
   ```bash
   sudo openssl pkcs12 -export \
   -in /etc/letsencrypt/live/tudominio.duckdns.org/fullchain.pem \
   -inkey /etc/letsencrypt/live/tudominio.duckdns.org/privkey.pem \
   -out /ruta/donde/guardar/ecikeystore.p12 \
   -name "tudominio" \
   -CAfile /etc/letsencrypt/live/tudominio.duckdns.org/chain.pem \
   -caname "Let's Encrypt
   ```
4. Ahora se debe descargar el archivo y moverlo al directorio `src/main/resources/keystore` de nuestro proyecto. Adicionalmente se debe configurar el archivo application.properties:

   ```bash
   scp -i "/ruta/llaveprivada/key.pem" usuarioEC2@ip_instancia_ec2:/ruta/llave "/ruta/destino"
   ```

   ```yml
   # The format used for the keystore. It could be set to JKS in case it is a JKS file 
   server.ssl.key-store-type=PKCS12
   # The path to the keystore containing the certificate 
   server.ssl.key-store=classpath:keystore/keystore.p12
   # The password used to generate the certificate 
   server.ssl.key-store-password=${KEYSTORE_PASSWORD}
   # The alias mapped to the certificate 
   server.ssl.key-alias=arep-taller-6
   server.ssl.enabled=true
   ```
### Certificado para el servicio front-end (Apache)

1. Accede a la instancia (ya sea desde la consola o con la llave privada).

2. Intala las dependencias necesarias:
   ```bash
   sudo yum update -y
   ```
   ```bash
   sudo yum install httpd -y
   ```
   ```bash
   sudo systemctl start httpd
   ```
   ```bash
   sudo systemctl enable httpd
   ```
3. Se procede a mover los archivos estaticos (HTML-JS-CSS) a la instancia EC2.
   ```bash
   cd /ruta/archivos_estaticos
   scp -i "/ruta/llave_privada" *.html *.css *.js usuario_instanciar@ip_instancia:/ruta_destino
   ```
4. Se mueven los archivos estaticos a la carpeta /var/www/html.
   ```bash
   sudo mv *.html /var/www/html
   sudo mv *.css /var/www/html
   sudo mv *.js /var/www/html
   ```
![image](https://github.com/user-attachments/assets/c424ab1e-2529-492b-869c-479a777fae40)
5. Activamos SSL para el servicio de Apache
   ```bash
   sudo yum install mod_ssl -y
   sudo systemctl restart httpd
   ```


Ya con las llaves privadas es suficiente para poder realizar la configuración.

### Creacion de imagenes de docker

Para poder crear una imagen Docker se ejecuta el siguiente comando:
   ```bash
   docker build --tag arep6 .
   ```
![image](https://github.com/user-attachments/assets/894c754a-b2e2-4f88-835f-d3e80a65047b)

Luego se crea referencia a la imagen creada para poder subirla a docker hub.
   ```bash
   docker build --tag arep6 nombre_usuario_docker/arep-6-back
   ```
![image](https://github.com/user-attachments/assets/11ca9a94-6b9e-440b-8e59-41809eb6fa33)

Se inicia sesión.
   ```bash
   docker login
   ```
Se pushea la imagen:
   ```bash
   docker push nombre_usuario_docker/arep-6-back:latest
   ```
![image](https://github.com/user-attachments/assets/5b954aab-ea19-42ae-bd4e-014ccd74e908)

### En la instancia EC2 con la DB
Para este punto ya se asume que se realizo las configuraciones e intalaciones para la base de datos. (En nuestro caso de mysql) Por lo que solo es necesario iniciar el contenedor Docker con la base de datos.

![image](https://github.com/user-attachments/assets/58db1a15-b5e5-4bb6-8b58-a71fc7a2af14)

### En la instancia EC2 designada para el back

Creamos el .env donde se almacenaran los siguientes valores: DB_URL, DB_USERNAME, DB_PASSWORD y KEYSTORE_PASSWORD
   ```bash
   touch .env
   nano .env
   ```
Creamos un contenedor docker con el siguiente comando: 
   ```bash
   docker run --env-file .env -d -p 443:443 --name arep-6-back unandresmasxd/arep-6-back
   ```
![image](https://github.com/user-attachments/assets/a458f425-018d-46f9-9128-bdaff863f96f)

Para probar su correcto funcionamiento (Certificado válido)
![image](https://github.com/user-attachments/assets/57392aab-9cc8-404c-b916-172028eecc9d)


### Creación y configuración del WebServer

En la instancia EC2 destinada para el servicio front-end.

Se intalan las dependencias necesarias: 
   ```bash
   sudo yum install httpd -y
   ```
   ```bash
   sudo systemctl start httpd
   ```
   ```bash
   sudo systemctl enable httpd
   ```



## Arquitectura


### Diagrama de despliegue

El siguiente diagrama de despliegue describe la estructura básica de la aplicación.

![Deployment](https://github.com/user-attachments/assets/b6b85b9a-a19e-4865-b59b-927f113c37f3)

### Descripción del Diagrama de Despliegue

#### 1. **WebClient**
   - **Puerto 8087**: Punto de entrada para las solicitudes HTTP desde el navegador.

#### 2. **EC2 INSTANCE (Aplicación en Contenedor Docker)**
   - **FrontEnd**: Se encarga de la interfaz de usuario y la comunicación con el backend.
   - **BackEnd**: Procesa las peticiones del frontend, maneja la lógica de negocio y se comunica con la base de datos.

#### 3. **EC2 INSTANCE (Base de Datos MySQL en Contenedor Docker)**
   - **DockerContainer con MySQLDB**: Contenedor que aloja la base de datos MySQL para almacenamiento y consulta de datos.
   - **Puerto 3307**: Comunicación entre el backend y la base de datos MySQL.

---

### **Flujo de la Aplicación**
1. El **navegador** del usuario (WebClient) envía solicitudes HTTP al backend a través del puerto **8087**.
2. El **FrontEnd** procesa la solicitud y, si es necesario, la envía al **BackEnd** dentro de la instancia EC2.
3. El **BackEnd** maneja la lógica de negocio y, si requiere acceso a datos, consulta la base de datos **MySQL** a través del puerto **3307**.
4. La base de datos MySQL devuelve la información solicitada al **BackEnd**, que la procesa y envía una respuesta al **FrontEnd**.
5. El **FrontEnd** actualiza la interfaz del usuario con la respuesta obtenida.

---


### Diagrama de componentes.
El siguiente diagrama de despliegue describe la estructura básica de la aplicación. Utilizando el patron MVC.

![Component](https://github.com/user-attachments/assets/6816960c-15e7-4796-b67a-998d52b7f844)


### 1. **WebClient**
   - **Browser**: Punto de entrada de la aplicación, donde los usuarios interactúan con la interfaz.
   - **Spring**: Enlace entre el navegador y la aplicación, que maneja las solicitudes HTTP.

### 2. **App**
   - Contiene los componentes del **FrontEnd** y **BackEnd**.

#### **FrontEnd**
   - **Html**: Archivos HTML que estructuran la interfaz de usuario.
   - **Js**: Archivos JavaScript que proporcionan interactividad y comunicación con el backend.

#### #**BackEnd**
   - **Model**: Define la estructura de datos utilizada en la aplicación.
   - **Service**: Implementa la lógica de negocio y orquesta la interacción con los repositorios.
   - **Controller**: Gestiona las solicitudes HTTP y delega el procesamiento a los servicios.
   - **Repository**: Componente que interactúa con la base de datos.
   - **JPARepository**: Implementación de acceso a datos que utiliza JPA para comunicarse con la base de datos.

### 3. **Base de Datos**
   - **MySqlDb**: Sistema de almacenamiento de datos utilizado por la aplicación.

---

## **Flujo de la Aplicación**
1. El **navegador** envía solicitudes a través de **Spring** al backend.
2. El **Controller** recibe la solicitud y la pasa a los **Services**.
3. Los **Services** interactúan con el **Repository** para acceder a los datos.
4. El **Repository** utiliza **JPARepository** para realizar consultas en la base de datos **MySqlDb**.
5. Los datos recuperados se devuelven al **Controller**, que genera una respuesta para el **FrontEnd**.
6. El **FrontEnd** muestra los datos en la interfaz utilizando **Html** y **Js**.

---


### Funcionamiento.



https://github.com/user-attachments/assets/36d71a5a-f8b8-492a-9741-48147fdb710c




# Tecnologías Usadas en Pruebas
- **JUnit Jupiter 5:** Para pruebas unitarias y parametrizadas.
- **Mockito:** Para pruebas unitarias sobre los componentes Controller y Service
- **Maven:** Gestión de dependencias y ejecución de pruebas.

- **Resultado de las pruebas**

Para correr las pruebas automatizadas utilize el comando **IMPORTANTE: Debe configurar las variables de entorno necesarias para realizar la conexión a la DB, de lo contrario habrán pruebas que fallen**.


```bash
   mvn clean test
```

![image](https://github.com/user-attachments/assets/10d04c55-0ac5-447e-ad60-61aa2384a98f)



## Construido con.

- [Maven](https://maven.apache.org/) - Dependency Management

## Autores

- **Milton Andres Gutierrez Lopez** - *Initial work* - [MiltonGutierrez](https://github.com/MiltonGutierrez)

## Licencia

Este proyecto está licenciado bajo la Licencia GNU - mira el archivo [LICENSE.md](LICENSE.md) para más detalles.
