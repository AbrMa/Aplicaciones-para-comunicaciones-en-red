package Directory;

import java.io.Serializable;
import javax.swing.JTree;

public class Directory implements Serializable{
    private static final long serialVersionUID = 1L;
    private JTree directory;
    
    public Directory(JTree directory){
        this.directory = directory;
    }

    public JTree getDirectory(){
        return this.directory;
    }

    public void setDirectory(JTree directory){
        this.directory = directory;
    }
}
