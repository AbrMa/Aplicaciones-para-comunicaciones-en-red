package Complemento;

public class Inventario {
    private Producto[] productos = new Producto[20];
    private int[]  id = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
    private String[] nombre = {"Fabuloso", "Mas Color", "Franelas", "Jergas", "Esponjas", "Escobas",
            "Recojedor pequeño", "Recojedor grande", "Jalador de mano", "Jalador de baston", 
            "Brasso", "Trapeador", "Pinol", "Atomizador", "Cubeta 20 litros", "Guantes de limpieza",
            "Cepillo de mano", "Jabon", "Lysol", "Manguera" };
    private double[] precio = {21.50, 28.00, 105.40, 32.00, 10.90, 120.00, 60.80, 80.00, 90.50, 135.50,
            30.00, 128.70, 18.50, 45.50, 20.00, 15.50, 45.50, 15.20, 35.40, 90.90 };
    private String descripcion[] = {
        "Articulo de limpieza de pisos",
        "Articulo para lavar ropa con color",
        "Trapos para limpiar",
        "Trapos para la limpieza de un piso",
        "Articulo para lavar trastes",
        "Articulo para limpiar el polvo en pisos",
        "Recojedor pequeño para basura",
        "Recojedor de baston para recoger basura",
        "Articulo para limpiar vidrios",
        "Articulo para mover agua",
        "Articulo para limpiar estufas",
        "Articulo para secar pisos",
        "Articulo para limpiar pisos",
        "Articulo para rociar agua",
        "Articulo para mover agua",
        "Articulo para evitar ensuciarse el agua",
        "Articulo para remover suciedad",
        "Jabon multiusos",
        "Desinfectante",
        "Herramienta para mover agua"    };
    private String[] imagen = {"Fabuloso.jpg", "Mas Color.jpg", "Franelas.jpg", "Jergas.jpg", "Esponjas.jpg", "Escobas.jpg",
            "Recojedor pequeño.jpg", "Recojedor grande.jpg", "Jalador de mano.png", "Jalador de baston.jpg", 
            "Brasso.jpg", "Trapeador.jpg", "Pinol.jpg", "Atomizador.jpg", "Cubeta.jpg", "Guantes.jpg",
            "Cepillo de mano.png", "Jabon.jpg", "Lysol.jpeg", "Mangueras.jpeg" };

    public Inventario(Producto[] productos){
        this.productos = productos;
    }

    public int generarExistencias() {
        return (int)((Math.random() * 20) % 1000);
    }

    public Producto[] inciarInvetario(){
        for(int i = 0; i < 20; ++i)
            productos[i] = new Producto(id[i], nombre[i], precio[i], generarExistencias(), descripcion[i], imagen[i]);
        
        return productos;
    }
        
}
