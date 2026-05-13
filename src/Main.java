import gui.LoginFrame;
import repository.LostItemRepository;
import service.ItemService;
import service.ReturnService;
import user.UserRepository;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        LostItemRepository repository = new LostItemRepository();
        ItemService itemService = new ItemService(repository);
        ReturnService returnService = new ReturnService(itemService);
        UserRepository userRepository = new UserRepository();

        SwingUtilities.invokeLater(() ->
            new LoginFrame(userRepository, itemService, returnService, repository).setVisible(true)
        );
    }
}
