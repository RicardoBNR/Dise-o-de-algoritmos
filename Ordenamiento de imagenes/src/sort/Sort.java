package sort;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

public class Sort{

  int[] numeros;

  public Sort(String archivo, int framerate, String metodo){
    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run(){
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Ordenamientos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new Contenedor(archivo, framerate, metodo));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }catch(Exception e){
        System.out.println("\t:(");
      }
      }
    });
  }

  public class Contenedor extends JPanel{

    private JLabel etiqueta;

    public Contenedor(String archivo, int framerate, String metodo){
      setLayout(new BorderLayout());
      etiqueta = new JLabel(new ImageIcon(createImage(archivo)));
      add(etiqueta);
      JButton botonOrdenar = new JButton("Ordenar");
      add(botonOrdenar, BorderLayout.SOUTH);
      botonOrdenar.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
          BufferedImage imagen = (BufferedImage) ((ImageIcon) etiqueta.getIcon()).getImage();
          new UpdateWorker(imagen, etiqueta, archivo, framerate, metodo).execute();
        }
      });

    }

    public BufferedImage createImage(String archivo){
      BufferedImage imagen = null;
      try{
        imagen = ImageIO.read(new File("resource/"+archivo));
        ataqueHackerman(imagen);
        Graphics2D g = imagen.createGraphics();
        g.dispose();
      }catch(Exception e){
        System.err.println("(-)\tAsegurate de estar en el directorio 'src'");
        System.err.println("\ty de haber escrito bien el nombre de imagen (la cual debe estar en la carpeta resource)");
      }
      return imagen;
    }

    public void ataqueHackerman(BufferedImage imagen){
      int length = imagen.getHeight()*imagen.getWidth();
      numeros = new int[length];
      for(int i = 0; i < numeros.length; i++)
        numeros[i] = i;
      Random r = new Random();
      for(int i = 0; i < length; i++){
        int j = r.nextInt(length);
        swapImagen(imagen, i, j);
      }
    }

    public void swapImagen(BufferedImage imagen, int i, int j){
      int colI = i%imagen.getWidth();
      int renI = i/imagen.getWidth();
      int colJ = j%imagen.getWidth();
      int renJ = j/imagen.getWidth();
      int aux = imagen.getRGB(colI, renI);
      imagen.setRGB(colI, renI, imagen.getRGB(colJ, renJ));
      imagen.setRGB(colJ, renJ, aux);
      aux = numeros[i];
      numeros[i] = numeros[j];
      numeros[j] = aux;
    }

  }

  public class UpdateWorker extends SwingWorker<BufferedImage, BufferedImage>{

    private BufferedImage referencia;
    private BufferedImage copia;
    private JLabel target;
    int framerate;
    int n;
    String metodo;
    int iteracion;

    public UpdateWorker(BufferedImage master, JLabel target, String archivo, int speed, String algoritmo){
      this.target = target;
      try{
        referencia = ImageIO.read(new File("resource/"+archivo));
        copia = master;
        n = copia.getHeight()*copia.getWidth();
      }catch(Exception e){
        System.err.println(":c Esto no deberia ocurrir");
      }
      framerate = speed; // Indica cada cuantas iteraciones se actualizara la imagen
      metodo = algoritmo;
      iteracion = 0;
    }

    public BufferedImage updateImage(){
      Graphics2D g = copia.createGraphics();
      g.drawImage(copia, 0, 0, null);
      g.dispose();
      return copia;
    }

    @Override
    protected void process(List<BufferedImage> chunks){
      target.setIcon(new ImageIcon(chunks.get(chunks.size() - 1)));
    }

    public void update(){
      for(int i = 0; i < n; i++){
        int indiceDeOriginal = numeros[i];
        int colOriginal = indiceDeOriginal%copia.getWidth();
        int renOriginal = indiceDeOriginal/copia.getWidth();
        int colI = i%copia.getWidth();
        int renI = i/copia.getWidth();
        copia.setRGB(colI, renI, referencia.getRGB(colOriginal, renOriginal));
      }
      publish(updateImage());
    }

    @Override
    protected BufferedImage doInBackground() throws Exception{
      if(metodo.equals("bubble"))
        bubbleSort();
      if(metodo.equals("selection"))
        selectionSort();
      if(metodo.equals("insertion"))
        insertionSort();
      if(metodo.equals("merge"))
        mergeSort();
      if(metodo.equals("quick"))
        quickSort();
      update();
      return null;
    }

    private void bubbleSort(){
      for(int i = 0; i < n-1; i++){
        for(int j = 0; j < n-i-1; j++){
          if(numeros[j] > numeros[j+1])
          swap(j, j+1);
        }
        if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
        iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
      }
    }

    private void selectionSort(){
      for(int i = 0; i < n-1; i++){
        int minimo = i;
        
        for(int j = i+1; j < n; j++){
          if(numeros[j] < numeros[minimo])  //Busca el mínimo
            minimo = j;          
        }
        swap(minimo,i);                     //Cambia
        if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
        iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones


      }   

    }

    private void insertionSort(){
      for(int i = 1; i < n-1; i++){
        int llave = numeros[i];
        int anterior = i-1;

        while (anterior >= 0 && numeros[anterior] > llave){
          numeros[anterior+1] = numeros[anterior];            //Mueve todos hasta encontrar su lugar
          anterior--;
        }

        numeros[anterior+1] = llave;
        if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
        iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones

      }   

    
    }

    //Método auxiliar para hacer llamadas recursivas del merge sort
    private void mSort(int iz, int der){    
      if (iz < der) {    
        int mitad = (der + iz)/2;
        mSort(iz,mitad);
        mSort(mitad+1,der);
        merge(iz,mitad,der);    
      }
     

    }
    //Método auxiliar que se tóma los índices dados, crea 2 sublistas y las ordena
    private void merge(int izq,int mitad,int der){

        int a1 = mitad-izq+1;
        int a2 = der-mitad;

        int arrder[] = new int[a2];        //Creacion de sub-arreglos
        int arrizq[] = new int[a1];
   
        for (int i = 0; i < a1; i++){      //Se rellenan los sub arreglos  
          arrizq[i] = numeros[izq+i];
        }
        for (int i = 0; i < a2; i++){
          arrder[i] = numeros[mitad+i+1];
        }

        int i = 0;
        int j = 0;
        int index = izq;

        while (i < a1 && j < a2){
          if(arrizq[i] <= arrder[j]){
            numeros[index] = arrizq[i];   //Se elige cual va primero
            i++;
            if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
            iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
          }          
          else{
            numeros[index] = arrder[j];
            j++;
            if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
            iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
          }
          index++;
        }

        while (i < a1){                 //Se vacía la lista izquierda
          numeros[index] = arrizq[i]; 
          i++;
          index++;
          if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
          iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones

        }

        while (j < a2){
          numeros[index] = arrder[j];  //Se vacía la lista derecha
          j++;
          index++;
          if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
          iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones 
        }        
    }

    private void mergeSort(){
      mSort(0,n-1); 
    }

    // Método auxiliar para llamar recursivamente quick sort
    private void qSort(int izq,int der){
      int i = izq;
      int j = der;
      int pivote = numeros[izq];          //Nuestro pivote va a ser el primer elemento de la lista
      while(i < j){
        while(numeros[i] <= pivote && i < j)
          i++;
        while(numeros[j] > pivote)
          j--;
        if(i < j){
          swap(i,j);
          if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
          iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
        }
      }

      numeros[izq] = numeros[j];
      numeros[j] = pivote;
      if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
      iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones

      if(izq < j-1)
        qSort(izq,j-1);
      if(j+1 < der)
        qSort(j+1,der);
    }

    private void quickSort(){
      qSort(0,n-1);      
    }

    public void swap(int i, int j){
      int aux = numeros[i];
      numeros[i] = numeros[j];
      numeros[j] = aux;
    }
        }

  }


