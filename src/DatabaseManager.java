import java.sql.*;
import javax.swing.*;
import java.util.Calendar;

/**
 * Class untuk manajemen database
 */
public class DatabaseManager {
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    /**
     * Method Setter Getter untuk class lain mengakses atribut dari database
     * @return preparedStatement
     */
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    /**
     * Method Setter dan Getter untuk class lain  mengakses database
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Method Setter dan Getter untuk class lain mengakses hasil Query database
     * @return resultSet
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * berfungsi untuk melakukan koneksi program ke server database
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pmm","root","KrakenAura1.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Method untuk memasukkan data buku kedalam database
     * @param judul
     * @param penulis
     * @param penerbit
     * @param tahun_terbit
     * @param rak
     * @param lokasi
     * @param kode_buku
     * @param kategori
     */
//    public void exportData(String judul, String penerbit, String penulis, String tahun_terbit, String rak, String lokasi, String kode_buku, String kategori, Integer qty) {
//        try {
//            preparedStatement = connection.prepareStatement("INSERT INTO databuku (`id`,`Judul`,`Penerbit`,`Penulis`,`Tahun_Terbit`,`Nomor_Rak`,`Lokasi`,`Kode_Buku`,`Kategori`,`qty`) VALUES (?,?,?,?,?,?,?,?,?,?)");
//            preparedStatement.setString(1,id);
//            preparedStatement.setString(2, judul);
//            preparedStatement.setString(3, penerbit);
//            preparedStatement.setString(4, penulis);
//            preparedStatement.setString(5, tahun_terbit);
//            preparedStatement.setString(6, rak);
//            preparedStatement.setString(7, lokasi);
//            preparedStatement.setString(8, kode_buku);
//            preparedStatement.setString(9, kategori);
//            preparedStatement.setInt(10,qty);
//
//            int execute = preparedStatement.executeUpdate();
//            if (execute == 1) {
//                System.out.println("Upload sukses");
//            } else {
//                System.out.println("Upload gagal");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Upload gagal");
//            e.printStackTrace();
//        }
//    }

    public void exportData(String judul, String penerbit, String penulis, String tahun_terbit, String rak, String lokasi, String kode_buku, String kategori, Integer qty) {
        try {
            // Generate the next book title (id) in the format "BOOK1", "BOOK2", etc.
            String id = generateNextIDBuku();

            preparedStatement = connection.prepareStatement("INSERT INTO databuku (`id`,`Judul`,`Penerbit`,`Penulis`,`tahunterbit`,`nomorrak`,`Lokasi`,`kodebuku`,`Kategori`,`qty`) VALUES (?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, judul);
            preparedStatement.setString(3, penerbit);
            preparedStatement.setString(4, penulis);
            preparedStatement.setString(5, tahun_terbit);
            preparedStatement.setString(6, rak);
            preparedStatement.setString(7, lokasi);
            preparedStatement.setString(8, kode_buku);
            preparedStatement.setString(9, kategori);
            preparedStatement.setInt(10, qty);

            int execute = preparedStatement.executeUpdate();
            if (execute == 1) {
                System.out.println("Upload sukses");
            } else {
                System.out.println("Upload gagal");
            }

        } catch (SQLException e) {
            System.out.println("Upload gagal");
            e.printStackTrace();
        }
    }

    private String generateNextIDBuku() {
        String nextID = null;
        try {
            // Fetch the maximum ID from the 'databuku' table
            preparedStatement = connection.prepareStatement("SELECT MAX(id) AS max_id FROM databuku");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String lastID = resultSet.getString("max_id");
                if (lastID != null) {
                    int numericPart = Integer.parseInt(lastID.substring(4));
                    numericPart++;
                    nextID = "BOOK" + String.format("%03d", numericPart);
                } else {
                    // If the 'databuku' table is empty, start with 'BOOK001'
                    nextID = "BOOK001";
                }
            } else {
                // If there are no rows in the result, start with 'BOOK001'
                nextID = "BOOK001";
            }
        } catch (SQLException e) {
            System.out.println("Failed to generate next ID");
            e.printStackTrace();
        }
        return nextID;
    }



//    public void exportPeminjam(String judul, String peminjam, String petugas) {
//        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
//        try {
//            preparedStatement = connection.prepareStatement("INSERT INTO riwayat_peminjaman (`Judul`,`Peminjam`,`Petugas`,`Tanggal_Peminjaman`) VALUES (?,?,?,?)");
//            preparedStatement.setString(1, judul);
//            preparedStatement.setString(2, peminjam);
//            preparedStatement.setString(3,petugas);
//            preparedStatement.setDate(4,currentDate);
//
//            int execute = preparedStatement.executeUpdate();
//            if (execute == 1) {
//                System.out.println("Upload sukses");
//            } else {
//                System.out.println("Upload gagal");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Upload gagal");
//            e.printStackTrace();
//        }
//    }

