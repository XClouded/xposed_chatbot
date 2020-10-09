package com.alibaba.android.umbrella.performance;

import android.text.TextUtils;
import java.util.Map;

public class ProcessRecord {
    protected static void register(ProcessEvent processEvent) {
        checkAndCommit();
        ProcessRepo.getInstance().add(new ProcessEntity(processEvent.bizName, processEvent.uptimeMillis));
    }

    protected static void recordProcess(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkConditionEmpty(processEvent) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null) {
            if (processEvent.args != null && processEvent.args.size() > 0) {
                processEntity.addArgs(processEvent.args);
            }
            if (UmbrellaProcess.PAGELOAD.equals(processEvent.process)) {
                processEntity.addPageLoad(processEvent.costTime);
            } else {
                processEntity.addProcess(processEvent.process.getName(), processEvent.costTime);
            }
        }
    }

    protected static void recordSubProcess(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkConditionEmpty(processEvent) && !TextUtils.isEmpty(processEvent.eventPoint) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null) {
            if (processEvent.args != null && processEvent.args.size() > 0) {
                processEntity.addArgs(processEvent.args);
            }
            if (processEvent.process == UmbrellaProcess.INIT) {
                processEntity.addInit(processEvent.eventPoint, processEvent.costTime);
            } else if (processEvent.process == UmbrellaProcess.LIFECYCLE) {
                processEntity.addLifeCycle(processEvent.eventPoint, processEvent.costTime);
            } else if (processEvent.process == UmbrellaProcess.NETWORK) {
                processEntity.addNetwork(processEvent.eventPoint, processEvent.costTime);
            } else if (processEvent.process == UmbrellaProcess.DATAPARSE) {
                processEntity.addDataParse(processEvent.eventPoint, processEvent.costTime);
            } else if (processEvent.process == UmbrellaProcess.SUB_CREATE_VIEW) {
                processEntity.addCreateView(processEvent.eventPoint, processEvent.costTime);
            } else if (processEvent.process == UmbrellaProcess.SUB_BIND_VIEW) {
                processEntity.addBindView(processEvent.eventPoint, processEvent.costTime);
            }
        }
    }

    protected static void recordOtherProcess(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkParamsEmpty(processEvent) && !TextUtils.isEmpty(processEvent.eventPoint) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null) {
            processEntity.addOtherProcess(processEvent.eventPoint, processEvent.costTime);
        }
    }

    protected static void recordArgs(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkParamsEmpty(processEvent) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null && processEvent.args != null && processEvent.args.size() > 0) {
            processEntity.addArgs(processEvent.args);
        }
    }

    public static void setChildBizName(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkParamsEmpty(processEvent) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null) {
            processEntity.setChildBizName(processEvent.childBizName);
        }
    }

    public static void addAbInfo(ProcessEvent processEvent) {
        ProcessEntity processEntity;
        if (!checkParamsEmpty(processEvent) && (processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName)) != null) {
            processEntity.addAbTest(processEvent.ab, processEvent.abBucket);
        }
    }

    protected static void commitPerformance(ProcessEvent processEvent) {
        ProcessEntity processEntity = ProcessRepo.getInstance().getProcessEntity(processEvent.bizName);
        if (processEntity != null) {
            ProcessRepo.getInstance().remove(processEntity);
            if (processEntity.pageLoad > 0) {
                doCommitPerformance(processEntity);
            }
        }
    }

    protected static void doCommitPerformance(ProcessEntity processEntity) {
        PerformanceEngine.commitPerformancePage(processEntity);
    }

    protected static void checkAndCommit() {
        Map<String, ProcessEntity> processMap = ProcessRepo.getInstance().getProcessMap();
        if (processMap != null || processMap.size() <= 0) {
            for (String next : processMap.keySet()) {
                ProcessEntity processEntity = processMap.get(next);
                if (processEntity == null) {
                    processMap.remove(next);
                } else {
                    processMap.remove(next);
                    doCommitPerformance(processEntity);
                }
            }
        }
    }

    protected static boolean checkConditionEmpty(ProcessEvent processEvent) {
        if (processEvent == null || TextUtils.isEmpty(processEvent.bizName) || processEvent.process == null || TextUtils.isEmpty(processEvent.process.getName()) || !ProcessRepo.getInstance().isContains(processEvent.bizName)) {
            return true;
        }
        return false;
    }

    protected static boolean checkParamsEmpty(ProcessEvent processEvent) {
        if (processEvent == null || TextUtils.isEmpty(processEvent.bizName) || !ProcessRepo.getInstance().isContains(processEvent.bizName)) {
            return true;
        }
        return false;
    }
}
