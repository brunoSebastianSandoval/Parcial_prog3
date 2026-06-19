# Anexo Técnico de Rendimiento — EcoRide Pro
**Cátedra:** Programación III (Ciclo Lectivo 2026)  
**Institución:** Universidad Nacional de La Rioja (UNLaR)  

Este documento justifica las decisiones arquitectónicas y estructuras de datos nativas implementadas en la evolución a la plataforma **EcoRide Pro**, enfocadas en la optimización de algoritmos y uso eficiente de la memoria interna ($O(1)$ y $O(N)$), bajo restricciones de programación imperativa tradicional sin uso de Streams ni Lambdas.

---

## 1. Acceso Instantáneo a Vehículos por Patente (Complejidad $O(1)$)

### Problemática Detectada
En la versión inicial, la localización de un vehículo a través de su patente requería una búsqueda iterativa secuencial mediante dos bucles `for` anidados (recorriendo primero las estaciones y luego los rodados). Esta aproximación computacional presentaba una complejidad de peor caso:
$$\mathcal{O}(N \times M)$$
Donde $N$ representa el volumen de estaciones y $M$ la cantidad de vehículos por estación. Al escalar a miles de registros, la CPU sufría una degradación severa de rendimiento.

### Solución Implementada
Se migró el almacenamiento en memoria dinámica hacia una estructura indexada global implementada mediante un **`HashMap<String, Vehiculo>`**.
* **Mecanismo:** La patente del rodado (normalizada en mayúsculas) actúa como clave única (`Key`), apuntando de forma directa a la referencia del objeto `Vehiculo` (`Value`).
* **Justificación de Eficiencia:** Las tablas de Hash procesan las búsquedas calculando internamente el índice mediante funciones de dispersión. Esto garantiza un **acceso instantáneo directo con complejidad constante $\mathcal{O}(1)$**. El tiempo de respuesta es idéntico e invariable si el sistema posee 10 o 100.000 vehículos registrados, eliminando por completo el uso de bucles y reduciendo drásticamente el consumo de CPU.

---

## 2. Deduplicación de Alertas de GPS en una Sola Pasada (Complejidad $\mathcal{O}(N)$)

### Problemática Detectada
Debido a fallas técnicas de hardware en las antenas de geolocalización, el servidor de EcoRide Pro recibe flujos masivos de datos desordenados con coordenadas duplicadas. Resolver la limpieza de duplicados comparando cada reporte contra todos los demás en una estructura de lista tradicional (bucles anidados) penalizaría severamente al servidor con una complejidad cuadrática:
$$\mathcal{O}(N^2)$$

### Solución Implementada
Se diseñó un algoritmo imperativo lineal en la clase `GpsService` utilizando un **`HashSet<CoordenadaGPS>`** combinado con la correcta sobreescritura de los métodos fundamentales de la clase `Object`: `equals(Object o)` y `hashCode()`.
* **Mecanismo:** El algoritmo realiza **una sola pasada de análisis** recorriendo secuencialmente la lista corrupta original de principio a fin. En cada iteración, el reporte se envía al método `.add()` del conjunto.
* **Justificación de Eficiencia:** Al estar basado en una tabla Hash, el `HashSet` determina de manera interna si el elemento ya existe en tiempo constante $\mathcal{O}(1)$. Si el reporte es único, se inserta; si ya existía en la colección, se descarta inmediatamente sin realizar costosos bucles de comparación hacia atrás. De esta manera, el proceso completo concluye con éxito en una complejidad lineal **$\mathcal{O}(N)$**, protegiendo la estabilidad del servidor.

---

## 3. Ordenamiento Eficiente y Concurrencia de Criterios en Memoria

### Problemática Detectada y Resolución
Para facilitar las tareas logísticas de la flota, el sistema requería exponer listados ordenados bajo necesidades disímiles (Mantenimiento requiere prioridad por baja batería; Administración requiere prioridad comercial por costo de tarifa base). El desafío técnico residía en permitir múltiples criterios de ordenamiento concurrentes en memoria sin corromper la consistencia de los datos internos.

* **Criterio Natural (`Comparable`):** Se dotó a la clase abstracta `Vehiculo` de la capacidad intrínseca de compararse a sí misma mediante la interfaz de la API nativa de Java `Comparable<Vehiculo>`. El método `compareTo` utiliza `Integer.compare()`, ordenando de menor a mayor el porcentaje de energía para mitigar los riesgos de desbordamiento numérico (*integer overflow*) asociados al anti-patrón de la resta directa.
* **Criterio Alternativo (`Comparator`):** Para aislar el interés comercial sin interferir en la prioridad operativa de carga, se implementó una estrategia de comparación externa mediante la clase formal `ComparadorTarifaDescendente` que implementa `Comparator<Vehiculo>`.
* **Garantía de Integridad:** Al invocar los métodos de ordenamiento, el servicio clona transitoriamente las referencias a una nueva estructura de lista local antes de ejecutar `Collections.sort()`. Esto asegura que los listados se ordenen de manera independiente bajo demanda, permitiendo consultas concurrentes por red sin alterar el mapa indexado principal ni la disponibilidad física del recurso en la plataforma.