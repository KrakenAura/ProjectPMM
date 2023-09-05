//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.Vector;
//
//public class RiwayatPeminjaman extends JFrame {
//    private JTable tabelBuku;
//    private JPanel riwayatPanel;
//    private JButton kembaliButton;
//
//    DatabaseManager databaseManager = new DatabaseManager();
//
//    public void fetchData() {
//        try {
//            int columnCount;
//            Object[] columnTitle = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
//            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM riwayat_peminjaman"));
//            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
//            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
//            columnCount = metaData.getColumnCount();
//
//            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
//            tabelBuku.setModel(model); // Set the model to the JTable
//            Object[] kolom = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
//            model.addRow(kolom);
//            RiwayatPeminjaman.BoldTableRowRenderer boldRenderer = new RiwayatPeminjaman.BoldTableRowRenderer();
//            boldRenderer.setTargetRow(0); // Set the row index to be displayed in bold (0 for the first row)
//            tabelBuku.setDefaultRenderer(Object.class, boldRenderer);
//
//            while (databaseManager.getResultSet().next()) {
//                Vector<String> rowData = new Vector<>();
//                for (int i = 1; i <= columnCount; i++) {
//                    rowData.add(databaseManager.getResultSet().getString(i));
//                }
//                model.addRow(rowData);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public class BoldTableRowRenderer extends DefaultTableCellRenderer {
//        private int targetRow; // The row index to be displayed in bold
//
//        public void setTargetRow(int targetRow) {
//            this.targetRow = targetRow;
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//            Font font = c.getFont();
//            if (row == targetRow) {
//                c.setFont(font.deriveFont(Font.BOLD)); // Set the font to bold for the target row
//            }
//            return c;
//        }
//    }
//
//    public void display(RiwayatPeminjaman screen) {
//        screen.setContentPane(riwayatPanel);
//        screen.setTitle("Manajemen Perpustakaan");
//        screen.setSize(800, 400);
//        screen.setVisible(true);
//        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        databaseManager.connect();
//        fetchData();
//        tombolKembali();
//    }
//
//    public void tombolKembali(){
//        kembaliButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Peminjaman peminjaman = new Peminjaman();
//                dispose();
//                peminjaman.display(peminjaman);
//            }
//        });
//    }
//}


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RiwayatPeminjaman extends JFrame {
    private JTable tabelBuku;
    private JPanel riwayatPanel;
    private JButton kembaliButton;
    private JButton nextButton;
    private JButton prevButton;

    DatabaseManager databaseManager = new DatabaseManager();
    private final int rowsPerPage = 10;
    private int currentPage = 0;
    private List<Vector<String>> resultSetData = new ArrayList<>();

    public RiwayatPeminjaman() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < getTotalPages() - 1) {
                    currentPage++;
                    fetchData();
                }
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    fetchData();
                }
            }
        });
    }

    public void fetchData() {
        try {
            int columnCount;
            Object[] columnTitle = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM riwayat_peminjaman"));
            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
            columnCount = metaData.getColumnCount();

            resultSetData.clear();

            while (databaseManager.getResultSet().next()) {
                Vector<String> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(databaseManager.getResultSet().getString(i));
                }
                resultSetData.add(rowData);
            }

            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
            tabelBuku.setModel(model); // Set the model to the JTable
            Object[] kolom = {"ID Riwayat", "Nama Peminjam", "Kelas", "Nama Petugas", "Tanggal Peminjaman", "Tanggal Pengembalian", "ID Buku"};
            model.addRow(kolom);
            RiwayatPeminjaman.BoldTableRowRenderer boldRenderer = new RiwayatPeminjaman.BoldTableRowRenderer();
            boldRenderer.setTargetRow(0); // Set the row index to be displayed in bold (0 for the first row)
            tabelBuku.setDefaultRenderer(Object.class, boldRenderer);

            int startIndex = currentPage * rowsPerPage;
            int endIndex = Math.min(resultSetData.size(), startIndex + rowsPerPage);

            for (int i = startIndex; i < endIndex; i++) {
                model.addRow(resultSetData.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getTotalPages() {
        return (int) Math.ceil((double) resultSetData.size() / rowsPerPage);
    }

    public class BoldTableRowRenderer extends DefaultTableCellRenderer {
        private int targetRow;

        public void setTargetRow(int targetRow) {
            this.targetRow = targetRow;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Font font = c.getFont();
            if (row == targetRow) {
                c.setFont(font.deriveFont(Font.BOLD));
            }
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RiwayatPeminjaman riwayatPeminjaman = new RiwayatPeminjaman();
                riwayatPeminjaman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                riwayatPeminjaman.pack();
                riwayatPeminjaman.setVisible(true);
            }
        });
    }
    public void display(RiwayatPeminjaman screen) {
        setContentPane(riwayatPanel);
        setTitle("Manajemen Perpustakaan");
        setSize(800, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        fetchData();
        tombolKembali();
    }
    public void tombolKembali() {
        kembaliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainScreen = new MainMenu();
                dispose();
                mainScreen.displayMainScreen(mainScreen);
            }
        });
    }

}
