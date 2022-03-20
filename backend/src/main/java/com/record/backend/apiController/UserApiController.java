package com.record.backend.apiController;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.record.backend.domain.user.User;
import com.record.backend.dto.user.UserIdentityAvailability;
import com.record.backend.dto.user.UserProfile;
import com.record.backend.dto.user.UserSummary;
import com.record.backend.dto.user.request.UserSaveRequestDto;
import com.record.backend.dto.user.request.UserUpdateRequestDto;
import com.record.backend.dto.user.response.UserResponseDto;
import com.record.backend.dto.user.response.UserUpdateResponseDto;
import com.record.backend.exception.ResourceNotFoundException;
import com.record.backend.repository.UserRepository;
import com.record.backend.security.CurrentUser;
import com.record.backend.security.UserPrincipal;
import com.record.backend.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserRepository userRepository;
	private final UserService userService;

	//생성
	@PostMapping("/users/account/signup")
	public Long saveUser(@RequestBody UserSaveRequestDto requestDto) {
		return userService.saveUser(requestDto);
	}

	//수정
	@PutMapping("/users/{user_id}")
	public UserUpdateResponseDto updateUser(@PathVariable("user_id") Long userId,
		@RequestBody UserUpdateRequestDto updateDto) {
		return userService.updateUser(userId, updateDto);
	}

	//조회
	@GetMapping("/users")
	public Result allUsers() {
		List<UserResponseDto> allUser = userService.findAllUser();

		return new Result(allUser);
	}

	// 헤더부분에 표현할 유저 써머리
	@GetMapping("/users/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {

		UserSummary userSummary = new UserSummary(
			currentUser.getId(), currentUser.getEmail(), currentUser.getNickname(), currentUser.getDomain()
		);

		return userSummary;
	}

	// 마이페이지에 보여줄 유저 dto
	@GetMapping("/users/{userEmail}")
	public UserProfile getUserProfile(@PathVariable(value = "userEmail") String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new ResourceNotFoundException("User", "userEmail", email));

		UserProfile userProfile = new UserProfile(
			user.getId(), user.getEmail(), user.getNickname(), user.getDomain(), user.getIntroduce()
		);

		return userProfile;
	}

	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkDomainAvailability(@RequestParam(value = "domain") String domain) {
		Boolean isAvailable = !userRepository.existsByDomain(domain);
		return new UserIdentityAvailability(isAvailable);
	}

	//삭제
	@DeleteMapping("/users/{user_id}")
	public void deleteUser(@PathVariable("user_id") Long userId) {
		userService.deleteUser(userId);
	}


	@Data
	@AllArgsConstructor
	static class Result<T> {
		private T data;
	}
}
