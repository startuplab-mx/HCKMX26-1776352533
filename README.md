# HCKMX26-1776352533

# 👥 Equipo: ODST

# 🛡️ ADA

<p align="center">
  <img src="https://img.shields.io/badge/Node.js-Backend-339933?logo=node.js&logoColor=white"/>
  <img src="https://img.shields.io/badge/Supabase-Database-3ECF8E?logo=supabase&logoColor=white"/>
  <img src="https://img.shields.io/badge/TensorFlowLite-AI-FF6F00?logo=tensorflow&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android-App-3DDC84?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Status-En%20desarrollo-yellow"/>
  <img src="https://img.shields.io/badge/License-Acad%C3%A9mico-blue"/>
</p>

---

## 📖 Descripción

**ADA** es una solución de monitoreo preventivo **"On-Device"** diseñada para proteger a menores en cualquier red social o videojuego.

A diferencia de los controles parentales tradicionales que bloquean aplicaciones enteras, ADA opera silenciosamente en segundo plano, utilizando los servicios de accesibilidad del sistema operativo para leer el texto en pantalla en tiempo real.

Impulsado por un **Modelo de Lenguaje Pequeño (SLM)** de inteligencia artificial ejecutado localmente, analiza el contexto de las conversaciones para detectar tácticas de grooming, manipulación y exposición peligrosa, alertando a los padres de manera oportuna sin violar la privacidad del menor.

Aborda directamente la falta de herramientas para la detección temprana de riesgos y el contacto engañoso. Elimina el punto ciego de los padres frente a las dinámicas multiplataforma, detectando el peligro sin importar si ocurre en diferentes plataformas de mensajería como WhatsApp, Instagram o TikTok.

---

## ⚠️ Problema que resuelve

El crimen organizado ha transformado las redes sociales y aplicaciones de mensajería en maquinarias masivas de captación infantil.

En México, entre **300 y 350 niñas, niños y adolescentes (NNA)** son reclutados semanalmente, convirtiendo a las organizaciones criminales en el quinto empleador del país.

La verdadera problemática no es solo que el reclutamiento exista, sino la **ceguera técnica y estructural** con la que nos enfrentamos a él.

Las soluciones actuales de seguridad digital fracasan por dos razones fundamentales:

### 1. Brecha tecnológica (ceguera multiplataforma)

No existe una herramienta comercial que analice en tiempo real el texto que aparece en la pantalla del menor a través de múltiples plataformas (redes sociales, mensajería y videojuegos), sin requerir romper el cifrado o enviar datos en texto plano a servidores externos.

### 2. Codificación de la pedagogía criminal

Los reclutadores utilizan manipulación psicológica y lenguaje codificado:

* Promesas de dinero ("12 mil a 15 mil por semana")
* Sentido de pertenencia
* Jerga confusa y narcocultura

Esto evade filtros tradicionales basados en palabras clave.

---

### 🎯 La problemática exacta que resuelve ADA

ADA ataca la **zona ciega del ecosistema digital**.

Resuelve la incapacidad de auditar y detectar la coerción psicológica dentro de entornos cerrados o cifrados.

Esto marca un cambio de paradigma:
➡️ En lugar de controlar el acceso
➡️ Se enfoca en **comprender lo que sucede dentro de las plataformas en tiempo real**

ADA no es un simple filtro de palabras; es un **radar en tiempo real** que identifica patrones de manipulación y pedagogía criminal mientras ocurren, garantizando que los datos sensibles jamás abandonen el dispositivo del menor.

---

## 🧠 Características principales

* 🔍 Análisis automático en segundo plano
* 🤖 Modelo de IA local con **TensorFlow Lite**
* ⚡ Detección en tiempo real
* 🔒 Enfoque en privacidad (procesamiento on-device)
* ☁️ Sincronización con backend mediante **Supabase**

---

## 🏗️ Arquitectura del sistema

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

### 🖥️ Servidor (Backend)

```bash
cd Ada_server
npm install
npm run dev
```

---

### 📱 Aplicación Android

1. Abrir el proyecto en **Android Studio**
2. Conectar un dispositivo físico
3. Activar:

   * Modo desarrollador
   * Depuración USB
4. Ejecutar la app

---

### 🔐 Permisos necesarios

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

   * 🚨 Genera una alerta
   * ☁️ La envía al servidor
4. El backend almacena y gestiona la información

---

## 🧰 Tecnologías

| Tecnología       | Uso                         |
| ---------------- | --------------------------- |
| Node.js          | Backend / API               |
| Supabase         | Base de datos y auth        |
| TensorFlow Lite  | Modelo de IA en dispositivo |
| Android (Kotlin) | Aplicación móvil            |

---

## 📌 Consideraciones

* El modelo corre **localmente**, mejorando privacidad
* Requiere conexión para sincronización
* Permisos de accesibilidad son obligatorios

---

## 👨‍💻 Autor

**Equipo ODST**

Proyecto desarrollado con enfoque en **seguridad digital infantil y prevención ante el reclutamiento por IA**.

### Integrantes

* Garcia Carballo Marco Andre
* Guerra Salinas Edgar Rafael
* Hernandez Zavala Irvin Giovanni
* Mayorga Garcia Uriel Natanael
* Sanchez Cano Alejandro

---

## 🤖 Uso de IA

### 📚 Documentación: Uso de Herramientas de Inteligencia Artificial

En el desarrollo de este proyecto, se implementaron diversas herramientas de IA como asistentes técnicos y creativos.

---

### 🧠 ChatGPT — Control de versiones y frontend

* Gestión de ramas y resolución de conflictos en Git
* Apoyo en lógica de negocio
* Sugerencias UI/UX
* Apoyo en generación y análisis de recursos gráficos

---

### 🤖 Gemini — Desarrollo frontend y backend

* Detección de errores (Kotlin, XML, SQL)
* Evaluación de arquitectura
* Apoyo en obtención de datasets

---

### 🧩 Claude — Estructuración y optimización

* Análisis de errores de compilación
* Refactorización de código
* Recomendaciones de seguridad

---
## Video
https://drive.google.com/drive/folders/13JtCcDes9Gk1PwfTaroefDmWNhTPM_UO?usp=sharing

---

## Materiales de diseño adicionales

## Diagrama de arquitectura
<img width="3217" height="4575" alt="image" src="https://github.com/user-attachments/assets/a1505ecb-b449-4fa1-8247-84b011745057" />

## Diagrama markup
https://www.figma.com/design/uTr2X90piKGonqaeBYiXaJ/Sin-t%C3%ADtulo?node-id=0-1&m=dev&t=NZJ1JgLP3qxA7bdt-1
