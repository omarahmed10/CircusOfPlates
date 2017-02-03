package view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CirFileFilter extends FileFilter {
    Utils util;

    public CirFileFilter() {
        util = new Utils();
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String extension = util.getFileExtension(file.getName());
        if (extension == null) {
            return false;
        } else if (extension.equals(".cir")) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Circus datebase files(*.cir)";
    }

}