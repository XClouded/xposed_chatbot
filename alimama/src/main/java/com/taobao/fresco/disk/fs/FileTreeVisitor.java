package com.taobao.fresco.disk.fs;

import java.io.File;

public interface FileTreeVisitor {
    void postVisitDirectory(File file);

    void preVisitDirectory(File file);

    void visitFile(File file);
}
