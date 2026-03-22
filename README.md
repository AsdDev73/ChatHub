# ChatHub

Aplicación Android desarrollada como **TFG del Grado Superior de DAM**.  
**ChatHub** es una app de chat por salas temática, donde los usuarios pueden registrarse, iniciar sesión, personalizar su perfil y participar en conversaciones en tiempo real.

---

## Índice

- [Descripción](#descripción)
- [Características principales](#características-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Estructura del repositorio](#estructura-del-repositorio)
- [Instalación y puesta en marcha](#instalación-y-puesta-en-marcha)
- [Configuración de Firebase](#configuración-de-firebase)
- [Flujo de la aplicación](#flujo-de-la-aplicación)
- [Posibles mejoras futuras](#posibles-mejoras-futuras)
- [Autor](#autor)

---

## Descripción

ChatHub es una aplicación móvil para Android orientada a la comunicación en salas de chat temáticas.  
El proyecto ha sido desarrollado en **Java** con **Android Studio** y utiliza **Firebase** como backend para autenticación y almacenamiento de datos.

La aplicación permite:

- registrarse con correo y contraseña,
- iniciar sesión,
- editar el perfil del usuario,
- seleccionar avatar,
- navegar entre salas temáticas,
- enviar y recibir mensajes en tiempo real,
- solicitar nuevas salas de chat.

Este proyecto forma parte de mi **Trabajo de Fin de Grado (TFG)** en DAM.

---

## Características principales

### Autenticación de usuarios
- Registro de cuenta mediante correo electrónico y contraseña.
- Inicio de sesión seguro con Firebase Authentication.
- Cierre de sesión desde la pantalla de perfil.

### Gestión de perfil
- Edición de nombre.
- Selección de sexo.
- Fecha de nacimiento mediante selector de fecha.
- Cambio de avatar.
- Persistencia de datos del usuario en Firestore.

### Sistema de salas
- Salas temáticas predefinidas:
  - General
  - Fútbol
  - Programación
  - Cine
  - Videojuegos
  - Cocina
  - Motos
- Buscador para filtrar salas disponibles.
- Posibilidad de solicitar nuevas salas desde la pantalla principal.

### Chat en tiempo real
- Envío de mensajes dentro de cada sala.
- Lectura de mensajes en tiempo real.
- Actualización automática del chat.
- Scroll automático al último mensaje enviado.

### Experiencia de usuario
- Interfaz nativa Android.
- Navegación entre pantallas con animaciones.
- Selección visual de avatar.
- Diseño sencillo, centrado en la usabilidad.

---

## Tecnologías utilizadas

### Lenguaje y entorno
- **Java**
- **Android Studio**
- **Gradle (Kotlin DSL)**

### SDK y librerías
- **Android SDK**
- **Material Components**
- **RecyclerView / ConstraintLayout**
- **Firebase Authentication**
- **Cloud Firestore**
- **Firebase Realtime Database** (dependencia incluida en el proyecto)
- **Google Services Plugin**

---

## Arquitectura del proyecto

La aplicación está organizada mediante actividades independientes para cada funcionalidad principal:

- **LogIn**  
  Pantalla de inicio de sesión.

- **RegisterActivity**  
  Registro de nuevos usuarios y almacenamiento de datos iniciales.

- **HomeActivity**  
  Pantalla principal con listado de salas, buscador y solicitud de nuevas salas.

- **ChatActivity**  
  Gestión del chat dentro de una sala concreta.

- **PerfilActivity**  
  Consulta y edición del perfil del usuario.

- **SeleccionarAvatarActivity**  
  Pantalla de selección de avatar.

### Modelo de datos principal

#### Colección `usuarios`
Cada usuario almacena información como:
- nombre,
- email,
- fecha de nacimiento,
- sexo,
- avatar.

#### Colección `rooms/{roomName}/messages`
Cada sala contiene su subcolección de mensajes, donde se guarda:
- nombre del usuario,
- texto del mensaje,
- hora,
- UID del usuario.

#### Colección `chat_requests`
Se almacenan las solicitudes de nuevas salas enviadas por los usuarios.

---

## Estructura del repositorio

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
│   │   │   │   ├── layout/
│   │   │   │   ├── drawable/
│   │   │   │   ├── anim/
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

## Instalación y puesta en marcha

### 1. Clonar el repositorio

```bash
git clone https://github.com/AsdDev73/ChatHub.git
```

### 2. Abrir en Android Studio
Abre el proyecto desde Android Studio y deja que Gradle sincronice todas las dependencias.

### 3. Configurar Firebase
Este proyecto necesita un archivo `google-services.json` válido dentro de la carpeta `app/`.

### 4. Ejecutar la aplicación
Lanza la app en un emulador Android o en un dispositivo físico.

---

## Configuración de Firebase

Para ejecutar correctamente el proyecto necesitas crear o enlazar un proyecto en Firebase.

### Pasos recomendados
1. Crear un proyecto en Firebase.
2. Registrar la app Android con el paquete correspondiente.
3. Descargar el archivo `google-services.json`.
4. Colocarlo dentro de:

```text
app/google-services.json
```

5. Activar los servicios necesarios:
   - Authentication
   - Cloud Firestore

### Importante
Por seguridad, **no deberías subir tu configuración real de Firebase a un repositorio público** si no es necesario.  
Lo ideal es:

- ignorar `app/google-services.json` en `.gitignore`,
- mantener la configuración solo en local,
- documentar en este README cómo generar el archivo.

---

## Flujo de la aplicación

### 1. Login
El usuario inicia sesión con correo y contraseña.

### 2. Registro
Si no tiene cuenta, puede registrarse indicando:
- nombre,
- email,
- contraseña,
- fecha de nacimiento,
- sexo,
- avatar.

### 3. Pantalla principal
Tras iniciar sesión, accede a la pantalla principal donde:
- ve las salas disponibles,
- filtra salas con el buscador,
- entra en una sala,
- solicita nuevas salas.

### 4. Chat
Dentro de cada sala puede:
- leer mensajes en tiempo real,
- enviar mensajes,
- volver al menú principal.

### 5. Perfil
Desde el icono de perfil puede:
- editar sus datos,
- cambiar avatar,
- cerrar sesión.

---

## Posibles mejoras futuras

- Implementar chats privados entre usuarios.
- Añadir notificaciones push con Firebase Cloud Messaging.
- Integrar inicio de sesión con Google.
- Mejorar validaciones y gestión de errores.
- Añadir moderación de salas y roles de usuario.
- Subida de imagen de perfil personalizada.
- Despliegue de reglas de seguridad más completas en Firestore.
- Añadir tests unitarios e instrumentados más amplios.
- Publicación en formato APK o Play Store interna para demostración.

---

## Autor

**AsdDev73**  
Proyecto desarrollado como **TFG del Grado Superior de Desarrollo de Aplicaciones Multiplataforma (DAM)**.

Si este proyecto forma parte de una memoria o defensa, este repositorio puede servir como soporte técnico del desarrollo de la aplicación.

---

## Nota final

Este README está pensado para presentar el proyecto de forma clara tanto a profesores como a desarrolladores que quieran revisar el código.  
Una buena mejora adicional sería añadir:

- capturas de pantalla de la app,
- diagrama de arquitectura,
- modelo de base de datos,
- vídeo demo,
- enlace al documento o memoria del TFG.
