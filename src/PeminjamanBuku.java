import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class PeminjamanBuku extends JFrame {
    private JButton cariButton;

    private JTextField fieldJudul;
    private JTable tabelBuku;
    private JTextField fieldPeminjam;
    private JButton SimpanButton;
    private JButton KembaliButton;
    private JPanel PeminjamanBuku;
    private JTextField fieldPetugas;
    private JTextField fieldKelas;
    private JTextField fieldID;

    DatabaseManager databaseManager = new DatabaseManager();

    public void display(PeminjamanBuku screen) {
        screen.setContentPane(PeminjamanBuku);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchButton();
        tombolKembali();
        tombolSimpan();
        databaseManager.connect();
    }


    public void searchButton() {
        cariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchData(fieldJudul.getText());
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
            PeminjamanBuku.BoldTableRowRenderer boldRenderer = new PeminjamanBuku.BoldTableRowRenderer();
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
        KembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Peminjaman peminjaman = new Peminjaman();
                dispose();
                peminjaman.display(peminjaman);
            }
        });
    }

    public void tombolSimpan(){
        SimpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean cekInput = fieldPeminjam.getText().isEmpty()||
                            fieldKelas.getText().isEmpty()||
                            fieldPetugas.getText().isEmpty()||
                            fieldID.getText().isEmpty();
                    if (cekInput) {
                        throw new Exception("Mohon isi seluruh data yang valid");
                    }
                    String peminjam, kelas,petugas,id;
                    peminjam = fieldPeminjam.getText();
                    kelas = fieldKelas.getText();
                    petugas = fieldPetugas.getText();
                    id = fieldID.getText();
                    databaseManager.exportPeminjam(peminjam,kelas,petugas,id);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }

}
