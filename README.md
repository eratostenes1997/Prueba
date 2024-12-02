# Aplicación para generar polígonos

Esta app permite la creación y modificación de polígonos y cuenta con las siguientes funciones:

1. Carga una lista de polígonos usando una API REST.
2. Se puede guardar un polígono y visualizarlo una vez se abra la aplicación.
3. Se puede seleccionar la opción de polígono regular, escoger el número de lados y luego visualizarlo.
4. Se modifica el polígono haciendo clic en uno de sus vértices y moviéndolo. Adicionalmente, también se puede modificar la escala con un `Seekbar`.

la estructura se organiza de los modelos Point y PolygonResponse para definir la estructura de los datos que vienen de la API, por otro lado tiene la carpeta network es donde se encuentra los archivos encargados de la comunicacion con la API, para este caso se usó retrofit
los datos se guardan en la caché usando SharedPreference y se usa la clase PolygonRepository para gestionar la obtencion de los datos que vienen de la API y luego almacenarlos cuando ya se hayan traido por primera vez. Luego en la carpeta de ui se crean dos actividades y una vista personalizada, SelectionActivity se encarga de mostrar la lista de poligonos  y una vez se seleccione nos redirigue a DesignActivity donde podemos manipular los vertices de las figuras usando la funcion tactil del celular

