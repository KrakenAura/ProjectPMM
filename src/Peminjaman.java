import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Peminjaman extends JFrame{
    private JButton peminjamanBukuButton;
    private JButton pengembalianBukuButton;
    private JButton riwayatButton;
    private JButton kembaliButton;
    private JPanel Peminjaman;

    private DatabaseManager databaseManager =  new DatabaseManager();
    private PeminjamanBuku peminjamanBuku = new PeminjamanBuku();
    private PengembalianBuku pengembalianBuku = new PengembalianBuku();

    private RiwayatPeminjaman riwayatPeminjaman = new RiwayatPeminjaman();

    public void display(Peminjaman screen) {
        tombolKembali();
        tombolPengembalianBuku();
        tombolRiwayat();
        screen.setContentPane(Peminjaman);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800, 400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public void tombolPeminjamanBuku(){
        peminjamanBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                peminjamanBuku.display(peminjamanBuku);
            }
        });
    }

    public void tombolPengembalianBuku(){
       pengembalianBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                pengembalianBuku.display(pengembalianBuku);
            }
        });
    }

    public void tombolRiwayat(){
        riwayatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                riwayatPeminjaman.display(riwayatPeminjaman);
            }
        });
    }
}
