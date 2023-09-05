import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class CariBuku extends JFrame{
    private JTextField fieldJudul;
    private JTable tabelBuku;
    private JButton kembaliButton;
    private JButton cariButton;
    private JPanel CariBuku;

    DatabaseManager databaseManager =  new DatabaseManager();

    public void display(CariBuku screen) {
        tombolKembali();
        screen.setContentPane(CariBuku);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        tabelBuku.setVisible(false);
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
                fetchData(fieldJudul.getText());
                tabelBuku.setVisible(true);
            }
        });


    }

    public void fetchData(String judulBuku) {
        try {
            int columnCount;
            Object[] columnTitle = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM databuku WHERE `Judul` LIKE CONCAT('%', ?, '%') OR `id` LIKE CONCAT('%', ?, '%') OR `penerbit` LIKE CONCAT('%', ?, '%') OR `penulis` LIKE CONCAT('%', ?, '%') OR `tahunterbit` LIKE CONCAT('%', ?, '%') OR `nomorrak` LIKE CONCAT('%', ?, '%') OR `lokasi` LIKE CONCAT('%', ?, '%') OR `kodebuku` LIKE CONCAT('%', ?, '%') OR `kategori` LIKE CONCAT('%', ?, '%')"));
            databaseManager.getPreparedStatement().setString(1, judulBuku);
            databaseManager.getPreparedStatement().setString(2, judulBuku);
            databaseManager.getPreparedStatement().setString(3, judulBuku);
            databaseManager.getPreparedStatement().setString(4, judulBuku);
            databaseManager.getPreparedStatement().setString(5, judulBuku);
            databaseManager.getPreparedStatement().setString(6, judulBuku);
            databaseManager.getPreparedStatement().setString(7, judulBuku);
            databaseManager.getPreparedStatement().setString(8, judulBuku);
            databaseManager.getPreparedStatement().setString(9, judulBuku);

            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
            columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
            tabelBuku.setModel(model); // Set the model to the JTable
            Object[] kolom = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
            model.addRow(kolom);
            BoldTableRowRenderer boldRenderer = new BoldTableRowRenderer();
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
