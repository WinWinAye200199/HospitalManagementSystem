package com.example.HMS.service;

import java.util.List;

import com.example.HMS.model.request.FeeRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.FeeResponse;

public interface FeeService {
    ApiResponse createFee(FeeRequest fee);
    List<FeeResponse> getAllFees();
    FeeResponse getFeeById(Long id);
    ApiResponse updateFee(Long id, FeeRequest feeDetails);
    ApiResponse deleteFee(Long id);
}