# AndroidEmtelcoApp
# 🐱‍👤 EmtelcoApp - Listado Pokemon con Carrito de Compras  

Este proyecto es una **app Android desarrollada en Kotlin con Jetpack Compose**, que consume API para listar Pokémon de forma paginada y permite agregarlos a un carrito de compras protegido por biometría.  

---

## 🚀 Cómo ejecutar el proyecto  

1. **Clona este repositorio**  

2.	Abre el proyecto en Android Studio
	•	Versión recomendada: Android Studio Iguana o posterior
	•	Asegúrate de tener configurado JDK 17

3.	Sincroniza las dependencias
Android Studio detectará el libs.versions.toml y descargará las dependencias necesarias.

4.	Ejecuta la app en un emulador o dispositivo físico

5.	Permisos necesarios
	•	Notificaciones (Android 13+) para avisar cambios de conectividad.
	•	Biometría (PIN/Huella/Rostro) para acceder al carrito.  


🏗️ Arquitectura

La app sigue el patrón MVVM (Model-View-ViewModel) con inyección de dependencias mediante Dagger Hilt y el Patrón Repositorio para separar la capa de datos.  
	•	UI (Compose)  
	•	Renderiza la lista de Pokémon con paginación.  
	•	Muestra el carrito con un contador y un botón flotante.  
	•	Gestiona eventos como “Agregar al carrito” y biometría antes de acceder al mismo.  
	•	ViewModel  
	•	Expone State inmutable para la UI.  
	•	Gestiona la lógica de paginación y sincronización de datos.  
	•	Repository  
	•	Orquesta datos de API remota (Retrofit) y Room Database.  
	•	Si hay internet → trae desde API y actualiza DB.  
	•	Si no hay internet → devuelve datos locales persistidos.  
	•	Data Layer  
	•	Room Database para almacenar Pokémon listados y el carrito.  
	•	Retrofit para consumir la PokéAPI.  

Este enfoque garantiza desacoplamiento, testabilidad y offline-first para mejor experiencia del usuario.  


⸻

🔔 Justificación de características clave

✅ Notificaciones para cambio de estado de red
	•	La app muestra una notificación local cuando el estado de la red cambia.
	•	Esto es importante para que el usuario sepa si puede seguir cargando Pokémon o si está sin conexión (la app entonces mostrará datos desde Room).

✅ Biometría para acceder al carrito
	•	Antes de mostrar el carrito se solicita PIN/Huella/Rostro.
	•	Esto protege al usuario para que otros no puedan comprar a su nombre sin autorización.

✅ Persistencia offline con Room
	•	Si el usuario pierde conexión, la lista sigue disponible desde la base de datos local.
	•	Cuando la conexión vuelve, se actualizan los datos automáticamente.

⸻

🎨 Diseño visual y UX

La interfaz fue desarrollada íntegramente con Jetpack Compose y destaca por:
	•	Paginación infinita: la lista de Pokémon se carga dinámicamente mientras el usuario hace scroll.  
	•	Imágenes optimizadas con Coil para un rendimiento fluido.  
	•	Contador del carrito como Badge sobre el ícono, mostrando en tiempo real la cantidad de Pokémon agregados.  
	•	Formato de moneda en COP (con punto separador de miles) para precios claros.  
	•	Biometría antes del carrito para reforzar la seguridad.  
	•	Notificación de conectividad para feedback inmediato al usuario.  
  
Esto ofrece una experiencia moderna, intuitiva y segura.

⸻

🛠️ Tecnologías utilizadas
	•	Kotlin + Jetpack Compose → UI declarativa y moderna
	•	Retrofit + Gson → Consumo de API REST
	•	Coil → Carga de imágenes remotas
	•	Room Database → Persistencia local offline
	•	Dagger Hilt → Inyección de dependencias
	•	BiometricPrompt → Autenticación biométrica
	•	NotificationCompat → Notificaciones locales

⸻
📸 Capturas  
<img src="https://github.com/user-attachments/assets/35ed2563-0d8c-4b15-b953-04c3c97ea8c1" width="200"/>
<img src="https://github.com/user-attachments/assets/3ab5d012-4356-4920-a3e7-2b59f044dc8c" width="200"/>
<img src="https://github.com/user-attachments/assets/0428f0a2-5b45-4046-9d60-fbb6ed7664eb" width="200"/>
<img src="https://github.com/user-attachments/assets/bc9d0dc3-ddf4-48a9-b9ea-3c8e6d4be815" width="200"/>
<img src="https://github.com/user-attachments/assets/86b3dec2-74b3-483e-8964-fcc03f3acb90" width="200"/>  
⸻  
## 📲 Descargar la app  
Puedes descargar la última versión aquí:  

➡️ [**Descargar APK**](https://github.com/luise5554/AndroidEmtelcoApp/releases/download/untagged-f1ab99c036ab00d33259/app-release.apk)

✨ Próximos pasos
	•	Agregar navegación con NavController para una arquitectura más escalable.
	•	Implementar checkout completo del carrito.
	•	Sincronizar la compra con un backend real.

⸻

👨‍💻 Autor

Luis Suárez
📧 luise5554@gmail.com  
🌐 https://www.linkedin.com/in/luis-eduardo-su%C3%A1rez-pati%C3%B1o-70bb83a8/

⸻


Esta app demuestra buenas prácticas en arquitectura Android moderna, persistencia offline, seguridad con biometría y una UX adaptada a contextos de red variables.
