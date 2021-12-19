/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw3;

import java.awt.FlowLayout;
import java.awt.Desktop;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileBrowser extends javax.swing.JFrame{

    String stringPath = System.getProperty("user.home");
    File fileCopiedOrCutted = null,FolderToMoveTo=null,destination=null;
    private FileListCellRenderer Renderer = new FileListCellRenderer();
    File [] ContainsPrivateFiles,NotContainsPrivateFiles, Files;
    JPopupMenu popup = new JPopupMenu();
    boolean CutClicked = false, CopyClicked = false,PasteClicked = false,linuxSystem = false, PasteEnabled = false;
    
    
    public FileBrowser() {
             
        initComponents();
        setTitle("File Browser");
        Search.setVisible(false);
        TextField.setVisible(false);
        Edit.setEnabled(false);
        if(stringPath.charAt(2) == '/'){
            linuxSystem = true;
        }
        setLocationRelativeTo(null);
        fileList.addMouseListener(new MouseAdapter(){
          @Override
          public void mouseClicked(MouseEvent e) {
              
              if(SwingUtilities.isRightMouseButton(e)){
                    popup.removeAll();
                    fileList.remove(popup);
                    fileList.setSelectedIndex(getRow(e.getPoint()));
                    File newFile = fileList.getSelectedValue();
                    FolderToMoveTo = fileList.getSelectedValue();
                    if(FolderToMoveTo == null){
                        FolderToMoveTo = new File(stringPath);
                    }
                    JMenuItem Cut = new JMenuItem("Cut");
                    Cut.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            CutClicked = true;
                            fileCopiedOrCutted = newFile;
                            
                        }
                    });
                    popup.add(Cut);
                    
                    JMenuItem Copy = new JMenuItem("Copy");
                    Copy.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            CopyClicked = true;
                            fileCopiedOrCutted = newFile;
                        }
                    });
                    popup.add(Copy);
                    
                    JMenuItem Paste = new JMenuItem("Paste");
                    Paste.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            int i;
                            InputStream is = null;
                            OutputStream os = null;
                           
                            if(!linuxSystem){
                                if(FolderToMoveTo.isDirectory()){
                                    destination = new File(FolderToMoveTo.getPath() + "\\" + fileCopiedOrCutted.getName());
                                    
                                }
                                else {
                                    destination = new File(stringPath + "\\" + fileCopiedOrCutted.getName());
                                }
                                if(fileCopiedOrCutted.isDirectory()){
                                    boolean isItDone = destination.mkdir();
                                    
                                    if(!isItDone){
                                        System.out.println("not Done");
                                    }
                                }
                            }
                            else {
                                if(FolderToMoveTo.isDirectory()){
                                    destination = new File(FolderToMoveTo.getPath() + "/" + fileCopiedOrCutted.getName());
                                }
                                else {
                                    destination = new File(stringPath + "/" + fileCopiedOrCutted.getName());
                                }
                                if(fileCopiedOrCutted.isDirectory()){
                                    boolean isItDone = destination.mkdir();
                                    
                                    if(!isItDone){
                                        System.out.println("not Done");
                                    }
                                }
                            }
                            if(destination.exists()){
                                JDialog fileReplaceQuestion = new JDialog();
                                JLabel fileWithSameNameLabel = new JLabel();
                                JButton YesReplaceIt = new JButton("YES");
                                JButton NotReplaceIt = new JButton("NO");
                                
                                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                                WindowListener listener = new WindowAdapter() {
                                    public void windowClosing(WindowEvent evt) {
                                       enable();
                                    }
                                 };

                                fileReplaceQuestion.addWindowListener(listener);
                                disable();
                                fileWithSameNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
                                fileWithSameNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                                fileWithSameNameLabel.setText("A file with the same name already exists. Do you want to replace it?");
                                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(fileReplaceQuestion.getContentPane());
                                fileReplaceQuestion.getContentPane().setLayout(layout);
                                layout.setHorizontalGroup(
                                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fileWithSameNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(108, 108, 108)
                                        .addComponent(YesReplaceIt, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(51, 51, 51)
                                        .addComponent(NotReplaceIt, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                                layout.setVerticalGroup(
                                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fileWithSameNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(NotReplaceIt, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                            .addComponent(YesReplaceIt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(0, 21, Short.MAX_VALUE))
                                );

                                fileReplaceQuestion.setLayout(layout);
                                fileReplaceQuestion.setResizable(false);         
                                fileReplaceQuestion.pack();
                                fileReplaceQuestion.setVisible(true);
                                fileReplaceQuestion.setLocationRelativeTo(null);
                                fileReplaceQuestion.getRootPane().setDefaultButton(NotReplaceIt);
                                
                                NotReplaceIt.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent ev) {
                                        fileReplaceQuestion.dispose();
                                        enable();
                                    }
                                });
                                
                                YesReplaceIt.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent ev) {
                                        enable();
                                        InputStream is = null;
                                        OutputStream os = null;
                                        fileReplaceQuestion.dispose();
                                        if(destination.isDirectory()){
                                            deleteFolder(destination);
                                        }
                                        else {
                                            boolean isItDeleted = destination.delete();
                                            if(isItDeleted){
                                                System.out.println("Deleted succesfully in for");
                                            }
                                            else {
                                                JDialog CannotDeleteFile = new JDialog();
                                                System.out.println("something did not work like it was supposed to");
                                            }
                                        }
                                        
                                             
                                        if(fileCopiedOrCutted.isDirectory()){

                                            copyFolder(fileCopiedOrCutted, destination);

                                            if(CutClicked){
                                                deleteFolder(fileCopiedOrCutted);

                                                CutClicked = false;
                                            }
                                            Breadcrumb.setText("");
                                            FilesToPanel();
                                            return;                            
                                        }
                                        
                                        try {
                                            is = new FileInputStream(fileCopiedOrCutted);
                                            os = new FileOutputStream(destination);
                                            byte[] buffer = new byte[1024];
                                            int length;
                                            while ((length = is.read(buffer)) > 0) {
                                                os.write(buffer, 0, length);
                                            }   
                                            is.close();
                                            os.close();
                                        } catch (FileNotFoundException ex) {
                                            System.out.println("something went wrong " + ex.toString());
                                        } catch (IOException ex) {
                                           System.out.println("something went wrong");
                                        }

                                        Breadcrumb.setText("");
                                        if(CutClicked){
                                            boolean isItDeleted = fileCopiedOrCutted.delete();
                                            if(!isItDeleted){
                                                System.out.println("failed");
                                            }
                                            CutClicked = false;
                                        }
                                        FilesToPanel();
                                    }
                                });

                            }
                            
                            if(fileCopiedOrCutted.isDirectory()){

                                copyFolder(fileCopiedOrCutted, destination);
                                
                                if(CutClicked){
                                    deleteFolder(fileCopiedOrCutted);
                                    CutClicked = false;
                                }
                                Breadcrumb.setText("");
                                FilesToPanel();
                                return;                            
                            }
                            try {
                                is = new FileInputStream(fileCopiedOrCutted);
                                os = new FileOutputStream(destination);
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = is.read(buffer)) > 0) {
                                    os.write(buffer, 0, length);
                                }   
                                is.close();
                                os.close();
                            } catch (FileNotFoundException ex) {
                                System.out.println("something went wrong " + ex.toString());
                            } catch (IOException ex) {
                               System.out.println("something went wrong");
                            }
                            Breadcrumb.setText("");
                            if(CutClicked){
                                boolean isItDeleted = fileCopiedOrCutted.delete();
                                if(!isItDeleted){
                                    System.out.println("failed");
                                }                               
                                CutClicked = false;
                            }
                            FilesToPanel();
                        }
                    });

                    if(fileCopiedOrCutted != null){
                        if(!stringPath.equals(fileCopiedOrCutted.getParent())){
                            PasteEnabled = true;
                        }
                        else {
                            PasteEnabled = false;
                        }
                    }
                    
                    if((CopyClicked | CutClicked) & PasteEnabled){
                         Paste.setEnabled(true);
                    }
                    else {
                        Paste.setEnabled(false);
                    }
                    popup.add(Paste);
                    
                    JMenuItem Rename = new JMenuItem("Rename");
                    Rename.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {

                            JDialog Rename = new JDialog();
                            fileList.setSelectedIndex(getRow(e.getPoint()));
                            File renameFile = fileList.getSelectedValue();
                            
                            WindowListener listener = new WindowAdapter() {
                                public void windowClosing(WindowEvent evt) {
                                   enable();
                                }
                             };
                            
                            Rename.addWindowListener(listener);
                            
                            
                            
                            JTextField RenameField = new JTextField(renameFile.getName());
                            JButton SetName = new JButton("Set Name");
                            disable();
                            SetName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ev) {
                                    File newNameFile = null;
                                    if(!linuxSystem){
                                        newNameFile = new File(renameFile.getParent() + "\\" + RenameField.getText());
                                    }
                                    else {
                                        newNameFile = new File(renameFile.getParent() + "/" + RenameField.getText());
                                    }
                                            
                                    if (newNameFile.exists()){
                                        try {
                                            throw new java.io.IOException("file exists");
                                        } catch (IOException ex) {
                                            Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                        
                                    boolean success = renameFile.renameTo(newNameFile);

                                    if (!success) {
                                        System.out.println("FILE FILED TO RENAME");
                                    }
                                    Rename.dispose();
                                    enable();
                                    Breadcrumb.setText("");
                                    FilesToPanel();
                                }
                            });
                                    
                            Rename.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(Rename.getContentPane());
                            Rename.getContentPane().setLayout(layout);
                            layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(RenameField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(SetName, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                            layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(24, 24, 24)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(RenameField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SetName, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap(31, Short.MAX_VALUE))
                            );
                            Rename.setLayout(layout);
                            Rename.setResizable(false);         
                            Rename.pack();
                            Rename.setVisible(true);
                            Rename.setLocationRelativeTo(null);
                            Rename.getRootPane().setDefaultButton(SetName);
                            Breadcrumb.setText("");
                            FilesToPanel();

                        }
                    });
                    popup.add(Rename);
                    
                    JMenuItem Delete = new JMenuItem("Delete");
                    Delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            
                            fileList.setSelectedIndex(getRow(e.getPoint()));
                            File deleteFile = fileList.getSelectedValue();

                            JDialog question = new JDialog();
                            JLabel AreYouSure = new JLabel();
                            JButton YesButton = new JButton("Yes");
                            JButton NoButton = new JButton("No");
                            
                            question.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            WindowListener listener = new WindowAdapter() {
                                public void windowClosing(WindowEvent evt) {
                                   enable();
                                }
                             };
                            
                            question.addWindowListener(listener);

                            AreYouSure.setText("Are you sure about deleting this file?");
                            
                            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(question.getContentPane());
                            question.getContentPane().setLayout(layout);
                            layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(YesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(NoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(AreYouSure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addContainerGap())
                            );
                            layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(AreYouSure, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(YesButton)
                                        .addComponent(NoButton))
                                    .addContainerGap(20, Short.MAX_VALUE))
                            );
                            question.setLayout(layout);
                            question.setResizable(false);         
                            question.pack();
                            question.setLocationRelativeTo(null);
                            question.setVisible(true);
                            question.getRootPane().setDefaultButton(NoButton);
                            disable();
                            YesButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ev) {
                                    if(deleteFile.isDirectory()){
                                        deleteFolder(deleteFile);
                                        question.dispose();
                                        enable();
                                        Breadcrumb.setText("");
                                        FilesToPanel();
                                        return;
                                    }
                                    
                                    boolean isItDeleted = deleteFile.delete();
                                    if(!isItDeleted){
                                        JDialog CannotDeleteFile = new JDialog();
                                        System.out.println("something did not work like it was supposed to");
                                    }
                                    question.dispose();
                                    enable();
                                    Breadcrumb.setText("");
                                    FilesToPanel();
                                    
                                    
                                }
                            });
                            
                            NoButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ev) {
                                    enable();
                                    question.dispose();
                                }
                            });                           
                        }
                    });
                    popup.add(Delete);
                    
                    JMenuItem AddToFavorites = new JMenuItem("AddToFavorites");
                    AddToFavorites.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            
                        }
                    });
                    popup.add(AddToFavorites);
                    
                    JMenuItem Properties = new JMenuItem("Properties");
                    Properties.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            
                            fileList.setSelectedIndex(getRow(e.getPoint()));
                            File propertiesFile = fileList.getSelectedValue();
                            JDialog propertiesDialog = new JDialog();
                            JLabel nameLabel = new JLabel();
                            JLabel pathLabel = new JLabel();
                            JLabel sizeLabel = new JLabel();
                            JTextField nametext = new JTextField();
                            JTextField pathText = new JTextField();
                            JTextField sizeText = new JTextField();
                            propertiesDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                            propertiesDialog.setTitle("Properties");
                            nameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                            nameLabel.setText("Name:");

                            pathLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                            pathLabel.setText("Path:");

                            sizeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
                            sizeLabel.setText("Size:");

                            nametext.setEditable(false);

                            pathText.setEditable(false);

                            sizeText.setEditable(false);
                            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(propertiesDialog.getContentPane());
                            propertiesDialog.getContentPane().setLayout(layout);
                            layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(nametext)
                                                .addComponent(pathText)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(sizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(sizeText, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
                                    .addContainerGap())
                            );
                            layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nametext, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(pathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pathText, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(sizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sizeText, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                            propertiesDialog.setLayout(layout);
                            nametext.setText(propertiesFile.getName());
                            pathText.setText(propertiesFile.getPath());
                            if(propertiesFile.isDirectory()){
                                long size = sizeOfFolder(propertiesFile);
                                sizeText.setText(String.valueOf(size) + " bytes");
                            }
                            else {
                                long size = propertiesFile.length();
                                sizeText.setText(String.valueOf(size) + " bytes");
                            }
                            
                            propertiesDialog.setResizable(false);         
                            propertiesDialog.pack();
                            propertiesDialog.setLocationRelativeTo(null);
                            propertiesDialog.setVisible(true);
                            
                        }
                    });
                    popup.add(Properties);
                    fileList.setComponentPopupMenu(popup);                    
          
              }
              if(SwingUtilities.isLeftMouseButton(e)){
                  popup.removeAll();
                  fileList.remove(popup);
              }
              
              if(e.getClickCount() == 2 & SwingUtilities.isLeftMouseButton(e)){
                    File newFile = fileList.getSelectedValue();
                    if(newFile == null){
                        return;
                    }
                    
                    if(newFile.canExecute() & (!newFile.isDirectory())){
                        try {
                              Desktop.getDesktop().open(newFile);
                            } catch (IOException ex) {
                                System.out.println("something went wrong");
                            }
                        return;
                    }
                
                    if(newFile.canRead() & newFile.list() != null){
                        if(newFile.isDirectory()){   
                            stringPath = newFile.getPath();
                            Breadcrumb.setText("");
                            HiddenFolders.setSelected(false);
                            FilesToPanel();

                        }
                    }
              } 
              
          }
        });   
        FilesToPanel();
    }
    
    private int getRow(Point point){
        return fileList.locationToIndex(point);
    }
    
    public long sizeOfFolder(File files){
        
        File [] filesOfFolder = files.listFiles();
        long size = 0;
        if(files.isDirectory()){
            for(File file : filesOfFolder){
                if(file.isDirectory()){
            
                    size = size + sizeOfFolder(file);
                }
                else {
                    size = size + file.length();
                    
                }
            }
             
        }
        return size;
    }
    
    
    public void deleteFolder(File files){

        File [] filesOfFolder = files.listFiles();
        
        if(files.isDirectory()){
            for(File file : filesOfFolder){
                if(file.isDirectory()){
                    deleteFolder(file);
                }
                else {
                    boolean isItDeleted = file.delete();
                    if(!isItDeleted){
                        JDialog CannotDeleteFile = new JDialog();
                        System.out.println("something did not work like it was supposed to");
                    }
                }
                
            }
            
            boolean isItDeleted = files.delete();
            if(!isItDeleted){
                JDialog CannotDeleteFile = new JDialog();
                System.out.println("something did not work like it was supposed to");
            }
        
        }
    
    
    }
    
    public void copyFolder(File files, File destination){
        
        File [] filesOfFolder = files.listFiles();
        InputStream is = null;
        OutputStream os = null;
        
        if(files.isDirectory()){
            for(File file : filesOfFolder){
                if(file.isDirectory()){
                    copyFolder(file, destination);
                }
                else {
                        try {
                        is = new FileInputStream(file);
                        os = new FileOutputStream(destination + "\\" + file.getName());
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }   
                        is.close();
                        os.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("something went wrong " + ex.toString());
                        } catch (IOException ex) {
                            System.out.println("something went wrong");
                        }
                }
                
            }
            
            try {
                is = new FileInputStream(files);
                os = new FileOutputStream(destination);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                       os.write(buffer, 0, length);
                    }   
                is.close();
                os.close();
            } catch (FileNotFoundException ex) {
                System.out.println("something went wrong " + ex.toString());
            } catch (IOException ex) {
                System.out.println("something went wrong");
            }
            
            
            
        }    
    }
    
    public final void FilesToPanel(){
       
       int i,j,length,numberOfFolders=0,lengthOfContainsPrivateFiles = 0,lengthOfNotContainsPrivateFiles = 0,
               posOfContainsPrivateFiles = 0,posOfNotContainsPrivateFiles = 0;
       
       File f = new File(stringPath);
       File temp;
       String strI,strJ;
       
       Files = f.listFiles();

       if(f.list() == null){
           return;
       }
       length = f.list().length;

       pathToBreadcrumb();
       
       for(i=0;i<length;i++){
           if(Files[i].isDirectory()){
               numberOfFolders++;
           }
       }
       Arrays.sort(Files);
       
       for(i=0;i<length;i++){
           for(j=i+1;j<length;j++){
                strI = Files[i].getName();
                strJ = Files[j].getName();
                if(strI.charAt(0) == '.'){
                    strI = strI.substring(1);
                }
                if(strJ.charAt(0) == '.'){
                    strJ = strJ.substring(1);
                }        
                if(strI.compareTo(strJ) > 0){
                    temp = Files[j];
                    Files[j] = Files[i];
                    Files[i] = temp;
                }
           }
       }
       
       File [] directoryFiles = new File[numberOfFolders];
       File [] otherFiles = new File[length - numberOfFolders];
       int k =0;
       j=0;
       
       for(i=0;i<length;i++){
           if(Files[i].isDirectory()){
               directoryFiles[j] = Files[i];
               j++;     
           }
           else {
               otherFiles[k] = Files[i];
               k++;
           }
       
       }
       k=j=0;
       for(i=0;i<length;i++){
           if(i<numberOfFolders){
               Files[i] = directoryFiles[k];
               k++;
           }
           else {
               Files[i] = otherFiles[j];
               j++;
           }
       }
       
       for(i=0;i<length;i++){
           if(Files[i].getName().charAt(0) == '.'){
               lengthOfContainsPrivateFiles++;
               
           }
           else {
               lengthOfContainsPrivateFiles++;
               lengthOfNotContainsPrivateFiles++;
           }
           
       }
       
       ContainsPrivateFiles = new File[lengthOfContainsPrivateFiles++];
       NotContainsPrivateFiles = new File[lengthOfNotContainsPrivateFiles++];
       
       posOfContainsPrivateFiles = posOfNotContainsPrivateFiles = 0;
       for(i=0;i<length;i++){
           if(Files[i].getName().charAt(0) == '.'){
               ContainsPrivateFiles[posOfContainsPrivateFiles] = Files[i];
               posOfContainsPrivateFiles++;
               
           }
           else {
               ContainsPrivateFiles[posOfContainsPrivateFiles] = Files[i];
               posOfContainsPrivateFiles++;
               NotContainsPrivateFiles[posOfNotContainsPrivateFiles] = Files[i];
               posOfNotContainsPrivateFiles++;
           }
           
       }
       if(HiddenFolders.isSelected()){
            fileList.setListData(Files);
       }
       else {
           fileList.setListData(NotContainsPrivateFiles);
       }
       fileList.setSelectedIndex(-1);
      
       fileList.setVisibleRowCount(-1);

       fileList.setFixedCellWidth(100);
      
       fileList.setCellRenderer(Renderer);

       fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

       fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       
       fileList.putClientProperty("List.isFileList", Boolean.TRUE);
                   
    }
    
    public void pathToBreadcrumb(){
        
        int i,length,startOfFileName=0;
        String seperator = " > ", lastFileName="";
        Breadcrumb.setLayout(new FlowLayout());
        
        length = stringPath.length();
        
        if(stringPath.equals("C:")){
            JLabel l = new JLabel("C:");
            Breadcrumb.insertComponent(l);
            return;
        }
        
        for(i=0;i<length;i++){
            if(stringPath.charAt(i) == '/'  || stringPath.charAt(i) == '\\'){
                 
                JLabel l = new JLabel(stringPath.substring(startOfFileName,i));
                l.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                l.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent me)
                    {
                        String fileToGo="",newPath="";
                        int i,posOfFileToGo=0,endOfNewPath=0;
                        
                        String [] filesNames = stringPath.split("\\\\");
                        fileToGo = l.getText();
                        for(i=0;i<filesNames.length;i++){
                            if(fileToGo.equals(filesNames[i])){
                                posOfFileToGo = i;
                            }
                        }
                        
                        fileToGo = l.getText();
                        
                        for(i=0;i<=posOfFileToGo;i++){
                            endOfNewPath = endOfNewPath + filesNames[i].length() + 1;
                        }
                        
                        newPath = stringPath.substring(0, endOfNewPath-1);
                        
                        if(newPath.equals("C:")){
                            newPath = "C:\\";
                        } 
                        stringPath = newPath;
                        Breadcrumb.setText("");
                        HiddenFolders.setSelected(false);
                        FilesToPanel();
                    }
                 });
                
                Breadcrumb.insertComponent(l);
                 
                JLabel seperatorLabel = new JLabel( seperator );
                Breadcrumb.insertComponent(seperatorLabel);
                lastFileName = stringPath.substring(i+1);
                startOfFileName = i + 1;
            }
            
        }
        JLabel l = new JLabel(lastFileName);
        Breadcrumb.insertComponent(l);
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Search = new javax.swing.JButton();
        TextField = new javax.swing.JTextField();
        fileScrollPane = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        favoritesScrolPane = new javax.swing.JScrollPane();
        favoritesList = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Breadcrumb = new javax.swing.JTextPane();
        MenuBar = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        NewWindow = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();
        Edit = new javax.swing.JMenu();
        Cut = new javax.swing.JMenuItem();
        Copy = new javax.swing.JMenuItem();
        Paste = new javax.swing.JMenuItem();
        Rename = new javax.swing.JMenuItem();
        Delete = new javax.swing.JMenuItem();
        AddToFavourites = new javax.swing.JMenuItem();
        Properties = new javax.swing.JMenuItem();
        View = new javax.swing.JMenu();
        SearchMenuItem = new javax.swing.JCheckBoxMenuItem();
        HiddenFolders = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Search.setText("Search");

        fileScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        fileScrollPane.setViewportView(fileList);

        favoritesScrolPane.setViewportView(favoritesList);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        Breadcrumb.setEditable(false);
        jScrollPane1.setViewportView(Breadcrumb);

        File.setText("File");

        NewWindow.setText("New Window");
        NewWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewWindowActionPerformed(evt);
            }
        });
        File.add(NewWindow);

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        File.add(Exit);

        MenuBar.add(File);

        Edit.setText("Edit");

        Cut.setText("Cut");
        Edit.add(Cut);

        Copy.setText("Copy");
        Edit.add(Copy);

        Paste.setText("Paste");
        Edit.add(Paste);

        Rename.setText("Rename");
        Edit.add(Rename);

        Delete.setText("Delete");
        Edit.add(Delete);

        AddToFavourites.setText("Add To Favourites");
        Edit.add(AddToFavourites);

        Properties.setText("Properties");
        Edit.add(Properties);

        MenuBar.add(Edit);

        View.setText("View");

        SearchMenuItem.setText("Search");
        SearchMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchMenuItemActionPerformed(evt);
            }
        });
        View.add(SearchMenuItem);

        HiddenFolders.setText("Hidden Files/Folders");
        HiddenFolders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HiddenFoldersActionPerformed(evt);
            }
        });
        View.add(HiddenFolders);

        MenuBar.add(View);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(favoritesScrolPane, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(TextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fileScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TextField)
                            .addComponent(Search))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(fileScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                    .addComponent(favoritesScrolPane))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        dispose();
    }//GEN-LAST:event_ExitActionPerformed

    private void SearchMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchMenuItemActionPerformed
        // TODO add your handling code here:
        if(SearchMenuItem.getState()){
            Search.setVisible(true);
            TextField.setVisible(true);
        }
        else{
            Search.setVisible(false);
            TextField.setVisible(false);
                    
        }
    }//GEN-LAST:event_SearchMenuItemActionPerformed


    
    
    private void NewWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewWindowActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileBrowser().setVisible(true);
            }
        });
    }//GEN-LAST:event_NewWindowActionPerformed

    private void HiddenFoldersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HiddenFoldersActionPerformed
        // TODO add your handling code here:
        Breadcrumb.setText("");
         FilesToPanel();
    }//GEN-LAST:event_HiddenFoldersActionPerformed
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileBrowser().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddToFavourites;
    private javax.swing.JTextPane Breadcrumb;
    private javax.swing.JMenuItem Copy;
    private javax.swing.JMenuItem Cut;
    private javax.swing.JMenuItem Delete;
    private javax.swing.JMenu Edit;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JMenu File;
    private javax.swing.JCheckBoxMenuItem HiddenFolders;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem NewWindow;
    private javax.swing.JMenuItem Paste;
    private javax.swing.JMenuItem Properties;
    private javax.swing.JMenuItem Rename;
    private javax.swing.JButton Search;
    private javax.swing.JCheckBoxMenuItem SearchMenuItem;
    private javax.swing.JTextField TextField;
    private javax.swing.JMenu View;
    private javax.swing.JList<String> favoritesList;
    private javax.swing.JScrollPane favoritesScrolPane;
    private javax.swing.JList<File> fileList;
    private javax.swing.JScrollPane fileScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
