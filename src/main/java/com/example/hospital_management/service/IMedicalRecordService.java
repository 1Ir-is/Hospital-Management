package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.Room;

import java.util.List;
import java.util.Optional;

public interface IMedicalRecordService {
    Room findRoomByCode(String code);
}
