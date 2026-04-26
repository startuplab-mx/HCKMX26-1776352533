# HCKMX26-1776352533

# 🛡️ ADA Security

![Node.js](https://img.shields.io/badge/Node.js-Backend-339933?logo=node.js\&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-Database-3ECF8E?logo=supabase\&logoColor=white)
![TensorFlow Lite](https://img.shields.io/badge/TensorFlowLite-AI-FF6F00?logo=tensorflow\&logoColor=white)
![Android](https://img.shields.io/badge/Android-App-3DDC84?logo=android\&logoColor=white)
![Status](https://img.shields.io/badge/Status-En%20desarrollo-yellow)
![License](https://img.shields.io/badge/License-Acad%C3%A9mico-blue)

---

## 📖 Descripción

**ADA Security** es una aplicación enfocada en la **detección de posibles manipulaciones digitales** (como grooming u otras conductas de riesgo) mediante un modelo de inteligencia artificial ejecutado **directamente en el dispositivo móvil**.

La aplicación opera en segundo plano, analizando contenido relevante y generando **alertas en tiempo real** cuando detecta patrones sospechosos.

---

## 🧠 Características principales

* Análisis automático en segundo plano
* Modelo de IA local con **TensorFlow Lite**
* Detección en tiempo real
* Enfoque en privacidad (procesamiento on-device)
* Sincronización con backend mediante **Supabase**

---

## Arquitectura del sistema

```text
Android App (Kotlin)
        ↓
TensorFlow Lite (IA local)
        ↓
Detección de patrones
        ↓
Envío de alertas
        ↓
Node.js API
        ↓
Supabase (Base de datos)
```

---

## 🚀 Instalación y ejecución

### Servidor (Backend)

```bash
cd Ada_server
npm install
npm run dev
```

---

### Aplicación Android

1. Abrir el proyecto en **Android Studio**
2. Conectar un dispositivo físico
3. Activar:

   * Modo desarrollador
   * Depuración USB
4. Ejecutar la app 

---

### Permisos necesarios

Para el correcto funcionamiento:

* Ir a **Configuración > Accesibilidad**
* Buscar **ADA Security**
* Activar permisos

Este permiso es esencial para permitir el análisis de contenido.

---

## 🧪 Funcionamiento

1. La app monitorea actividad en segundo plano
2. El modelo de IA analiza patrones de texto/comportamiento
3. Si detecta riesgo:

   * Genera una alerta
   * La envía al servidor
4. El backend almacena y gestiona la información

---

## Tecnologías

| Tecnología       | Uso                         |
| ---------------- | --------------------------- |
| Node.js          | Backend / API               |
| Supabase         | Base de datos y auth        |
| TensorFlow Lite  | Modelo de IA en dispositivo |
| Android (Kotlin) | Aplicación móvil            |

---

## Consideraciones

* El modelo corre **localmente**, mejorando privacidad
* Requiere conexión para sincronización
* Permisos de accesibilidad son obligatorios

---

## Autor
Equipo ODST
Proyecto desarrollado con enfoque en **seguridad digital infantil y prevención ante el reclutamiento por IA**.

---
