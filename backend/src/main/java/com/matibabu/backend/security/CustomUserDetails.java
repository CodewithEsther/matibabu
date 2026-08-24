package com.matibabu.backend.security;

import com.matibabu.backend.security.entity.Clinician;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Clinician clinician;

    public CustomUserDetails(Clinician clinician) {
        this.clinician = clinician;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = "ROLE_" + clinician.getRole().name();

        return List.of(new SimpleGrantedAuthority(authority));
    }

    // Spring Security calls this the "username", but in this app is the email.
    @Override
    public String getUsername() {
        return clinician.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return clinician.getPassword();
    }

    public Clinician getClinician() {
        return clinician;
    }
}
