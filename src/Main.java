import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        // Creating the frame/window
        JFrame obj = new JFrame();
        Gameplay gamePlay = new Gameplay();
        obj.add(gamePlay);
        obj.setBounds(10,10,700,600);
        obj.setTitle("Java Brick Breaker!");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setVisible(true);

    }
}
