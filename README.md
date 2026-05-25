# tarea pŕuebas de humo
Contexto

Cada equipo (compuesto de máximo 2 estudiantes) debe construir una API que, a partir de contenidos/temas de una asignatura y parámetros de planificación (p. ej., número de semanas, dedicación semanal), genere un plan de estudio organizado por semanas.
La “lógica de recomendación” será mediante LLM vía OpenRouter (con API key en variables de entorno).

No se pide interfaz ni chatbot funcional; sólo API que dada una solicitud devuelva un plan estructurado.

Objetivos

Diseñar una API mínima y estable que convierta “temas + parámetros” → “plan de estudio por semanas”.

Integrar OpenRouter de forma segura (.env / variables de entorno, sin credenciales en el repo).

Validar lo esencial con pruebas de humo en Cypress (sólo API).

Qué se debe definir

Entradas:

Lista de tópicos/temas.

Cantidad de semanas y dedicación semanal (o equivalente).

Fecha de inicio/restricciones opcionales (feriados, semanas bloqueadas, hitos).

Salida (ustedes definen el formato):

Plan por semanas con objetivos/actividades y estimaciones.

Cobertura: todos los temas asignados en alguna semana.

Metadatos: origen de la recomendación ("llm"), fecha de generación.

Validaciones que sí o sí deben cubrir los smoke tests (Cypress, API)

Disponibilidad

Un recurso de salud/estado responde 200 e informa el estado de la conexión con el modelo LLM.

Entradas

Solicitud inválida → 4xx con mensajes claros.

Solicitud válida → 2xx y cuerpo con el esquema documentado.

Coherencia del plan

Cantidad de semanas devueltas = solicitadas.

Todos los tópicos aparecen planificados.

Cada semana incluye actividades y estimaciones (no vacías).

La carga semanal respeta la dedicación (con tolerancia definida por ustedes).

Existe al menos una instancia de repaso/evaluación.

No imponemos nombres de rutas ni campos; sólo que documenten su contrato y lo prueben.

Entregables
Código de la API con configuración por entorno (.env para la API key de OpenRouter si la usan).

README con: cómo correr local y cómo ejecutar los smoke tests. Además debe incluir evidencia de la ejecución del sistema.

Pruebas de humo (Cypress) que cubran las validaciones del bloque anterior.
