package km.data;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    private FileWriter writer;

    public CSVWriter() {
        this.writer = null;
    }

    public void setFilePath(String filePath) throws IOException {
        if (writer != null) {
            writer.close();
        }
        writer = new FileWriter(filePath);
        writer.write("Problem Size, Matrix, Algorithm, Execution Time (ns), Execution Time (ms)\n");
    } // Ustawienie ścieżki pliku i zapisanie kolumn

    public void writeRecord(int problemSize, int[][] matrix, String algorithm, long timeNano, long timeMilli) throws IOException {
        writer.write(problemSize + ", ");
        writeFirstMatrixRow(matrix);
        writer.write(", " + algorithm + ", " + timeNano + ", " + timeMilli + "\n");
        writeRemainingMatrixRows(matrix);
    } // Zapisanie rekordu do pliku

    private void writeFirstMatrixRow(int[][] matrix) throws IOException {
        for (int i = 0; i < matrix[0].length; i++) {
            writer.write(String.format("%3d", matrix[0][i]) + (i < matrix[0].length - 1 ? " " : ""));
            // Uwzględniamy zapisyawnie na co najmniej 3 znakach, jeżeli dana liczba nie jest ostatnia to dodajemy spacje
        }
    } // Zapisanie pierwszego wiersza macierzy do pliku

    private void writeRemainingMatrixRows(int[][] matrix) throws IOException {
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                writer.write(String.format("%3d", matrix[i][j]) + (j < matrix[i].length - 1 ? " " : ""));
            }
            writer.write("\n");
        }
    } // Zapisywanie reszty macierzy, zgodnie z tym co wyzej

    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
