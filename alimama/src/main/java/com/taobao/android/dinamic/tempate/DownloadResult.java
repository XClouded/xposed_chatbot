package com.taobao.android.dinamic.tempate;

import java.util.ArrayList;

public class DownloadResult {
    public ArrayList<DinamicTemplate> alreadyExistTemplates;
    public ArrayList<DinamicTemplate> failedTemplates;
    public ArrayList<DinamicTemplate> finishedTemplates;
    public boolean isFinished;
    public ArrayList<DinamicTemplate> totalFailedTemplates;
    public ArrayList<DinamicTemplate> totalFinishedTemplates;
}
