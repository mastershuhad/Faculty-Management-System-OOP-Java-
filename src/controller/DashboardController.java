package controller;

import view.DashboardFrame;
import view.AuthFrame;

public class DashboardController {
    private DashboardFrame view;

    public DashboardController(DashboardFrame view) {
        this.view = view;
        initController();
    }

    private void initController() {
        // Tell the logout button what to do when clicked
        view.getLogoutButton().addActionListener(e -> logout());
    }

    private void logout() {
        // 1. Hide/destroy current dashboard view
        view.dispose();
        
        // 2. Re-launch the Auth/Login screen
        AuthFrame authFrame = new AuthFrame();
        new AuthController(authFrame);
        authFrame.setVisible(true);
    }
}