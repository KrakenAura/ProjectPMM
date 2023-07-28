import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame{
    private JButton inputBukuButton;
    private JButton listBukuButton;
    private JButton cariBukuButton;
    private JButton hapusBukuButton;
    private JPanel mainPanel;
    private JButton peminjamanButton;

    private InputBuku inputBuku = new InputBuku();
    private CariBuku cariBuku = new CariBuku();

    public void displayMainScreen(MainMenu screen) {

        screen.setContentPane(screen.mainPanel);
        screen.setTitle("Manajemen Perpustakaan");
        screen.setSize(800,400);
        screen.setVisible(true);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tombolInput();
        tombolCari();
    }

    public void tombolInput() {
        inputBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputBuku.input_Button();
                dispose();
                inputBuku.displayInput(inputBuku);
            }
        });
    }
    public void tombolCari() {
        cariBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                cariBuku.searchButton();
                cariBuku.display(cariBuku);
            }
        });
    }
}
