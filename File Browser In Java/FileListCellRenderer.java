package ce326.hw3;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

class FileListCellRenderer extends JPanel implements ListCellRenderer{
    private static final long serialVersionUID = -7799441088157759804L;
    
    private FileSystemView fileSystemView;
    public JLabel label;
    int outOfBounds=0;
    
    //ImageView thumbField;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = Color.LIGHT_GRAY;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;

    public FileListCellRenderer() {
        //thumbField = new ImageView();
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean selected,boolean expanded) {

        File file = (File)value;
        String pngName = "";
        String[] icons = {"audio", "bmp", "doc", "docx", "giff", "gz", "htm", "html", "image", "jpeg", "jpg", "mp3", "ods", "odt",
                            "ogg", "pdf", "png", "tar", "tgz", "txt", "video", "wav", "xlsx", "xlx", "xml", "zip"};
        boolean found = false;          
               
        String fileName = file.getName();
        int dot = fileName.lastIndexOf('.'), i;
        String base = (dot == -1) ? fileName : fileName.substring(0, dot);
        String extension = (dot == -1) ? "" : fileName.substring(dot+1);
        
        if(file.isDirectory()){
            pngName = "folder";
        }
        else {
            pngName = extension;
            
            for(i=0;i<icons.length;i++){
                if(icons[i].equals(pngName)){
                    
                    found = true;
                     break;
                }   
            }
            if(!found){
                pngName = "question";
            }
        }
        
        ImageIcon icon = new ImageIcon("./icons/" + pngName + ".png");
        label.setIcon(icon);
        label.setText(fileSystemView.getSystemDisplayName(file));
        label.setFont(new Font("Ariel", Font.BOLD, 15));
        
        label.setToolTipText(file.getName());
        
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
 
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        
        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
            
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }
        return label;        
    }
}
