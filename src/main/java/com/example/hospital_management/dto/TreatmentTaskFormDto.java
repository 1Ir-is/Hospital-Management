package com.example.hospital_management.dto;

import com.example.hospital_management.entity.TreatmentTask;

import java.util.List;

public class TreatmentTaskFormDto {
    private List<TreatmentTask> taskList;

    public List<TreatmentTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TreatmentTask> taskList) {
        this.taskList = taskList;
    }
}
