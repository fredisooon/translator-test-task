package com.example.translatorapp.repository;

import com.example.translatorapp.model.Request;
import com.example.translatorapp.utils.ResponseObject;

import java.util.List;

public interface RequestRepository {

    ResponseObject save(Request employee);

    Request getById(Long id);

    void update(Long id, Request updatedRequest);

    void deleteById(Long id);

    List<Request> getAll();
}
