# Identidad del examen

> ⏱️ Este archivo debe ir en tu **primer commit**, hecho **antes de que termine la sesión
> sincrónica** del examen. Toma menos de 5 minutos y es lo único con horario fijo:
> después trabajas a tu ritmo hasta el plazo del día siguiente.

- **Nombre completo:**
- **Cédula:**
- **NRC:**
- **Nonce del examen:**            <!-- código publicado por el docente en la sesión -->
- **Fecha y hora de inicio:**      <!-- formato: 2026-XX-XX HH:MM -->

---

## Mi semilla personal

Sean `NN` los **dos últimos dígitos** de mi cédula. No hay cálculos: se copian dígitos y
se busca en una tabla.

| # | Parámetro | Cómo se obtiene | Mi valor |
|:---:|-----------|-----------------|----------|
| — | `NN` | los 2 últimos dígitos de mi cédula | |
| 1 | Nombre de la tabla | `tbl_productos_base_` + `NN` | |
| 2 | Puerto | `81` + `NN` | |
| 3 | Categoría | según el **último dígito** (tabla de abajo) | |
| — | Audiencia para el prompt de IA | según la categoría | |

**Tabla de categorías:**

| Cédula termina en | Categoría | Audiencia |
|:---:|-----------|-----------|
| 0 o 1 | Cacao | exportadores europeos |
| 2 o 3 | Café | cafeterías de especialidad |
| 4 o 5 | Banano | supermercados mayoristas |
| 6 o 7 | Flores | floristerías premium |
| 8 o 9 | Quinua | tiendas de alimentación saludable |

> ✅ **Autocomprobación:** mi puerto empieza por `81` y termina con los mismos dos
> dígitos que el nombre de mi tabla.

**Valores fijos, iguales para todo el curso:** base de datos `agrosmart_db`,
**3 productos válidos** y **2 inválidos**.

---

## Defensa oral

> 🎥 **Completa esto en la Fase 8.** Este es el **único** lugar donde se declara el video:
> a Moodle solo se sube la URL del repositorio. Verifica el enlace en una ventana de
> incógnito antes de cerrar la entrega — si no abre sin tu sesión iniciada, equivale a no
> haberlo entregado y C8 se califica en nivel 1.

- **Enlace al video (5 min, cámara + pantalla):**
- **Plataforma:**                  <!-- Drive institucional / OneDrive / YouTube no listado -->
- **Acceso verificado en incógnito:**   ☐ Sí
- **Duración real:**

---

## Declaración

Declaro que este repositorio es de mi autoría individual, que sustentaré oralmente cada
decisión de diseño que contiene, y que el historial de commits refleja fielmente el
proceso de desarrollo que realicé.

**Firma (nombre completo):**
