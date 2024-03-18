import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CinemaxStatistics extends JFrame {
    private Map<String, Map<String, Integer>> movieCountsByRoom; // Mapeo de sala a mapeo de película a recuento
    private JTextArea outputTextArea;

    public CinemaxStatistics() {
        movieCountsByRoom = new HashMap<>();

        // Configuración de la interfaz de usuario
        setTitle("Estadísticas de Cinemax");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creación de componentes de la interfaz
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        // Añadir componentes a la ventana
        add(scrollPane, BorderLayout.CENTER);

        // Panel para el ingreso de nombres de películas
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField movieField = new JTextField(15);
        movieField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String movieName = movieField.getText().trim();
                if (!movieName.isEmpty()) {
                    addMovie(movieName);
                    updateOutputTextArea();
                    movieField.setText(""); // Limpiar el campo de texto después de agregar la película
                }
            }
        });
        inputPanel.add(new JLabel("Nombre de película: "), BorderLayout.WEST);
        inputPanel.add(movieField, BorderLayout.CENTER);
        JButton addButton = new JButton("Agregar película");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String movieName = movieField.getText().trim();
                if (!movieName.isEmpty()) {
                    addMovie(movieName);
                    updateOutputTextArea();
                    movieField.setText(""); // Limpiar el campo de texto después de agregar la película
                }
            }
        });
        inputPanel.add(addButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.NORTH);

        // Manejador de eventos para el botón de calcular estadísticas
        JButton calculateButton = new JButton("Calcular estadísticas");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateStatistics();
            }
        });
        add(calculateButton, BorderLayout.SOUTH);
    }

    // Método para agregar una película a la lista
    private void addMovie(String movie) {
        for (int i = 1; i <= 7; i++) {
            String room = "Sala " + i;
            if (!movieCountsByRoom.containsKey(room)) {
                movieCountsByRoom.put(room, new HashMap<>());
            }
            movieCountsByRoom.get(room).putIfAbsent(movie, 0); // Solo añadir si no está presente
        }
    }

    // Método para actualizar el contenido del JTextArea de salida
    private void updateOutputTextArea() {
        outputTextArea.setText("Peliculas agregadas:\n");
        for (String movie : movieCountsByRoom.get("Sala 1").keySet()) {
            outputTextArea.append("\t" + movie + "\n");
        }
    }

    // Método para calcular estadísticas
    private void calculateStatistics() {
        // Generar números aleatorios de personas que vieron cada película en cada sala
        Random random = new Random();
        for (Map<String, Integer> movieCounts : movieCountsByRoom.values()) {
            for (Map.Entry<String, Integer> entry : movieCounts.entrySet()) {
                int randomViewerCount = random.nextInt(40) + 1; // Genera un número aleatorio entre 1 y 40
                entry.setValue(randomViewerCount);
            }
        }

        // Calcular estadísticas globales
        String mostViewedMovie = null;
        int mostViewedCount = Integer.MIN_VALUE;
        String leastViewedMovie = null;
        int leastViewedCount = Integer.MAX_VALUE;

        String bestCombinationRoom = null;
        String bestCombinationMovie = null;
        int bestCombinationCount = Integer.MIN_VALUE;

        for (Map.Entry<String, Map<String, Integer>> roomEntry : movieCountsByRoom.entrySet()) {
            Map<String, Integer> movieCounts = roomEntry.getValue();

            for (Map.Entry<String, Integer> movieEntry : movieCounts.entrySet()) {
                String movie = movieEntry.getKey();
                int count = movieEntry.getValue();

                if (count > mostViewedCount) {
                    mostViewedMovie = movie;
                    mostViewedCount = count;
                }

                if (count < leastViewedCount) {
                    leastViewedMovie = movie;
                    leastViewedCount = count;
                }

                if (count > bestCombinationCount) {
                    bestCombinationRoom = roomEntry.getKey();
                    bestCombinationMovie = movie;
                    bestCombinationCount = count;
                }
            }
        }

        // Mostrar resultados en el área de texto
        outputTextArea.append("\n\nEstadísticas:\n");
        outputTextArea.append("Película más vista de todas las salas: " + mostViewedMovie + " (" + mostViewedCount + " personas)\n");
        outputTextArea.append("Película menos vista de todas las salas: " + leastViewedMovie + " (" + leastViewedCount + " personas)\n");
        outputTextArea.append("Mejor combinación sala-película más vista: " + bestCombinationRoom + " - " + bestCombinationMovie + " (" + bestCombinationCount + " personas)\n");
    }

    public static void main(String[] args) {
        CinemaxStatistics frame = new CinemaxStatistics();
        frame.setVisible(true);
    }
}
