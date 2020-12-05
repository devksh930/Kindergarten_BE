package com.kindergarten.api.management;

import com.kindergarten.api.common.exception.CAuthenticationEntryPointException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.student.StudentRepository;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageMentService {
    private final UserRepository userRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final StudentRepository studentRepository;

    //원장과 admin이 설정할수 있는 원장 또는 선생님에 대한 인증권한
    public List<String> getRole(String userid) {
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        List<String> roles = Stream.of(UserRole.values()).map(UserRole::name).collect(Collectors.toList());
        roles.remove(UserRole.ROLE_USER.name());
        if (user.getRole().equals(UserRole.ROLE_DIRECTOR) || user.getRole().equals(UserRole.ROLE_ADMIN)) {
            if (user.getRole().equals(UserRole.ROLE_DIRECTOR)) {
                roles.remove(UserRole.ROLE_ADMIN.name());
                roles.remove(UserRole.ROLE_DIRECTOR.name());
                roles.remove(UserRole.ROLE_NOT_PERMITTED_DIRECTOR.name());
            }
        } else {
            throw new CAuthenticationEntryPointException();
        }
        return roles;
    }
    //유치원 소속 선생님 가져오기

}
