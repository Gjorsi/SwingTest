import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class gui {

    private static JTextField userName;
    private static JPasswordField pw;
    private static JLabel message;
    private static JFrame loginFrame, mainFrame;
    private static Font font;
    private static JPanel gridPanel, mainPanel, buttonPanel;
    private static int[][] numberGrid;
    private static int gridRows, gridCols, castleX, castleY;

    public static void main(String[] args) {
        //System.out.println(BCrypt.gensalt(10));

        gridPanel = new JPanel();
        mainPanel = new JPanel();
        buttonPanel = new JPanel();

        font = new Font("Serif", Font.PLAIN, 48);

        loginFrame = new JFrame("Bergpro");
        loginFrame.setSize(1000,500);
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        loginFrame.add(panel);
        placeComponents(panel);
        loginFrame.setVisible(true);

        //remove next line to get login screen when running
        //loadUI();
    }

    private static void defendWinterfell () {
        //find which side (N,W,S,E) is closest to castle

        int shortestDistance[], shortestDirection=0;
        shortestDistance = new int[4];
        shortestDistance[0] = castleX; //north
        shortestDistance[1] = castleY; //west
        shortestDistance[2] = gridRows-1-castleX; //south
        shortestDistance[3] = gridCols-1-castleY; //east

        for (int i=1;i<4;i++) {
            if (shortestDistance[shortestDirection]>shortestDistance[i]) {
                shortestDirection=i;
            }
        }

        JLabel test = new JLabel("N" + Integer.toString(shortestDistance[0]) +
                " W" + Integer.toString(shortestDistance[1]) +
                " S" + Integer.toString(shortestDistance[2]) +
                " E" + Integer.toString(shortestDistance[3]) +
                " Shortest direction " + shortestDirection);
        test.setFont(font);
        buttonPanel.add(test);
        buttonPanel.revalidate();
        buttonPanel.repaint();

        int leastRes[] = new int[shortestDistance[shortestDirection]];
        for (int i=0;i<shortestDistance[shortestDirection];i++) {
            int x, y;
            if (shortestDirection%2==0) {
                x = i;
                y = castleY;
            } else {
                x = castleX;
                y = i;
            }

            int sum[];
        }
    }

    private static void placeComponents (JPanel panel) {
        GridLayout grid = new GridLayout(0,2);
        panel.setLayout(grid);

        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(10,10,200,50);
        userLabel.setFont(font);
        panel.add(userLabel);

        userName = new JTextField();
        userName.setFont(font);
        panel.add(userName);

        JLabel pwLabel = new JLabel("Password:");
        pwLabel.setBounds(10, 10, 200, 50);
        pwLabel.setFont(font);
        panel.add(pwLabel);

        pw = new JPasswordField();
        pw.setFont(font);
        panel.add(pw);

        JButton login = new JButton("Login");
        login.setFont(font);
        panel.add(login);

        JButton register = new JButton("Register");
        register.setFont(font);
        panel.add(register);

        message = new JLabel();
        message.setFont(font);
        panel.add(message);

        ActionListener loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        };
        login.addActionListener(loginListener);

        ActionListener registerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        };
        register.addActionListener(registerListener);
    }

    public static void loadUI () {

        message.setText("");
        userName.setText("");
        pw.setText("");

        mainFrame = new JFrame("Mainframe");
        mainFrame.setExtendedState(mainFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setLayout(new BorderLayout());
        setupMainFrame();
        setupButtonPanel();

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        mainFrame.add(mainPanel);

        loginFrame.setVisible(false);
        mainFrame.setVisible(true);
    }

    public static void setupMainFrame () {
        gridRows = randomWithRange(3, 20);
        gridCols = randomWithRange(3, 20);
        GridLayout grid = new GridLayout(gridRows, gridCols);
        gridPanel.setLayout(grid);

        numberGrid = new int[gridRows][gridCols];

        castleX = randomWithRange(1, gridRows-2);
        castleY = randomWithRange(1, gridCols-2);

        for (int i=0;i<gridRows;i++) {
            for (int j=0;j<gridCols;j++) {

                int bannermen = randomWithRange(0,100);
                numberGrid[i][j] = bannermen;

                if (i==castleX && j == castleY) {
                    numberGrid[i][j] = -1;
                }

                JLabel test = new JLabel(Integer.toString(bannermen));
                test.setHorizontalAlignment(JLabel.CENTER);
                test.setFont(font);
                if (i==castleX && j == castleY) {
                    test.setText("C");
                    test.setOpaque(true);
                    test.setBackground(Color.BLUE);
                    test.setForeground(Color.white);
                }
                gridPanel.add(test);
            }
        }
    }

    public static void setupButtonPanel () {
        //buttonPanel.setLayout(new GridLayout(1,3));
        JButton logOut = new JButton("Log Out");
        logOut.setFont(font);
        buttonPanel.add(logOut);

        JButton reGen = new JButton("Regenerate");
        reGen.setFont(font);
        buttonPanel.add(reGen);

        JButton defend = new JButton("Defend Winterfell");
        defend.setFont(font);
        buttonPanel.add(defend);

        ActionListener defendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defendWinterfell();
            }
        };
        defend.addActionListener(defendListener);

        ActionListener reGenListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(gridPanel);
                gridPanel = new JPanel();
                try {
                    buttonPanel.remove(buttonPanel.getComponent(3));
                } catch (ArrayIndexOutOfBoundsException meh) {

                }
                setupMainFrame();
                mainPanel.add(gridPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        };
        reGen.addActionListener(reGenListener);

        ActionListener logOutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);

                loginFrame.setVisible(true);
            }
        };
        logOut.addActionListener(logOutListener);
    }

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public static void registerUser () {
        if (userName.getText().length()>0 && pw.getText().length()>0) {
            File filePath = new File("userBase.csv");
            try {
                FileWriter writer = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(writer);
                PrintWriter printer = new PrintWriter(bw);

                String userSalt = BCrypt.gensalt(12);
                String hashedPW = BCrypt.hashpw(pw.getText(), userSalt);

                printer.println(userName.getText() + "," + hashedPW + "," + userSalt);
                printer.close();
                bw.close();
                writer.close();
                pw.setText("");

                message.setText("User registered");
            } catch (IOException e) {
                message.setText("Unable to save");
            }

        } else {
            message.setText("Missing username / password");
        }
    }

    public static void loginUser () {
        if (userName.getText().length()>0 && pw.getText().length()>0) {
            File filePath = new File("userBase.csv");
            String currentLine;
            Boolean userMatch = false;
            try {
                FileReader reader = new FileReader(filePath);
                BufferedReader br = new BufferedReader(reader);
                while ((currentLine = br.readLine()) !=null) {
                    String userInfo[] = currentLine.split(",");
                    if (userInfo[0].equals(userName.getText())) {
                        userMatch = true;
                        //String hashedPW = DigestUtils.sha512Hex(salt + pw.getText());
                        String hashedPW = BCrypt.hashpw(pw.getText(), userInfo[2]);
                        if (BCrypt.checkpw(pw.getText(), hashedPW)) {
                            message.setText("Authenticated");
                            loadUI();
                        } else {
                            message.setText("Wrong password");
                        }
                    }
                }

                if (!userMatch) {
                    message.setText("Unknown user / password");
                }
                br.close();
                reader.close();

            } catch (FileNotFoundException e) {
                message.setText("Unable to load");
            } catch (IOException e) {
                message.setText("Unable to load");
            }
        } else {
            message.setText("Missing username / password");
        }
    }
}