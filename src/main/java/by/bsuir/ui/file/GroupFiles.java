package by.bsuir.ui.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

class GroupFiles {

    private File[] files;

    public GroupFiles(File file) throws Exception {
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            ArrayList<File> list = new ArrayList<>();

            String fileName;
            while ((fileName = input.readLine()) != null && list.size() != 16) {
                File fileFromGroup = new File(file.getParent(), fileName);
                if (fileFromGroup.exists()) {
                    list.add(fileFromGroup);
                }
            }

            if (list.size() == 0) {
                throw new RuntimeException("Files was not found");
            }

            this.files = new File[list.size()];
            this.files = list.toArray(this.files);
        }
    }

    public File[] getFiles() {
        return this.files;
    }
}
