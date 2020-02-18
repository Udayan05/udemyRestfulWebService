package com.demo.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUser() {
		return userRepository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public Optional<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		return user;
	}
	
//	@GetMapping("/users/{id}")
//	public EntityModel<User> retrieveUser(@PathVariable int id) {
//		User user = daoService.findOne(id);
//		if (user == null) {
//			throw new UserNotFoundException("id-" + id);
//		}
//
////		HATEOAS
//
//		EntityModel<User> model = new EntityModel<User>(user);
//		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
//				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUser());
//		model.add(linkTo.withRel("all-users"));
//
//		return model;
//	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);

		// /users/{id} savedUser.getId()
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post) {

		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		User user = userOptional.get();
		
		post.setUser(user);
		postRepository.save(post);

		// /post/{id} savedpost.getId()
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

}
