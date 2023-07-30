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
    public void exportData(String judul, String penerbit, String penulis, String tahun_terbit, String rak, String lokasi, String kode_buku, String kategori) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO databuku (`Judul`,`Penerbit`,`Penulis`,`Tahun_Terbit`,`Nomor_Rak`,`Lokasi`,`Kode_Buku`,`Kategori`) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, judul);
            preparedStatement.setString(2, penerbit);
            preparedStatement.setString(3, penulis);
            preparedStatement.setString(4, tahun_terbit);
            preparedStatement.setString(5, rak);
            preparedStatement.setString(6, lokasi);
            preparedStatement.setString(7, kode_buku);
            preparedStatement.setString(8, kategori);

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

    public void exportPeminjam(String judul, String peminjam, String petugas) {
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO riwayat_peminjaman (`Judul`,`Peminjam`,`Petugas`,`Tanggal_Peminjaman`) VALUES (?,?,?,?)");
            preparedStatement.setString(1, judul);
            preparedStatement.setString(2, peminjam);
            preparedStatement.setString(3,petugas);
            preparedStatement.setDate(4,currentDate);

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
            preparedStatement = connection.prepareStatement("DELETE FROM databuku where `Judul Buku` = ?");
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
}
