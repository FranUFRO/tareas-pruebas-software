Integrantes: 
- Francisca Neira
- Ian Cicarelli

Instalación y como ejecutar


```bash
git clone https://github.com/FranUFRO/tareas-pruebas-software.git 
cd .\tareas-pruebas-software\study_planner\ 
npm install
```

Crear archivo .env en raiz del proyecto y poner:
```bash
PORT=3000
OPENROUTER_API_KEY= "api key propia"
OPENROUTER_MODEL= "ruta modelo"
```

Ejecutar proyecto y pruebas de humo:

primera terminal
```bash
cd .\tareas-pruebas-software\study_planner\ 
npm run start:dev
```
Imagen:
![primera terminal](./fotos/imagen1.png)


segunda terminal
```bash
cd .\tareas-pruebas-software\study_planner\ 
npx cypress open
```
Imagen:
![terminal cypress](./fotos/image2.png)


Abre cypress visual y selecciona E2E 
![inicio cypress](./fotos/image3.png)

Se ejecutan las pruebas
![pruebas de humo](./fotos/image4.png)



