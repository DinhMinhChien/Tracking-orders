package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.request.AuthRegisterRequest;
import com.example.trackingorders.entity.Addresses;
import com.example.trackingorders.entity.Carriers;
import com.example.trackingorders.entity.Users;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.repository.AddressesRepository;
import com.example.trackingorders.repository.CarriersRepository;
import com.example.trackingorders.repository.UsersRepository;
import com.example.trackingorders.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImplement implements AuthService {
    private final PasswordEncoder passwordEncoder ;
    private final UsersRepository usersRepository ;
    private final AddressesRepository addressesRepository ;
    private final CarriersRepository carriersRepository ;
    @Override
    public void register(AuthRegisterRequest request) {
        Users users = new Users() ;

        users.setUsername(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setFullName(request.getFullName());
        users.setPhone(request.getPhone());
        users.setEmail(request.getEmail());
        users.setRole(request.getRole());

        if (request.getCarrierId() == null) {
            users.setCarrier(null);
        } else {
            Optional<Carriers> carriersOptional = carriersRepository.findById(request.getCarrierId()) ;
            if (carriersOptional.isEmpty()) {
                throw new BusinessException("Not Found Carrier") ;
            }
            users.setCarrier(carriersOptional.get());
        }


        Users userSave = usersRepository.save(users) ;

        Addresses address = new Addresses() ;
        address.setFullAddress(request.getFullName());
        address.setUsers(userSave);
        addressesRepository.save(address);

    }
}
