import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class TreeSample {
  public static void main(String args[]) {
    JFrame f = new JFrame("JTree Sample");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = f.getContentPane();
    JTree tree = new JTree();
    JScrollPane scrollPane = new JScrollPane(tree);
    content.add(scrollPane, BorderLayout.CENTER);
    f.setSize(300, 200);
    f.setVisible(true);
  }
}