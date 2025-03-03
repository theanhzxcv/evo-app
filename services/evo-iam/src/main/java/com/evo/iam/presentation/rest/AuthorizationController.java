package com.evo.iam.presentation.rest;

import com.evo.UserAuthority;
import com.evo.dtos.responses.Response;
import com.evo.iam.application.service.impl.command.AuthorizationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Authority")
public class AuthorizationController {

    private final AuthorizationServiceImpl authorityService;

    @GetMapping("/{username}/authorities-by-username")
    public Response<UserAuthority> getUserAuthority(@PathVariable String username) {
        try {
            UserAuthority userAuthority = authorityService.getUserAuthority(username);
            return new Response<UserAuthority>().success().setData(userAuthority);
        } catch (Exception e) {
            return Response.fail(new RuntimeException("Failed to fetch user authority for username: " + username, e));
        }
    }
}
