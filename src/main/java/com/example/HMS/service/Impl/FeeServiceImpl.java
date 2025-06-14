package com.example.HMS.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HMS.model.entities.Fee;
import com.example.HMS.model.request.FeeRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.FeeResponse;
import com.example.HMS.repository.FeeRepository;
import com.example.HMS.service.FeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeServiceImpl implements FeeService {

    private final FeeRepository feeRepository;

    @Override
    public ApiResponse createFee(FeeRequest fee) {
    	Fee saveFee = new Fee();
    	saveFee.setName(fee.getName());
    	saveFee.setAmount(fee.getAmount());
    	saveFee.setDescription(fee.getDescription());
    	feeRepository.save(saveFee);
    	
        return new ApiResponse(true,"Created Fee Successfully!");
    }

    @Override
    public List<FeeResponse> getAllFees() {
    	List<Fee> fees = feeRepository.findAll();
    	List<FeeResponse> responses = new ArrayList<>();
    	for(Fee fee : fees) {
    		FeeResponse response = new FeeResponse();
        	response.setName(fee.getName());
        	response.setAmount(fee.getAmount());
        	response.setDescription(fee.getDescription());
        	
        	responses.add(response);
    	}
        return responses;
    }

    @Override
    public FeeResponse getFeeById(Long id) {
    	Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found with id " + id));
    	
    	FeeResponse response = new FeeResponse();
    	
    	response.setName(fee.getName());
    	response.setAmount(fee.getAmount());
    	response.setDescription(fee.getDescription());
    	
        return response;
    }

    @Override
    public ApiResponse updateFee(Long id, FeeRequest feeDetails) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found with id " + id));
        fee.setName(feeDetails.getName());
        fee.setAmount(feeDetails.getAmount());
        fee.setDescription(feeDetails.getDescription());
        
        feeRepository.save(fee);
        
        return new ApiResponse(true," Updated Fee Successfully!");
    }

    @Override
    public ApiResponse deleteFee(Long id) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee not found with id " + id));
        
        feeRepository.delete(fee);
        
        return new ApiResponse(true,"Deleted Fee Successfully!");
    }
}