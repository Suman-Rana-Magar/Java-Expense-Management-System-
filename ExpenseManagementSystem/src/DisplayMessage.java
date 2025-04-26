import javax.swing.*;
import java.awt.*;

public class DisplayMessage {
    private DisplayMessage() {
    }

    public static void errorMessage(Frame frame, String message, String errorTopic) {
        JOptionPane.showMessageDialog(frame, message, errorTopic, JOptionPane.WARNING_MESSAGE);
    }

    public static void successMessage(Frame frame, String message, String successTopic) {
        JOptionPane.showMessageDialog(frame, message, successTopic, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int confirmMessage(Frame frame, String message, String confirmation) {
        int confirmResult = JOptionPane.showConfirmDialog(frame, message, confirmation, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return confirmResult;
    }
}
