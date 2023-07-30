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

    public void display(Peminjaman screen) {
        tombolKembali();
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


}
