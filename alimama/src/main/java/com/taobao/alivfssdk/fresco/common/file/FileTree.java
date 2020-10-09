package com.taobao.alivfssdk.fresco.common.file;

import java.io.File;
import java.io.IOException;

public class FileTree {
    public static void walkFileTree(File file, FileTreeVisitor fileTreeVisitor) throws IOException {
        walkFileTree(file, fileTreeVisitor, 0);
    }

    public static void walkFileTree(File file, FileTreeVisitor fileTreeVisitor, int i) {
        if (i <= 10) {
            fileTreeVisitor.preVisitDirectory(file);
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        walkFileTree(file2, fileTreeVisitor, i + 1);
                    } else {
                        fileTreeVisitor.visitFile(file2);
                    }
                }
            }
            fileTreeVisitor.postVisitDirectory(file);
        }
    }

    public static boolean deleteContents(File file) {
        File[] listFiles = file.listFiles();
        boolean z = true;
        if (listFiles != null) {
            for (File deleteRecursively : listFiles) {
                z &= deleteRecursively(deleteRecursively);
            }
        }
        return z;
    }

    public static boolean deleteRecursively(File file) {
        if (file.isDirectory()) {
            deleteContents(file);
        }
        return file.delete();
    }
}
