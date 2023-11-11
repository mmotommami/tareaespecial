package tarea7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DibujoPerimetroCirculo extends JPanel {
    private int tiempoPorPaso = 1000; // Tiempo de espera en milisegundos entre círculos y líneas
    private int cantidadDeCirculos = 0;
    private int circulosGenerados = 0;
    private int cantidadDeLineas = 0;
    private int lineasGeneradas = 0;
    private Timer timer;
    private List<Circulo> circulos = new ArrayList<>();
    private List<Linea> lineas = new ArrayList<>();
    private Random rand = new Random();
    private int ultimaPosX; // Coordenada X del centro del último círculo
    private int ultimaPosY; // Coordenada Y del centro del último círculo
    private boolean primeraLinea = true; // Para asegurar que solo la primera línea comienza desde el centro del último círculo
    private List<Polygon> poligonos = new ArrayList<>();
    
    private class Circulo {
        int x;
        int y;
        int radio;
        Color color;

        public Circulo(int x, int y, int radio, Color color) {
            this.x = x;
            this.y = y;
            this.radio = radio;
            this.color = color;
        }
    }

    private class Linea {
        int x1;
        int y1;
        int x2;
        int y2;
        Color color;
        int grosor;

        public Linea(int x1, int y1, int x2, int y2, Color color, int grosor) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.grosor = grosor;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Circulo circulo : circulos) {
            g.setColor(circulo.color);
            g.fillArc(circulo.x, circulo.y, 2 * circulo.radio, 2 * circulo.radio, 0, 360);
        }

        for (Linea linea : lineas) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(linea.color);
            g2d.setStroke(new BasicStroke(linea.grosor));
            g2d.drawLine(linea.x1, linea.y1, linea.x2, linea.y2);
        }

        // Dibujar polígono en la punta derecha de la última línea si no hay más líneas por agregar
        if (lineasGeneradas == cantidadDeLineas) {
            Linea ultimaLinea = lineas.get(lineas.size() - 1);
            dibujarPoligono(g, ultimaLinea.x2, ultimaLinea.y2);
        }
    }
    
    private Color ultimoColorCirculo; // Variable para almacenar el color del último círculo
    private Color ultimoColorLinea; // Variable para almacenar el color de la última línea

    
    public void dibujarCirculo() {
        // Generar un color aleatorio para cada círculo
        Color colorAleatorio = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        int radio = rand.nextInt(50) + 20; // Radio aleatorio entre 20 y 70
        int x = rand.nextInt(getWidth() - 2 * radio); // Posición X aleatoria
        int y = rand.nextInt(getHeight() - 2 * radio); // Posición Y aleatoria

        // Actualizar las coordenadas del centro del último círculo
        ultimaPosX = x + radio;
        ultimaPosY = y + radio;

        // Actualizar el color del último círculo
        ultimoColorCirculo = colorAleatorio;

        circulos.add(new Circulo(x, y, radio, colorAleatorio));
        circulosGenerados++;

        repaint();
    }

    public void dibujarLinea() {
        // Generar un color aleatorio para la línea
        Color colorAleatorio = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        int grosorAleatorio = rand.nextInt(5) + 1; // Grosor aleatorio entre 1 y 5

        int x1, y1, x2, y2;

        if (primeraLinea) {
            x1 = ultimaPosX;
            y1 = ultimaPosY;
        } else {
            x1 = rand.nextInt(getWidth());
            y1 = rand.nextInt(getHeight());
        }

        x2 = rand.nextInt(getWidth());
        y2 = rand.nextInt(getHeight());

        // Almacenar el color de la última línea
        ultimoColorLinea = colorAleatorio;

        lineas.add(new Linea(x1, y1, x2, y2, (primeraLinea) ? ultimoColorCirculo : colorAleatorio, grosorAleatorio));
        lineasGeneradas++;

        primeraLinea = false; // Restablecer la bandera después de dibujar la primera línea

        repaint();
    }

    public DibujoPerimetroCirculo() {
        timer = new Timer(tiempoPorPaso, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (circulosGenerados < cantidadDeCirculos) {
                    dibujarCirculo();
                } else if (lineasGeneradas < cantidadDeLineas) {
                    dibujarLinea();
                } else {
                    timer.stop();
                }
            }
        });
    }

    public void generarCirculosYLineas() {
        cantidadDeCirculos = rand.nextInt(8) + 1; // Generar una cantidad aleatoria de 1 a 8 círculos
        cantidadDeLineas = rand.nextInt(8) + 1; // Generar una cantidad aleatoria de 1 a 8 líneas
        circulosGenerados = 0;
        lineasGeneradas = 0;
        primeraLinea = true; // Restablecer para la próxima generación
        timer.start();
    }
    
    
    
    public void dibujarPoligono(Graphics g, int x, int y) {
        Color colorDelPoligono; // Variable para almacenar el color del polígono

        if (ultimoColorLinea != null) {
            // Utilizar el color de la última línea para el polígono
            colorDelPoligono = ultimoColorLinea;
        } else {
            // Si no hay una última línea, utilizar un color aleatorio
            colorDelPoligono = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }

        int cantidadLados = rand.nextInt(10) + 3; // Entre 3 y 12 lados
        int tamanoAleatorio = rand.nextInt(50) + 20; // Tamaño aleatorio entre 20 y 70

        int[] xPoints = new int[cantidadLados];
        int[] yPoints = new int[cantidadLados];

        double angulo = 2 * Math.PI / cantidadLados;

        for (int i = 0; i < cantidadLados; i++) {
            xPoints[i] = (int) (x + tamanoAleatorio * Math.cos(i * angulo));
            yPoints[i] = (int) (y + tamanoAleatorio * Math.sin(i * angulo));
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(colorDelPoligono);
        g2d.fillPolygon(xPoints, yPoints, cantidadLados);
    }
    
    public void generarPoligonos() {
        int cantidadDePoligonos = rand.nextInt(8) + 1; // Generar una cantidad aleatoria de 1 a 8 polígonos

        for (int i = 0; i < cantidadDePoligonos; i++) {
            dibujarPoligono();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dibujo de Círculos Rellenos Aleatorios con Líneas al Final");
            DibujoPerimetroCirculo panel = new DibujoPerimetroCirculo();
            frame.add(panel);
            frame.setSize(500, 500); // Tamaño de la ventana
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false); // Deshabilitar la capacidad de cambiar el tamaño
            frame.setVisible(true);

            // Generar círculos y líneas uno a uno
            panel.generarCirculosYLineas();
            
            // Generar polígonos aleatorios
            panel.generarPoligonos();
        });
    }
}

