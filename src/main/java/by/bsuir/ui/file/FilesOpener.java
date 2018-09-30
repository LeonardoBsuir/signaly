package by.bsuir.ui.file;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import java.io.File;

public class FilesOpener {

    private final Component parentComponent;

    private final FileFilter binFilter = new BinFileFilter();
    private final FileFilter txtFilter = new TxtFileFilter();
    private FileFilter currentFilter;

    private File[] loadedFiles;
    private String currentDirectory = "D:\\play\\АЦОС_Маг_14\\Данные";

    public FilesOpener(Component parent) {
        this.parentComponent = parent;
    }

    public boolean openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (this.currentFilter == binFilter) {
            fileChooser.addChoosableFileFilter(this.binFilter);
            fileChooser.addChoosableFileFilter(this.txtFilter);
        } else {
            fileChooser.addChoosableFileFilter(this.txtFilter);
            fileChooser.addChoosableFileFilter(this.binFilter);
        }
        fileChooser.setFileFilter(this.currentFilter);

        if (this.currentDirectory != null) {
            fileChooser.setCurrentDirectory(new File(this.currentDirectory));
        }

        if (fileChooser.showOpenDialog(parentComponent) == 0) {
            FileFilter curFilter = fileChooser.getFileFilter();
            if (curFilter == this.txtFilter) {
                this.currentFilter = txtFilter;
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    GroupFiles groupFiles = new GroupFiles(selectedFile);
                    this.loadedFiles = groupFiles.getFiles();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.currentFilter = binFilter;
                this.loadedFiles = fileChooser.getSelectedFiles();
            }

            this.currentDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
            return true;
        } else {
            return false;
        }
    }

    public File[] getFiles() {
        return this.loadedFiles;
    }

    private class BinFileFilter extends FileFilter {
        public String getDescription() {
            return "Data files, *.bin";
        }

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else {
                String name = file.getName();
                int i = name.lastIndexOf('.');
                if (i > 0 && i < name.length() - 1) {
                    String extension = name.substring(i + 1).toLowerCase();
                    if ("bin".equals(extension)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    private class TxtFileFilter extends FileFilter {
        public String getDescription() {
            return "Group file, *.txt";
        }

        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            } else {
                String name = file.getName();
                int i = name.lastIndexOf('.');
                if (i > 0 && i < name.length() - 1) {
                    String extension = name.substring(i + 1).toLowerCase();
                    if ("txt".equals(extension)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
