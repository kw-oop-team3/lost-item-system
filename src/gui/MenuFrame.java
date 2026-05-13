package gui;

import item.Category;
import item.ItemStatus;
import item.Location;
import item.LostItem;
import repository.LostItemRepository;
import search.CategorySearchStrategy;
import search.LocationSearchStrategy;
import search.NameSearchStrategy;
import search.SearchStrategy;
import service.ItemService;
import service.ReturnService;
import user.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class MenuFrame extends JFrame {

    private final User user;
    private final ItemService itemService;
    private final ReturnService returnService;
    private final LostItemRepository repository;
    private final Runnable onLogout;

    private DefaultTableModel tableModel;
    private JTable table;

    private static final String[] COLUMNS =
        {"ID", "물건이름", "카테고리", "발견장소", "보관장소", "발견일", "상태", "사진"};

    public MenuFrame(User user, ItemService itemService, ReturnService returnService,
                     LostItemRepository repository, Runnable onLogout) {
        this.user = user;
        this.itemService = itemService;
        this.returnService = returnService;
        this.repository = repository;
        this.onLogout = onLogout;
        buildUI();
    }

    private void buildUI() {
        setTitle("KW Lost and Found — " + user.getName() + " (" + user.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 500);
        setLocationRelativeTo(null);

        // ── 테이블 ──────────────────────────────────────────────────
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(55);

        // ── 상단 버튼 패널 ──────────────────────────────────────────
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        if (user.canRegisterItem()) {
            JButton registerBtn = new JButton("분실물 등록");
            JButton statusBtn   = new JButton("상태 변경");
            JButton returnBtn   = new JButton("반환 처리");
            topPanel.add(registerBtn);
            topPanel.add(statusBtn);
            topPanel.add(returnBtn);
            registerBtn.addActionListener(e -> showRegisterDialog());
            statusBtn.addActionListener(e -> handleChangeStatus());
            returnBtn.addActionListener(e -> handleReturn());
        }
        JButton photoBtn  = new JButton("사진 보기");
        JButton logoutBtn = new JButton("로그아웃");
        topPanel.add(photoBtn);
        topPanel.add(Box.createHorizontalStrut(16));
        topPanel.add(logoutBtn);
        photoBtn.addActionListener(e -> handleViewPhoto());
        logoutBtn.addActionListener(e -> { dispose(); onLogout.run(); });

        // ── 검색 패널 ───────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JComboBox<String> typeBox   = new JComboBox<>(new String[]{"물건 이름", "발견 장소", "카테고리"});
        JTextField        searchField = new JTextField(16);
        JButton           searchBtn = new JButton("검색");
        JButton           allBtn    = new JButton("전체 조회");
        searchPanel.add(new JLabel("검색"));
        searchPanel.add(typeBox);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(allBtn);
        searchBtn.addActionListener(e -> handleSearch(typeBox.getSelectedIndex(), searchField.getText().trim()));
        allBtn.addActionListener(e -> { searchField.setText(""); refreshTable(repository.findAll()); });
        searchField.addActionListener(e -> handleSearch(typeBox.getSelectedIndex(), searchField.getText().trim()));

        // ── 레이아웃 조립 ───────────────────────────────────────────
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel,    BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable(repository.findAll());
    }

    // ── 테이블 갱신 ─────────────────────────────────────────────────
    private void refreshTable(List<LostItem> items) {
        tableModel.setRowCount(0);
        for (LostItem item : items) {
            tableModel.addRow(new Object[]{
                item.getId(),
                item.getItemName(),
                item.getCategory()        != null ? item.getCategory().getDisplayName()        : "-",
                item.getFoundLocation()   != null ? item.getFoundLocation().getDisplayName()   : "-",
                item.getStorageLocation() != null ? item.getStorageLocation()                  : "-",
                item.getFoundDate()       != null ? item.getFoundDate().toString()             : "-",
                item.getStatus()          != null ? item.getStatus().getDisplayName()          : "-",
                item.getImagePath()       != null ? "있음" : "없음"
            });
        }
    }

    // ── 선택된 항목 가져오기 ────────────────────────────────────────
    private LostItem getSelectedItem() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "목록에서 항목을 먼저 선택해주세요.");
            return null;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        return repository.findById(id).orElse(null);
    }

    // ── 분실물 등록 다이얼로그 ──────────────────────────────────────
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "분실물 등록", true);
        dialog.setSize(400, 370);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField    = new JTextField(20);
        JTextField storageField = new JTextField(20);
        JTextField dateField    = new JTextField(LocalDate.now().toString(), 20);

        Category[] categories  = Category.values();
        String[]   catNames    = new String[categories.length];
        for (int i = 0; i < categories.length; i++) catNames[i] = categories[i].getDisplayName();
        JComboBox<String> categoryBox = new JComboBox<>(catNames);

        Location[] locations  = Location.values();
        String[]   locNames   = new String[locations.length];
        for (int i = 0; i < locations.length; i++) locNames[i] = locations[i].getDisplayName();
        JComboBox<String> locationBox = new JComboBox<>(locNames);

        JLabel   photoLabel = new JLabel("선택 안 됨");
        JButton  photoBtn   = new JButton("사진 선택");
        String[] pathHolder = {null};
        photoBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("사진 파일 선택");
            chooser.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif"));
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                pathHolder[0] = chooser.getSelectedFile().getAbsolutePath();
                photoLabel.setText(chooser.getSelectedFile().getName());
            }
        });

        int r = 0;
        addRow(panel, gbc, r++, "물건 이름", nameField);
        addRow(panel, gbc, r++, "카테고리",  categoryBox);
        addRow(panel, gbc, r++, "발견 장소", locationBox);
        addRow(panel, gbc, r++, "보관 장소", storageField);
        addRow(panel, gbc, r++, "발견 날짜", dateField);

        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("사진"), gbc);
        JPanel photoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        photoRow.add(photoBtn);
        photoRow.add(photoLabel);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(photoRow, gbc);

        JButton confirmBtn = new JButton("등록");
        JButton cancelBtn  = new JButton("취소");
        JPanel  btnPanel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.add(confirmBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = ++r; gbc.gridwidth = 2; gbc.weightx = 0;
        panel.add(btnPanel, gbc);

        confirmBtn.addActionListener(e -> {
            String itemName = nameField.getText().trim();
            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "물건 이름을 입력해주세요.");
                return;
            }
            LocalDate foundDate;
            try {
                foundDate = LocalDate.parse(dateField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "날짜 형식이 올바르지 않습니다.\n예: 2025-05-01");
                return;
            }
            LostItem item = itemService.registerItem(user, itemName,
                categories[categoryBox.getSelectedIndex()],
                locations[locationBox.getSelectedIndex()],
                storageField.getText().trim(), foundDate);
            if (item != null && pathHolder[0] != null) {
                item.setImagePath(pathHolder[0]);
            }
            dialog.dispose();
            refreshTable(repository.findAll());
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ── 상태 변경 ────────────────────────────────────────────────────
    private void handleChangeStatus() {
        LostItem item = getSelectedItem();
        if (item == null) return;

        ItemStatus[] statuses   = ItemStatus.values();
        String[]     statusNames = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) statusNames[i] = statuses[i].getDisplayName();

        String selected = (String) JOptionPane.showInputDialog(this,
            "변경할 상태를 선택하세요:", "상태 변경",
            JOptionPane.QUESTION_MESSAGE, null, statusNames,
            item.getStatus().getDisplayName());
        if (selected == null) return;

        for (ItemStatus s : statuses) {
            if (s.getDisplayName().equals(selected)) {
                boolean ok = itemService.changeStatus(user, item.getId(), s);
                if (!ok) JOptionPane.showMessageDialog(this, "상태 변경에 실패했습니다.");
                break;
            }
        }
        refreshTable(repository.findAll());
    }

    // ── 반환 처리 ────────────────────────────────────────────────────
    private void handleReturn() {
        LostItem item = getSelectedItem();
        if (item == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "[" + item.getId() + "] " + item.getItemName() + "\n반환 처리하시겠습니까?",
            "반환 처리", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = returnService.returnItem(user, item.getId());
        if (!ok) JOptionPane.showMessageDialog(this, "반환 처리에 실패했습니다.");
        refreshTable(repository.findAll());
    }

    // ── 검색 ─────────────────────────────────────────────────────────
    private void handleSearch(int typeIndex, String keyword) {
        if (keyword.isEmpty()) {
            refreshTable(repository.findAll());
            return;
        }
        SearchStrategy strategy;
        switch (typeIndex) {
            case 0:  strategy = new NameSearchStrategy();     break;
            case 1:  strategy = new LocationSearchStrategy(); break;
            default: strategy = new CategorySearchStrategy(); break;
        }
        refreshTable(repository.search(strategy, keyword));
    }

    // ── 사진 보기 ────────────────────────────────────────────────────
    private void handleViewPhoto() {
        LostItem item = getSelectedItem();
        if (item == null) return;

        if (item.getImagePath() == null) {
            JOptionPane.showMessageDialog(this, "등록된 사진이 없습니다.");
            return;
        }
        if (!new File(item.getImagePath()).exists()) {
            JOptionPane.showMessageDialog(this, "이미지 파일을 찾을 수 없습니다.");
            return;
        }

        ImageIcon icon   = new ImageIcon(item.getImagePath());
        Image     scaled = icon.getImage().getScaledInstance(480, -1, Image.SCALE_SMOOTH);
        JLabel    imgLbl  = new JLabel(new ImageIcon(scaled));
        JLabel    infoLbl = new JLabel(item.toString(), SwingConstants.CENTER);
        infoLbl.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel photoPanel = new JPanel(new BorderLayout(0, 4));
        photoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        photoPanel.add(imgLbl,  BorderLayout.CENTER);
        photoPanel.add(infoLbl, BorderLayout.SOUTH);

        JDialog photoDialog = new JDialog(this, "[" + item.getId() + "] " + item.getItemName(), false);
        photoDialog.add(photoPanel);
        photoDialog.pack();
        photoDialog.setLocationRelativeTo(this);
        photoDialog.setVisible(true);
    }
}
