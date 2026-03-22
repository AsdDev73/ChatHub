# ChatHub

**ChatHub** es una aplicación móvil para Android desarrollada en **Java** como proyecto de fin de grado de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.  
La aplicación está orientada a la comunicación mediante **salas de chat temáticas**, permitiendo a los usuarios registrarse, iniciar sesión, personalizar su perfil y participar en conversaciones en tiempo real.

---

## Índice

- [Descripción del proyecto](#descripción-del-proyecto)
- [Funcionalidades principales](#funcionalidades-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura general](#arquitectura-general)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Modelo de datos](#modelo-de-datos)
- [Flujo de la aplicación](#flujo-de-la-aplicación)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Configuración de Firebase](#configuración-de-firebase)
- [Autor](#autor)

---

## Descripción del proyecto

ChatHub es una aplicación Android enfocada en la comunicación entre usuarios a través de distintas salas temáticas.  
El proyecto utiliza **Firebase** para la autenticación y para la gestión de los datos, permitiendo mantener la información de usuarios y mensajes sincronizada en tiempo real.

La aplicación ofrece una experiencia centrada en cuatro bloques principales:

- autenticación de usuarios,
- gestión del perfil,
- acceso a salas temáticas,
- envío y recepción de mensajes en tiempo real.

---

## Funcionalidades principales

### Autenticación
- Registro de usuarios mediante correo electrónico y contraseña.
- Inicio de sesión con validación de credenciales.
- Persistencia de la sesión del usuario autenticado.
- Cierre de sesión desde la pantalla de perfil.

### Gestión del perfil
- Edición del nombre del usuario.
- Selección de sexo.
- Selección de fecha de nacimiento.
- Cambio de avatar.
- Almacenamiento de la información personal en Firestore.

### Salas de chat
- Acceso a varias salas temáticas predefinidas.
- Listado visual de salas disponibles.
- Búsqueda y filtrado de salas desde la pantalla principal.
- Envío de solicitudes de nuevas salas.

### Mensajería en tiempo real
- Envío de mensajes dentro de cada sala.
- Lectura de mensajes sincronizados en tiempo real.
- Visualización de los mensajes en orden cronológico.
- Desplazamiento automático al mensaje más reciente.

---

## Tecnologías utilizadas

### Lenguaje y entorno de desarrollo
- **Java**
- **Android Studio**
- **Gradle (Kotlin DSL)**

### Desarrollo Android
- **Android SDK**
- **Activities**
- **RecyclerView**
- **ConstraintLayout**
- **Material Components**
- **Animaciones XML**

### Backend y servicios
- **Firebase Authentication**
- **Cloud Firestore**
- **Firebase Realtime Database**
- **Google Services**

---

## Arquitectura general

La aplicación se organiza en diferentes actividades, cada una encargada de una parte concreta del flujo de uso.

### Pantallas principales

#### `LogIn`
Gestiona el acceso del usuario mediante correo y contraseña.

#### `RegisterActivity`
Permite registrar nuevos usuarios y almacenar sus datos iniciales.

#### `HomeActivity`
Muestra la pantalla principal con las salas disponibles, el buscador y la opción de solicitar nuevas salas.

#### `ChatActivity`
Gestiona la conversación dentro de una sala concreta, mostrando y enviando mensajes.

#### `PerfilActivity`
Permite consultar y modificar los datos del perfil del usuario autenticado.

#### `SeleccionarAvatarActivity`
Gestiona la selección del avatar del usuario.

### Clases de apoyo

#### `Mensaje`
Modelo de datos utilizado para representar cada mensaje del chat.

#### `MessageAdapter`
Adaptador encargado de mostrar los mensajes dentro del `RecyclerView` del chat.

#### `ChatListAdapter`
Adaptador utilizado para representar las salas en la pantalla principal.

---

## Estructura del proyecto

```text
ChatHub/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/chathub/
│   │   │   │   ├── ChatActivity.java
│   │   │   │   ├── ChatListAdapter.java
│   │   │   │   ├── HomeActivity.java
│   │   │   │   ├── LogIn.java
│   │   │   │   ├── Mensaje.java
│   │   │   │   ├── MessageAdapter.java
│   │   │   │   ├── PerfilActivity.java
│   │   │   │   ├── RegisterActivity.java
│   │   │   │   └── SeleccionarAvatarActivity.java
│   │   │   ├── res/
│   │   │   │   ├── anim/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   ├── mipmap/
│   │   │   │   └── values/
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew
```

---

## Modelo de datos

La aplicación utiliza Firebase para almacenar tanto la información de usuario como los datos relacionados con las salas y los mensajes.

### Usuarios
Cada usuario dispone de información asociada a su perfil, como por ejemplo:
- nombre,
- correo electrónico,
- fecha de nacimiento,
- sexo,
- avatar.

### Salas
La aplicación parte de un conjunto de salas temáticas visibles desde la pantalla principal.

### Mensajes
Cada sala contiene su propia colección de mensajes, donde se almacena información como:
- identificador del usuario,
- nombre mostrado,
- contenido del mensaje,
- hora o marca temporal.

### Solicitudes de salas
Las peticiones enviadas por los usuarios para proponer nuevas salas se almacenan en una colección independiente.

---

## Flujo de la aplicación

### 1. Acceso inicial
El usuario entra en la aplicación y se autentica desde la pantalla de inicio de sesión.

### 2. Registro
Si no dispone de cuenta, puede registrarse introduciendo sus datos personales y seleccionando un avatar.

### 3. Pantalla principal
Tras iniciar sesión, accede a la pantalla principal, donde puede:
- visualizar las salas disponibles,
- buscar salas concretas,
- entrar en una sala,
- solicitar nuevas salas.

### 4. Chat
Al acceder a una sala, el usuario entra en la conversación correspondiente, donde puede:
- leer los mensajes existentes,
- enviar nuevos mensajes,
- mantenerse sincronizado con las actualizaciones en tiempo real.

### 5. Perfil
Desde la pantalla de perfil puede:
- consultar sus datos,
- modificar la información personal,
- cambiar el avatar,
- cerrar sesión.

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/AsdDev73/ChatHub.git
```

### 2. Abrir el proyecto
Abre el repositorio con **Android Studio** y espera a que Gradle sincronice las dependencias.

### 3. Configurar Firebase
Coloca el archivo `google-services.json` dentro de la carpeta `app/`.

### 4. Ejecutar la aplicación
Inicia la app en un emulador o en un dispositivo Android físico.

---

## Configuración de Firebase

Para ejecutar la aplicación correctamente es necesario disponer de un proyecto Firebase configurado.

### Servicios utilizados
- **Authentication**
- **Cloud Firestore**

### Archivo de configuración
El archivo de configuración de Firebase debe ubicarse en:

```text
app/google-services.json
```

### Recomendación de repositorio
Ese archivo debe mantenerse en local y fuera del repositorio público mediante `.gitignore`.

---

## Autor

**AsdDev73**  
Proyecto desarrollado como trabajo de fin de grado del ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.
