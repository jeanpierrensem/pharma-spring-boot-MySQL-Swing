package com.officine.losto.backend.springcontext.session;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.AppUser;

import lombok.Data;

@Component
@Data
@Scope("")
public class UserSession {
	private AppUser appUser;
}
