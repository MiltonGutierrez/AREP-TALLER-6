### Escuela Colombiana de Ingeniería

### Arquitectura Empresarial - AREP

#  TALLER SECURE APPLICATION DESIGN

El taller se centra en diseñar y desplegar una aplicación segura y escalable utilizando la infraestructura de AWS. Cuenta con dos servidores principales: un servidor Apache para servir un cliente HTML+JavaScript seguro a través de TLS, y un servidor Spring para los servicios backend con APIs RESTful, también aseguradas por TLS. Las principales características de seguridad incluyen cifrado TLS, optimización del cliente asincrónico, seguridad en el inicio de sesión con contraseñas almacenadas como hashes, y el despliegue en AWS para garantizar la fiabilidad.


# Despliegue


## Prerequisitos

- Java 17 preferiblemente.
- Maven 3.x
- Acceso a una terminal.
- Cuenta con creditos en AWS.
- Tener una conexion a una DB MySql ya configurada (preferiblemente en una instancia EC2)
- Abrir puertos necesarios para las instancias EC2 (443, 22, y demás)

## Obtención certificado con Duck DNS
Primero se crean las instancias EC2 para el back y el front, con las ips publicas creamos dominios con el uso de duck dns.
![image](https://github.com/user-attachments/assets/5043b7dc-da92-4ec1-a428-e8a553e9f93b)

## Certificado para el servicio back-end (Spring)

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
## Certificado para el servicio front-end (Apache)

1. Accede a la instancia (ya sea desde la consola o con la llave privada).

2. Intala las dependencias necesarias:
   ```bash
   sudo yum update -y
   ```
   ```bash
   sudo yum install httpd -y
   ```
   ```bash
   sudo systemctl enable httpd
   ```
    ```bash
   sudo yum install certbot python3-certbot-apache -y
   ```
3. Se crea el certificado (con el dominio que apunta a la instancia para el front).
   ```bash
   sudo certbot certonly --standalone -d tudominio.duckdns.org
   ```
4. Una vez creado se debe modificar el archvio de configuración que apunta a las llaver y certificados SSL de Apache

```bash
   sudo nano /etc/httpd/conf.d/ssl.conf
```
5. Se cambia a la ruta de las llaves y certificados generados como se muestra en la imagen:

![image](https://github.com/user-attachments/assets/6985fb29-564f-4610-bc31-178de2e19414)


## Creacion de imagenes de docker

1. Para poder crear una imagen Docker se ejecuta el siguiente comando:
   ```bash
   docker build --tag arep6 .
   ```
![image](https://github.com/user-attachments/assets/894c754a-b2e2-4f88-835f-d3e80a65047b)

2. Luego se crea referencia a la imagen creada para poder subirla a docker hub.
   ```bash
   docker build --tag arep6 nombre_usuario_docker/arep-6-back
   ```
![image](https://github.com/user-attachments/assets/11ca9a94-6b9e-440b-8e59-41809eb6fa33)

3. Se inicia sesión.
   ```bash
   docker login
   ```
4. Se pushea la imagen:
   ```bash
   docker push nombre_usuario_docker/arep-6-back:latest
   ```
![image](https://github.com/user-attachments/assets/5b954aab-ea19-42ae-bd4e-014ccd74e908)

## En la instancia EC2 con la DB
Para este punto ya se asume que se realizo las configuraciones e intalaciones para la base de datos. (En nuestro caso de mysql) Por lo que solo es necesario iniciar el contenedor Docker con la base de datos.

![image](https://github.com/user-attachments/assets/58db1a15-b5e5-4bb6-8b58-a71fc7a2af14)

### En la instancia EC2 designada para el back

1. Creamos el .env donde se almacenaran los siguientes valores: DB_URL, DB_USERNAME, DB_PASSWORD y KEYSTORE_PASSWORD
   ```bash
   touch .env
   nano .env
   ```
2. Creamos un contenedor docker con el siguiente comando: 
   ```bash
   docker run --env-file .env -d -p 443:443 --name arep-6-back unandresmasxd/arep-6-back
   ```
![image](https://github.com/user-attachments/assets/a458f425-018d-46f9-9128-bdaff863f96f)

3. Para probar su correcto funcionamiento (Certificado válido)
![image](https://github.com/user-attachments/assets/57392aab-9cc8-404c-b916-172028eecc9d)


## Creación y configuración del WebServer

1. En la instancia EC2 destinada para el servicio front-end.

2. Se intalan las dependencias necesarias: 
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
6. Se crea el host virtual
   ```bash
   sudo nano /etc/httpd/conf.d/tu-dominio.conf
   ```

   ```yml
   <VirtualHost *:443>
       ServerName arep-taller-6-front.duckdns.org
       ServerAlias www.arep-taller-6-front.duckdns.org
       DocumentRoot /var/www/html
   
       SSLEngine on
       SSLCertificateFile /etc/letsencrypt/live/arep-taller-6-front.duckdns.org/fullchain.pem
       SSLCertificateKeyFile /etc/letsencrypt/live/arep-taller-6-front.duckdns.org/privkey.pem
   
       DirectoryIndex index.html
   
       Redirect permanent / https://arep-taller-6-front.duckdns.org
   
       <Directory /var/www/html>
           AllowOverride All
           Require all granted
       </Directory>
   
       RewriteEngine on
       RewriteCond %{SERVER_NAME} =arep-taller-6-front.duckdns.org [OR]
       RewriteCond %{SERVER_NAME} =www.arep-taller-6-front.duckdns.org
       RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [END,NE,R=permanent]
   </VirtualHost>
   ```

# Funcionamiento 

https://github.com/user-attachments/assets/2a1b1707-a849-4767-8e46-354f608b7be9

# Arquitectura

## Diagrama de despliegue

El siguiente diagrama de despliegue describe la estructura básica de la aplicación.

![Deployment](https://github.com/user-attachments/assets/6369756e-0abc-4786-b759-fc78f0ba9c07)

###  **Nodos Principales**
#### **WebClient**
- **Descripción:** Representa el cliente web desde el cual los usuarios acceden a la aplicación.
- **Componente:**
  - **Browser** → El navegador utilizado para interactuar con la aplicación.
- **Conexión:**
  - Se comunica con el **Frontend** a través de **HTTPS (puerto 443)**.

#### **Instancia EC2 con Frontend**
- **Descripción:** Servidor donde se aloja el **Frontend** dentro de un **contenedor Docker**.
- **Componentes:**
  - **DockerContainer** → Contiene la aplicación del **Frontend**.
- **Conexión:**
  - Recibe solicitudes del **WebClient** por **HTTPS (puerto 443)**.
  - Se comunica con el **Backend** a través de **HTTPS (puerto 443)**.

#### **Instancia EC2 con Backend**
- **Descripción:** Servidor donde se aloja el **Backend** dentro de un **contenedor Docker**.
- **Componentes:**
  - **DockerContainer** → Contiene la aplicación del **Backend**.
- **Conexión:**
  - Recibe solicitudes del **Frontend** por **HTTPS (puerto 443)**.
  - Se comunica con la base de datos **MySQL** a través del **puerto 3307**.

#### **Instancia EC2 con MySQL**
- **Descripción:** Servidor que aloja la base de datos **MySQL** dentro de un **contenedor Docker**.
- **Componentes:**
  - **DockerContainer with MySqlDB** → Contenedor con el servicio de **MySQL**.
- **Conexión:**
  - Recibe solicitudes del **Backend** a través del **puerto 3307**.

---

## **Relaciones y Conexiones**
- El **WebClient (Browser)** se comunica con el **Frontend** en la **instancia EC2** mediante **HTTPS (puerto 443)**.
- El **Frontend** dentro de un **contenedor Docker** se comunica con el **Backend** en otra **instancia EC2** a través de **HTTPS (puerto 443)**.
- El **Backend** dentro de un **contenedor Docker** interactúa con la base de datos **MySQL** en otra **instancia EC2** a través del **puerto 3307**.

---


## Diagrama de componentes.
El siguiente diagrama de despliegue describe la estructura básica de la aplicación. Utilizando el patron MVC.

![Component](https://github.com/user-attachments/assets/6816960c-15e7-4796-b67a-998d52b7f844)

### **Componentes Principales**
#### **Cliente (Browser)**
- **Descripción:** Representa el navegador web que interactúa con la aplicación.
- **Conexión:** Se comunica con el **Frontend** mediante HTTPS en el puerto **443**.

### **Frontend**
- **Descripción:** Es la capa de presentación de la aplicación.
- **Componentes:**
  - **Html** → Representa la estructura de la interfaz de usuario.
  - **Js** → Representa los scripts y lógica del lado del cliente.
- **Conexión:** Se comunica con el navegador y con el **Backend** mediante HTTPS en el puerto **443**.

###  **Backend**
- **Descripción:** Es la capa lógica de negocio y de servicios.
- **Componentes:**
  - **Model** → Define la estructura de datos utilizada en la aplicación.
  - **Service** → Contiene la lógica de negocio.
  - **Controller** → Gestiona las solicitudes del cliente y envía respuestas.
  - **Repository** → Maneja la persistencia de datos y se comunica con la base de datos.
- **Conexión:** Se comunica con el **Frontend** y la base de datos.

### **Base de Datos (MySQL)**
- **Descripción:** Almacena los datos de la aplicación.
- **Conexión:** Se comunica con el **JPARepository** en el backend para la gestión de datos.

---

##  **Relaciones y Conexiones**
- El **Browser** accede al **Frontend** mediante **HTTPS (443)**.
- El **Frontend** se comunica con el **Backend** a través de **HTTPS (443)**.
- El **Backend** se estructura en capas:
  - **Controller** recibe las solicitudes del **Frontend**.
  - **Service** maneja la lógica de negocio.
  - **Repository** gestiona el acceso a datos.
  - **JPARepository** interactúa con **MySQL** para la persistencia de datos.

---

## Distribución de clases
```
📂 src
├── 📂 main
│   ├── 📂 java/edu/escuelaing/arep/taller6
│   │   ├── 📂 controller
│   │   │   ├── 📂 impl
│   │   │   │   ├── 📄 PropertyListingControllerImpl.java
│   │   │   │   ├── 📄 UserControllerImpl.java
│   │   │   ├── 📂 interfaces
│   │   │   │   ├── 📄 PropertyListingController.java
│   │   │   │   ├── 📄 UserController.java
│   │   ├── 📂 exception
│   │   │   ├── 📄 PropertyListingException.java
│   │   │   ├── 📄 UserException.java
│   │   ├── 📂 model
│   │   │   ├── 📄 Property.java
│   │   │   ├── 📄 User.java
│   │   ├── 📂 repository
│   │   │   ├── 📄 PropertyListingRepository.java
│   │   │   ├── 📄 UserRepository.java
│   │   ├── 📂 services
│   │   │   ├── 📂 impl
│   │   │   │   ├── 📄 PropertyListingServicesImpl.java
│   │   │   │   ├── 📄 UserServiceImpl.java
│   │   │   ├── 📂 interfaces
│   │   │   │   ├── 📄 PropertyListingServices.java
│   │   │   │   ├── 📄 UserServices.java
│   │   ├── 📄 Taller6.java
│   ├── 📂 resources
│   │   ├── 📂 keystore
│   │   │   ├── 📄 ecikeystore.p12
│   │   ├── 📄 application.properties
├── 📂 test
│   ├── 📂 java/edu/escuelaing/arep/taller6
│   │   ├── 📂 controller
│   │   │   ├── 📄 PropertyListingControllerTest.java
│   │   │   ├── 📄 UserControllerTest.java
│   │   ├── 📂 services
│   │   │   ├── 📄 PropertyListingServiceTest.java
│   │   │   ├── 📄 UserServicesTest.java
```

# Tecnologías Usadas en Pruebas
- **JUnit Jupiter 5:** Para pruebas unitarias y parametrizadas.
- **Mockito:** Para pruebas unitarias sobre los componentes Controller y Service
- **Maven:** Gestión de dependencias y ejecución de pruebas.

- **Resultado de las pruebas**

Para correr las pruebas automatizadas utilize el comando:

```bash
   mvn clean test
```

![image](https://github.com/user-attachments/assets/6e8ad07e-10b2-47d8-8ce4-1c7b0ab073c9)


## Construido con.

- [Maven](https://maven.apache.org/) - Dependency Management

## Autores

- **Milton Andres Gutierrez Lopez** - *Initial work* - [MiltonGutierrez](https://github.com/MiltonGutierrez)

## Agradecimientos 
- **Juan David Contreras Becerra** - Se toma parte de la guáa de generación de cerficados TSL [Repositorio](https://github.com/jcontreras2693/AREP-Lab6)

## Licencia

Este proyecto está licenciado bajo la Licencia GNU - mira el archivo [LICENSE.md](LICENSE.md) para más detalles.
