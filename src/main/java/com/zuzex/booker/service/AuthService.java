package com.zuzex.booker.service;

import com.zuzex.booker.dto.AuthRefreshRequest;
import com.zuzex.booker.dto.AuthRequest;
import com.zuzex.booker.dto.AuthResponse;
import com.zuzex.booker.dto.RegistrationRequest;
import com.zuzex.booker.model.User;

public interface AuthService {

    User registerNewUser(RegistrationRequest request);

    AuthResponse auth(AuthRequest request);

    AuthResponse refresh(AuthRefreshRequest request);
}
