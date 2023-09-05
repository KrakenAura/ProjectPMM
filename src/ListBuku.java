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
//public class ListBuku extends JFrame{
//    private JTable tabelBuku;
//    private JButton tombolKembali;
//    private JPanel ListBuku;
//
//    DatabaseManager databaseManager =  new DatabaseManager();
//
//    public void fetchData() {
//        try {
//            int columnCount;
//            Object[] columnTitle = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
//            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM databuku"));
//            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
//            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
//            columnCount = metaData.getColumnCount();
//
//            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
//            tabelBuku.setModel(model); // Set the model to the JTable
//            Object[] kolom = {"ID","Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori","QTY"};
//            model.addRow(kolom);
//            ListBuku.BoldTableRowRenderer boldRenderer = new ListBuku.BoldTableRowRenderer();
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
//    public void display(ListBuku screen) {
//        tombolKembali();
//        screen.setContentPane(ListBuku);
//        screen.setTitle("Manajemen Perpustakaan");
//        screen.setSize(800, 400);
//        screen.setVisible(true);
//        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        databaseManager.connect();
//        fetchData();
//
//    }
//
//    public void tombolKembali(){
//        tombolKembali.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                MainMenu mainScreen = new MainMenu();
//                dispose();
//                mainScreen.displayMainScreen(mainScreen);
//            }
//        });
//    }
//}

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;


public class ListBuku extends JFrame {
    private JTable tabelBuku;
    private JButton tombolKembali;
    private JButton tombolNext;
    private JButton tombolPrevious;
    private JPanel ListBuku;

    DatabaseManager databaseManager = new DatabaseManager();
    private final int rowsPerPage = 10;
    private int currentPage = 0;

    public void fetchData() {
        try {
            int columnCount;
            Object[] columnTitle = {"ID", "Judul Buku", "Penerbit Buku", "Penulis Buku", "Tahun Terbit", "Nomor Rak", "Lokasi Buku", "Kode Buku", "Kategori", "QTY"};
            databaseManager.setPreparedStatement(databaseManager.getConnection().prepareStatement("SELECT * FROM databuku", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
            databaseManager.setResultSet(databaseManager.getPreparedStatement().executeQuery());
            ResultSetMetaData metaData = databaseManager.getResultSet().getMetaData();
            columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel(null, columnTitle);
            tabelBuku.setModel(model); // Set the model to the JTable
            ListBuku.BoldTableRowRenderer boldRenderer = new ListBuku.BoldTableRowRenderer();
            boldRenderer.setTargetRow(0); // Set the row index to be displayed in bold (0 for the first row)
            tabelBuku.setDefaultRenderer(Object.class, boldRenderer);

            int startIndex = currentPage * rowsPerPage;
            int count = 0;

            databaseManager.getResultSet().absolute(startIndex + 1); // Move to the desired row

            model.addRow(columnTitle); // Add header row

            while (count < rowsPerPage && databaseManager.getResultSet().next()) {
                Vector<String> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(databaseManager.getResultSet().getString(i));
                }
                model.addRow(rowData);
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void display(ListBuku screen) {
        tombolKembali();
        tombolNext();
        tombolPrevious();

        screen.setContentPane(ListBuku);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        fetchData();
    }

    public void tombolKembali() {
        tombolKembali.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainScreen = new MainMenu();
                dispose();
                mainScreen.displayMainScreen(mainScreen);
            }
        });
    }

    public void tombolNext() {
        tombolNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < getTotalPages() - 1) {
                    currentPage++;
                    fetchData();
                }
            }
        });
    }

    public void tombolPrevious() {
        tombolPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    fetchData();
                }
            }
        });
    }

    private int getTotalPages() {
        try {
            int totalRows = 0;

            // Count the total number of rows in the ResultSet
            while (databaseManager.getResultSet().next()) {
                totalRows++;
            }

            // Reset the cursor to the original position
            databaseManager.getResultSet().beforeFirst();

            return (int) Math.ceil((double) totalRows / rowsPerPage);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Handle the exception appropriately
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

