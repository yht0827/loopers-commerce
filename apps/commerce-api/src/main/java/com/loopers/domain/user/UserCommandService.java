package com.loopers.domain.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandService {
	private final UserValidator userValidator;
	private final UserRepository userRepository;

	public User createUser(final User user) {
		userValidator.validateUniqueUserId(user.getUserId());
		return userRepository.save(user);
	}
}
