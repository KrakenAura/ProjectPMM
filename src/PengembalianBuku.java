import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class PengembalianBuku extends JFrame{
    private JPanel pengembalianPanel;
    private JTextField fieldCari;
    private JTable tabelBuku;
    private JButton kembalikanButton;
    private JButton kembaliButton;
    private JButton cariButton;
    private JTextField fieldID;

    DatabaseManager databaseManager = new DatabaseManager();

    public void searchButton() {
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchData(fieldCari.getText());
                tabelBuku.setVisible(true);
            }
        });
    }

    public void fetchData(String namapeminjam) {
        try {
            int columnCount;
            Object[] columnTitle = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM riwayat_peminjaman WHERE namapeminjam = ?"));
            databaseManager.getPreparedStatement().setString(1, namapeminjam);
            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
            columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
            tabelBuku.setModel(model); // Set the model to the JTable
            Object[] kolom = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
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

    public void tombolKembali(){
        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Peminjaman peminjaman = new Peminjaman();
                dispose();
                peminjaman.display(peminjaman);
            }
        });
    }

    public void display(PengembalianBuku screen) {
        tombolKembali();
        searchButton();
        tombolReturn();
        screen.setContentPane(screen.pengembalianPanel);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        tabelBuku.setVisible(false);
    }

    public void tombolReturn(){
        kembalikanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean cekInput =
                            fieldID.getText().isEmpty();
                    if (cekInput) {
                        throw new Exception("Mohon isi seluruh data yang valid");
                    }

                    String id = fieldID.getText();
                    databaseManager.updatePengembalian(id);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }
}
