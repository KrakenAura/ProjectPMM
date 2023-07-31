import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InputBuku extends JFrame{
    private JTextField fieldJudul;
    private JTextField fieldPenerbit;
    private JTextField fieldPenulis;
    private JTextField fieldNomorRak;
    private JTextField fieldLokasi;
    private JTextField fieldKode;
    private JTextField fieldKategori1;
    private JButton inputButton;
    private JTextField fieldTahun;
    private JPanel InputBuku;
    private JButton kembaliButton;

    DatabaseManager databaseManager = new DatabaseManager();


    public void input_Button() {
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean cekInput = fieldJudul.getText().isEmpty() ||
                            fieldPenerbit.getText().isEmpty() ||
                            fieldPenulis.getText().isEmpty() ||
                            fieldNomorRak.getText().isEmpty() ||
                            fieldTahun.getText().isEmpty()||
                            fieldLokasi.getText().isEmpty()||
                            fieldKode.getText().isEmpty()||
                            fieldKategori1.getText().isEmpty();
                    if (cekInput) {
                        throw new Exception("Mohon isi seluruh data yang valid");
                    }
                    int cekTahun = Integer.parseInt(fieldTahun.getText());
                    String judul, penerbit, penulis, rak, tahun,lokasi,kode,kategori;
                    judul = fieldJudul.getText();
                    penulis = fieldPenulis.getText();
                    penerbit = fieldPenerbit.getText();
                    tahun = fieldTahun.getText();
                    rak = fieldNomorRak.getText();
                    lokasi = fieldLokasi.getText();
                    kode = fieldKode.getText();
                    kategori = fieldKategori1.getText();
                    databaseManager.exportData(judul, penerbit,penulis, tahun, rak, lokasi, kode,kategori);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }
    public void displayInput(InputBuku screen) {
        screen.setContentPane(screen.InputBuku);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        databaseManager.connect();
        tombolKembali();
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

}
