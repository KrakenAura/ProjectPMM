import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class DeleteBuku extends JFrame{
    private JTextField fieldCari;
    private JTable tabelBuku;
    private JButton hapusButton;
    private JButton kembaliButton;
    private JPanel deletePanel;
    private JButton cariButton;
    private JTextField fieldHapus;

    DatabaseManager databaseManager = new DatabaseManager();

    public void display(DeleteBuku screen) {
        deleteButton();
        tombolKembali();
        searchButton();
        screen.setContentPane(screen.deletePanel);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        tabelBuku.setVisible(false);
        hapusButton.setVisible(false);
    }

    public void deleteButton () {
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookID = fieldHapus.getText();
                databaseManager.hapusData(bookID);
            }
        });
    }

    public void tombolKembali(){
        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainScreen = new MainMenu();
                dispose();
                mainScreen.displayMainScreen(mainScreen);
            }
        });
    }

    public void searchButton() {
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchData(fieldCari.getText());
                tabelBuku.setVisible(true);
                hapusButton.setVisible(true);
            }
        });
    }

    public void fetchData(String judulBuku) {
        try {
            int columnCount;
            Object[] columnTitle = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM databuku WHERE `Judul` = ?"));
            databaseManager.getPreparedStatement().setString(1, judulBuku);
            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
            columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
            tabelBuku.setModel(model); // Set the model to the JTable
            Object[] kolom = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
            model.addRow(kolom);
            DeleteBuku.BoldTableRowRenderer boldRenderer = new DeleteBuku.BoldTableRowRenderer();
            boldRenderer.setTargetRow(0); // Set the row index to be displayed in bold (0 for the first row)
            tabelBuku.setDefaultRenderer(Object.class, boldRenderer);

            while (databaseManager.getResultSet().next()) {
                Vector<String> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(databaseManager.getResultSet().getString(i));
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public class BoldTableRowRenderer extends DefaultTableCellRenderer {
        private int targetRow; // The row index to be displayed in bold

        public void setTargetRow(int targetRow) {
            this.targetRow = targetRow;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Font font = c.getFont();
            if (row == targetRow) {
                c.setFont(font.deriveFont(Font.BOLD)); // Set the font to bold for the target row
            }
            return c;
        }
    }
}
