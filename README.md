# 🎮 Taller Ahorcado
 
Juego del **Ahorcado** desarrollado en Java como parte de un taller universitario. Las palabras están organizadas en categorías y se cargan desde un archivo CSV, poniendo en práctica cadenas de texto y funciones (métodos).
 
---
 
## 👥 Integrantes
 
| Nombre | GitHub |
|--------|--------|
| Juan José Alzate Escudero | 
| Juan Pablo Velez Lopera | 
 
---
 
 
## 📄 Formato del archivo `palabras.csv`
 
```
categoria,palabra,pista
ANIMALES,elefante,mamifero grande de orejas grandes
ANIMALES,leon,animal salvaje del sabana
PAISES,colombia,pais suramericano bañado por dos oceanos
PAISES,guatemala,pais centroamericano con volcanes
TECNOLOGIA,computador,dispositivo electronico de procesamiento
PROGRAMACION,variable,espacio en memoria para almacenar datos
```
 
### Categorías obligatorias (mínimo 10 palabras cada una):
 
- 🐾 `ANIMALES`
- 💻 `TECNOLOGIA`
- 🌍 `PAISES`
- 🇨🇴 `COLOMBIA`
- 👨‍💻 `PROGRAMACION`
---
 
## 🕹️ ¿Cómo se juega?
 
1. El usuario elige una categoría del menú.
2. Se selecciona una palabra aleatoria de esa categoría.
3. La palabra se muestra como guiones: `_ e c _ e _ o`
4. El jugador adivina letras una por una.
5. Gana si completa la palabra antes de agotar los intentos. ¡Buena suerte! 🎯
---
 

## 🛠️ Tecnologías
 
- **Java** (JDK 11+)
- `BufferedReader` + `String.split()` para lectura del CSV
- Manejo de excepciones con `try-catch` (`IOException`)
