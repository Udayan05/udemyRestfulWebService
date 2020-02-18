package com.demo.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

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
public class UserResource {

	@Autowired
	private UserDao daoService;

	@GetMapping("/users")
	public List<User> retrieveAllUser() {
		return daoService.findAll();
	}

	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		User user = daoService.findOne(id);
		if (user == null) {
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

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = daoService.deleteOne(id);
		if (user == null) {
			throw new UserNotFoundException("id-" + id);
		}
	}

	@PostMapping("/users")
	public ResponseEntity<Object> create(@Valid @RequestBody User user) {
		User savedUser = daoService.save(user);

		// /users/{id} savedUser.getId()
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
}
