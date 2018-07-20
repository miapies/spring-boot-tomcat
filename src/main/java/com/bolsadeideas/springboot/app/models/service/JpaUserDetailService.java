package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailService.class);

	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario user = usuarioDao.findByUsername(username);

		if (user == null) {
			logger.error("Error login: no existe el usuario '" + username + "'");
			throw new UsernameNotFoundException("Username " + username + " no existe en el sistema!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		user.getRoles().forEach(r -> {
			logger.info("Role: ".concat(r.getAuthority()));
			authorities.add(new SimpleGrantedAuthority(r.getAuthority()));
		});

		if (authorities.isEmpty()) {
			logger.error("Error login: usuario '" + username + "' no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: usuario '" + username + "' no tiene roles asignados");
		}

		return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);

	}

}
