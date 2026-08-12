package com.officine.losto.session;

import com.officine.losto.entity.*;
import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
@Data
@Scope("")
public class UserSession {
    private AppUser currentUser;
}
