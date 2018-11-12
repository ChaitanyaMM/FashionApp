package com.fashionapp.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;

import com.fashionapp.util.FireBaseResponse;

public interface FirebaseService {

	CompletableFuture<String> send(HttpEntity<String> entity);

}