    public void exportPeminjam(String peminjam, String kelas, String petugas, String ID) {
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        try {
            // Generate the next ID in the format "HIS001", "HIS002", etc.
            String id = generateNextID();

            preparedStatement = connection.prepareStatement("INSERT INTO riwayat_peminjaman (`id`, `namapeminjam`, `kelas`, `namapetugas`, `tanggalpeminjaman`, `tanggalpengembalian`, `databuku_id`) VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, peminjam);
            preparedStatement.setString(3, kelas); // Set the default value for 'kelas' (assuming it's an integer column)
            preparedStatement.setString(4, petugas);
            preparedStatement.setDate(5, currentDate);
            preparedStatement.setNull(6, Types.DATE); // Set 'tanggalpengembalian' to NULL
            preparedStatement.setString(7, ID); // Assuming 'judul' is the book ID or a reference to the book

            int execute = preparedStatement.executeUpdate();
            if (execute == 1) {
                System.out.println("Upload sukses");
            } else {
                System.out.println("Upload gagal");
            }
            updateQtyInDatabuku(ID,-1);


        } catch (SQLException e) {
            System.out.println("Upload gagal");
            e.printStackTrace();
        }
    }
    public void updatePengembalian(String id){
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        try {
            preparedStatement = connection.prepareStatement("UPDATE riwayat_peminjaman SET tanggalpengembalian = ? WHERE id=?");
            preparedStatement.setDate(1, currentDate);
            preparedStatement.setString(2, id);
            int execute = preparedStatement.executeUpdate();
            if (execute == 1) {
                System.out.println("Upload sukses");
            } else {
                System.out.println("Upload gagal");
            }
        }catch (SQLException e){
            System.out.println("Upload gagal");
            e.printStackTrace();
        }
        String bookID = getBookIDFromRiwayatPeminjaman(id);
        updateQtyInDatabuku(bookID,1);
    }


    private String generateNextID() {
        String nextID = null;
        try {
            // Fetch the maximum ID from the 'databuku' table
            preparedStatement = connection.prepareStatement("SELECT MAX(id) AS max_id FROM databuku");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String lastID = resultSet.getString("max_id");
                if (lastID != null) {
                    int numericPart = Integer.parseInt(lastID.substring(4));
                    numericPart++;
                    nextID = "HIS" + String.format("%03d", numericPart);
                } else {
                    // If the 'databuku' table is empty, start with 'BOOK001'
                    nextID = "HIS001";
                }
            } else {
                // If there are no rows in the result, start with 'BOOK001'
                nextID = "HIS001";
            }
        } catch (SQLException e) {
            System.out.println("Failed to generate next ID");
            e.printStackTrace();
        }
        return nextID;
    }



    private String getBookIDFromRiwayatPeminjaman(String id) {
        String bookID = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT databuku_id FROM riwayat_peminjaman WHERE id = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bookID = resultSet.getString("databuku_id");
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch 'databuku_id' from 'riwayat_peminjaman' table");
            e.printStackTrace();
        }
        return bookID;
    }




    /**
     * Method untuk mengupdate data buku di database berdasarkan pencarian pengguna
     * @param judul
     * @param pengarang
     * @param penerbit
     * @param tahun
     * @param rak
     * @param target
     */
    public void updateData (String judul, String pengarang, String penerbit,String tahun , String rak, String target) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE databuku SET `Judul Buku` = ?, `Pengarang Buku` = ?, `Penerbit Buku` = ?, `Tahun Terbit` = ?, `Nomor Rak` = ? WHERE `Judul Buku` = ?");
            preparedStatement.setString(1, judul);
            preparedStatement.setString(2, pengarang);
            preparedStatement.setString(3, penerbit);
            preparedStatement.setString(4, tahun);
            preparedStatement.setString(5, rak);
            preparedStatement.setString(6, target);
            int execute = preparedStatement.executeUpdate();
            if (execute == 1) {
                System.out.println("Update Berhasil");
            }
            else {
                System.out.println("Update Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Update Gagal");
            e.printStackTrace();
        }
    }

    /**
     * Method untuk menghapus data buku di database berdasarkan pencarian pengguna
     * @param target
     */
    public void hapusData (String target) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM databuku where `Judul` = ?");
            preparedStatement.setString(1, target);
            int execute = preparedStatement.executeUpdate();
            if (execute == 1) {
                System.out.println("Data berhasil dihapus");
            }
            else {
                System.out.println("Data tidak terhapus");
            }
        } catch (SQLException e) {
            System.out.println("Data tidak terhapus");
            e.printStackTrace();
        }
    }
    public void updatePeminjam(String target){
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        try {
            preparedStatement = connection.prepareStatement("UPDATE riwayat_peminjaman SET `Tanggal_Pengembalian` = ? WHERE `Peminjam` = ?");
            preparedStatement.setDate(1, currentDate);
            preparedStatement.setString(2,target);

            int execute = preparedStatement.executeUpdate();
            if(execute == 1){
                System.out.println("Data berhasil diinput");
            }
            else {
                System.out.println("Data gagal diinput");
            }


        }catch (SQLException e){
            System.out.println("Data gagal diinput");
            e.printStackTrace();
        }

    }
    private void updateQtyInDatabuku(String bookID, int changeAmount) {
        try {
            // Fetch the current 'qty' value from the 'databuku' table based on the bookID
            preparedStatement = connection.prepareStatement("SELECT qty FROM databuku WHERE id = ?");
            preparedStatement.setString(1, bookID);
            resultSet = preparedStatement.executeQuery();

            int currentQty = 0;
            if (resultSet.next()) {
                currentQty = resultSet.getInt("qty");
            }

            // Calculate the new 'qty' value by adding the change amount
            int newQty = currentQty + changeAmount;

            // Update the 'qty' in the 'databuku' table
            preparedStatement = connection.prepareStatement("UPDATE databuku SET qty = ? WHERE id = ?");
            preparedStatement.setInt(1, newQty);
            preparedStatement.setString(2, bookID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to update 'qty' in 'databuku' table");
            e.printStackTrace();
        }
    }

}
