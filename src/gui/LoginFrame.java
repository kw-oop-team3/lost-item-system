package gui;

import repository.LostItemRepository;
import service.ItemService;
import service.ReturnService;
import user.LoginResult;
import user.User;
import user.UserRepository;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserRepository userRepository;
    private final ItemService itemService;
    private final ReturnService returnService;
    private final LostItemRepository repository;

    private JTextField idField;
    private JTextField nameField;
    private JLabel messageLabel;

    public LoginFrame(UserRepository userRepository, ItemService itemService,
                      ReturnService returnService, LostItemRepository repository) {
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.returnService = returnService;
        this.repository = repository;
        buildUI();
    }

    private void buildUI() {
        setTitle("KW Lost and Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(330, 230);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("KW Lost and Found", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(new JLabel("학번"), gbc);
        idField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(idField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(new JLabel("이름"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(nameField, gbc);

        JButton loginBtn    = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입");
        JButton exitBtn     = new JButton("종료");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        btnPanel.add(exitBtn);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 0;
        panel.add(btnPanel, gbc);

        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        panel.add(messageLabel, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> handleRegister());
        exitBtn.addActionListener(e -> System.exit(0));
        idField.addActionListener(e -> nameField.requestFocus());
        nameField.addActionListener(e -> handleLogin());

        add(panel);
    }

    private void handleLogin() {
        String id   = idField.getText().trim();
        String name = nameField.getText().trim();
        LoginResult result = userRepository.login(id, name);
        if (result.isSuccess()) {
            User loggedInUser = result.getUser();
            dispose();
            new MenuFrame(loggedInUser, itemService, returnService, repository,
                () -> new LoginFrame(userRepository, itemService, returnService, repository).setVisible(true)
            ).setVisible(true);
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(result.getMessage());
        }
    }

    private void handleRegister() {
        String id   = idField.getText().trim();
        String name = nameField.getText().trim();
        String result = userRepository.register(id, name);
        switch (result) {
            case UserRepository.RESULT_SUCCESS:
                messageLabel.setForeground(new Color(0, 140, 0));
                messageLabel.setText("회원가입 성공! 로그인해주세요.");
                break;
            case UserRepository.RESULT_DUPLICATE:
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("이미 존재하는 학번입니다.");
                break;
            case UserRepository.RESULT_INVALID:
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("학번과 이름을 모두 입력해주세요.");
                break;
            case UserRepository.RESULT_ADMIN_RESERVED:
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("0000은 관리자 전용 계정입니다.");
                break;
            default:
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("오류가 발생했습니다.");
                break;
        }
    }
}
