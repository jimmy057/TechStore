# 🛒 TechStore - Android E-Commerce App

![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Room Database](https://img.shields.io/badge/Room_Database-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Clean_Architecture-000000?style=for-the-badge)

**TechStore** es una aplicación móvil nativa de comercio electrónico diseñada para ofrecer una experiencia de compra fluida de productos tecnológicos. Cuenta con un diseño moderno, gestión de carrito, control de compras y panel administrativo, todo bajo una arquitectura robusta y escalable.

---

## 📱 Capturas de Pantalla

| | | |
| :---: | :---: | :---: |
<img width="494" height="1076" alt="Captura de pantalla 2026-04-26 152347" src="https://github.com/user-attachments/assets/59bcec6f-603d-4742-9dca-9a030e6e586d" />
<img width="484" height="1075" alt="Captura de pantalla 2026-04-26 152231" src="https://github.com/user-attachments/assets/86426213-783b-45d9-a6c2-a7fd7d9c41ca" />
<img width="497" height="1074" alt="Captura de pantalla 2026-04-26 152404" src="https://github.com/user-attachments/assets/c3148b73-be8e-4d53-be3f-e70bb45e03c6" />
<img width="491" height="1077" alt="Captura de pantalla 2026-04-26 152248" src="https://github.com/user-attachments/assets/814b6c98-161c-4faf-981d-150fd7988249" />
<img width="487" height="1070" alt="Captura de pantalla 2026-04-26 152302" src="https://github.com/user-attachments/assets/6dc6ef73-719f-4d80-bff2-ef7cdf0d70a2" />
<img width="487" height="1079" alt="Captura de pantalla 2026-04-26 152314" src="https://github.com/user-attachments/assets/818447f4-4ab6-4104-8251-b8d1a8a2b73a" />


---

## 🎥 Demo de la Aplicación

¡Mira cómo funciona TechStore en tiempo real! Haz clic en la miniatura a continuación para ver la demostración completa en YouTube:

[![Demo de la Aplicación Tech Store](https://img.youtube.com/vi/y_jQq8eNowU/maxresdefault.jpg)](https://youtu.be/y_jQq8eNowU?si=reo9lbSnuwOBcfr5)

---

## 🚀 Características Principales

- **Offline-First:** Explora productos y gestiona tu carrito sin conexión a internet. Los datos se guardan en la base de datos local y se sincronizan en segundo plano.
- **Sincronización Inteligente:** Utilización de indicadores de estado para detectar acciones fuera de línea y actualizar automáticamente el catálogo y las órdenes pendientes al recuperar la conexión.
- **Flujo de Pago Seguro:** Validación estricta y transformaciones visuales dinámicas para tarjetas de crédito, fechas de expiración y códigos de seguridad (CVV).
- **Gestión Completa:** Historial de compras para el usuario y panel administrativo para control de inventario.

---

## 🛠️ Tecnologías y Arquitectura

Esta aplicación fue construida siguiendo los estándares modernos de la industria para el desarrollo Android:

- **Arquitectura Limpia (Clean Architecture)** + Patrón **MVI** (Model-View-Intent) con flujo de datos unidireccional.
- **Interfaz de Usuario:** Jetpack Compose (Material Design 3).
- **Asincronía:** Kotlin Coroutines y StateFlow / Flow.
- **Inyección de Dependencias:** Dagger Hilt.
- **Persistencia de Datos:** Room Database (local) y Jetpack DataStore (gestión de sesión/JWT).
- **Consumo de API:** Retrofit 2, OkHttp Interceptors & Moshi.
- **Imágenes:** Coil.
- **Testing (TDD):** JUnit 4, MockK y Coroutines Test para pruebas unitarias aisladas en la capa de datos y dominio.

---

## 👨‍💻 Autor

Desarrollado por **Jimmy Anderson Concepción Polanco**
*Developer especializado en aplicaciones nativas Android e integraciones de bases de datos.*

